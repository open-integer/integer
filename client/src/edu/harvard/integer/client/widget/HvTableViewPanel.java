package edu.harvard.integer.client.widget;

import java.util.List;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.common.ID;

/**
 * The Class HvTableViewPanel represents a panel showing the table content
 * This is a subclass class extended from com.google.gwt.user.client.ui.VerticalPanel.
 * It includes 
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvTableViewPanel extends VerticalPanel {
	
	/** The dock panel. */
	protected DockPanel dockPanel = new DockPanel();
	
	/** The title panel. */
	protected VerticalPanel titlePanel = new VerticalPanel();
	
	/** The first title label. */
	protected Label firstTitleLabel = new Label();
	
	/** The second title label. */
	protected Label secondTitleLabel = new Label();
	
	/** The action panel. */
	protected HorizontalPanel actionPanel = new HorizontalPanel();
	
	/** The flex table. */
	protected HvFlexTable flexTable;
	
	/** The add button. */
	protected HvIconButton addButton = new HvIconButton("Add");
	
	/**
	 * Creates a new HvTableViewPanel instance.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public HvTableViewPanel(String title, String[] headers) {
		this(title, null, headers);
	}
	
	/**
	 * Creates a new HvTableViewPanel instance.
	 *
	 * @param title the title
	 * @param subTitle the sub title
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
	
	/**
	 * Sets the action panel visible.
	 *
	 * @param visible the new action panel visible
	 */
	public void setActionPanelVisible(boolean visible) {
		dockPanel.setVisible(visible);
	}
	
	/**
	 * Update title.
	 *
	 * @param title the title
	 */
	public void updateTitle(String title) {
		firstTitleLabel.setText(title);
	}
	
	/**
	 * Show id list.
	 *
	 * @param result the result
	 */
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
