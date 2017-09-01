/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.repository.TechnicianWorkOrderStatusRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class TechnicianWorkOrderStatusService {

	@Autowired
	TechnicianWorkOrderStatusRepository technicianWorkOrderStatusRepository;

	public void save(Collection<TechnicianWorkOrderStatus> technicianWorkOrderStatusList) {
		technicianWorkOrderStatusRepository.save(technicianWorkOrderStatusList);
	}

	public TechnicianWorkOrderStatus save(TechnicianWorkOrderStatus technicianWorkOrderStatus) {
		return technicianWorkOrderStatusRepository.save(technicianWorkOrderStatus);
	}
	
	public TechnicianWorkOrderStatus find(final Long id) {
		return technicianWorkOrderStatusRepository.findOne(id);
	}
	
	public List<TechnicianWorkOrderStatus> findByTechnician(final Technician technician) {
		return technicianWorkOrderStatusRepository.findByTechnician(technician);
	}
	
	public List<TechnicianWorkOrderStatus> findAllByStatus(final WorkOrderStatus orderStatus) {
		return technicianWorkOrderStatusRepository.findByCurrentStatus(orderStatus);
	}
	
	public TechnicianWorkOrderStatus findByTechnicianAndStatus(final Technician technician, final WorkOrderStatus orderStatus) {
		return technicianWorkOrderStatusRepository.findByTechnicianAndCurrentStatus(technician, orderStatus);
	}

	public TechnicianWorkOrderStatus findByWorkorder(WorkOrder workOrder) {
		return technicianWorkOrderStatusRepository.findByWorkOrder(workOrder);
	} 
}
