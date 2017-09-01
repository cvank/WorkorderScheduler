/**
 * 
 */
package com.workorder.scheduler.ui.view;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Technician;
import com.workorder.scheduler.ui.chart.MyImageSource;
import com.workorder.scheduler.ui.chart.TechnicianWorkOrderRouteMap;
import com.workorder.scheduler.ui.component.ViewData;

/**
 * @author chandrashekar.v
 *
 */
@SpringView(name = ScheduleView.VIEW_NAME)
public class ScheduleView extends VerticalLayout implements View {
	@Autowired
	ViewData viewData;

	private static final long serialVersionUID = -6785410509835787835L;

	public static final String VIEW_NAME = "schedule";

	@Autowired
	TechnicianWorkOrderRouteMap routeMap;

	@PostConstruct
	void init() {
		setMargin(true);
		setSpacing(true);
		setWidth(-1, Unit.PIXELS); 
		setHeight(-1, Unit.PIXELS); 
		final List<com.workorder.scheduler.domain.Technician> technicians = viewData.fetchTechnicians();

		for (Technician technician : technicians) {
			Image image = plot(technician);
			if (null != image)
				addComponent(image);
		}

	}

	private Image plot(Technician technician) {
		List<LocationWorkOrderMap> locationWorkOrderMaps = viewData.fetchLocationWorkOrderData(technician);
		if (null != locationWorkOrderMaps && !locationWorkOrderMaps.isEmpty()) {
			String path = routeMap.plotChart(locationWorkOrderMaps);

			MyImageSource imagesource = new MyImageSource();
			imagesource.setPath(path);
			StreamResource resource = new StreamResource(imagesource, technician.getName());
			Image image = new Image(technician.getName(), resource);

			return image;
		} else
			return null;

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}