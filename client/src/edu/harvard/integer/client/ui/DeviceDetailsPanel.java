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
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.DeviceDetails;

/**
 * This class represents a form panel for importing MIB file.
 *
 * @author jhuang
 */
public class DeviceDetailsPanel extends FormPanel {
	
	public static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("MMM-dd-yyyy HH:mm:ss z");
	
	public DeviceDetailsPanel(String name, DeviceDetails deviceDetails) {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(8, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		final TextBox nameTextBox = new TextBox();
		nameTextBox.setText(name);
		grid.setWidget(0, 1, nameTextBox);

		grid.setWidget(1, 0, new Label("Description"));
		final TextBox descTextBox = new TextBox();
		descTextBox.setText(deviceDetails.getDescription());
		grid.setWidget(1, 1, descTextBox);
		
		grid.setWidget(2, 0, new Label("Primary Location"));
		final TextBox locationTextBox = new TextBox();
		ID primaryLocationId = deviceDetails.getPrimaryLocation();
		if (primaryLocationId != null && primaryLocationId.getName() != null)
			locationTextBox.setText(primaryLocationId.getName());
		grid.setWidget(2, 1, locationTextBox);

		grid.setWidget(3, 0, new Label("Operational Control ID"));
		final TextBox controlIdTextBox = new TextBox();
		ID operationalControlId = deviceDetails.getOperationalControlId();
		if (operationalControlId != null && operationalControlId.getName() != null)
			controlIdTextBox.setText(operationalControlId.getName());
		grid.setWidget(3, 1, controlIdTextBox);
		
		grid.setWidget(4, 0, new Label("Criticality"));
		final TextBox criticalityTextBox = new TextBox();
		criticalityTextBox.setText(""+deviceDetails.getServiceElementCriticality());
		grid.setWidget(4, 1, criticalityTextBox);

		grid.setWidget(5, 0, new Label("Created Date"));
		final TextBox createdTextBox = new TextBox();
		Date createdDate = deviceDetails.getCreated();
		if (createdDate != null && createdDate.toString() != null)
			createdTextBox.setText(DATE_FORMAT.format(createdDate));
		grid.setWidget(5, 1, createdTextBox);
		
		grid.setWidget(6, 0, new Label("Update Date"));
		final TextBox updatedTextBox = new TextBox();
		Date updatedDate = deviceDetails.getUpdated();
		if (updatedDate != null && updatedDate.toString() != null)
			updatedTextBox.setText(DATE_FORMAT.format(updatedDate));
		grid.setWidget(6, 1, updatedTextBox);
		
		grid.setWidget(7, 0, new Label("Comment"));
		final TextBox commentTextBox = new TextBox();
		String comment = deviceDetails.getComment();
		if (comment != null)
			commentTextBox.setText(comment);
		grid.setWidget(7, 1, commentTextBox);

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "180px");
		grid.getCellFormatter().setWidth(0, 1, "250px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

	}
}
