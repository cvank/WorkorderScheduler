/**
 * 
 */
package com.workorder.scheduler.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianLocationMap;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface TechnicianLocationMapRepository extends CrudRepository<TechnicianLocationMap, Long> {
	
	public TechnicianLocationMap findByTechnician(Technician technician);

}
