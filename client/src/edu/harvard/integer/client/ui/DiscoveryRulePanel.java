/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.client.widget.HvListBoxPanel;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.IpTopologySeed;

/**
 * The Class IpTopologySeedPanelPanel represents a panel to configure/view IpTopologySeedPanelPanel object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class DiscoveryRulePanel extends FormPanel {

	private TextBox nameTextBox = new TextBox();
	private TextBox descriptionTextBox = new TextBox();
	private TextBox typeTextBox = new TextBox();
	private TextBox createdTextBox = new TextBox();
	private TextBox modifiedTextBox = new TextBox();
	
	private HvListBoxPanel seedListBox = new HvListBoxPanel();
	
	/**
	 * Create a new IpTopologySeedPanelPanel.
	 */
	public DiscoveryRulePanel() {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(6, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		grid.setWidget(0, 1, nameTextBox);
		
		grid.setWidget(1, 0, new Label("Description"));
		grid.setWidget(1, 1, descriptionTextBox);
		
		grid.setWidget(2, 0, new Label("Discovery Type"));
		grid.setWidget(2, 1, typeTextBox);
		
		grid.setWidget(3, 0, new Label("Created Time"));
		grid.setWidget(3, 1, createdTextBox);
		
		grid.setWidget(4, 0, new Label("Last Modified Time"));
		grid.setWidget(4, 1, modifiedTextBox);
		
		grid.setWidget(5, 0, new Label("IP Topology Seeds"));
		grid.setWidget(5, 1, seedListBox.getVisualPanel());
		
		seedListBox.setVisibleItemCount(5);
		seedListBox.setAddDeleteButtonVisible(false);

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "150px");
		grid.getCellFormatter().setWidth(0, 1, "300px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void update(DiscoveryRule rule) {
		nameTextBox.setText(rule.getName());
		descriptionTextBox.setText(rule.getDescription());
		typeTextBox.setText(rule.getDiscoveryType().name());
		createdTextBox.setText(rule.getCreated() != null ? rule.getCreated().toString() : "");
		modifiedTextBox.setText(rule.getModified() != null ? rule.getModified().toString() : "");
		
		seedListBox.clear();
		for (IpTopologySeed seed : rule.getTopologySeeds())
			seedListBox.addItem(seed.toString());
	}
}
