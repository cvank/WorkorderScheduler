package com.workorder.scheduler.ui.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class MyErrorView extends HorizontalLayout implements View {

	private static final long serialVersionUID = 8251148062777463183L;

	@Override
	public void enter(ViewChangeEvent event) {
		addComponent(new Label("Error Occurred"));

	}

}
