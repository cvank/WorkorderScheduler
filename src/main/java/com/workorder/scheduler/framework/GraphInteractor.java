/**
 * 
 */
package com.workorder.scheduler.framework;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.SchedulingAlgo;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianLocationMap;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.scheduler.strategy.SchedulingStrategy;
import com.workorder.scheduler.scheduler.strategy.StrategyIdentifier;
import com.workorder.scheduler.service.LocationWorkorderMapService;
import com.workorder.scheduler.service.TechnicianLocationMapService;

/**
 * @author chandrashekar.v
 *
 */
@Component
public class GraphInteractor {

	@Autowired
	StrategyIdentifier strategyIdentifier;

	@Autowired
	TechnicianLocationMapService technicianLocationMapService;

	@Autowired
	LocationWorkorderMapService locationWorkorderMapService;

	public Graph<Location> updateWithLatestWorkOrder(Technician preferredTechnician, WorkOrder workOrder) {
		Graph<Location> graph = getLocationMatrixTechnicianMap(preferredTechnician);
		graph.addNode(workOrder.getLocation());
		addOrUpdateEdges(graph);
		return graph;
	}
	
	public Graph<Location> updateWithLatestWorkOrder(Technician preferredTechnician, List<WorkOrder> workOrders) {
		Graph<Location> graph = getLocationMatrixTechnicianMap(preferredTechnician);
		workOrders.stream().forEach((wo)->graph.addNode(wo.getLocation()));
		addOrUpdateEdges(graph);
		return graph;
	}

	private void addOrUpdateEdges(Graph<Location> graph) {
		List<Location> nodes = graph.getVertexes();
		nodes.stream().forEach((node) -> {
			for (Location neighbor : nodes) {
				if (!node.equals(neighbor))
					graph.addEdge(node, neighbor, getDistance(node, neighbor));
			}
		});
	}

	public Graph<Location> getLocationMatrixTechnicianMap(Technician preferredTechnician) {
		List<LocationWorkOrderMap> locationWorkorderMapData = locationWorkorderMapService
				.findByTechnician(preferredTechnician);
		TechnicianLocationMap locationMap = getTechnicianCurrentLocation(preferredTechnician);
		Graph<Location> graph = constructGraph(locationWorkorderMapData, locationMap);
		return graph;
	}

	/**
	 * Construct graph based on the given locations.
	 * 
	 * @param locationWorkorderMapData
	 * @param currentLocation
	 * @return
	 */
	private Graph<Location> constructGraph(List<LocationWorkOrderMap> locationWorkorderMapData,
			TechnicianLocationMap locationMap) {
		Graph<Location> graph = new Graph<>();
		if (locationMap == null && locationWorkorderMapData == null)
			return graph;

		Location currentLocation = null;
		if (locationMap != null)
			currentLocation = locationMap.getCurrentLocation();

		boolean reachedCurrentLocation = false;
		for (LocationWorkOrderMap locationWorkorder : locationWorkorderMapData) {
			if (currentLocation == null) {
				graph.addNode(locationWorkorder.getLocation());
				continue;
			}
			if (locationWorkorder.getLocation().getLocationId().equals(currentLocation.getLocationId())) {
				reachedCurrentLocation = true;
			} else if (reachedCurrentLocation)
				reachedCurrentLocation = true;

			if (reachedCurrentLocation)
				graph.addNode(locationWorkorder.getLocation());
		}

		addOrUpdateEdges(graph);
		return graph;
	}

	public Map<Location, ConcurrentLinkedQueue<WorkOrder>> getLocationToWorkOrderMap(Technician technician) {
		List<LocationWorkOrderMap> locationWorkorderMapData = locationWorkorderMapService.findByTechnician(technician);

		Map<Location, ConcurrentLinkedQueue<WorkOrder>> locationToWO = new LinkedHashMap<>();

		for (LocationWorkOrderMap locationWorkorder : locationWorkorderMapData) {
			createOrUpdateQueueByLocation(locationToWO, locationWorkorder);
		}

		return locationToWO;
	}

	private void createOrUpdateQueueByLocation(Map<Location, ConcurrentLinkedQueue<WorkOrder>> locationToWO,
			LocationWorkOrderMap locationWorkorder) {
		if (!locationToWO.containsKey(locationWorkorder.getLocation())) {
			ConcurrentLinkedQueue<WorkOrder> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
			concurrentLinkedQueue.add(locationWorkorder.getWorkOrder());
			locationToWO.put(locationWorkorder.getLocation(), concurrentLinkedQueue);
		} else {
			locationToWO.get(locationWorkorder.getLocation()).add(locationWorkorder.getWorkOrder());
		}
	}

	public SchedulingAlgo getActiveAlgorithm() {
		// TODO: fetch from DB.

		return SchedulingAlgo.MST;
	}

	/**
	 * Identifies algorithm to perform scheduling.
	 * 
	 * @param algo
	 * @param priorityQueue
	 * @param technician
	 * @return
	 */
	public SchedulingStrategy identifyStrategy() {
		return strategyIdentifier.identifyStrategy(getActiveAlgorithm());
	}

	public TechnicianLocationMap getTechnicianCurrentLocation(Technician technician) {
		TechnicianLocationMap locationMap = technicianLocationMapService.findByTechnicianId(technician);
		return locationMap;
	}

	public int getDistance(Location currentLocation, Location location) {
		double val = Math.abs(Math.pow((currentLocation.getCoordinates().getLat() - location.getCoordinates().getLat()),
				2)
				+ Math.abs(Math.pow((currentLocation.getCoordinates().getLang() - location.getCoordinates().getLang()),
						2)));
		return Double.valueOf(Math.sqrt(val)).intValue();
	}

}
