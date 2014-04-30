/*
 * 
 */
package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.widget.HvTableViewPanel;

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
		flexTable.insertRow(1);
		flexTable.insertRow(2);
		
		flexTable.applyDataRowStyles();
		flexTable.getVisualPanel().setSize("200px", "500px");
	}

	/**
	 * Update.
	 *
	 * @param result the result
	 */
	public void update(String name, List<Object[]> result) {
		updateTitle(name);
		
		if (result == null || result.isEmpty())
			return;
		
		flexTable.clean();

		int i = 0;
		for (Object[] obj: result) {
			flexTable.addRow(obj);
		}

	}
}
