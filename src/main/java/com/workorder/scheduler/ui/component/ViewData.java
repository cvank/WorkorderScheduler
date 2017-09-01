/**
 * 
 */
package com.workorder.scheduler.ui.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.framework.Location;
import com.workorder.scheduler.framework.WorkOrderQ;
import com.workorder.scheduler.service.LocationService;
import com.workorder.scheduler.service.LocationWorkorderMapService;
import com.workorder.scheduler.service.SkillService;
import com.workorder.scheduler.service.TechnicianService;
import com.workorder.scheduler.service.TechnicianWorkOrderStatusService;
import com.workorder.scheduler.service.WorkOrderService;

/**
 * @author chandrashekar.v
 *
 */
@SpringComponent
@UIScope
public class ViewData {

	@Autowired
	TechnicianService technicianService;

	@Autowired
	WorkOrderService workOrderService;

	@Autowired
	SkillService skillService;

	@Autowired
	LocationService LocationService;

	@Autowired
	LocationWorkorderMapService locationWorkorderMapService;

	@Autowired
	TechnicianWorkOrderStatusService technicianWorkOrderStatusService;

	public List<Technician> fetchTechnicians() {
		return technicianService.findAll();
	}

	public List<Technician> fetchTechnicians(String filter) {
		return technicianService.findAll(filter);
	}

	public List<WorkOrder> fetchorkorders() {
		return workOrderService.findAll();
	}

	public List<LocationWorkOrderMap> fetchLocationWorkOrderData(Technician technician) {
		return locationWorkorderMapService.findByTechnician(technician);
	}

	public List<Skill> fetchSkills() {
		return skillService.findAll();

	}

	public Location fetchLocation(final String lat, final String lang) {
		return LocationService.fetchLocation(lat, lang);
	}

	public Skill fetchSkillByName(String skill) {
		return skillService.findByType(skill);
	}

	@Autowired
	WorkOrderQ workOrderQ;
	
	public WorkOrder saveWorkOrder(WorkOrder workOrder) {
		return workOrderQ.subscribeForWorkOrder(workOrder);
	}

	public TechnicianWorkOrderStatus fetchTechnicianWorkOrderData(WorkOrder workOrder) {
		return technicianWorkOrderStatusService.findByWorkorder(workOrder);
	}

	public LocationWorkOrderMap fetchLocationWorkOrderData(WorkOrder workOrder) {
		return locationWorkorderMapService.findByWorkorder(workOrder);

	}

	public Location saveLocation(Location location) {
		return LocationService.saveLocation(location);

	}

}
