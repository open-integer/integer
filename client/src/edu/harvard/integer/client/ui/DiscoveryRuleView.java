package edu.harvard.integer.client.ui;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.topology.DiscoveryRule;

/**
 * The Class DiscoveryRuleView represents a discovery rule table view panel object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class DiscoveryRuleView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new DiscoveryRule view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public DiscoveryRuleView(String title, String[] headers) {
		super(title, headers);
		addButton.setVisible(false);
	}

	/**
	 * Update method will refresh the Discovery Rule view given by the list of results.
	 *
	 * @param result the result
	 */
	public void update(DiscoveryRule[] result) {
		if (result == null || result.length == 0) {
			MainClient.statusPanel.updateStatus("No Discovery Rule");
			return;
		}
		
		flexTable.clean();
		
		for (DiscoveryRule rule : result) {
			String name = rule.getName();
			String description = rule.getDescription();
			String type = rule.getDiscoveryType().name();
			int seedsCount = rule.getTopologySeeds().size();
			String created = rule.getCreated() != null ? rule.getCreated().toString() : "";
			String modified = rule.getModified() != null ? rule.getModified().toString() : "";
			
			Object[] rowData = { name, description, type, seedsCount, created, modified};
			flexTable.addRow(rowData);
		}
		flexTable.applyDataRowStyles();
		
		MainClient.statusPanel.updateStatus("Total " + result.length + " Discovery Rule(s)");
	}
}
