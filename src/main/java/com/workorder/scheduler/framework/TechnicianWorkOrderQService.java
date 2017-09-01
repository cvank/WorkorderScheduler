/**
 * 
 */
package com.workorder.scheduler.framework;

import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Technician;

import rx.Observable;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class TechnicianWorkOrderQService {

	private TechnicianWorkOrderQueue<Technician> allocationQ = new TechnicianWorkOrderQueue<>();

	public Observable<Technician> accessTechnicianWorkOrderQ() {
		return allocationQ.getTechnicianWorkOrderQueue().asObservable();
	}

	public void assignWorkOrderForProcessing(Technician technician) {
		allocationQ.addToQ(technician);
	}

}
