/**
 * 
 */
package com.workorder.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianLocationMap;
import com.workorder.scheduler.repository.TechnicianLocationMapRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class TechnicianLocationMapService {

	@Autowired
	TechnicianLocationMapRepository technicianLocationMapRepository;

	public TechnicianLocationMap findByTechnicianId(final Technician technician) {
		return technicianLocationMapRepository.findByTechnician(technician);
	}
	
	public TechnicianLocationMap saveStatus(TechnicianLocationMap technicianLocationMap) {
		return technicianLocationMapRepository.save(technicianLocationMap);
	}

}
