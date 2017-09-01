package com.workorder.scheduler.framework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianLocationMap;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.service.LocationWorkorderMapService;

@Component
public class WorkOrderRouter {

	@Autowired
	GraphInteractor graphInteractor;

	@Autowired
	LocationWorkorderMapService locationWorkorderMapService;

	public PriorityQueue<LocationWorkOrderMap> allocateWorkOrders(Technician preferredTechnician, WorkOrder workOrder) {
		PriorityQueue<LocationWorkOrderMap> locationWorkOrderMaps = null;

		TechnicianLocationMap technicianLocationMap = graphInteractor.getTechnicianCurrentLocation(preferredTechnician);
		Graph<Location> graph = graphInteractor.updateWithLatestWorkOrder(preferredTechnician, workOrder);

		Location currentLocation = null;
		if (null != technicianLocationMap)
			currentLocation = technicianLocationMap.getCurrentLocation();

		Queue<Location> locationPath = graphInteractor.identifyStrategy().apply(graph, currentLocation);

		locationWorkOrderMaps = constructLocationWorkOrderQueue(preferredTechnician, locationPath, workOrder);

		return locationWorkOrderMaps;
	}

	public PriorityQueue<LocationWorkOrderMap> allocateWorkOrders(Technician preferredTechnician,
			List<WorkOrder> workOrders) {
		PriorityQueue<LocationWorkOrderMap> locationWorkOrderMaps = null;

		TechnicianLocationMap technicianLocationMap = graphInteractor.getTechnicianCurrentLocation(preferredTechnician);
		Graph<Location> graph = graphInteractor.updateWithLatestWorkOrder(preferredTechnician, workOrders);

		Location currentLocation = null;
		if (null != technicianLocationMap)
			currentLocation = technicianLocationMap.getCurrentLocation();

		Queue<Location> locationPath = graphInteractor.identifyStrategy().apply(graph, currentLocation);

		locationWorkOrderMaps = constructLocationWorkOrderQueue(preferredTechnician, locationPath, workOrders);

		return locationWorkOrderMaps;
	}

	private PriorityQueue<LocationWorkOrderMap> constructLocationWorkOrderQueue(Technician preferredTechnician,
			Queue<Location> locationPath, WorkOrder workOrder) {
		List<LocationWorkOrderMap> locationWorkorderMapData = locationWorkorderMapService
				.findByTechnician(preferredTechnician);

		Map<Location, LocationWorkOrderMap> map = new HashMap<>();
		if (null != locationWorkorderMapData)
			locationWorkorderMapData.stream().forEach((t) -> map.put(t.getLocation(), t));
		PriorityQueue<LocationWorkOrderMap> queue = new PriorityQueue<>();

		int sequence = 1;
		while (locationPath.size() > 0) {
			Location location = locationPath.poll();
			if (map.get(location) == null || map.get(location).getWorkOrder().getId() != workOrder.getId()) {
				LocationWorkOrderMap locationWorkOrderMap = new LocationWorkOrderMap();
				locationWorkOrderMap.setLocation(location);
				locationWorkOrderMap.setTechnician(preferredTechnician);
				locationWorkOrderMap.setWorkOrder(workOrder);
				map.put(location, locationWorkOrderMap);
			}
			map.get(location).setQueuePosition(sequence++);
			queue.add(map.get(location));
		}

		locationWorkorderMapService.saveAll(map.values());
		return queue;
	}

	private PriorityQueue<LocationWorkOrderMap> constructLocationWorkOrderQueue(Technician preferredTechnician,
			Queue<Location> locationPath, List<WorkOrder> workOrders) {
		List<LocationWorkOrderMap> locationWorkorderMapData = locationWorkorderMapService
				.findByTechnician(preferredTechnician);

		Map<Location, PriorityQueue<LocationWorkOrderMap>> map = new HashMap<>();
		Comparator<? super LocationWorkOrderMap> comparator = (o1, o2) -> o1.getWorkOrder().getPriority()
				.compareTo(o1.getWorkOrder().getPriority());

		List<Long> woAdded = new ArrayList<>();
		if (null != locationWorkorderMapData)
			locationWorkorderMapData.stream().forEach((t) -> {
				workorderQueueByLocation(map, comparator, t.getWorkOrder(), t);
				woAdded.add(t.getWorkOrder().getId());
			});

		PriorityQueue<LocationWorkOrderMap> queue = new PriorityQueue<>(comparator);

		workOrders.stream().forEach((wo) -> {
			if (!woAdded.contains(wo.getId())) {
				LocationWorkOrderMap locationWorkOrderMap = new LocationWorkOrderMap();
				locationWorkOrderMap.setLocation(wo.getLocation());
				locationWorkOrderMap.setTechnician(preferredTechnician);
				locationWorkOrderMap.setWorkOrder(wo);
				workorderQueueByLocation(map, comparator, wo, locationWorkOrderMap);
			}
		});

		int count = 1;
		while(locationPath.size() > 0) {
			Location location = locationPath.poll();

			PriorityQueue<LocationWorkOrderMap> priorityQueue = map.get(location);
			for (LocationWorkOrderMap locationWorkOrderMap : priorityQueue) {
				locationWorkOrderMap.setQueuePosition(count++);
				queue.add(locationWorkOrderMap);
			}
		}

		List<LocationWorkOrderMap> list = new ArrayList<>();
		map.values().stream().forEach((queueData) -> list.addAll(queueData));
		locationWorkorderMapService.saveAll(list);
		return queue;
	}

	private void workorderQueueByLocation(Map<Location, PriorityQueue<LocationWorkOrderMap>> map,
			Comparator<? super LocationWorkOrderMap> comparator, WorkOrder wo,
			LocationWorkOrderMap locationWorkOrderMap) {
		if (map.containsKey(wo.getLocation()))
			map.get(wo.getLocation()).add(locationWorkOrderMap);
		else {
			PriorityQueue<LocationWorkOrderMap> queueByLocation = new PriorityQueue<>(comparator);
			queueByLocation.offer(locationWorkOrderMap);
			map.put(wo.getLocation(), queueByLocation);
		}
	}

}
