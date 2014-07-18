package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class StatusPanel extends HorizontalPanel {
	
	public static int STATUS_WIDTH = 600;
	public static int STATUS_HEIGHT = 28;
	private TextBox statusBox = new TextBox();
	
	public StatusPanel() {
		statusBox.setReadOnly(true);
		statusBox.setSize(STATUS_WIDTH+"px", STATUS_HEIGHT+"px");
		
		add(statusBox);
	}
	
	public void update(String text) {
		statusBox.setText(text);
		Window.alert(text);
	}
}
