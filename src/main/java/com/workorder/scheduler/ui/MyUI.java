/**
 * 
 */
package com.workorder.scheduler.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.workorder.scheduler.ui.component.ViewData;
import com.workorder.scheduler.ui.error.MyErrorView;
import com.workorder.scheduler.ui.view.AddWorkorderView;
import com.workorder.scheduler.ui.view.ScheduleView;
import com.workorder.scheduler.ui.view.TechniciansView;
import com.workorder.scheduler.ui.view.WorkordersView;
import com.workorder.scheduler.ui.view.display.MyViewDisplay;

/**
 * @author chandrashekar.v
 *
 */
@Theme("valo")
@SpringUI(path = "/test")
public class MyUI extends UI {

	@Autowired
	private MyViewDisplay myViewDisplay;

	private static final long serialVersionUID = 873341727631742020L;

	@Autowired
	ViewData viewData;

	@Override
	protected void init(VaadinRequest request) {
		// setContent(new Label(greeter.sayHello()));

		getNavigator().setErrorView(MyErrorView.class);
		final VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.setCaption("Work Order scheduling");
		root.setSpacing(true);
		setContent(root);

		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		navigationBar.addComponent(createNavigationButton("Technicians", TechniciansView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Work Orders", WorkordersView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Schedule", ScheduleView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Add Work Order", AddWorkorderView.VIEW_NAME));

		root.addComponent(navigationBar);
		myViewDisplay.setSpringViewDisplay(new Panel());
		myViewDisplay.getSpringViewDisplay().setSizeFull();
		myViewDisplay.getSpringViewDisplay().setCaption("Workorder scheduling");
		myViewDisplay.getSpringViewDisplay().setData(new String("testdata"));
		myViewDisplay.getSpringViewDisplay().setResponsive(true);
		root.addComponent(myViewDisplay.getSpringViewDisplay());
		root.setExpandRatio(myViewDisplay.getSpringViewDisplay(), 1.0f);

	}

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		// If you didn't choose Java 8 when creating the project, convert this
		// to an anonymous listener class
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}
}
