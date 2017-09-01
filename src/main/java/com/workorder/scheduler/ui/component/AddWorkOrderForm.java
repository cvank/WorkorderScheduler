/**
 * 
 */
package com.workorder.scheduler.ui.component;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.Priority;
import com.workorder.scheduler.domain.Skill;
import com.workorder.scheduler.domain.TechnicianWorkOrderStatus;
import com.workorder.scheduler.domain.WorkOrder;
import com.workorder.scheduler.domain.WorkOrderStatus;
import com.workorder.scheduler.framework.Location;
import com.workorder.scheduler.scheduler.strategy.mst.Coordinates;
import com.workorder.scheduler.ui.MyUI;

/**
 * @author chandrashekar.v
 *
 */
@SpringComponent
@UIScope
public class AddWorkOrderForm extends FormLayout {
	private static final long serialVersionUID = -656131447979887918L;
	ComboBox priority = new ComboBox("Priority");
	ComboBox skill = new ComboBox("Skill");
	TextField duration = new TextField("Duration(In Hours)");
	TextField lat = new TextField("X");
	TextField lang = new TextField("Y");

	FormLayout form = new FormLayout();

	public FormLayout getForm() {
		return form;
	}

	public void setForm(FormLayout form) {
		this.form = form;
	}

	@Autowired
	ViewData viewData;

	@PostConstruct
	public void create() {

		form.setCaption("Create New Work Order");

		// Duration
		durationField(form);

		// Skill
		populateSkills(form);

		// Priority
		populatePriority(form);

		// Location
		coordinates(form);

		addButtons(form);
	}

	private void addButtons(FormLayout form) {
		// A button to commit the buffer
		Button addButton = new Button("Create Work Order");
		addButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				listen(event);
			}

			private void listen(ClickEvent event) {
				Button source = event.getButton();
				FormLayout formLayout = (FormLayout) source.getParent();
				String duration = null;
				String lat = null;
				String lang = null;
				String skill = null;
				Priority priority = null;
				for (Component c : formLayout) {
					if (c instanceof Field) {
						Field f = (Field) c;
						if (f.getCaption().equals("Duration(In Hours)"))
							duration = (String) f.getValue();
						else if (f.getCaption().equals("Priority"))
							priority = (Priority) f.getValue();
						else if (f.getCaption().equals("Skill"))
							skill = (String) f.getValue();
					} else if (c instanceof Layout) {
						Layout layout = (Layout) c;
						for (Component c1 : layout) {
							if (c1 instanceof Field) {
								Field f1 = (Field) c1;
								if (f1.getCaption().equals("Location-X"))
									lat = (String) f1.getValue();
								else if (f1.getCaption().equals("Location-Y"))
									lang = (String) f1.getValue();
							}
						}
					}
				}
				WorkOrder workOrder = new WorkOrder();
				workOrder.setDuration(Double.valueOf(duration));
				Location location = viewData.fetchLocation(lat, lang);
				if (null == location) {
					Coordinates coordinates = new Coordinates(Integer.valueOf(lat), Integer.valueOf(lang));
					location = new Location("Location(" + lat + "," + lang + ")", coordinates);
					location = viewData.saveLocation(location);
				}
				workOrder.setLocation(location);
				workOrder.setPriority(priority);
				Skill skillData = viewData.fetchSkillByName(skill);
				if (null == skillData) {
					skillData = new Skill();
					skillData.setType(skill);
				}
				workOrder.setSkill(skillData);

				workOrder = viewData.saveWorkOrder(workOrder);
				MyUI current = (MyUI) UI.getCurrent();
				TechnicianWorkOrderStatus orderStatus = viewData.fetchTechnicianWorkOrderData(workOrder);
				Notification notification = new Notification("Successfully Created Work Order with following details:",
						constructNotificationMessage(workOrder, orderStatus), Type.TRAY_NOTIFICATION, true);
				notification.setDelayMsec(10000);
				notification.setIcon(FontAwesome.THUMBS_O_UP); 
				current.showNotification(notification);
			}

			private String constructNotificationMessage(WorkOrder workOrder, TechnicianWorkOrderStatus orderStatus) {
				StringBuilder message = new StringBuilder();
				if (null != workOrder) {
					message.append("<br>Identifier:<b>" + workOrder.getId() + "</b></br>");
					if (workOrder.getCurrentStatus() == WorkOrderStatus.ASSIGNED
							|| workOrder.getCurrentStatus() == WorkOrderStatus.WORK_IN_PROGRESS) {
						message.append("<br>Technician Name:<b>" + orderStatus.getTechnician().getName() + "</b>");
						message.append("<br>Scheduled Time:<b>" + orderStatus.getEstimatedStartTime() + "</b>");
					}
					LocationWorkOrderMap locationWorkOrderMap = viewData.fetchLocationWorkOrderData(workOrder);
					if (null != locationWorkOrderMap)
						message.append("<br>Queue Position:<b>" + locationWorkOrderMap.getQueuePosition() + "</b>");
				}
				return message.toString();
			}
		});

		form.addComponent(addButton);
	}

	private void coordinates(FormLayout form) {
		lat.setCaption("Location-X");
		lat.setIcon(FontAwesome.LOCATION_ARROW);
		lat.setInputPrompt("Enter Between 1 to 100");

		lang.setCaption("Location-Y");
		lang.setIcon(FontAwesome.LOCATION_ARROW);
		lang.setInputPrompt("Enter Between 1 to 100");

		HorizontalLayout horizontalLayout = new HorizontalLayout(lat, lang);
		form.addComponent(horizontalLayout);
	}

	private void durationField(FormLayout form) {
		duration.setRequired(true);
		duration.setIcon(FontAwesome.CLOCK_O);
		duration.addValidator(new NullValidator("Duration Must be given", false));
		form.addComponent(duration);
	}

	private void populateSkills(FormLayout form) {
		List<Skill> skills = viewData.fetchSkills();
		skill.setInvalidAllowed(false);
		skill.setNullSelectionAllowed(false);
		skills.stream().forEach(item -> skill.addItem(item.getType()));
		form.addComponent(skill);
	}

	private void populatePriority(FormLayout form) {
		priority.setNullSelectionAllowed(false);

		priority.addItem(Priority.CRITICAL);
		priority.addItem(Priority.HIGH);
		priority.addItem(Priority.LOW);
		priority.addItem(Priority.MEDIUM);

		// Preselect an item
		priority.setValue(Priority.HIGH);
		form.addComponent(priority);
	}

	public ComboBox getSkill() {
		return skill;
	}

	public void setSkill(ComboBox skill) {
		this.skill = skill;
	}

	public TextField getDuration() {
		return duration;
	}

	public void setDuration(TextField duration) {
		this.duration = duration;
	}

	public TextField getLat() {
		return lat;
	}

	public void setLat(TextField lat) {
		this.lat = lat;
	}

	public TextField getLang() {
		return lang;
	}

	public void setLang(TextField lang) {
		this.lang = lang;
	}

	public ViewData getViewData() {
		return viewData;
	}

	public void setViewData(ViewData viewData) {
		this.viewData = viewData;
	}

	public ComboBox getPriority() {
		return priority;
	}

	public void setPriority(ComboBox priority) {
		this.priority = priority;
	}

}