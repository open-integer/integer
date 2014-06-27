package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Button;

/**
 * The Class HvLittelSquareButton represents a button in a little square shape.
 * This is a subclass class extended from com.google.gwt.user.client.ui.Button.
 * It includes 
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvLittelSquareButton extends Button {

	/**
	 * Creates a new HvLittelSquareButton instance.
	 */
	public HvLittelSquareButton() {
		super();
		init();
	}
	
	/**
	 * Creates a new HvLittelSquareButton instance.
	 *
	 * @param html the html
	 */
	public HvLittelSquareButton(String html) {
		super(html);
		init();
	}
	
	/**
	 * Initializes the instance.
	 */
	private void init() {
		setStyleName("littleSquareButton");
	}
}
