/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.WorkingStatus;
import com.workorder.scheduler.repository.TechnicianRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class TechnicianService {

	@Autowired
	TechnicianRepository technicianRepository;

	public List<Technician> findBySkill(Skill skill, WorkingStatus... workingStatus) {
		return technicianRepository.findBySkillsAndWorkingStatusIn(skill, Arrays.asList(workingStatus));
	}

	public Technician save(Technician technician) {
		return technicianRepository.save(technician);
	}

	public Technician find(Technician technician) {
		return technicianRepository.findOne(technician.getTechnicianId());
	}

	public List<Technician> findAll() {
		return (List<Technician>) technicianRepository.findAll();
	}

	public List<Technician> findAll(final String filter) {
		return (List<Technician>) technicianRepository.findByNameContaining(filter);
	}
}
