/**
 * 
 */
package com.workorder.scheduler.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.framework.Location;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface WorkOrderRepository extends CrudRepository<WorkOrder, Long> {

	List<WorkOrder> findBySkillAndCurrentStatusIn(Skill skill, Collection<WorkOrderStatus> collection);

	List<WorkOrder> findBySkillAndCurrentStatus(Skill skill, WorkOrderStatus collection);

	public Location findLocationById(Long wo);
}
