/**
 * 
 */
package com.workorder.scheduler.framework;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianTimeManager;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.service.TechnicianTimeManagerService;
import com.workorder.scheduler.service.TechnicianWorkOrderStatusService;

/**
 * @author chandrashekar.v
 *
 */
@Component
@Scope("prototype")
public class TechnicianWorkorderExecutor implements Callable<Boolean> {

	private static final int MILLISEC_MULTIPLIER = 3600000;

	@Autowired
	private ExecutionTracker executionTracker;

	@Autowired
	private TechnicianWorkOrderStatusService technicianWorkOrderStatusService;

	@Autowired
	private TechnicianTimeManagerService technicianTimeManagerService;

	public void buildTracker(Technician technician, WorkOrder workOrder,
			TechnicianWorkOrderStatus technicianWorkOrderStatus, TechnicianTimeManager technicianTimeManager) {
		executionTracker.setTechnician(technician);
		executionTracker.setWorkOrder(workOrder);
		executionTracker.setTechnicianWorkOrderStatus(technicianWorkOrderStatus);
		executionTracker.setTechnicianTimeManager(technicianTimeManager);
	}

	@Override
	public Boolean call() {
		executionTracker.setProgress(new AtomicLong(0L));
		executionTracker.getEndTime().set((long) (executionTracker.getWorkOrder().getDuration() * MILLISEC_MULTIPLIER));

		while (executionTracker.getEndTime().get() >= executionTracker.getProgress().get()) {
			try {
				Thread.sleep(1000);
				perform();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	private void perform() {
		executionTracker.getProgress().set(executionTracker.getProgress().get() + 1000);

		// Update technicianWorkOrderStatus
		executionTracker.getTechnicianWorkOrderStatus()
				.setTimeSpent(executionTracker.getTechnicianWorkOrderStatus().getTimeSpent() + 1000);
		technicianWorkOrderStatusService.save(executionTracker.getTechnicianWorkOrderStatus());

		// Update Time manager of technician

		executionTracker.getTechnicianTimeManager().setTotalRemainingAtCurrentTask(
				executionTracker.getTechnicianTimeManager().getTotalRemainingAtCurrentTask() - 1000);
		executionTracker.getTechnicianTimeManager().setTotalRemainingTimeForTheDay(
				executionTracker.getTechnicianTimeManager().getTotalRemainingTimeForTheDay() - 1000);
		executionTracker.getTechnicianTimeManager()
				.setTotalTimeSpent(executionTracker.getTechnicianTimeManager().getTotalTimeSpent() + 1000);

		technicianTimeManagerService.save(executionTracker.getTechnicianTimeManager());
	}

}
