package com.workorder.scheduler.ui.view;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.ui.component.ViewData;

//Pay attention to the order of annotations
@UIScope
@SpringView(name = TechniciansView.VIEW_NAME)
public class TechniciansView extends VerticalLayout implements View {
	private static final long serialVersionUID = -4223149097422298420L;

	public static final String VIEW_NAME = "technicians";

	@Autowired
	ViewData viewData;

	@PostConstruct
	void init() {
		setMargin(true);
		setSpacing(true);
		Grid grid = new Grid();
		grid.setWidth("100%");
		grid.setHeight(-1, Unit.PIXELS);
		showOrFilterTechnicians(grid);
		addComponent(new Label("Technicians"));
		addComponent(grid);
	}

	private void showOrFilterTechnicians(Grid grid) {
		addColumns(grid);
		fetchAll(grid);

	}

	private void addColumns(Grid grid) {
		grid.addColumn("name", String.class);
		grid.addColumn("Start Time", String.class);
		grid.addColumn("End Time", String.class);
		grid.addColumn("Skills", String.class);
	}

	private void fetchAll(Grid grid) {
		viewData.fetchTechnicians().stream().forEach((item) -> {

			// Add some data rows
			grid.addRow(item.getName(), item.getStartTime(), item.getEndTime(), fetchSkills(item.getSkills()));

		});
	}

	private String fetchSkills(Set<Skill> skills) {
		StringBuilder builder = new StringBuilder();
		skills.stream().forEach((skill) -> builder.append(skill.getType()).append(", "));

		return builder.toString().substring(0, builder.length()-2);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}
