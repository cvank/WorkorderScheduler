/**
 * 
 */
package com.workorder.scheduler.ui.view;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.ui.component.ViewData;

/**
 * @author chandrashekar.v
 *
 */
@SpringView(name = WorkordersView.VIEW_NAME)
public class WorkordersView extends VerticalLayout implements View {
	private static final long serialVersionUID = -3531882237299057406L;

	public static final String VIEW_NAME = "work-orders";

	@Autowired
	ViewData viewData;

	@PostConstruct
	void init() {
		setMargin(true);
		setSpacing(true);
		addComponent(new Label("WorkOrders:"));

		Grid grid = new Grid();
		grid.setWidth("100%");
		grid.setHeight(-1, Unit.PIXELS);
		shoWorkOrders(grid);
		addComponent(grid);
	}

	private void shoWorkOrders(Grid grid2) {
		addWorkOrderColumns(grid2);
		ftchWorkOrders(grid2);

	}

	private void ftchWorkOrders(Grid grid2) {
		viewData.fetchorkorders().stream().forEach((item) -> addToGrid(item, grid2));
	}

	private void addToGrid(WorkOrder item, Grid grid) {
		TechnicianWorkOrderStatus technicianWorkOrderStatus = viewData.fetchTechnicianWorkOrderData(item);
		String technicianName = "Technician NOT Assined";
		String startTimeStr = "";
		if (null != technicianWorkOrderStatus) {
			technicianName = technicianWorkOrderStatus.getTechnician().getName();
			startTimeStr = String.valueOf(technicianWorkOrderStatus.getEstimatedStartTime());
		}

		grid.addRow(item.getId(), technicianName, item.getSkill().getType(), item.getPriority().toString(),
				String.valueOf(item.getCurrentStatus()), locationDisplayName(item), item.getDuration(), startTimeStr);
	}

	private String locationDisplayName(WorkOrder item) {
		return item.getLocation().getName() + "(" + item.getLocation().getCoordinates().getLat() + ","
				+ item.getLocation().getCoordinates().getLang() + ")";
	}

	private void addWorkOrderColumns(Grid grid2) {
		grid2.addColumn("identifier", Long.class);
		grid2.addColumn("Technician", String.class);
		grid2.addColumn("Skill", String.class);
		grid2.addColumn("Priority", String.class);
		grid2.addColumn("Status", String.class);
		grid2.addColumn("Location", String.class);
		grid2.addColumn("duration", Double.class);
		grid2.addColumn("Scheduled Start Time", String.class);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}