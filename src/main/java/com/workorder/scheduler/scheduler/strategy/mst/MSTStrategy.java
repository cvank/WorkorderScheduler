/**
 * 
 */
package com.workorder.scheduler.scheduler.strategy.mst;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.SchedulingAlgo;
import com.workorder.scheduler.framework.Graph;
import com.workorder.scheduler.framework.Location;
import com.workorder.scheduler.scheduler.strategy.SchedulingStrategy;
import com.workorder.scheduler.scheduler.strategy.Strategy;

/**
 * @author chandrashekar.v
 *
 */
@Component
@Strategy(value = SchedulingAlgo.MST)
public class MSTStrategy implements SchedulingStrategy {

	@Autowired
	MinimumSpaninngTree minimumSpaninngTree;

	@Override
	public Queue<Location> apply(Graph<Location> locationMatrix, Location currentLocation) {
		Queue<Location> locationsOrder = minimumSpaninngTree.identifyPath(locationMatrix, currentLocation);
		return locationsOrder;
	}

}
