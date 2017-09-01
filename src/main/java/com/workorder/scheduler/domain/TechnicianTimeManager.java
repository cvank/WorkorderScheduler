/**
 * 
 */
package com.workorder.scheduler.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TECHNICIAN_TIME_MANAGER")
public class TechnicianTimeManager implements Serializable {

	private static final long serialVersionUID = -6776196186943969045L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "TECHNICIAN_ID")
	private Technician technician;

	@Column(name = "TIME_REMAINING_FOR_DAY")
	private Long totalRemainingTimeForTheDay;

	@Column(name = "TOTAL_TIME_SPENT")
	private Long totalTimeSpent;

	@Column(name = "DATE_OF_ALLOCATION")
	@Temporal(TemporalType.DATE)
	private Date dateOfAllocation;

	@Column(name = "LAST_MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	@Column(name = "TIME_REMAINING_AT_CURRENT_TASK")
	private Long totalRemainingAtCurrentTask;

	@PrePersist
	protected void onCreate() {
		dateOfAllocation = lastModifiedDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		lastModifiedDate = new Date();
	}

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public Long getId() {
		return id;
	}

	public Date getDateOfAllocation() {
		return dateOfAllocation;
	}

	public void setDateOfAllocation(Date dateOfAllocation) {
		this.dateOfAllocation = dateOfAllocation;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getTotalRemainingAtCurrentTask() {
		return totalRemainingAtCurrentTask;
	}

	public void setTotalRemainingAtCurrentTask(Long totalRemainingAtCurrentTask) {
		this.totalRemainingAtCurrentTask = totalRemainingAtCurrentTask;
	}

	public void setTotalRemainingTimeForTheDay(Long totalRemainingTimeForTheDay) {
		this.totalRemainingTimeForTheDay = totalRemainingTimeForTheDay;
	}

	public Long getTotalRemainingTimeForTheDay() {
		return totalRemainingTimeForTheDay;
	}

	public void setTotalTimeSpent(Long totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}

	public Long getTotalTimeSpent() {
		return totalTimeSpent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfAllocation == null) ? 0 : dateOfAllocation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((technician == null) ? 0 : technician.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalRemainingAtCurrentTask);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalRemainingTimeForTheDay);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTimeSpent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		TechnicianTimeManager other = (TechnicianTimeManager) obj;
		if (dateOfAllocation == null) {
			if (other.dateOfAllocation != null)
				return false;
		} else if (!dateOfAllocation.equals(other.dateOfAllocation))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (technician == null) {
			if (other.technician != null)
				return false;
		} else if (!technician.equals(other.technician))
			return false;
		if (Double.doubleToLongBits(totalRemainingAtCurrentTask) != Double
				.doubleToLongBits(other.totalRemainingAtCurrentTask))
			return false;
		if (Double.doubleToLongBits(totalRemainingTimeForTheDay) != Double
				.doubleToLongBits(other.totalRemainingTimeForTheDay))
			return false;
		if (Double.doubleToLongBits(totalTimeSpent) != Double.doubleToLongBits(other.totalTimeSpent))
			return false;
		return true;
	}

}
