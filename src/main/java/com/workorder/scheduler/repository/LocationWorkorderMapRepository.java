/**
 * 
 */
package com.workorder.scheduler.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface LocationWorkorderMapRepository extends CrudRepository<LocationWorkOrderMap, Long> {

	List<LocationWorkOrderMap> findByTechnicianOrderByQueuePositionAsc(Technician technician);

	List<LocationWorkOrderMap> findByTechnicianAndWorkOrderCurrentStatusAndWorkOrderScheduledDateOrderByQueuePositionAsc(
			Technician technician, WorkOrderStatus workOrderStatus, Date scheduledDate);

	LocationWorkOrderMap findByWorkOrder(WorkOrder workOrder);

}
