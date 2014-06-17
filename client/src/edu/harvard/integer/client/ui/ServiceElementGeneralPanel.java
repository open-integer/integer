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
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;

/**
 * This class represents a form panel for importing MIB file.
 *
 * @author jhuang
 */
public class ServiceElementGeneralPanel extends FormPanel {
	
	public static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("MMM-dd-yyyy HH:mm:ss z");
	private Grid grid;
	private TextBox nameTextBox = new TextBox();
	private TextBox descTextBox = new TextBox();
	private TextBox locationTextBox = new TextBox();
	private TextBox controlIdTextBox = new TextBox();
	private TextBox criticalityTextBox = new TextBox();
	private TextBox createdTextBox = new TextBox();
	private TextBox updatedTextBox = new TextBox();
	private TextBox commentTextBox = new TextBox();
	private TextBox categoryTextBox = new TextBox();
	
	public ServiceElementGeneralPanel() {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		grid = new Grid(9, 2);
		grid.setWidget(0, 0, new Label("Name"));
		grid.setWidget(0, 1, nameTextBox);

		grid.setWidget(1, 0, new Label("Description"));
		grid.setWidget(1, 1, descTextBox);
		
		grid.setWidget(2, 0, new Label("Primary Location"));
		grid.setWidget(2, 1, locationTextBox);

		grid.setWidget(3, 0, new Label("Operational Control ID"));
		grid.setWidget(3, 1, controlIdTextBox);
		
		grid.setWidget(4, 0, new Label("Criticality"));
		grid.setWidget(4, 1, criticalityTextBox);

		grid.setWidget(5, 0, new Label("Created Date"));
		grid.setWidget(5, 1, createdTextBox);
		
		grid.setWidget(6, 0, new Label("Update Date"));
		grid.setWidget(6, 1, updatedTextBox);
		
		grid.setWidget(7, 0, new Label("Comment"));
		grid.setWidget(7, 1, commentTextBox);
		
		grid.setWidget(8, 0, new Label("Category"));
		grid.setWidget(8, 1, categoryTextBox);

		grid.getCellFormatter().setWidth(0, 0, "180px");
		grid.getCellFormatter().setWidth(0, 1, "250px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		
		ScrollPanel scrollPanel = new ScrollPanel(grid);
		scrollPanel.setHeight("100%");
		setWidget(scrollPanel);
	}
	
	public void update(ServiceElement serviceElement) {
		nameTextBox.setText(serviceElement.getName());
		descTextBox.setText(serviceElement.getDescription());
		
		ID primaryLocationId = serviceElement.getPrimaryLocation();
		if (primaryLocationId != null && primaryLocationId.getName() != null)
			locationTextBox.setText(primaryLocationId.getName());
		
		ID operationalControlId = serviceElement.getOperationalControlId();
		if (operationalControlId != null && operationalControlId.getName() != null)
			controlIdTextBox.setText(operationalControlId.getName());
		
		criticalityTextBox.setText(""+serviceElement.getServiceElementCriticality());
		
		Date createdDate = serviceElement.getCreated();
		if (createdDate != null && createdDate.toString() != null)
			createdTextBox.setText(DATE_FORMAT.format(createdDate));
		
		Date updatedDate = serviceElement.getUpdated();
		if (updatedDate != null && updatedDate.toString() != null)
			updatedTextBox.setText(DATE_FORMAT.format(updatedDate));
		
		String comment = serviceElement.getComment();
		if (comment != null)
			commentTextBox.setText(comment);

	}
	
	public void update(ServiceElementType serviceElementType) {
		String category = serviceElementType.getCategory().name();
		if (category != null)
			categoryTextBox.setText(category);
	}
}
