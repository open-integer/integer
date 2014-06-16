package edu.harvard.integer.client.widget;

import java.util.List;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.common.ID;

/**
 * The Class HvTableViewPanel.
 */
public class HvTableViewPanel extends VerticalPanel {
	
	/** The dock panel. */
	protected DockPanel dockPanel = new DockPanel();
	
	/** The title panel. */
	protected VerticalPanel titlePanel = new VerticalPanel();
	protected Label firstTitleLabel = new Label();
	protected Label secondTitleLabel = new Label();
	
	/** The action panel. */
	protected HorizontalPanel actionPanel = new HorizontalPanel();
	
	/** The flex table. */
	protected HvFlexTable flexTable;
	
	/** The add button. */
	protected HvIconButton addButton = new HvIconButton("Add");
	
	public HvTableViewPanel(String title, String[] headers) {
		this(title, null, headers);
	}
	
	/**
	 * Instantiates a new hv table view panel.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public HvTableViewPanel(String title, String subTitle, String[] headers) {
		
		if (title != null && !title.isEmpty()) {
			firstTitleLabel.setText(title);
			titlePanel.add(firstTitleLabel);
			if (subTitle != null) {
				secondTitleLabel.setText(subTitle);
				titlePanel.add(secondTitleLabel);
			}
			
			
			actionPanel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);
			actionPanel.add(addButton);

			dockPanel.setStyleName("barPanel");
			dockPanel.add(titlePanel,DockPanel.WEST);
			dockPanel.add(actionPanel, DockPanel.EAST);
			
			add(dockPanel);
		}
		
		flexTable = new HvFlexTable(headers);
		add(flexTable.getVisualPanel());
	}
	
	public void setActionPanelVisible(boolean visible) {
		dockPanel.setVisible(visible);
	}
	
	public void updateTitle(String title) {
		firstTitleLabel.setText(title);
	}
	
	public void showIdList(List<ID> result) {
		if (result == null || result.isEmpty())
			return;
		
		flexTable.clean();
		
		for (ID id : result) {
			String name = id.getName();	
			Object[] rowData = {name};
			flexTable.addRow(rowData);
		}
		flexTable.applyDataRowStyles();
	}
}
