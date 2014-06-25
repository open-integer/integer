package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Button;

/**
 * The Class HvIconButton represents a button with an icon option
 * This is a subclass class extended from com.google.gwt.user.client.ui.Button.
 * It includes 
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvIconButton extends Button {

	/**
	 * Creates a new HvIconButton instance.
	 */
	public HvIconButton() {
		super();
		init();
	}
	
	/**
	 * Creates a new HvIconButton instance.
	 *
	 * @param html the html
	 */
	public HvIconButton(String html) {
		super(html);
		init();
	}
	
	/**
	 * Initializes the instance.
	 */
	private void init() {
		setStyleName("iconButton");
	}
}
