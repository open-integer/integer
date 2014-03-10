package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Button;

/**
 * The Class HvIconButton.
 */
public class HvIconButton extends Button {

	/**
	 * Instantiates a new hv icon button.
	 */
	public HvIconButton() {
		super();
		init();
	}
	
	/**
	 * Instantiates a new hv icon button.
	 *
	 * @param html the html
	 */
	public HvIconButton(String html) {
		super(html);
		init();
	}
	
	/**
	 * Inits the.
	 */
	private void init() {
		setStyleName("iconButton");
	}
}
