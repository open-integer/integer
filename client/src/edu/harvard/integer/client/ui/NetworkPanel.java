package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.widget.HvTitlePanel;

public class NetworkPanel extends VerticalPanel {

	private HvTitlePanel titlePanel = new HvTitlePanel("Network Map");
	private NetworkMapPanel networkMapPanel = new NetworkMapPanel(IntegerMap.MAP_WIDTH, IntegerMap.MAP_HEIGHT);
	
	public NetworkPanel() {
        add(titlePanel);
		add(networkMapPanel);
		setSize("100%", "100%");
	}

	public NetworkMapPanel getNetworkMapPanel() {
		return networkMapPanel;
	}
}
