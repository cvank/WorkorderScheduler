/**
 * 
 */
package com.workorder.scheduler.ui.view;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.ui.component.AddWorkOrderForm;
import com.workorder.scheduler.ui.component.ViewData;

/**
 * @author chandrashekar.v
 *
 */
@SpringView(name = AddWorkorderView.VIEW_NAME)
public class AddWorkorderView extends VerticalLayout implements View {
	private static final long serialVersionUID = -3531882237299057406L;

	public static final String VIEW_NAME = "add-work-order";

	@Autowired
	AddWorkOrderForm addWorkOrderForm;

	@PostConstruct
	void init() {
		setMargin(true);
		setSpacing(true);
		addComponent(addWorkOrderForm.getForm());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}