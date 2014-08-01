/*
 * 
 */
package edu.harvard.integer.client.ui;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.Category;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;

/**
 * The ServiceElementGeneralPanel class represents a panel to display general attributes of Service Element.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ServiceElementGeneralPanel extends FormPanel {
	
	/** The date format. */
	public static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("MMM-dd-yyyy HH:mm:ss z");
	
	/** The grid. */
	private Grid grid;
	
	/** The name text box. */
	private TextBox nameTextBox = new TextBox();
	
	/** The desc text box. */
	private TextBox descTextBox = new TextBox();
	
	/** The location text box. */
	private TextBox locationTextBox = new TextBox();
	
	/** The control id text box. */
	private TextBox controlIdTextBox = new TextBox();
	
	/** The criticality text box. */
	private TextBox criticalityTextBox = new TextBox();
	
	/** The created text box. */
	private TextBox createdTextBox = new TextBox();
	
	/** The updated text box. */
	private TextBox updatedTextBox = new TextBox();
	
	/** The comment text box. */
	private TextBox commentTextBox = new TextBox();
	
	/** The category text box. */
	private TextBox categoryTextBox = new TextBox();
	
	/**
	 * Instantiates a new service element general panel.
	 */
	public ServiceElementGeneralPanel() {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		grid = new Grid(9, 2);
		grid.setWidget(0, 0, new Label("Name"));
		grid.setWidget(0, 1, nameTextBox);

		grid.setWidget(1, 0, new Label("Description"));
		grid.setWidget(1, 1, descTextBox);
		
		grid.setWidget(2, 0, new Label("Category"));
		grid.setWidget(2, 1, categoryTextBox);
		
		grid.setWidget(3, 0, new Label("Primary Location"));
		grid.setWidget(3, 1, locationTextBox);

		grid.setWidget(4, 0, new Label("Operational Control ID"));
		grid.setWidget(4, 1, controlIdTextBox);
		
		grid.setWidget(5, 0, new Label("Criticality"));
		grid.setWidget(5, 1, criticalityTextBox);

		grid.setWidget(6, 0, new Label("Created Date"));
		grid.setWidget(6, 1, createdTextBox);
		
		grid.setWidget(7, 0, new Label("Update Date"));
		grid.setWidget(7, 1, updatedTextBox);
		
		grid.setWidget(8, 0, new Label("Comment"));
		grid.setWidget(8, 1, commentTextBox);

		grid.getCellFormatter().setWidth(0, 0, "180px");
		grid.getCellFormatter().setWidth(0, 1, "250px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		
		ScrollPanel scrollPanel = new ScrollPanel(grid);
		scrollPanel.setHeight("100%");
		setWidget(scrollPanel);
	}
	
	/**
	 * Update method will refresh the panel with the given ServiceElement object.
	 *
	 * @param serviceElement the service element
	 */
	public void update(ServiceElement serviceElement) {
		nameTextBox.setText(serviceElement.getName());
		descTextBox.setText(serviceElement.getDescription());
		
		Category category = serviceElement.getCategory();
		categoryTextBox.setText(category != null ? category.toString() : "");
		
		ID primaryLocationId = serviceElement.getPrimaryLocation();
		locationTextBox.setText(primaryLocationId != null ? primaryLocationId.getName() : "");
		
		ID operationalControlId = serviceElement.getOperationalControlId();
		controlIdTextBox.setText(operationalControlId != null ? operationalControlId.getName() : "");
		
		criticalityTextBox.setText(""+serviceElement.getServiceElementCriticality());
		
		Date createdDate = serviceElement.getCreated();
		createdTextBox.setText(createdDate != null ? DATE_FORMAT.format(createdDate) : "");
		
		Date updatedDate = serviceElement.getUpdated();
		updatedTextBox.setText(updatedDate != null ? DATE_FORMAT.format(updatedDate) : "");
		
		String comment = serviceElement.getComment();
		commentTextBox.setText(comment != null ? comment : "");

	}
	
	/**
	 * Update method will refresh the category atttribute with the given ServiceElementType object.
	 *
	 * @param serviceElementType the service element type
	 */
	public void update(ServiceElementType serviceElementType) {
		String category = serviceElementType.getCategory().getName();
		if (category != null)
			categoryTextBox.setText(category);
	}
}
