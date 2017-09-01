/**
 * 
 */
package com.workorder.scheduler.framework;

import java.io.Serializable;
import java.util.Date;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.WorkOrder;

/**
 * @author chandrashekar.v
 *
 */
public class ProcessData implements Serializable {

	private static final long serialVersionUID = 82578457293266963L;

	private WorkOrder workOrder;

	private Technician technician;

	private Date scheduledTime;

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

}
