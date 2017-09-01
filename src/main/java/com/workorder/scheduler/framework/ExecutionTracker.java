/**
 * 
 */
package com.workorder.scheduler.framework;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianTimeManager;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;

/**
 * @author chandrashekar.v
 *
 */
@Component
@Scope("prototype")
public class ExecutionTracker implements Serializable {

	private static final long serialVersionUID = 2572916320735248593L;
	private Technician technician;
	private WorkOrder workOrder;
	private TechnicianWorkOrderStatus technicianWorkOrderStatus;
	private TechnicianTimeManager technicianTimeManager;
	private AtomicLong progress = new AtomicLong(0L);
	private AtomicLong endTime = new AtomicLong(0L);

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public TechnicianWorkOrderStatus getTechnicianWorkOrderStatus() {
		return technicianWorkOrderStatus;
	}

	public void setTechnicianWorkOrderStatus(TechnicianWorkOrderStatus technicianWorkOrderStatus) {
		this.technicianWorkOrderStatus = technicianWorkOrderStatus;
	}

	public TechnicianTimeManager getTechnicianTimeManager() {
		return technicianTimeManager;
	}

	public void setTechnicianTimeManager(TechnicianTimeManager technicianTimeManager) {
		this.technicianTimeManager = technicianTimeManager;
	}

	public AtomicLong getProgress() {
		return progress;
	}

	public void setProgress(AtomicLong progress) {
		this.progress = progress;
	}

	public AtomicLong getEndTime() {
		return endTime;
	}

	public void setEndTime(AtomicLong endTime) {
		this.endTime = endTime;
	}

}
