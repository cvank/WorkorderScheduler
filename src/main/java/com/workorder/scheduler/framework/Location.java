/**
 * 
 */
package com.workorder.scheduler.framework;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.workorder.scheduler.scheduler.strategy.mst.Coordinates;

/**
 * @author chandrashekar.v
 *
 */
@Entity
@Table(name = "LOCATION")
public class Location implements Serializable {

	private static final long serialVersionUID = 6936098282541579414L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LOCATION_ID")
	private Long locationId;

	@Column(name = "NAME")
	private String name;

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "COORDINATES_ID")
	private Coordinates coordinates;

	public void setName(String name) {
		this.name = name;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Location(String name, Coordinates coordinates) {
		this.name = name;
		this.coordinates = coordinates;

	}

	public Location(String name, int lat, int lang) {
		this.name = name;
		coordinates = new Coordinates(lat, lang);
	}

	public Location() {
		super();
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locationId == null) ? 0 : locationId.hashCode());
		return result;
	}

	public Long getLocationId() {
		return locationId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (locationId == null) {
			if (other.getLocationId() != null)
				return false;
		} else if (!locationId.equals(other.getLocationId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + " (" + this.getCoordinates().getLat() + ", " + this.getCoordinates().getLang() + ")";
	}

}
