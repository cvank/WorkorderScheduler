package com.workorder.scheduler.framework;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianLocationMap;
import com.workorder.scheduler.domain.TechnicianTimeManager;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.domain.WorkingStatus;
import com.workorder.scheduler.service.LocationService;
import com.workorder.scheduler.service.LocationWorkorderMapService;
import com.workorder.scheduler.service.TechnicianLocationMapService;
import com.workorder.scheduler.service.TechnicianService;
import com.workorder.scheduler.service.TechnicianTimeManagerService;
import com.workorder.scheduler.service.TechnicianWorkOrderStatusService;
import com.workorder.scheduler.service.WorkOrderService;

import rx.Observable;
import rx.schedulers.Schedulers;

@Component
public class WorkOrderScheduler {

	private static final Logger log = Logger.getLogger(WorkOrderScheduler.class);

	@Autowired
	WorkOrderService workOrderService;

	@Autowired
	LocationService locationService;

	@Autowired
	TechnicianService technicianService;

	@Autowired
	TechnicianWorkOrderStatusService technicianWorkOrderStatusService;

	@Autowired
	LocationWorkorderMapService locationWorkorderMapService;

	@Autowired
	TechnicianTimeManagerService technicianTimeManagerService;

	@Autowired
	TechnicianLocationMapService technicianLocationService;

	@Autowired
	TechnicianWorkOrderQService technicianWorkOrderQService;

	@PostConstruct
	public void invoke() {

		technicianWorkOrderQService.accessTechnicianWorkOrderQ().onErrorResumeNext(throwable -> Observable.empty())
				.subscribe(this::schedule, this::handleError);

	}

	private void schedule(Technician technician) {

		if (technician.getWorkingStatus() == WorkingStatus.IDLE) {
			WorkOrder order = findWorkOrderInQueue(technician).get(0).getWorkOrder();
			Observable.just(order).subscribeOn(Schedulers.newThread()).subscribe((item) -> doWork(item, technician));
		}
	}

	private List<LocationWorkOrderMap> findWorkOrderInQueue(Technician technician) {
		List<LocationWorkOrderMap> locationWorkorders = locationWorkorderMapService
				.findTodaysWaitingOrdersByTechnician(technician, WorkOrderStatus.ASSIGNED, new Date());
		locationWorkorders.sort((lw1, lw2) -> Integer.valueOf(lw1.getQueuePosition())
				.compareTo(Integer.valueOf(lw2.getQueuePosition())));

		return locationWorkorders;
	}

	public void handleError(Throwable throwable) {
		log.error("Error while doingn work:" + throwable.getMessage());
	}

	private void doWork(WorkOrder wo, Technician technician) {
		log.info("Technician " + technician.getTechnicianId() + " running on thread:"
				+ Thread.currentThread().getName());

		validateAndTrigger(wo, technician);
	}

	private void validateAndTrigger(WorkOrder wo, Technician technician) {
		if (wo.getCurrentStatus() != WorkOrderStatus.WORK_IN_PROGRESS
				&& wo.getCurrentStatus() != WorkOrderStatus.COMPLETED) {

			// check if technician has enough time to perform the task.
			TechnicianTimeManager timeManager = technicianTimeManagerService.getTechnicianTimeManager(technician);
			if (null != timeManager && timeManager.getTotalRemainingAtCurrentTask() == 0) {
				timeManager.setTotalRemainingTimeForTheDay(timeManager.getTotalRemainingTimeForTheDay().longValue()
						- (new Date().getTime() - timeManager.getLastModifiedDate().getTime()));
				technicianTimeManagerService.save(timeManager);
			}
			if (null == timeManager || (timeManager.getTotalRemainingTimeForTheDay()
					- timeManager.getTotalRemainingAtCurrentTask()) >= (long) (wo.getDuration() * 3600000)
					&& !isDayOver(technician)) {

				updateTechnicianLocation(technician, wo);
				trigger(wo, technician, technicianWorkOrderStatusService.find(wo.getId()));
			} else {
				// update estimated start time,end time and scheduled date
				LocalDateTime time = LocalDateTime.now();
				int daysToAdd = getDaysToAdd(technician);
				time.plusDays(daysToAdd);
				Date newScheduledDate = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
				wo.setScheduledDate(newScheduledDate);
			}
		}
	}

	private boolean isDayOver(Technician technician) {
		String endTime = technician.getEndTime();
		LocalDateTime time = LocalDateTime.now();
		time.plusMinutes((long) (Double.valueOf(endTime) * 60));
		Date javaEndTime = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
		return javaEndTime.before(new Date());
	}

	private int getDaysToAdd(Technician technician) {
		// TODO:Based on work orders in queue return next date to be scheduled.
		return 1;
	}

	private void updateTechnicianLocation(Technician technician, WorkOrder wo) {
		log.info("UPDATING TECHNICIAN LOCATION MAP TO:" + wo.getLocation().getLocationId());
		TechnicianLocationMap technicianLocationMap = null;
		technicianLocationMap = technicianLocationService.findByTechnicianId(technician);
		if (null == technicianLocationMap) {
			technicianLocationMap = new TechnicianLocationMap();
			technicianLocationMap.setTechnician(technicianService.find(technician));
		}
		technicianLocationMap.setCurrentLocation(locationService.findOne(wo.getLocation().getLocationId()));
		technicianLocationService.saveStatus(technicianLocationMap);
	}

	private void trigger(WorkOrder workorder, Technician technician,
			TechnicianWorkOrderStatus technicianWorkOrderStatus) {
		updateTechnician(technician);
		updateWorkorder(workorder);
		technicianWorkOrderStatus = updateTechnicianWorkOrderStatus(technicianWorkOrderStatus);
		TechnicianTimeManager technicianTimeManager = updateTechnicianTimeManager(workorder, technician);
		Future<Boolean> future = start(workorder, technician, technicianWorkOrderStatus, technicianTimeManager);
		try {
			if (future.get()) {
				log.info("Work completed. Proceeding with post processing");

				postProcessing(workorder, technicianWorkOrderStatus, technicianTimeManager);
				triggerNextWorkOrder(technician);

				log.info("Execution of all jobs completed for Technician: " + technician.getName());

				updateTechnicianStatusToIdle(technician);
				return;
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error("Process interupted. ");
			e.printStackTrace();
		}
	}

	private Future<Boolean> start(WorkOrder workorder, Technician technician,
			TechnicianWorkOrderStatus technicianWorkOrderStatus, TechnicianTimeManager technicianTimeManager) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		TechnicianWorkorderExecutor technicianWorkorderExecutor = createExecutorClient(workorder, technician,
				technicianWorkOrderStatus, technicianTimeManager);

		Future<Boolean> future = executorService.submit(technicianWorkorderExecutor);
		return future;
	}

	private void updateTechnicianStatusToIdle(Technician technician) {
		technician.setWorkingStatus(WorkingStatus.IDLE);
		technicianService.save(technician);
	}

	private void triggerNextWorkOrder(Technician technician) {
		List<LocationWorkOrderMap> locationWorkOrderMaps = findWorkOrderInQueue(technician);

		while (locationWorkOrderMaps.size() > 0) {
			locationWorkOrderMaps = findWorkOrderInQueue(technician);
			long travelingDuration = 0L;

			// Sleep for traveling duration.
			try {
				Thread.sleep(travelingDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (locationWorkOrderMaps != null && !locationWorkOrderMaps.isEmpty()) {
				log.info("Triggering next JOB:" + locationWorkOrderMaps.get(0).getWorkOrder().getId());
				validateAndTrigger(locationWorkOrderMaps.get(0).getWorkOrder(), technician);
			}

		}
	}

	private void updateTechnician(Technician technician) {
		if (technician.getWorkingStatus() != WorkingStatus.BUSY) {
			// Update Technician BUSY
			technician.setWorkingStatus(WorkingStatus.BUSY);
			technicianService.save(technician);

			log.info("CALLING WORK ORDER SCHEDULER...");
		}
	}

	private void updateWorkorder(WorkOrder workorder) {
		workOrderService.updateWorkOrderStatus(workorder, WorkOrderStatus.WORK_IN_PROGRESS);
	}

	private TechnicianTimeManager updateTechnicianTimeManager(WorkOrder workorder, Technician technician) {
		TechnicianTimeManager technicianTimeManager = technicianTimeManagerService
				.getTodaysTechnicianTimeManager(technician);
		if (null == technicianTimeManager) {
			technicianTimeManager = new TechnicianTimeManager();
			technicianTimeManager.setTechnician(technicianService.find(technician));
			technicianTimeManager.setTotalTimeSpent(0L);
			technicianTimeManager.setTotalRemainingTimeForTheDay(
					(long) (Double.valueOf(technician.getEndTime()) - Double.valueOf(technician.getStartTime()))
							* 3600000);
			technicianTimeManager.setDateOfAllocation(new Date());
		}
		technicianTimeManager.setTotalRemainingAtCurrentTask((long) (workorder.getDuration() * 3600000));
		technicianTimeManagerService.save(technicianTimeManager);
		return technicianTimeManager;
	}

	private TechnicianWorkOrderStatus updateTechnicianWorkOrderStatus(
			TechnicianWorkOrderStatus technicianWorkOrderStatus) {
		technicianWorkOrderStatus.setCurrentStatus(WorkOrderStatus.WORK_IN_PROGRESS);
		technicianWorkOrderStatus.setStartTime(new Date());
		technicianWorkOrderStatus = technicianWorkOrderStatusService.save(technicianWorkOrderStatus);
		return technicianWorkOrderStatus;
	}

	private void postProcessing(WorkOrder workorder, TechnicianWorkOrderStatus technicianWorkOrderStatus,
			TechnicianTimeManager technicianTimeManager) {
		workOrderService.updateWorkOrderStatus(workorder, WorkOrderStatus.COMPLETED);

		technicianWorkOrderStatus.setCurrentStatus(WorkOrderStatus.COMPLETED);
		technicianWorkOrderStatus.setLastModifiedTime(new Date());
		technicianWorkOrderStatusService.save(technicianWorkOrderStatus);

		// Update work order status and return
		technicianTimeManager.setTotalRemainingAtCurrentTask(0L);
		technicianTimeManagerService.save(technicianTimeManager);
	}

	@Autowired
	ApplicationContext applicationContext;

	private TechnicianWorkorderExecutor createExecutorClient(WorkOrder workorder, Technician technician,
			TechnicianWorkOrderStatus technicianWorkOrderStatus, TechnicianTimeManager technicianTimeManager) {

		TechnicianWorkorderExecutor technicianWorkorderExecutor = applicationContext
				.getBean(TechnicianWorkorderExecutor.class);
		technicianWorkorderExecutor.buildTracker(technician, workorder, technicianWorkOrderStatus,
				technicianTimeManager);
		return technicianWorkorderExecutor;
	}
}
