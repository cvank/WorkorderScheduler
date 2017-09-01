/**
 * 
 */
package com.workorder.scheduler.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workorder.scheduler.framework.Location;

/**
 * @author chandrashekar.v
 *
 */
@Transactional
@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

	List<Location> findByCoordinatesLangAndCoordinatesLat(Integer lang, Integer lat); 

}
