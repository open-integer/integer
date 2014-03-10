package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class HvTableViewPanel.
 */
public class HvTableViewPanel extends VerticalPanel {
	
	/** The dock panel. */
	protected DockPanel dockPanel = new DockPanel();
	
	/** The title panel. */
	protected HTMLPanel titlePanel;
	
	/** The action panel. */
	protected HorizontalPanel actionPanel = new HorizontalPanel();
	
	/** The flex table. */
	protected HvFlexTable flexTable;
	
	/** The add button. */
	protected HvIconButton addButton = new HvIconButton("Add");
	
	/**
	 * Instantiates a new hv table view panel.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public HvTableViewPanel(String title, String[] headers) {
		titlePanel = new HTMLPanel(title);
		flexTable = new HvFlexTable(headers);
		actionPanel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);
		actionPanel.add(addButton);

		dockPanel.setStyleName("barPanel");
		dockPanel.add(titlePanel,DockPanel.WEST);
		dockPanel.add(actionPanel, DockPanel.EAST);
		
		add(dockPanel);
		add(flexTable.getVisualPanel());
	}
}
