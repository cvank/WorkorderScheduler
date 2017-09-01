/**
 * 
 */
package com.workorder.scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workorder.scheduler.framework.Location;
import com.workorder.scheduler.repository.LocationRepository;

/**
 * @author chandrashekar.v
 *
 */
@Service
public class LocationService {

	@Autowired
	LocationRepository locationRepository;

	public Location findOne(Long locationId) {
		return locationRepository.findOne(locationId);
	}

	public Location fetchLocation(String lat, String lang) {
		List<Location> locations = locationRepository.findByCoordinatesLangAndCoordinatesLat(Integer.valueOf(lang),
				Integer.valueOf(lat));
		if (locations != null && !locations.isEmpty())
			return locations.get(0);

		return null;
	}

	public Location saveLocation(Location location) {
		return locationRepository.save(location);
	}

}
