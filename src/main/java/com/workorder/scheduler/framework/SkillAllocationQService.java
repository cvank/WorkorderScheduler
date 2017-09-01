/**
 * 
 */
package com.workorder.scheduler.framework;

import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.WorkOrder;

import rx.Observable;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class SkillAllocationQService {

	private SkillAllocationQ<WorkOrder> allocationQ = new SkillAllocationQ<>();

	public Observable<WorkOrder> accessSkillAllocationQ() {
		return allocationQ.getSkillAllocationQ().asObservable();
	}
	
	public void assignWorkOrderForProcessing(WorkOrder workOrder) {
		allocationQ.addToQ(workOrder);
	}

	
}
