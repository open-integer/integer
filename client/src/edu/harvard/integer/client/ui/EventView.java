package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.widget.HvTableViewPanel;

/**
 * The Class CapabilityView.
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
		flexTable.getVisualPanel().setSize("1150px", "200px");
	}

	/**
	 * Update.
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
