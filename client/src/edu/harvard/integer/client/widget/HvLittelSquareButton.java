package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Button;

public class HvLittelSquareButton extends Button {

	public HvLittelSquareButton() {
		super();
		init();
	}
	
	public HvLittelSquareButton(String html) {
		super(html);
		init();
	}
	
	private void init() {
		setStyleName("littleSquareButton");
	}
}
