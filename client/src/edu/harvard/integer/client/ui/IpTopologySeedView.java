package edu.harvard.integer.client.ui;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.topology.IpTopologySeed;

/**
 * The Class DiscoveryRuleView represents a IpTopologySeed table view panel object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class IpTopologySeedView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new IpTopologySeed view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public IpTopologySeedView(String title, String[] headers) {
		super(title, headers);
		addButton.setVisible(false);
	}

	/**
	 * Update method will refresh the IpTopologySeed view given by the list of results.
	 *
	 * @param result the result
	 */
	public void update(IpTopologySeed[] result) {
		if (result == null || result.length == 0) {
			MainClient.statusPanel.updateStatus("No IpTopologySeed");
			return;
		}
		
		flexTable.clean();
		
		for (IpTopologySeed seed : result) {
			String name = seed.getName();
			String description = seed.getDescription();
			String subnet = seed.getSubnet() != null ? seed.getSubnet().toString() : "";
			String mask = "Not Supported";
			int radius = seed.getRadius();
			Long discoveryTimeout = seed.getSnmpTimeoutServiceElementDiscovery();
			Integer discoveryRetries = seed.getSnmpRetriesServiceElementDiscovery();
			Long topologyTimeout = seed.getSnmpTimeoutTopologyDiscovery();
			Integer topologyRetries = seed.getSnmpRetriesTopologyDiscovery();
			Address gateway = seed.getInitialGateway();
			String initGateway = gateway != null ? gateway.getAddress() + "/" + gateway.getMask() : "";
			
			Object[] rowData = { name, description, subnet, mask, radius, discoveryTimeout, discoveryRetries, topologyTimeout, topologyRetries, initGateway};
			flexTable.addRow(rowData);
		}
		flexTable.applyDataRowStyles();
		
		MainClient.statusPanel.updateStatus("Total " + result.length + " IpTopologySeed(s)");
	}
}
