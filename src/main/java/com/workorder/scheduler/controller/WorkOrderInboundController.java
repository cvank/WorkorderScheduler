package com.workorder.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.framework.WorkOrderQ;

/**
 * Created by chandrashekar.v on 10/24/2016.
 */
@RestController
@RequestMapping("/work-order")
public class WorkOrderInboundController {

	@Autowired
	WorkOrderQ workOrderQ;

	@RequestMapping("/allocate1")
	public @ResponseBody Object allocateWorkOrderNew(@RequestBody WorkOrder workOrder) {

		workOrderQ.subscribeForWorkOrder(workOrder);
		return true;
	}
}
