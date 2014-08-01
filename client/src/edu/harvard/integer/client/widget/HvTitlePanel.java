package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HvTitlePanel extends VerticalPanel {

	private HvTitleBar titleBar;;
	private SimplePanel contentPanel;
	
	public HvTitlePanel(String title, SimplePanel contentPanel) {
		titleBar = new HvTitleBar(title);
		this.contentPanel = contentPanel;
		add(titleBar);
		add(contentPanel);
	}

	public HvTitleBar getTitleBar() {
		return titleBar;
	}

	public SimplePanel getContentPanel() {
		return contentPanel;
	}
	
}
