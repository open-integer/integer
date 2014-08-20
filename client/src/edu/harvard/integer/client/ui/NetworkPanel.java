package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NetworkPanel extends VerticalPanel {

	//private HvTitleBar titlePanel = new HvTitleBar("Network Map");
	private NetworkMapPanel networkMapPanel = new NetworkMapPanel(IntegerMap.MAP_WIDTH, IntegerMap.MAP_HEIGHT);
	
	public NetworkPanel() {
        //add(titlePanel);
		ScrollPanel scrollPanel = new ScrollPanel(networkMapPanel);
		scrollPanel.setAlwaysShowScrollBars(true);
		scrollPanel.setSize(IntegerMap.MAP_WIDTH+"px", IntegerMap.MAP_HEIGHT+"px");
		add(scrollPanel);
		setSize("100%", "100%");
	}

	public NetworkMapPanel getNetworkMapPanel() {
		return networkMapPanel;
	}
}
