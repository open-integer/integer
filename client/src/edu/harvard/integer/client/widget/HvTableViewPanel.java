package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HvTableViewPanel extends VerticalPanel {
	protected DockPanel dockPanel = new DockPanel();
	protected HTMLPanel titlePanel;
	protected HorizontalPanel actionPanel = new HorizontalPanel();
	protected HvFlexTable flexTable;
	protected HvIconButton addButton = new HvIconButton("Add");
	
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
