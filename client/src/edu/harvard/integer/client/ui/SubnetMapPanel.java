package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.mediator.EventFilter;
import com.emitrom.lienzo.client.core.mediator.MousePanMediator;
import com.emitrom.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class SubnetMapPanel extends LienzoPanel {
	public final SubnetMap subnetMap = new SubnetMap();
	
	public SubnetMapPanel(int width, int height) {
		super(width, height);

		add(subnetMap);

		getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.ANY));
		getViewport().pushMediator(new MousePanMediator(EventFilter.BUTTON_RIGHT));

		LienzoPanel.enableWindowMouseWheelScroll(true);
	}

	public SubnetMap getSubnetMap() {
		return subnetMap;
	}
}
