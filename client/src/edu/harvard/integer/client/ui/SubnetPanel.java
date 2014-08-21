package edu.harvard.integer.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvIconButton;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class SubnetPanel.
 */
public class SubnetPanel extends VerticalPanel {

	/** The subnetmap title. */
	public static String SUBNETMAP_TITLE = "Subnet ";
	
	/** The title panel. */
	//private HvTitleBar titlePanel = new HvTitleBar(SUBNETMAP_TITLE);
	
	/** The toolbar panel. */
	private HorizontalPanel toolbarPanel = new HorizontalPanel();
	
	/** The contained split panel contains ContainedTreeView and Detail Tab panel */
	private SplitLayoutPanel containedSplitPanel = new SplitLayoutPanel(MainClient.SPLITTER_SIZE);
	
	/** The subnet split panel contains subnetMapPanel and containedTreeView */
	private SplitLayoutPanel subnetSplitPanel = new SplitLayoutPanel(MainClient.SPLITTER_SIZE);
	
	/** The subnet map panel. */
	private SubnetMapPanel subnetMapPanel = new SubnetMapPanel(this, IntegerMap.MAP_WIDTH, IntegerMap.MAX_MAP_HEIGHT);
	
	/** The details button. */
	public HvIconButton detailsButton = new HvIconButton("Details");
	
	/** The selected entity. */
	public BaseEntity selectedEntity;
	
	/**
	 * Instantiates a new subnet panel.
	 */
	public SubnetPanel() {
		add(subnetMapPanel);
		setSize("100%", "100%");
	}

	/**
	 * Gets the subnet map panel.
	 *
	 * @return the subnet map panel
	 */
	public SubnetMapPanel getSubnetMapPanel() {
		return subnetMapPanel;
	}
	
	/**
	 * Update subnet.
	 *
	 * @param network the network
	 */
	public void updateSubnet(Network network) {
		//titlePanel.updateTitle(SUBNETMAP_TITLE + network.getName());
		subnetMapPanel.getSubnetMap().updateNetwork(network);
	}
	
	public void showContainedTreeView(final ServiceElement serviceElement) {
		// clear containedSplitPanel first
		containedSplitPanel.clear();
		
		ServiceElementDetailsTabPanel detailsTabPanel = new ServiceElementDetailsTabPanel();
		final ContainedTreeView containedTreeView = new ContainedTreeView("Device Details", containedSplitPanel, detailsTabPanel);
	    
	    containedSplitPanel.addSouth(detailsTabPanel, ServiceElementDetailsTabPanel.TABPANEL_HEIGHT);
	    containedSplitPanel.setWidgetHidden(detailsTabPanel, true);
	    containedSplitPanel.setWidgetToggleDisplayAllowed(detailsTabPanel, true);
	    containedSplitPanel.add(containedTreeView);
		
		MainClient.integerService.getServiceElementByParentId(serviceElement.getID(), new AsyncCallback<ServiceElement[]>() {

			@Override
			public void onFailure(Throwable caught) {
				MainClient.statusPanel.showAlert("Failed to receive contained service elements of " + serviceElement.getName());
			}

			@Override
			public void onSuccess(ServiceElement[] serviceElements) {
				subnetSplitPanel.setWidgetHidden(containedSplitPanel, false);
				
				MainClient.statusPanel.updateStatus("Received " + serviceElements.length + " contained service elements of " + serviceElement.getName());
				containedTreeView.updateTree(serviceElement, serviceElements);
			}
		});
	}

	public BaseEntity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(BaseEntity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

}
