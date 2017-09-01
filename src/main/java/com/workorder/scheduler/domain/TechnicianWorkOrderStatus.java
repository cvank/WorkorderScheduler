/**
 * 
 */
package com.workorder.scheduler.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author chandrashekar.v
 *
 */
@Entity
@Table(name = "TECHNICIAN_WORK_ORDER_STATUS")
public class TechnicianWorkOrderStatus implements Serializable {

	private static final long serialVersionUID = -820824890903451706L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TECHNICIAN_WORK_ORDER_STATUS_ID")
	private Long technicianWOStatusId;

	@OneToOne
	@JoinColumn(name = "WORK_ORDER_ID")
	private WorkOrder workOrder;

	@OneToOne
	@JoinColumn(name = "TECHNICIAN_ID")
	private Technician technician;

	@Column(name = "TIME_SPENT")
	private double timeSpent;

	@Column(name = "TIME_ALLOCATED")
	private double totalTimeAllocated;

	@Enumerated(EnumType.STRING)
	@Column(name = "CURRENT_STATUS")
	private WorkOrderStatus currentStatus;

	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "ESTIMATED_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date estimatedEndTime;

	@Column(name = "ESTIMATED_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date estimatedStartTime;

	@Column(name = "LASAT_MODIFIED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTime;

	@Column(name = "DATE_OF_ALLOCATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@PrePersist
	protected void onCreate() {
		createdDate = lastModifiedTime = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		lastModifiedTime = new Date();
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEstimatedEndTime() {
		return estimatedEndTime;
	}

	public void setEstimatedEndTime(Date estimatedEndTime) {
		this.estimatedEndTime = estimatedEndTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Long getTechnicianWOStatusId() {
		return technicianWOStatusId;
	}

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

	public WorkOrderStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(WorkOrderStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public double getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(double timeSpent) {
		this.timeSpent = timeSpent;
	}

	public double getTotalTimeAllocated() {
		return totalTimeAllocated;
	}

	public void setTotalTimeAllocated(double totalTimeAllocated) {
		this.totalTimeAllocated = totalTimeAllocated;
	}

	public Date getEstimatedStartTime() {
		return estimatedStartTime;
	}

	public void setEstimatedStartTime(Date estimatedStartTime) {
		this.estimatedStartTime = estimatedStartTime;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + ((technicianWOStatusId == null) ? 0 : technicianWOStatusId.hashCode());
		result = prime * result + ((technician == null) ? 0 : technician.hashCode());
		long temp;
		temp = Double.doubleToLongBits(timeSpent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTimeAllocated);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((workOrder == null) ? 0 : workOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TechnicianWorkOrderStatus other = (TechnicianWorkOrderStatus) obj;
		if (currentStatus != other.currentStatus)
			return false;
		if (technicianWOStatusId == null) {
			if (other.technicianWOStatusId != null)
				return false;
		} else if (!technicianWOStatusId.equals(other.technicianWOStatusId))
			return false;
		if (technician == null) {
			if (other.technician != null)
				return false;
		} else if (!technician.equals(other.technician))
			return false;
		if (Double.doubleToLongBits(timeSpent) != Double.doubleToLongBits(other.timeSpent))
			return false;
		if (Double.doubleToLongBits(totalTimeAllocated) != Double.doubleToLongBits(other.totalTimeAllocated))
			return false;
		if (workOrder == null) {
			if (other.workOrder != null)
				return false;
		} else if (!workOrder.equals(other.workOrder))
			return false;
		return true;
	}

	public Long getId() {
		return technicianWOStatusId;
	}

}
