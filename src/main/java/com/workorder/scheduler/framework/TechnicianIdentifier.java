/**
 * 
 */
package com.workorder.scheduler.framework;

import java.util.List;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianTimeManager;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkingStatus;
import com.workorder.scheduler.service.TechnicianService;
import com.workorder.scheduler.service.TechnicianTimeManagerService;

/**
 * @author chandrashekar.v
 *
 */
@Component
public class TechnicianIdentifier {

	private static final Logger LOG = Logger.getLogger(TechnicianIdentifier.class);

	@Autowired
	TechnicianTimeManagerService technicianTimeManagerService;

	@Autowired
	TechnicianService technicianService;

	/**
	 * Identify technician for the given work order.
	 * 
	 * @param workOrder
	 * @return
	 */
	public Technician defineTechnician(WorkOrder workOrder) {
		List<Technician> technicians = getTechniciansBySkill(workOrder.getSkill());

		Technician preferredTechnician = filterTechniciansThatHasBandwidthToPerformTask(technicians,
				workOrder.getDuration(), workOrder);

		return preferredTechnician;
	}

	/**
	 * Identify technician for the given work order.
	 * 
	 * @param workOrder
	 * @return
	 */
	public PriorityQueue<Technician> eligibleTechnicians(WorkOrder workOrder) {
		List<Technician> technicians = getTechniciansBySkill(workOrder.getSkill());

		if (null == technicians || technicians.size() == 0)
			return null;

		return preferredTechnicians(technicians, workOrder.getDuration());
	}

	private Technician filterTechniciansThatHasBandwidthToPerformTask(List<Technician> technicians, double duration,
			WorkOrder workOrder) {

		PriorityQueue<Technician> techniciansEligible = preferredTechnicians(technicians, duration);

		Technician preferredTechnician = techniciansEligible.poll();

		return preferredTechnician;

	}

	private PriorityQueue<Technician> preferredTechnicians(List<Technician> technicians, double duration) {
		PriorityQueue<Technician> techniciansEligible = new PriorityQueue<>();
		technicians.stream().forEach((technician) -> {
			TechnicianTimeManager technicianTimeManager = technicianTimeManagerService
					.getTechnicianTimeManager(technician);
			
			if (null == technicianTimeManager || technicianTimeManager.getTotalRemainingAtCurrentTask()
					+ technicianTimeManager.getTotalRemainingTimeForTheDay() >= duration)
				techniciansEligible.offer(technician);

		});
		return techniciansEligible;
	}

	/**
	 * Returns technicians relevant to the skill.
	 * 
	 * @param skill
	 * @return
	 */
	private List<Technician> getTechniciansBySkill(Skill skill) {
		if (null == skill)
			return null;
		
		LOG.info("Fetching technicians by Skill: " + skill.getType());
		return technicianService.findBySkill(skill, WorkingStatus.BUSY, WorkingStatus.IDLE);

	}

}
