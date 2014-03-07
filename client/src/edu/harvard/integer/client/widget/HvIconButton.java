package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Button;

public class HvIconButton extends Button {

	public HvIconButton() {
		super();
		init();
	}
	
	public HvIconButton(String html) {
		super(html);
		init();
	}
	
	private void init() {
		setStyleName("iconButton");
	}
}
