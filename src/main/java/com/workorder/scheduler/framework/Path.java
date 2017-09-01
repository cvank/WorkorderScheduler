/**
 * 
 */
package com.workorder.scheduler.framework;

import com.workorder.scheduler.framework.Path;

/**
 * @author chandrashekar.v
 *
 */
public class Path implements Comparable<Path> {
	private String id;
	private Location source;
	private Location destination;
	private double weight;

	public Path(String id, Location source, Location destination, double weight) {
		this.id = constructIdForEdge(source, destination, weight);
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public Path() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	private String constructIdForEdge(Location source, Location destination, double weight) {
		return source.getName() + "-" + destination.getName() + "-" + weight;
	}

	public String getId() {
		return id;
	}

	public Location getDestination() {
		return destination;
	}

	public Location getSource() {
		return source;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}

	@Override
	public int compareTo(Path o) {
		return ((Double) (this.weight - o.weight)).intValue();
	}

}