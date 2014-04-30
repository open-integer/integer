package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

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
		firstTitleLabel.setText(title);
		titlePanel.add(firstTitleLabel);
		if (subTitle != null) {
			secondTitleLabel.setText(subTitle);
			titlePanel.add(secondTitleLabel);
		}
		
		flexTable = new HvFlexTable(headers);
		actionPanel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);
		actionPanel.add(addButton);

		dockPanel.setStyleName("barPanel");
		dockPanel.add(titlePanel,DockPanel.WEST);
		dockPanel.add(actionPanel, DockPanel.EAST);
		
		add(dockPanel);
		add(flexTable.getVisualPanel());
	}
	
	public void updateTitle(String title) {
		firstTitleLabel.setText(title);
	}
}
