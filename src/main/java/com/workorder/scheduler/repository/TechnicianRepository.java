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
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.WorkingStatus;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface TechnicianRepository extends CrudRepository<Technician, Long> {
	List<Technician> findBySkillsAndWorkingStatusIn(Skill skill, Collection<WorkingStatus> workingStatus);

	List<Technician> findByNameContaining(final String name);

}
