/*
 * 
 */
package edu.harvard.integer.client.ui;

import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class ContainedView represents a contained view object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ContainedView extends HvTableViewPanel {
	
	/**
	 * Create a new ContainedView.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public ContainedView(String title, String[] headers) {
		super(title, headers);
		addButton.setVisible(false);
		
		flexTable.applyDataRowStyles();
		flexTable.getVisualPanel().setSize("250px", "500px");
	}

	/**
	 * Update method will refresh the contained tree view of the object by object's name and the list of service element objects.
	 *
	 * @param result the result
	 */
	public void update(String name, ServiceElement[] results) {
		flexTable.clean();
		
		updateTitle(name);
		
		if (results == null || results.length == 0)
			return;
 
		for (ServiceElement se: results) {
			String elementName = se.getName();
			if (elementName == null)
				elementName = "N/A";
			
			String desciption = null;
			if (desciption == null)
				desciption = "N/A";
			
			Object[] row = {elementName, "n/a", desciption};
			flexTable.addRow(row);
		}

		flexTable.applyDataRowStyles();
	}
}
