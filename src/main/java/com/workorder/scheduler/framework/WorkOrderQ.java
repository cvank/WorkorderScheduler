/**
 * 
 */
package com.workorder.scheduler.framework;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.service.LocationService;
import com.workorder.scheduler.service.SkillService;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author chandrashekar.v
 *
 */
@Component
public class WorkOrderQ {

	@Autowired
	SkillAllocationQService skillAllocationQService;

	@Autowired
	WorkOrderAllocater workOrderAllocater;

	@Autowired
	SkillService skillService;

	@Autowired
	LocationService locationService;

	private static final Logger log = Logger.getLogger(WorkOrderQ.class);

	private void process(WorkOrder workOrder) {
		skillAllocationQService.assignWorkOrderForProcessing(workOrder);
	}

	public WorkOrder subscribeForWorkOrder(WorkOrder workOrder) {
		workOrder.setCurrentStatus(WorkOrderStatus.RECEIVED);

		// Check for Skill.
		if (workOrder.getSkill().getSkillId() > 0) {
			Skill skill = skillService.findOne(workOrder.getSkill().getSkillId());
			if (skill != null)
				workOrder.setSkill(skill);
		}

		// Check for location.
		if (workOrder.getLocation().getLocationId() != null) {
			Location location = locationService.findOne(workOrder.getLocation().getLocationId());
			if (location != null)
				workOrder.setLocation(location);
		}

		log.info("Received workorder:" + workOrder.toString());
		if(workOrder.getScheduledDate() == null)
			workOrder.setScheduledDate(new Date());
		
		workOrder = workOrderAllocater.saveWorkOrder(workOrder);

		Observable.just(workOrder).doOnNext(t -> t.setCurrentStatus(WorkOrderStatus.RECEIVED))
				.subscribeOn(Schedulers.trampoline()).subscribe(this::process);
		
		return workOrder;
		
		
	}

}
