/**
 * 
 */
package com.workorder.scheduler.scheduler.strategy;

import java.util.Queue;

import com.workorder.scheduler.framework.Graph;
import com.workorder.scheduler.framework.Location;

/**
 * @author chandrashekar.v
 *
 */
public interface SchedulingStrategy {

	public Queue<Location> apply(Graph<Location> locationMatrix, Location currentLocation);
}
