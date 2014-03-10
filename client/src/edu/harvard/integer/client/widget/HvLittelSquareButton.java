package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.Button;

/**
 * The Class HvLittelSquareButton.
 */
public class HvLittelSquareButton extends Button {

	/**
	 * Instantiates a new hv littel square button.
	 */
	public HvLittelSquareButton() {
		super();
		init();
	}
	
	/**
	 * Instantiates a new hv littel square button.
	 *
	 * @param html the html
	 */
	public HvLittelSquareButton(String html) {
		super(html);
		init();
	}
	
	/**
	 * Inits the.
	 */
	private void init() {
		setStyleName("littleSquareButton");
	}
}
