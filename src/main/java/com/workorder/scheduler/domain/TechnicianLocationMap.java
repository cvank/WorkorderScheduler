/**
 * 
 */
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

/**
 * @author chandrashekar.v
 *
 */
@Entity
@Table(name = "TECHNICIAN_LOCATION")
public class TechnicianLocationMap implements Serializable {

	private static final long serialVersionUID = -3827241021352007515L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long technicianLocationMapId;

	@OneToOne
	@JoinColumn(name = "TECHNICIAN_ID")
	private Technician technician;

	@OneToOne
	@JoinColumn(name = "LOCATION_ID")
	private Location currentLocation;

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public Long getTechnicianLocationMapId() {
		return technicianLocationMapId;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

}
