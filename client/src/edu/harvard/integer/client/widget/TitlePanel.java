package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class TitlePanel extends SimplePanel{

	private HTML titleHtml;
	
	public TitlePanel(String title) {
		titleHtml = new HTML(title);
		add(titleHtml);
		setStyleName("titlePanel");
	}
	
	public void updateTitle(String title) {
		titleHtml.setHTML(title);
	}
}
