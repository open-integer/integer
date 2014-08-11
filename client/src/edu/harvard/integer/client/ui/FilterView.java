package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.widget.HvTableViewPanel;

/**
 * The Class FilterView represents a filter view object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class FilterView extends HvTableViewPanel {

	/**
	 * The Class FilterPanel represents a filter panel object of Integer. This
	 * is a subclass class extended from
	 * com.google.gwt.user.client.ui.DockPanel.
	 * 
	 * @author Joel Huang
	 * @version 1.0, May 2014
	 */
	public FilterView(String viewName, String filterName, String[] headers) {
		super("Current View: " + viewName, "Current Filter: " + filterName,
				headers);
		addButton.setVisible(false);

		//flexTable.setSize("250px", "200px");
		setTableSize("100%", "100%");
	}

	/**
	 * Update method will refresh the event view with the given list of objects.
	 * 
	 * @param result
	 *            the result
	 */
	public void update(List<Object> result) {
		if (result == null || result.isEmpty())
			return;

		flexTable.clean();

		flexTable.applyDataRowStyles();
	}
}
