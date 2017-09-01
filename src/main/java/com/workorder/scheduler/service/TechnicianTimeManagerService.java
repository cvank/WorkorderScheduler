/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianTimeManager;
import com.workorder.scheduler.repository.TechnicianTimeManagerRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class TechnicianTimeManagerService {

	@Autowired
	TechnicianTimeManagerRepository technicianTimeManagerRepository;

	public TechnicianTimeManager getTechnicianTimeManager(Technician technician) {
		return technicianTimeManagerRepository.findByTechnicianAndDateOfAllocation(technician, new Date());
	}

	public TechnicianTimeManager getTodaysTechnicianTimeManager(Technician technician) {
		return technicianTimeManagerRepository.findByTechnicianAndDateOfAllocation(technician, new Date());
	}

	public TechnicianTimeManager save(TechnicianTimeManager technicianTimeManager) {
		return technicianTimeManagerRepository.save(technicianTimeManager);
	}

}
