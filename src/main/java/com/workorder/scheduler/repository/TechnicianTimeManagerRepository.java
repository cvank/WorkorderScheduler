/**
 * 
 */
package com.workorder.scheduler.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianTimeManager;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface TechnicianTimeManagerRepository extends CrudRepository<TechnicianTimeManager, Long> {

	TechnicianTimeManager findByTechnicianAndDateOfAllocation(Technician technician, Date date);

}
