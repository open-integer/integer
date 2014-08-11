package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class StatusPanel extends HorizontalPanel {
	
	public static int STATUS_WIDTH = 800;
	public static int STATUS_HEIGHT = 18;
	private TextBox statusBox = new TextBox();
	
	public StatusPanel() {
		statusBox.setReadOnly(true);
		statusBox.setSize(STATUS_WIDTH+"px", STATUS_HEIGHT+"px");
		
		add(statusBox);
	}
	
	public void showAlert(String text) {
		statusBox.setText(text);
		Window.alert(text);
	}
	
	public void updateStatus(String text) {
		statusBox.setText(text);
	}
}
