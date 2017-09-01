/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.repository.LocationWorkorderMapRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class LocationWorkorderMapService {

	@Autowired
	LocationWorkorderMapRepository locationWorkorderMapRepository;

	public List<LocationWorkOrderMap> findByTechnician(Technician technician) {
		return locationWorkorderMapRepository.findByTechnicianOrderByQueuePositionAsc(technician);
	}

	public void saveAll(Collection<LocationWorkOrderMap> values) {
		locationWorkorderMapRepository.save(values);

	}

	public List<LocationWorkOrderMap> findTodaysWaitingOrdersByTechnician(Technician technician,
			WorkOrderStatus workOrderStatus, Date scheduledDate) {
		return locationWorkorderMapRepository
				.findByTechnicianAndWorkOrderCurrentStatusAndWorkOrderScheduledDateOrderByQueuePositionAsc(technician,
						workOrderStatus, scheduledDate);
	}

	public LocationWorkOrderMap findByWorkorder(WorkOrder workOrder) {
		return locationWorkorderMapRepository.findByWorkOrder(workOrder);
	}

}
