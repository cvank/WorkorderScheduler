/**
 * 
 */
package com.workorder.scheduler.framework;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.service.LocationWorkorderMapService;
import com.workorder.scheduler.service.TechnicianService;
import com.workorder.scheduler.service.TechnicianWorkOrderStatusService;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author chandrashekar.v
 *
 */
@Component
public class SkillAllocationProcessor {
	private static final Logger log = Logger.getLogger(SkillAllocationProcessor.class);

	@Autowired
	SkillAllocationQService qService;

	@Autowired
	WorkOrderAllocater workOrderAllocater;

	@Autowired
	TechnicianIdentifier technicianIdentifier;

	@Autowired
	TechnicianPicker technicianPicker;

	@Autowired
	WorkOrderRouter workOrderRouter;

	@Autowired
	WorkOrderScheduler workOrderScheduler;

	@Autowired
	TechnicianService technicianService;

	@Autowired
	TechnicianWorkOrderStatusService technicianWorkOrderStatusService;

	@Autowired
	LocationWorkorderMapService locationWorkorderMapService;

	@Autowired
	TechnicianWorkOrderQService technicianWorkOrderQService;

	@PostConstruct
	public void invoke() {
		log.info("Invoking allocation.");

		qService.accessSkillAllocationQ().onErrorResumeNext(throwable -> Observable.empty()).subscribe(this::allocate,
				this::handleError);
	}

	public void handleError(Throwable throwable) {
		log.error("Error:" + throwable.getMessage());
	}

	public void allocate(WorkOrder workOrder) {
		try {
			workOrder.setCurrentStatus(WorkOrderStatus.ADDED_TO_QUEUE);
			PriorityQueue<WorkOrder> workOrdersQueue = workOrderAllocater.addToQueue(workOrder);
			List<WorkOrder> unAssignedWorkOrders = new ArrayList<>();

			while (workOrdersQueue.size() != 0) {
				Technician technician = identifyTechnician(workOrder, workOrdersQueue);
				if (technician == null) {
					unAssignedWorkOrders.add(workOrdersQueue.poll());
				} else {
					workOrdersQueue.poll(); // dispose
					List<WorkOrder> workordersTemp = new ArrayList<>();
					workordersTemp.add(workOrder);
					PriorityQueue<LocationWorkOrderMap> locationWorkOrderMaps = workOrderRouter
							.allocateWorkOrders(technician, workordersTemp);

					final Technician technician2 = technician;
					// Trigger
					Observable.just(locationWorkOrderMaps).subscribeOn(Schedulers.newThread())
							.doOnSubscribe(() -> process(locationWorkOrderMaps, technician2)).subscribe();
					log.info("INITIATED PROCESSING ON NEW THREAD.. CONTINUEING WITH WORK ORDERS QUEUE:"
							+ Thread.currentThread().getName());

				}

			}
			updateWorkOrderStatus(unAssignedWorkOrders, WorkOrderStatus.UNASSIGNED);
		} catch (Exception e) {
			log.error("Exception while processing work order." + e.getMessage());
			e.printStackTrace();
		}
		log.info("COMPLETED WITH ALLOCATION....");
	}

	private Technician identifyTechnician(WorkOrder workOrder, PriorityQueue<WorkOrder> workOrdersQueue) {
		log.info("IDENTIFYING TECHNICIAN....");
		PriorityQueue<Technician> queuedTechnicians = technicianIdentifier.eligibleTechnicians(workOrdersQueue.peek());

		Technician technician = null;
		if (null != queuedTechnicians && queuedTechnicians.size() == 1) {
			technician = queuedTechnicians.poll();
		} else if (null != queuedTechnicians && queuedTechnicians.size() > 1) {
			log.info("MORE THAN ONE TECHNICIAN AVAILABLE..PICKING THE BEST POSSSIBLE ONE..");
			technician = technicianPicker.pick(queuedTechnicians, workOrder);
		}
		return technician;
	}

	/**
	 * Update work order status as not_allocated
	 * 
	 * @param workOrders
	 */
	private void updateWorkOrderStatus(List<WorkOrder> workOrders, WorkOrderStatus workOrderStatus) {
		log.info("UPDATING WOTK ORDER STATUS TO ..." + workOrderStatus.toString());
		if (null != workOrders && !workOrders.isEmpty())
			workOrderAllocater.updateWorkOrderStatus(workOrders, workOrderStatus);
	}

	private void process(PriorityQueue<LocationWorkOrderMap> locationWorkOrderMaps, Technician technician) {
		log.info("Processing work orders:" + Thread.currentThread().getName());

		// Persist details for each work order
		try {
			List<WorkOrder> workOrders = new ArrayList<>();
			locationWorkOrderMaps.stream().forEach(locWoMap -> workOrders.add(locWoMap.getWorkOrder()));

			log.info("UPDATING WOTK ORDER STATUS TO ..." + WorkOrderStatus.ASSIGNED);
			final List<WorkOrder> workOrdersUpdated = workOrders.stream().filter(item -> {
				return (item.getCurrentStatus() != WorkOrderStatus.WORK_IN_PROGRESS
						&& item.getCurrentStatus() != WorkOrderStatus.ASSIGNED
						&& item.getCurrentStatus() != WorkOrderStatus.COMPLETED);
			}).collect(Collectors.toList());
			updateWorkOrderStatus(workOrdersUpdated, WorkOrderStatus.ASSIGNED);

			// Check if technician is busy already
			technician = technicianService.find(technician);

			updateTechnicianWorkOrderStatus(workOrdersUpdated, technician);
			updateTechnicianWorkOrderStatusWithStartAndEndTimes(workOrders, technician);
			technicianWorkOrderQService.assignWorkOrderForProcessing(technician);

		} catch (Exception e) {
			log.error("Exception while processing work order. " + e.getMessage());
			e.printStackTrace();
		}

		log.info("FINISED PROCESSING...");
	}

	private void updateTechnicianWorkOrderStatusWithStartAndEndTimes(List<WorkOrder> workOrders,
			Technician technician) {

		List<TechnicianWorkOrderStatus> technicianWorkOrders = technicianWorkOrderStatusService
				.findByTechnician(technician);
		Map<Long, TechnicianWorkOrderStatus> map = new HashMap<>();
		technicianWorkOrders.stream().forEach((item) -> map.put(item.getWorkOrder().getId(), item));

		List<LocationWorkOrderMap> locationWorkOrderMaps = locationWorkorderMapService.findByTechnician(technician);

		TechnicianWorkOrderStatus technicianWOStatus = null;
		if(map == null || map.isEmpty())
			return;
		for (LocationWorkOrderMap locationWorkOrderMap : locationWorkOrderMaps) {
			if (map.get(locationWorkOrderMap.getWorkOrder().getId()).getCurrentStatus() != WorkOrderStatus.COMPLETED
					&& map.get(locationWorkOrderMap.getWorkOrder().getId())
							.getCurrentStatus() != WorkOrderStatus.WORK_IN_PROGRESS) {

				// Set estimated start time.
				Calendar startTime = Calendar.getInstance();
				double timeToTravel = 0; // in Hours
				if (null != technicianWOStatus) {
					startTime.setTime(technicianWOStatus.getEstimatedEndTime());
				} else {
					startTime.setTime(new Date());
				}
				startTime.add(Calendar.MINUTE, Double.valueOf(timeToTravel*60).intValue());
				map.get(locationWorkOrderMap.getWorkOrder().getId()).setEstimatedStartTime(startTime.getTime());

				// Set estimated end time.
				Calendar endTime = Calendar.getInstance();
				endTime.setTime(startTime.getTime()); 
				endTime.add(Calendar.MINUTE,
						Double.valueOf(locationWorkOrderMap.getWorkOrder().getDuration()*60).intValue());
				map.get(locationWorkOrderMap.getWorkOrder().getId()).setEstimatedEndTime(endTime.getTime());
				technicianWOStatus = map.get(locationWorkOrderMap.getWorkOrder().getId());
			} else {
				technicianWOStatus = map.get(locationWorkOrderMap.getWorkOrder().getId());
			}

		}
		technicianWorkOrderStatusService
		.save(map.values().stream()
				.filter((item) -> item.getCurrentStatus() != WorkOrderStatus.WORK_IN_PROGRESS
				|| item.getCurrentStatus() != WorkOrderStatus.COMPLETED)
				.collect(Collectors.toList()));
	}

	private Map<Long, TechnicianWorkOrderStatus> updateTechnicianWorkOrderStatus(List<WorkOrder> workOrders,
			Technician technician) {
		Map<Long, TechnicianWorkOrderStatus> technicianWorkOrderStatusMap = new HashMap<>();
		for (WorkOrder workOrder : workOrders) {
			log.info("UPDATING TechnicianWorkOrderStatus WITH TECHNICIAN & WORKORDER DETAILS...."
					+ WorkOrderStatus.ASSIGNED);
			TechnicianWorkOrderStatus orderStatus = new TechnicianWorkOrderStatus();
			orderStatus.setWorkOrder(workOrder);
			orderStatus.setTechnician(technician);
			orderStatus.setTotalTimeAllocated(workOrder.getDuration());
			orderStatus.setCurrentStatus(WorkOrderStatus.ASSIGNED);
			technicianWorkOrderStatusMap.put(workOrder.getId(), orderStatus);
		}
		log.info("SAVINg technicianWorkOrderStatus to DB:" + technicianWorkOrderStatusMap.values().size());
		technicianWorkOrderStatusService.save(technicianWorkOrderStatusMap.values());
		return technicianWorkOrderStatusMap;
	}

}
