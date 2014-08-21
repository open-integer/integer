package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class ServiceElementTreeDetailsPanel.
 */
public class ServiceElementTreeDetailsPanel extends SplitLayoutPanel {
	
	/**
	 * Instantiates a new service element tree details panel.
	 *
	 * @param serviceElement the service element
	 */
	public ServiceElementTreeDetailsPanel(final ServiceElement serviceElement) {
		super(MainClient.SPLITTER_SIZE);

		setSize("600px", "600px");
		
		final ServiceElementDetailsTabPanel detailsTabPanel = new ServiceElementDetailsTabPanel();
		final ContainedTreeView containedTreeView = new ContainedTreeView("Device Details", this, detailsTabPanel);
	    
	    addSouth(detailsTabPanel, ServiceElementDetailsTabPanel.TABPANEL_HEIGHT);
	    setWidgetToggleDisplayAllowed(detailsTabPanel, true);
	    add(containedTreeView);
		
		MainClient.integerService.getServiceElementByParentId(serviceElement.getID(), new AsyncCallback<ServiceElement[]>() {

			@Override
			public void onFailure(Throwable caught) {
				MainClient.statusPanel.showAlert("Failed to receive contained service elements of " + serviceElement.getName());
			}

			@Override
			public void onSuccess(ServiceElement[] serviceElements) {
				MainClient.statusPanel.updateStatus("Received " + serviceElements.length + " contained service elements of " + serviceElement.getName());
				containedTreeView.updateTree(serviceElement, serviceElements);
				
				// select root serviceElement and show it in general tab
				// detailsTabPanel.update(serviceElement);
			}
		});
	}
}
