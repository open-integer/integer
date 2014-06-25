package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * The Class HvMapIconPopup represents a PopupPanel showing name, status and location
 * This is a subclass class extended from com.google.gwt.user.client.ui.PopupPanel.
 * It includes 
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvMapIconPopup extends PopupPanel {

	/** The g. */
	private Grid g = new Grid(3, 2);
	
	/**
	 * Creates a new HvMapIconPopup instance.
	 *
	 * @param name the name
	 * @param status the status
	 * @param location the location
	 */
	public HvMapIconPopup(String name, String status, String location) {
		super(true);
		
		setStyleName("MapIconPopup");
		
		g.setText(0, 0, "Name");
		g.setText(0, 1, name);
		
		g.setText(1, 0, "Status");
		g.setText(1, 1, status);
		
		g.setText(2, 0, "Location");
		g.setText(2, 1, location);
		
		setWidget(g);

	}
}
