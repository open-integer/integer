package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;

public class HvMapIconPopup extends PopupPanel {

	private Grid g = new Grid(3, 2);
	
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
