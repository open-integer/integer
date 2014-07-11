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

	/** The grid */
	private Grid grid = new Grid(3, 2);
	
	public HvMapIconPopup() {
		super(true);
		setStyleName("MapIconPopup");
		setWidget(grid);
	}
	
	/**
	 * Creates a new HvMapIconPopup instance.
	 *
	 * @param name the name
	 * @param status the status
	 * @param location the location
	 */
	public HvMapIconPopup(String name) {
		this();
		update(name);
	}
	
	public void update(String name) {
		grid.setText(0, 0, "Name");
		grid.setText(0, 1, name);
	}
}
