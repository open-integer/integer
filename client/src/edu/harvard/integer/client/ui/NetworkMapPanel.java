package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.mediator.EventFilter;
import com.emitrom.lienzo.client.core.mediator.MousePanMediator;
import com.emitrom.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.topology.NetworkInformation;

public class NetworkMapPanel extends LienzoPanel {
	public final NetworkMap networkMap = new NetworkMap();
	
	public NetworkMapPanel(int width, int height) {
		super(width, height);

		// Network Map
		
		MainClient.integerService
				.getNetworkInformation(new AsyncCallback<NetworkInformation>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to receive Networks from Integer");
					}

					@Override
					public void onSuccess(NetworkInformation result) {
						networkMap.updateNetworkInformation(result);
					}
				});

		// networkMapPanel.setSize("100%", "100%");
		add(networkMap);

		getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.ANY));
		getViewport().pushMediator(new MousePanMediator(EventFilter.BUTTON_RIGHT));

		LienzoPanel.enableWindowMouseWheelScroll(true);
	}

	public NetworkMap getNetworkMap() {
		return networkMap;
	}
}