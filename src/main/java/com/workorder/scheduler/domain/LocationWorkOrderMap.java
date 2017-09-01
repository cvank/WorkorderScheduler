package com.workorder.scheduler.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.workorder.scheduler.framework.Location;

@Entity
@Table(name = "LOCATION_WORKORDER")
public class LocationWorkOrderMap implements Serializable, Comparable<LocationWorkOrderMap> {

	private static final long serialVersionUID = -3065634192455488843L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long locationWorkOrderMapId;

	@OneToOne
	@JoinColumn(name = "LOCATION_ID")
	private Location location;

	@OneToOne
	@JoinColumn(name = "TECHNICIAN_ID")
	private Technician technician;

	@OneToOne
	@JoinColumn(name = "WORK_ORDER_ID")
	private WorkOrder workOrder;

	@Column(name = "QUEUE_POSITION")
	private int queuePosition;

	@Column(name = "DISTANCE_TO_NEXT_POSITION")
	private double distanceToNextPos;

	@Column(name = "TIME_TO_NEXT_POSITION")
	private Long timeOfTravelToNextPos;

	public double getDistanceToNextPos() {
		return distanceToNextPos;
	}

	public void setDistanceToNextPos(double distanceToNextPos) {
		this.distanceToNextPos = distanceToNextPos;
	}

	public Long getTimeOfTravelToNextPos() {
		return timeOfTravelToNextPos;
	}

	public void setTimeOfTravelToNextPos(Long timeOfTravelToNextPos) {
		this.timeOfTravelToNextPos = timeOfTravelToNextPos;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

	public Long getLocationWorkOrderMapId() {
		return locationWorkOrderMapId;
	}

	public int getQueuePosition() {
		return queuePosition;
	}

	public void setQueuePosition(int queuePosition) {
		this.queuePosition = queuePosition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((locationWorkOrderMapId == null) ? 0 : locationWorkOrderMapId.hashCode());
		result = prime * result + ((technician == null) ? 0 : technician.hashCode());
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
		LocationWorkOrderMap other = (LocationWorkOrderMap) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (locationWorkOrderMapId == null) {
			if (other.locationWorkOrderMapId != null)
				return false;
		} else if (!locationWorkOrderMapId.equals(other.locationWorkOrderMapId))
			return false;
		if (technician == null) {
			if (other.technician != null)
				return false;
		} else if (!technician.equals(other.technician))
			return false;
		if (workOrder == null) {
			if (other.workOrder != null)
				return false;
		} else if (!workOrder.equals(other.workOrder))
			return false;
		return true;
	}

	@Override
	public int compareTo(LocationWorkOrderMap o) {
		return o.getQueuePosition() - this.getQueuePosition();
	}

}
