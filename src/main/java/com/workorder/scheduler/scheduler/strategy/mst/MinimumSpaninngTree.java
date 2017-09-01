/**
 * 
 */
package com.workorder.scheduler.scheduler.strategy.mst;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import com.workorder.scheduler.framework.Graph;
import com.workorder.scheduler.framework.Location;
import com.workorder.scheduler.framework.Path;

/**
 * @author chandrashekar.v
 *
 */
@Component
public class MinimumSpaninngTree {

	public Queue<Location> identifyPath(final Graph<Location> graph, Location location) {
		List<Location> vertices = new ArrayList<>(graph.getVertexes());
		List<Path> m = getMinimumSpanningTree(vertices, location);
		Queue<Location> mstTourVerticesQueue = new LinkedBlockingQueue<Location>();
		if (m.isEmpty()) {
			mstTourVerticesQueue.addAll(graph.getVertexes());
			return mstTourVerticesQueue;
		}

		return mstTour(m.get(0).getSource(), m, mstTourVerticesQueue);
	}

	private Queue<Location> mstTour(Location vertex, List<Path> mstEdges, Queue<Location> mstTourVerticesQueue) {

		mstTourVerticesQueue.offer(vertex);

		for (Path edge : mstEdges) {
			Location parent = (Location) edge.getSource();
			Location child = (Location) edge.getDestination();
			// for each child not already in tour
			if (parent.equals(vertex) && !mstTourVerticesQueue.contains(child)) {
				mstTourVerticesQueue = mstTour(edge.getDestination(), mstEdges, mstTourVerticesQueue);
			}
		}
		return mstTourVerticesQueue;
	}

	/**
	 * Minimum Spanning Tree Method
	 * 
	 * @param vertices
	 * @return
	 */
	private List<Path> getMinimumSpanningTree(List<Location> vertices, Location location) {

		ArrayList<Path> mstEdges = new ArrayList<Path>();
		ArrayList<Location> treeCities = new ArrayList<Location>();

		// add start Location to tree
		if (location == null)
			location = vertices.remove(0);

		vertices.remove(location);
		treeCities.add(location);

		// while there are vertices not connected to tree
		while (!vertices.isEmpty()) {

			Path lightestEdge = null;
			// initialize minimum distance
			double minDistance = Double.MAX_VALUE;

			// for each Location in tree
			for (Location start : treeCities) {
				// for each Location not yet added to tree
				for (Location end : vertices) {
					if (!start.equals(end)) {
						// get the distance from start to end

						double distance = getDistance(start, end);

						// if distance is a new minimum
						if (distance < minDistance) {
							lightestEdge = new Path(null, start, end, distance);
							// set new minimum distance
							minDistance = distance;
						}
					}
				}
			}
			// remove end from Location array
			vertices.remove(lightestEdge.getDestination());
			treeCities.add(lightestEdge.getDestination());
			// add edge to tree (grow the tree with lightest edge possible)
			mstEdges.add(lightestEdge);
		}

		return mstEdges;

	}

	private int getDistance(Location node, Location neighbor) {
		double val = Math.abs(Math.pow((node.getCoordinates().getLat() - neighbor.getCoordinates().getLat()), 2)
				+ Math.abs(Math.pow((node.getCoordinates().getLang() - neighbor.getCoordinates().getLang()), 2)));
		return Double.valueOf(Math.sqrt(val)).intValue();
	}

}
