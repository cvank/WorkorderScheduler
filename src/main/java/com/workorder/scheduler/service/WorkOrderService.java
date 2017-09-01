/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.framework.Location;
import com.workorder.scheduler.repository.WorkOrderRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class WorkOrderService {

	@Autowired
	WorkOrderRepository workOrderRepository;

	public List<WorkOrder> findAllUnassignedBySkill(Skill skill) {
		return workOrderRepository.findBySkillAndCurrentStatusIn(skill,
				Arrays.asList(WorkOrderStatus.UNASSIGNED, WorkOrderStatus.RECEIVED, WorkOrderStatus.ADDED_TO_QUEUE));
	}

	public List<WorkOrder> findAll() {
		return (List<WorkOrder>) workOrderRepository.findAll();
	}

	public WorkOrder save(WorkOrder workOrder) {
		return workOrderRepository.save(workOrder);
	}

	public void updateWorkOrderStatus(List<WorkOrder> workOrders, WorkOrderStatus workOrderStatus) {
		List<WorkOrder> orders = workOrders.stream().map(wo -> setStatus(workOrderStatus, wo))
				.collect(Collectors.toList());
		workOrderRepository.save(orders);
	}

	private WorkOrder setStatus(WorkOrderStatus workOrderStatus, WorkOrder wo) {
		wo.setCurrentStatus(workOrderStatus);
		return wo;
	}

	public void updateWorkOrderStatus(WorkOrder workorder, WorkOrderStatus status) {
		workorder.setCurrentStatus(status);
		workOrderRepository.save(workorder);
	}

	public Location findLocationByWorkorder(WorkOrder wo) {
		return workOrderRepository.findLocationById(wo.getId());
	}

}
