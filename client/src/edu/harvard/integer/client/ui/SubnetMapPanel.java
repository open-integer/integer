package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.mediator.EventFilter;
import com.emitrom.lienzo.client.core.mediator.MousePanMediator;
import com.emitrom.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.emitrom.lienzo.client.widget.LienzoPanel;

/**
 * The Class SubnetMapPanel.
 */
public class SubnetMapPanel extends LienzoPanel {
	
	/** The subnet map. */
	private final SubnetMap subnetMap;
	
	/**
	 * Instantiates a new subnet map panel.
	 * @param subnetPanel 
	 *
	 * @param width the width
	 * @param height the height
	 */
	public SubnetMapPanel(SubnetPanel parentPanel, int width, int height) {
		super(width, height);

		subnetMap = new SubnetMap(parentPanel);
		add(subnetMap);

		getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.ANY));
		getViewport().pushMediator(new MousePanMediator(EventFilter.BUTTON_LEFT));

		LienzoPanel.enableWindowMouseWheelScroll(true);
	}

	/**
	 * Gets the subnet map.
	 *
	 * @return the subnet map
	 */
	public SubnetMap getSubnetMap() {
		return subnetMap;
	}
}
