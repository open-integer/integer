package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;

public class NetworkPanel extends VerticalPanel {

	private NetworkMapPanel networkMapPanel = new NetworkMapPanel(IntegerMap.MAP_WIDTH, IntegerMap.MAX_MAP_HEIGHT);
	
	public NetworkPanel() {
		add(networkMapPanel);
		setSize("100%", "100%");
	}

	public NetworkMapPanel getNetworkMapPanel() {
		return networkMapPanel;
	}
}
