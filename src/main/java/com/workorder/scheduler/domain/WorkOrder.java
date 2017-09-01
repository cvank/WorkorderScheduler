package com.workorder.scheduler.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.workorder.scheduler.framework.Location;

/**
 * Created by chandrashekar.v on 10/24/2016.
 */
@Entity
@Table(name = "WORK_ORDER")
public class WorkOrder implements Serializable, Comparable<WorkOrder> {
	private static final long serialVersionUID = -233756132965750580L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WORK_ORDER_ID")
	private Long id;

	@OneToOne
	@JoinColumn(name = "SKILL_ID")
	private Skill skill;

	@Enumerated(EnumType.STRING)
	@Column(name = "PRIORITY")
	private Priority priority;

	@Column(name = "DURATION")
	private double duration;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private WorkOrderStatus currentStatus;
	
	@Column(name = "SCHEDULED_DATE")
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;

	@OneToOne
	@JoinColumn(name = "LOCATION_ID")
	private Location location;

	@Column(name = "TIME_OF_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivedDate;

	@Column(name = "LASAT_MODIFIED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTime;

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	@PrePersist
	protected void onCreate() {
		receivedDate = lastModifiedTime = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		lastModifiedTime = new Date();
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public WorkOrderStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(WorkOrderStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Long getId() {
		return id;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public double getDuration() {
		return duration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		long temp;
		temp = Double.doubleToLongBits(duration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((skill == null) ? 0 : skill.hashCode());
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
		WorkOrder other = (WorkOrder) obj;
		if (currentStatus != other.currentStatus)
			return false;
		if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (priority != other.priority)
			return false;
		if (skill == null) {
			if (other.skill != null)
				return false;
		} else if (!skill.equals(other.skill))
			return false;
		return true;
	}

	@Override
	public int compareTo(WorkOrder o) {
		int result = o.getPriority().getValue() - this.getPriority().getValue();
		if (result > 0)
			return -1;
		if (result < 0)
			return 1;
		else
			return 0;

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/*
	 * public static void main(String[] args) { WorkOrder order = new
	 * WorkOrder(); order.setDuration(4); Location location = new
	 * Location("JP Nagar", new Coordinates(10, 5));
	 * order.setLocation(location); order.setPriority(Priority.HIGH); Skill
	 * skill = new Skill(); skill.setSkillId(1); skill.setType("java");
	 * order.setSkill(skill);
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); try {
	 * System.out.println(mapper.writeValueAsString(order)); } catch
	 * (JsonProcessingException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

}
