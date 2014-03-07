package edu.harvard.integer.client.widget;

import com.emitrom.lienzo.client.core.shape.Layer;

/**
 * The Class WidgetLayer.
 */
public class WidgetLayer extends Layer {

	/** The height. */
	protected int width, height;
	
	/**
	 * Instantiates a new widget layer.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public WidgetLayer(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
