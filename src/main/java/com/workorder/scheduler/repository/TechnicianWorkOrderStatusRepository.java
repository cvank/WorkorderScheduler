/**
 * 
 */
package com.workorder.scheduler.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface TechnicianWorkOrderStatusRepository extends CrudRepository<TechnicianWorkOrderStatus, Long> {

	List<TechnicianWorkOrderStatus> findByCurrentStatus(final WorkOrderStatus orderStatus);
	
	List<TechnicianWorkOrderStatus> findByTechnician(final Technician technician);

	TechnicianWorkOrderStatus findByTechnicianAndCurrentStatus(Technician technician, WorkOrderStatus orderStatus);

	TechnicianWorkOrderStatus findByWorkOrder(WorkOrder workOrder); 

}
