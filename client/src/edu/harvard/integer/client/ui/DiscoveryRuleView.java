package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.client.widget.HvTitlePanel;
import edu.harvard.integer.common.topology.DiscoveryRule;

/**
 * The Class DiscoveryRuleView represents a discovery rule table view panel object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class DiscoveryRuleView extends HvTableViewPanel {
	
	private List<DiscoveryRule> list = new ArrayList<DiscoveryRule>();
	private DiscoveryRulePanel rulePanel = new DiscoveryRulePanel();
	
	/**
	 * Instantiates a new DiscoveryRule view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public DiscoveryRuleView(String title, String[] headers) {
		super(title, headers);
		
		addButton.setVisible(false);
		
		HvTitlePanel detailsTitlePanel = new HvTitlePanel("Discovery Rule", rulePanel);
		detailsPanel.setWidget(detailsTitlePanel);
		
		flexTable.addTableListener( new TableListener(){

			@Override
			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				DiscoveryRule rule = list.get(row-1);
				
				if (rule == null)
					return;
				
				rulePanel.update(rule);
				flexTable.setHighlighted(row);
				
				// show detailsPanel with the rulePanel
				splitPanel.setWidgetHidden(detailsPanel, false);
			}
           
        });
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
		list.clear();
		
		for (DiscoveryRule rule : result) {
			String name = rule.getName();
			String description = rule.getDescription();
			String type = rule.getDiscoveryType().name();
			int seedsCount = rule.getTopologySeeds().size();
			String created = rule.getCreated() != null ? DeviceDetailsPanel.DATE_FORMAT.format(rule.getCreated()) : "";
			String modified = rule.getModified() != null ? DeviceDetailsPanel.DATE_FORMAT.format(rule.getModified()) : "";
			
			Object[] rowData = { name, description, type, seedsCount, created, modified};
			flexTable.addRow(rowData);
			list.add(rule);
		}
		flexTable.applyDataRowStyles();
		
		MainClient.statusPanel.updateStatus("Total " + result.length + " Discovery Rule(s)");
	}
}
