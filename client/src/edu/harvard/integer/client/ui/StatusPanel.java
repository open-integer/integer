package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class StatusPanel extends HorizontalPanel {
	
	public static int STATUS_WIDTH = 600;
	private TextBox statusBox = new TextBox();
	
	public StatusPanel() {
		statusBox.setReadOnly(true);
		statusBox.setWidth(STATUS_WIDTH+"px");
		
		add(statusBox);
	}
	
	public void update(String text) {
		statusBox.setText(text);
		Window.alert(text);
	}
}
