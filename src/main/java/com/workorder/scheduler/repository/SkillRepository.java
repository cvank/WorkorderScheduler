/**
 * 
 */
package com.workorder.scheduler.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.domain.Skill;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {

	Skill findByType(String skill); 

}
