package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.snmp.SnmpGlobalReadCredential;
import edu.harvard.integer.common.topology.Credential;

/**
 * The Class SnmpGlobalReadCredentialView represents a SnmpGlobalReadCredential table view panel object of Integer.
 * This is a subclass class extended from HvTableViewPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, July 2014
 */
public class SnmpGlobalReadCredentialView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new SnmpGlobalReadCredential view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public SnmpGlobalReadCredentialView(String title, String[] headers) {
		super(title, headers);
		
		setSize("100%", "100%");
		addButton.setVisible(false);
	}

	/**
	 * Update method will refresh the SnmpGlobalReadCredential view given by the list of results.
	 *
	 * @param result the result
	 */
	public void update(SnmpGlobalReadCredential[] result) {
		if (result == null || result.length == 0) {
			MainClient.statusPanel.updateStatus("No SnmpGlobalReadCredential");
			return;
		}
		
		flexTable.clean();
		
		for (SnmpGlobalReadCredential readCredential : result) {
			String name = readCredential.getName();
			List<Credential> credentialList = readCredential.getCredentials();
			List<Integer> portList = readCredential.getAlternatePorts();
			
			StringBuffer credentials = new StringBuffer();
			for (Credential credential : credentialList)
				credentials.append(credential.toString()).append(" ");
			
			StringBuffer ports = new StringBuffer();
			for (Integer port : portList)
				ports.append(port).append(", ");
			
			Object[] rowData = { name, credentials.toString(), ports.toString()};
			flexTable.addRow(rowData);
		}
		flexTable.applyDataRowStyles();
		
		MainClient.statusPanel.updateStatus("Total " + result.length + " SnmpGlobalReadCredential(s)");
	}
}
