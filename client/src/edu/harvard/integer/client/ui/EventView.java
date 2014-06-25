package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.widget.HvTableViewPanel;

/**
 * The Class EventView represents a event table view panel object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class EventView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new capability view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public EventView(String title, String[] headers) {
		super(title, headers);
		addButton.setVisible(false);
		flexTable.insertRow(1);
		flexTable.insertRow(2);
		
		flexTable.applyDataRowStyles();
		flexTable.getVisualPanel().setSize("100%", "150px");
	}

	/**
	 * Update method will refresh the event view with the given list of objects.
	 *
	 * @param result the result
	 */
	public void update(List<Object> result) {
		if (result == null || result.isEmpty())
			return;
		
		flexTable.clean();

		flexTable.applyDataRowStyles();
	}
}
