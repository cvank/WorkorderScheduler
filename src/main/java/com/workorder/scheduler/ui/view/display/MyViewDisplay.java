/**
 * 
 */
package com.workorder.scheduler.ui.view.display;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;

/**
 * @author chandrashekar.v
 *
 */
@SpringViewDisplay
public class MyViewDisplay implements ViewDisplay {

	private static final long serialVersionUID = 6994875496400950295L;
	private Panel springViewDisplay;

	public Panel getSpringViewDisplay() {
		return springViewDisplay;
	}

	public void setSpringViewDisplay(Panel springViewDisplay) {
		this.springViewDisplay = springViewDisplay;
	}

	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}


}
