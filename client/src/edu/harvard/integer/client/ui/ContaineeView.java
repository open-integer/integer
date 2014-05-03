/*
 * 
 */
package edu.harvard.integer.client.ui;

import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class CapabilityView.
 */
public class ContaineeView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new capability view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public ContaineeView(String title, String[] headers) {
		super(title, headers);
		addButton.setVisible(false);
		
		flexTable.applyDataRowStyles();
		flexTable.getVisualPanel().setSize("200px", "500px");
	}

	/**
	 * Update.
	 *
	 * @param result the result
	 */
	public void update(String name, ServiceElement[] results) {
		flexTable.clean();
		
		updateTitle(name);
		
		if (results == null || results.length == 0)
			return;

		for (ServiceElement se: results) {
			Object[] row = {se.getName(), se.getConfiguredState(), se.getDescription()};
			flexTable.addRow(row);
		}

		flexTable.applyDataRowStyles();
	}
}
