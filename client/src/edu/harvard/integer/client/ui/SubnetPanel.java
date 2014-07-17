package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.widget.TitlePanel;
import edu.harvard.integer.common.topology.Network;

public class SubnetPanel extends VerticalPanel {

	public static String SUBNETMAP_TITLE = "Subnet ";
	
	private TitlePanel titlePanel = new TitlePanel(SUBNETMAP_TITLE);
	private SubnetMapPanel subnetMapPanel = new SubnetMapPanel(IntegerMap.MAP_WIDTH, IntegerMap.MAP_HEIGHT);
	
	public SubnetPanel() {
        add(titlePanel);
		add(subnetMapPanel);
		setSize("100%", "100%");
	}

	public SubnetMapPanel getSubnetMapPanel() {
		return subnetMapPanel;
	}
	
	public void updateSubnet(Network network) {
		titlePanel.updateTitle(SUBNETMAP_TITLE + network.getName());
		subnetMapPanel.getSubnetMap().updateNetwork(network);
	}
}
