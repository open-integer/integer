package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.widget.HvTableViewPanel;

/**
 * The Class CapabilityView.
 */
public class FilterView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new capability view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public FilterView(String viewName, String filterName, String[] headers) {
		super("<h3>Current View: <font color=#0020C2>" + viewName + "</font></h3>", 
			  "<h3>Current Filter: <font color=#0020C2>" + filterName + "</font></h3>", headers);
		addButton.setVisible(false);

		flexTable.setSize("250px", "200px");
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
