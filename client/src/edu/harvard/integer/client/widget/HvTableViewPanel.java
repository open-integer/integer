package edu.harvard.integer.client.widget;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.ui.SystemSplitViewPanel;
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
	
	/** The splitPanel contains flexTable at west and detailsPanel at east */
	protected SplitLayoutPanel splitPanel = new SplitLayoutPanel(MainClient.SPLITTER_SIZE);
	
	/** The detailsPanel will be displayed with the details of the row when row is selected */
	protected SimplePanel detailsPanel = new SimplePanel();
	
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
		setSize("100%", "100%");
		
		addStyleName("HvPanel");
		
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
		
		splitPanel.addEast(detailsPanel, 450);
		splitPanel.setWidgetHidden(detailsPanel, true);
		splitPanel.setWidgetToggleDisplayAllowed(detailsPanel, true);	
		splitPanel.add(flexTable);
		splitPanel.setSize("100%", SystemSplitViewPanel.CONTENT_HEIGHT+"px");
		splitPanel.setWidgetMinSize(detailsPanel, 20);
		splitPanel.setWidgetSnapClosedSize(detailsPanel, 30);

		add(splitPanel);

	}
	
	public void setTableSize(String width, String height) {
		splitPanel.setSize(width, height);
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
	
	public void setColumnsWidth(int[] columnWidth) {
		for (int i = 0; i < columnWidth.length; i++) {
			flexTable.getColumnFormatter().setWidth(i, columnWidth[i]+"px");
		}
	}
	
	public void addTableListener(TableListener listener) {
		flexTable.addTableListener(listener);
	}
}
