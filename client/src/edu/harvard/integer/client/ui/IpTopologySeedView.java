package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.client.widget.HvTitlePanel;
import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.IpTopologySeed;

/**
 * The Class DiscoveryRuleView represents a IpTopologySeed table view panel object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class IpTopologySeedView extends HvTableViewPanel {
	
	private List<IpTopologySeed> list = new ArrayList<IpTopologySeed>();
	private IpTopologySeedPanel topologySeedPanel = new IpTopologySeedPanel();
	
	/**
	 * Instantiates a new IpTopologySeed view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public IpTopologySeedView(String title, String[] headers) {
		super(title, headers);
		
		setSize("100%", "100%");
		addButton.setVisible(false);
		
		HvTitlePanel detailsTitlePanel = new HvTitlePanel("IP Topology Seed", topologySeedPanel);
		detailsPanel.setWidget(detailsTitlePanel);
		
		flexTable.addTableListener( new TableListener(){

			@Override
			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				IpTopologySeed seed = list.get(row-1);
				
				if (seed == null)
					return;
				
				topologySeedPanel.update(seed);
				flexTable.setHighlighted(row);
				
				// show detailsPanel with the rulePanel
				splitPanel.setWidgetHidden(detailsPanel, false);
			}
           
        });
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
			int radius = seed.getRadius();
			Long discoveryTimeout = seed.getSnmpTimeoutServiceElementDiscovery();
			Integer discoveryRetries = seed.getSnmpRetriesServiceElementDiscovery();
			Long topologyTimeout = seed.getSnmpTimeoutTopologyDiscovery();
			Integer topologyRetries = seed.getSnmpRetriesTopologyDiscovery();
			Address gateway = seed.getInitialGateway();
			String initGateway = gateway != null ? gateway.toString() : "";
			
			Object[] rowData = { name, description, subnet, radius, discoveryTimeout, discoveryRetries, topologyTimeout, topologyRetries, initGateway};
			flexTable.addRow(rowData);
			list.add(seed);
		}
		flexTable.applyDataRowStyles();
		
		MainClient.statusPanel.updateStatus("Total " + result.length + " IpTopologySeed(s)");
	}
}
