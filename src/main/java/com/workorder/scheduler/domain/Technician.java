package com.workorder.scheduler.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by chandrashekar.v on 10/24/2016.
 */
@Entity
@Table(name = "TECHNICIAN")
public class Technician implements Serializable, Comparable<Technician> {

	private static final long serialVersionUID = 3758463485170598459L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TECHNICIAN_ID")
	private Long technicianId;

	@Column(name = "NAME")
	private String name;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "TECHNICIAN_SKILL", joinColumns = @JoinColumn(name = "TECHNICIAN_ID"), inverseJoinColumns = @JoinColumn(name = "SKILL_ID"))
	private Set<Skill> skills;

	@Column(name = "START_TIME")
	private String startTime;

	@Column(name = "END_TIME")
	private String endTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "WORKING_STATUS")
	private WorkingStatus workingStatus;

	public Long getTechnicianId() {
		return technicianId;
	}

	public WorkingStatus getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(WorkingStatus workingStatus) {
		this.workingStatus = workingStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((technicianId == null) ? 0 : technicianId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Technician))
			return false;
		Technician other = (Technician) obj;
		if (technicianId == null) {
			if (other.technicianId != null)
				return false;
		} else if (!technicianId.equals(other.technicianId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Technician o) {
		return o.getWorkingStatus().getValue() - this.getWorkingStatus().getValue();

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
