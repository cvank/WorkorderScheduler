package com.workorder.scheduler.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Graph<T> {
	private final List<Location> vertexes = new ArrayList<>();
	private final List<Path> edges = new ArrayList<>();

	private final Map<Location, CopyOnWriteArraySet<Location>> adjGraph = new ConcurrentHashMap<>();

	public List<Location> getVertexes() {
		return vertexes;
	}

	public List<Path> getEdges() {
		return edges;
	}

	/**
	 * Adds a new node to the graph. If the node already exists, this function
	 * is a no-op.
	 *
	 * @param node
	 *            The node to add.
	 * @return Whether or not the node was added.
	 */
	public boolean addNode(Location node) {
		/* If the node already exists, don't do anything. */
		if (vertexes.contains(node))
			return false;

		/* Otherwise, add the node with an empty set of outgoing edges. */
		vertexes.add(node);

		if (!adjGraph.containsKey(node)) {
			/* Otherwise, add the node with an empty set of outgoing edges. */
			adjGraph.put(node, new CopyOnWriteArraySet<Location>());
		}
		return true;

	}

	/**
	 * Given two nodes, adds an arc of that length between those nodes. If
	 * either endpoint does not exist in the graph, throws a
	 * NoSuchElementException.
	 *
	 * @param one
	 *            The first node.
	 * @param two
	 *            The second node.
	 * @throws NoSuchElementException
	 *             If either the start or destination nodes do not exist.
	 */
	public void addEdge(Location one, Location two, int weight) {
		/* Confirm both end points exist. */
		if (!vertexes.contains(one) || !vertexes.contains(two))
			throw new NoSuchElementException("Both nodes must be in the graph.");

		/* Add the edge in both directions. */
		Path edge = new Path(null, one, two, weight);
		if (!edges.contains(edge)) {
			edges.add(edge);
		}

		if (!adjGraph.containsKey(one) || !adjGraph.containsKey(two))
			throw new NoSuchElementException("Both nodes must be in the graph.");

		/* Add the edge in both directions. */
		adjGraph.get(one).add(two);
		adjGraph.get(two).add(one);

	}

	/**
	 * Given a node in the graph, returns an immutable view of the edges leaving
	 * that node.
	 *
	 * @param node
	 *            The node whose edges should be queried.
	 * @return An immutable view of the edges leaving that node.
	 * @throws NoSuchElementException
	 *             If the node does not exist.
	 */
	public Set<Location> edgesFrom(Location node) {
		/* Check that the node exists. */
		Set<Location> arcs = adjGraph.get(node);
		if (arcs == null)
			throw new NoSuchElementException("Source node does not exist.");

		return Collections.unmodifiableSet(arcs);
	}

}