/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.repository.SkillRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class SkillService {

	@Autowired
	SkillRepository skillRepository;

	public Skill findOne(final Long skill) {
		return skillRepository.findOne(skill);

	}

	public List<Skill> findAll() {
		return (ArrayList<Skill>) skillRepository.findAll();
	}

	public Skill findByType(String skill) { 
		return skillRepository.findByType(skill); 
	}

}
