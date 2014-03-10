package edu.harvard.integer.client.ui;

import java.util.List;

import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.topology.Capability;

/**
 * The Class CapabilityView.
 */
public class CapabilityView extends HvTableViewPanel {
	
	/**
	 * Instantiates a new capability view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public CapabilityView(String title, String[] headers) {
		super(title, headers);
	}

	/**
	 * Update.
	 *
	 * @param result the result
	 */
	public void update(List<Capability> result) {
		if (result == null || result.isEmpty())
			return;
		
		flexTable.clean();
		
		for (Capability capability : result) {
			String name = ""+capability.getName();
			String description = ""+capability.getDescription();
			String fcaps = ""+capability.getFcaps().toString();
			String serElements = "";
			
			Object[] rowData = { name, description, fcaps, serElements};
			flexTable.addRow(rowData);
		}
		flexTable.applyDataRowStyles();
	}
}
