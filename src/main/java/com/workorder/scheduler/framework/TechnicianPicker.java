/**
 * 
 */
package com.workorder.scheduler.framework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.domain.TechnicianLocationMap;
import com.workorder.scheduler.domain.TechnicianTimeManager;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkingStatus;
import com.workorder.scheduler.scheduler.strategy.SchedulingStrategy;
import com.workorder.scheduler.service.TechnicianTimeManagerService;

/**
 * @author chandrashekar.v
 *
 */
@Component
public class TechnicianPicker {

	@Autowired
	TechnicianTimeManagerService technicianTimeManagerService;

	@Autowired
	GraphInteractor graphInteractor;

	/**
	 * Picks optimal technician.
	 * 
	 * @param technicians
	 * @param workOrder
	 * @return
	 */
	public Technician pick(PriorityQueue<Technician> technicians, WorkOrder workOrder) {
		if (null == technicians || null == workOrder)
			return null;
		ConcurrentSkipListMap<Double, List<Technician>> probableWinnersMap = new ConcurrentSkipListMap<>();
		technicians.stream().forEach((preferredTechnician) -> {
			process(preferredTechnician, workOrder, probableWinnersMap);
		});
		if (probableWinnersMap.size() > 0) {
			List<Technician> shortListedtechnicians = probableWinnersMap.firstEntry().getValue();
			Comparator<Technician> c = (Technician t1, Technician t2) -> {

				if (t1.getWorkingStatus() == t2.getWorkingStatus())
					return 0;
				else if (t1.getWorkingStatus() == WorkingStatus.IDLE)
					return -1;
				else if (t2.getWorkingStatus() == WorkingStatus.IDLE)
					return 1;

				return 1;
			};
			shortListedtechnicians.sort(c);
			return shortListedtechnicians.get(0); 
		}
		
		return null;


	}

	public void process(Technician preferredTechnician, WorkOrder workOrder, ConcurrentSkipListMap<Double, List<Technician>> probableWinnersMap) {

		TechnicianLocationMap locationMap = graphInteractor.getTechnicianCurrentLocation(preferredTechnician);
		Graph<Location> graph = graphInteractor.updateWithLatestWorkOrder(preferredTechnician, workOrder);

		Location currentLocation = null;
		if (null != locationMap)
			currentLocation = locationMap.getCurrentLocation();

		identifyProbableWinner(graphInteractor.identifyStrategy(), graph, currentLocation, preferredTechnician, probableWinnersMap);

	}

	public void identifyProbableWinner(SchedulingStrategy schedulingStrategy, Graph<Location> graph,
			Location currentLocation, Technician preferredTechnician, ConcurrentSkipListMap<Double, List<Technician>> probableWinnersMap) {

		Map<Location, ConcurrentLinkedQueue<WorkOrder>> locationToWorkOrder = graphInteractor
				.getLocationToWorkOrderMap(preferredTechnician);
		Queue<Location> locationPath = schedulingStrategy.apply(graph, currentLocation);
		double totalTime = calculateTotalTimeRequired(locationToWorkOrder, locationPath);

		TechnicianTimeManager technicianTimeManager = technicianTimeManagerService
				.getTechnicianTimeManager(preferredTechnician);
		if (null == technicianTimeManager || totalTime*3600000 <= technicianTimeManager.getTotalRemainingAtCurrentTask()
				+ technicianTimeManager.getTotalRemainingTimeForTheDay()) {
			addToProbableWinnersList(preferredTechnician, totalTime, probableWinnersMap);
		}

	}

	/**
	 * Add technicians in the order of time available.
	 * 
	 * @param probableWinnersMap
	 * @param preferredTechnician
	 * @param totalTime
	 */
	private void addToProbableWinnersList(Technician preferredTechnician, double totalTime, ConcurrentSkipListMap<Double, List<Technician>> probableWinnersMap) {
		if (probableWinnersMap.containsKey(totalTime))
			probableWinnersMap.get(totalTime).add(preferredTechnician);
		else {
			List<Technician> list = new ArrayList<>();
			list.add(preferredTechnician);
			probableWinnersMap.put(totalTime, list);
		}

	}

	/**
	 * Calculates total time required for technician to complete assigned tasks.
	 * Including the one about to get assigned.
	 * 
	 * @param locationToWorkOrderMap
	 * @param locationPath
	 * @param totalTime
	 */
	private double calculateTotalTimeRequired(Map<Location, ConcurrentLinkedQueue<WorkOrder>> locationToWorkOrderMap,
			Queue<Location> locationPath) {
		double totalTime = 0.0;
		Location prevLocation = null;
		Location curLocation = null;
		for (Location location : locationPath) {
			double distance = 0.0;
			if (prevLocation == null) {
				curLocation = location;
			} else {
				distance = graphInteractor.getDistance(curLocation, prevLocation);
			}
			double workOrderTime = 0.0;
			if (null != locationToWorkOrderMap && locationToWorkOrderMap.size() > 0) {
				ConcurrentLinkedQueue<WorkOrder> workOrdersQueue = locationToWorkOrderMap.get(curLocation);
				for (WorkOrder workOrder : workOrdersQueue) {
					workOrderTime += workOrder.getDuration();
				}
			}
			totalTime += workOrderTime + getTimeToTravel(distance);

			prevLocation = curLocation;
		}

		return totalTime;
	}

	/**
	 * Time required to travel given distance. Distance multiplied by 10
	 * minuites
	 * 
	 * @param distance
	 * @return
	 */
	private double getTimeToTravel(double distance) {
		return distance * 10 / 60;
	}
	
}
