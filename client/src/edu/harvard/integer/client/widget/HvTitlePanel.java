package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class HvTitlePanel extends SimplePanel{

	private HTML titleHtml;
	
	public HvTitlePanel(String title) {
		titleHtml = new HTML(title);
		add(titleHtml);
		setStyleName("titlePanel");
	}
	
	public void updateTitle(String title) {
		titleHtml.setHTML(title);
	}
}
