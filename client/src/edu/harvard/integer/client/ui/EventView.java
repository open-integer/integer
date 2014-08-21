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
	
	/** The Constant EVENT_VIEW_WIDTH. */
	public static final int EVENT_VIEW_WIDTH = SystemSplitViewPanel.CONTENT_WIDTH;
	
	/** The Constant EVENT_VIEW_HEIGHT. */
	public static final int EVENT_VIEW_HEIGHT = 180;
	
	/**
	 * Instantiates a new capability view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public EventView(String title, String[] headers) {
		super(title, headers);
		// setTableSize("100%", EVENT_VIEW_HEIGHT+"px");
		setSize(EVENT_VIEW_WIDTH+"px", EVENT_VIEW_HEIGHT+"px");
		addButton.setVisible(false);
		flexTable.insertRow(1);
		flexTable.insertRow(2);
		
		flexTable.applyDataRowStyles();
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
