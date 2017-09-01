package com.workorder.scheduler.framework;

import java.util.List;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.service.WorkOrderService;

@Component
public class WorkOrderAllocater {

	@Autowired
	WorkOrderService workOrderService;

	public PriorityQueue<WorkOrder> addToQueue(WorkOrder workOrder) {
		saveWorkOrder(workOrder);
		List<WorkOrder> workOrders = fetchUnassginedWorkOrders(workOrder.getSkill());
		PriorityQueue<WorkOrder> priorityQueue = new PriorityQueue<>(workOrders.size());
		workOrders.stream().forEach((wo) -> priorityQueue.add(wo));
		return priorityQueue;
	}

	public WorkOrder saveWorkOrder(WorkOrder workOrder) {
		return workOrderService.save(workOrder);
	}

	private List<WorkOrder> fetchUnassginedWorkOrders(Skill skill) {
		return workOrderService.findAllUnassignedBySkill(skill);
	}

	public void updateWorkOrderStatus(List<WorkOrder> workOrders, WorkOrderStatus workOrderStatus) {
		workOrderService.updateWorkOrderStatus(workOrders, workOrderStatus);
	}

}
