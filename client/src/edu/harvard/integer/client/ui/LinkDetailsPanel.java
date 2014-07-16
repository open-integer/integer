/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.InterNetworkLink;

/**
 * The Class DeviceDetailsPanel represents a panel to display DeviceDetails object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class LinkDetailsPanel extends FormPanel {
	
	public static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("MMM-dd-yyyy HH:mm:ss z");
	
	private Grid grid = new Grid(5, 2);
	
	/**
	 * Create a new DeviceDetailsPanel.
	 */
	public LinkDetailsPanel() {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		setWidget(grid);
	}
	
	public void update(InterNetworkLink link) {
		grid.setWidget(0, 0, new Label("Name"));
		grid.setWidget(0, 1, new Label(link.getName()));

		grid.setWidget(1, 0, new Label("Status"));
		grid.setWidget(1, 1, new Label("Not supported"));
		
		grid.setWidget(2, 0, new Label("Source"));
		String srcAddress = "not available";
		if (link.getSourceAddress() != null)
			srcAddress = link.getSourceAddress().getAddress();
		grid.setWidget(2, 1, new Label(srcAddress));
		
		grid.setWidget(3, 0, new Label("Destination"));
		String destAddress = "not available";
		if (link.getDestinationAddress() != null)
			destAddress = link.getDestinationAddress().getAddress();
		grid.setWidget(3, 1, new Label(destAddress));

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "180px");
		grid.getCellFormatter().setWidth(0, 1, "250px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public void update(InterDeviceLink link) {
		grid.setWidget(0, 0, new Label("Name"));
		grid.setWidget(0, 1, new Label(link.getName()));

		grid.setWidget(1, 0, new Label("Status"));
		grid.setWidget(1, 1, new Label("Not supported"));
		
		grid.setWidget(2, 0, new Label("Source"));
		String srcAddress = "not available";
		if (link.getSourceAddress() != null)
			srcAddress = link.getSourceAddress().getAddress();
		grid.setWidget(2, 1, new Label(srcAddress));
		
		grid.setWidget(3, 0, new Label("Destination"));
		String destAddress = "not available";
		if (link.getDestinationAddress() != null)
			destAddress = link.getDestinationAddress().getAddress();
		grid.setWidget(3, 1, new Label(destAddress));

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
