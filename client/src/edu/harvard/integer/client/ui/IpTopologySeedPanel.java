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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.client.widget.HvListBoxPanel;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.IpTopologySeed;

/**
 * The Class IpTopologySeedPanelPanel represents a panel to configure/view IpTopologySeedPanelPanel object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class IpTopologySeedPanel extends FormPanel {

	private TextBox nameTextBox = new TextBox();
	private TextBox descriptionTextBox = new TextBox();
	private TextBox subnetTextBox = new TextBox();
	private TextBox initGatewayTextBox = new TextBox();
	private TextBox radiusTextBox = new TextBox();
	private TextBox snmpDiscoveryTimeout = new TextBox();
	private TextBox snmpDiscoveryRetries = new TextBox();
	private TextBox snmpTopologyTimeout = new TextBox();
	private TextBox snmpTopologyRetries = new TextBox();
	
	private HvListBoxPanel credentialListBox = new HvListBoxPanel();
	
	/**
	 * Create a new IpTopologySeedPanelPanel.
	 */
	public IpTopologySeedPanel() {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(10, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		grid.setWidget(0, 1, nameTextBox);
		
		grid.setWidget(1, 0, new Label("Description"));
		grid.setWidget(1, 1, descriptionTextBox);
		
		grid.setWidget(2, 0, new Label("Subnet"));
		grid.setWidget(2, 1, subnetTextBox);
		
		grid.setWidget(3, 0, new Label("Initial Gateway"));
		grid.setWidget(3, 1, initGatewayTextBox);
		
		grid.setWidget(4, 0, new Label("Radius"));
		grid.setWidget(4, 1, radiusTextBox);
		
		grid.setWidget(5, 0, new Label("SNMP Discovery Timeout"));
		grid.setWidget(5, 1, snmpDiscoveryTimeout);
		
		grid.setWidget(6, 0, new Label("SNMP Discovery Retries"));
		grid.setWidget(6, 1, snmpDiscoveryRetries);
		
		grid.setWidget(7, 0, new Label("SNMP Topology Processing Timeout"));
		grid.setWidget(7, 1, snmpTopologyTimeout);
		
		grid.setWidget(8, 0, new Label("SNMP Topology Processing Retries"));
		grid.setWidget(8, 1, snmpTopologyRetries);
		
		grid.setWidget(9, 0, new Label("Credentials"));
		grid.setWidget(9, 1, credentialListBox);
		credentialListBox.setVisibleItemCount(5);
		credentialListBox.setAddDeleteButtonVisible(false);

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "150px");
		grid.getCellFormatter().setWidth(0, 1, "350px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void update(IpTopologySeed seed) {
		nameTextBox.setText(seed.getName());
		descriptionTextBox.setText(seed.getDescription());
		subnetTextBox.setText(seed.getSubnet() != null ? seed.getSubnet().toString() : "");
		initGatewayTextBox.setText(seed.getInitialGateway() != null ? seed.getInitialGateway().toString() : "");
		radiusTextBox.setText(""+seed.getRadius());
		snmpDiscoveryTimeout.setText(""+seed.getSnmpTimeoutServiceElementDiscovery());
		snmpDiscoveryRetries.setText(""+seed.getSnmpRetriesServiceElementDiscovery());
		snmpTopologyTimeout.setText(""+seed.getSnmpTimeoutTopologyDiscovery());
		snmpTopologyRetries.setText(""+seed.getSnmpRetriesTopologyDiscovery());
	
		credentialListBox.clear();
		for (Credential credential : seed.getCredentials())
			credentialListBox.addItem(credential.toString());
	}
}
