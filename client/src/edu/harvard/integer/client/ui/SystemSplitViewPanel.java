package edu.harvard.integer.client.ui;

import java.util.List;

import com.emitrom.lienzo.client.core.mediator.EventFilter;
import com.emitrom.lienzo.client.core.mediator.MousePanMediator;
import com.emitrom.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvIconButton;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class SystemSplitViewPanel represents a split panel object of System
 * This is a subclass class extended from com.google.gwt.user.client.ui.SplitLayoutPanel
 * It contains filterPanel at west side and eastPanel at east side
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class SystemSplitViewPanel extends SplitLayoutPanel {
	
	/** The Constant SPLITTER_SIZE. */
	public static final int SPLITTER_SIZE = 4;
	
	/** The Constant CONTENT_WIDTH. */
	public static final int CONTENT_WIDTH = 1200;
	
	/** The Constant CONTENT_HEIGHT. */
	public static final int CONTENT_HEIGHT = 600;
	
	/** The Constant WESTPANEL_WIDTH. */
	public static final int WESTPANEL_WIDTH = 250;

	/** The east panel. */
	public static DockPanel eastPanel = null;
	
	/** The east split panel. */
	public static SplitLayoutPanel eastSplitPanel = null;
	
	/** The service element map split panel. */
	public static SplitLayoutPanel serviceElementMapSplitPanel = null;
	
	/** The contained split panel. */
	public static SplitLayoutPanel containedSplitPanel= null;
	
	/** The details tab panel. */
	public static ServiceElementDetailsTabPanel detailsTabPanel = null;
	
	/** The Constant title. */
	public static final String title = "Device Children";
	
	/** The Constant headers. */
	public static final String[] headers = {"Name", "Status", "Description"};
	
	/** The contained tree view. */
	public static ContainedTreeView containedTreeView = null;
	
	/** The details button. */
	public static HvIconButton detailsButton = new HvIconButton("Summary");

	/** The selected entity. */
	public static BaseEntity selectedEntity;

	/** The network panel. */
	private NetworkPanel networkPanel = new NetworkPanel();
	
	/** The subnet panel. */
	private static SubnetPanel subnetPanel = new SubnetPanel();
	
	/** The serviceElement map panel */
	//private static LienzoPanel serviceElementMapPanel = new LienzoPanel(IntegerMap.MAP_WIDTH, CONTENT_HEIGHT);
	
	/** The service element map. */
	//final static ServiceElementMap serviceElementMap = new ServiceElementMap();

	/**
	 * Instantiates a new system split view panel.
	 */
	public SystemSplitViewPanel() {
		super(SPLITTER_SIZE);
        
        setSize("100%", MainClient.WINDOW_HEIGHT+"px");
        
        // Event View
		EventView eventView = createEventView();
		
		eastPanel = new DockPanel();
		eastPanel.setBorderWidth(1);
		eastPanel.setSize("100%", "100%");

		HorizontalPanel mapToolbarPanel = new HorizontalPanel();
		mapToolbarPanel.setStyleName("toolbar");
		
		// Details button
		detailsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				NetworkMap networkMap = networkPanel.getNetworkMapPanel().getNetworkMap();
				if (networkMap.getSelectedTimestamp() > containedTreeView.getSelectedTimestamp())
					selectedEntity = networkMap.getSelectedEntity();
				else
					selectedEntity = containedTreeView.getSelectedServiceElement();
				
				MainClient.integerService.getDeviceDetails(selectedEntity.getID(), new AsyncCallback<DeviceDetails>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to receive Devices from Integer");
					}

					@Override
					public void onSuccess(DeviceDetails deviceDetails) {
						DeviceDetailsPanel detailsPanel = new DeviceDetailsPanel(selectedEntity.getName(), deviceDetails);
						HvDialogBox detailsDialog = new HvDialogBox("Device Details", detailsPanel);
						detailsDialog.enableOkButton(false);
						detailsDialog.setSize("400px", "150px");
						detailsDialog.center();
						detailsDialog.show();
					}
				});
				
			}
			
		});
		mapToolbarPanel.add(detailsButton);
		
		eastSplitPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		eastSplitPanel.setSize("100%",  "500px");
		
		serviceElementMapSplitPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		serviceElementMapSplitPanel.setSize("100%",  "500px");
		
		eastSplitPanel.addEast(serviceElementMapSplitPanel, CONTENT_WIDTH/2);
		eastSplitPanel.setWidgetHidden(serviceElementMapSplitPanel, true);
		eastSplitPanel.setWidgetToggleDisplayAllowed(serviceElementMapSplitPanel, true);
		eastSplitPanel.add(networkPanel);
		
		containedSplitPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		
		containedTreeView = new ContainedTreeView(title, headers);
		
		detailsTabPanel = new ServiceElementDetailsTabPanel();
	    
	    containedSplitPanel.addSouth(detailsTabPanel, 300);
	    containedSplitPanel.setWidgetHidden(detailsTabPanel, true);
	    containedSplitPanel.setWidgetToggleDisplayAllowed(detailsTabPanel, true);
	    containedSplitPanel.add(containedTreeView);
		
		serviceElementMapSplitPanel.addEast(containedSplitPanel, CONTENT_WIDTH/4);
	    serviceElementMapSplitPanel.setWidgetHidden(containedSplitPanel, true);
	    serviceElementMapSplitPanel.setWidgetToggleDisplayAllowed(containedSplitPanel, true);
	    serviceElementMapSplitPanel.add(subnetPanel);
		
		eastPanel.add(mapToolbarPanel, DockPanel.NORTH);
		eastPanel.add(eastSplitPanel, DockPanel.CENTER);
		eastPanel.add(eventView, DockPanel.SOUTH);
		
		final FilterPanel filterPanel = new FilterPanel();
		//filterPanel.setHeight("100%");
		filterPanel.setSize("100%", "100%");
		
		addWest(filterPanel, WESTPANEL_WIDTH);
		setWidgetToggleDisplayAllowed(filterPanel, true);
		
		add(eastPanel);
		
		MainClient.integerService.getBlankSelection(new AsyncCallback<Selection>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Selection result) {
				filterPanel.update(result.getFilters().get(0));			
			}
			
		});
		
	}
	
	public void updateServiceElementMap(List<ServiceElement> list) {
		subnetPanel.getSubnetMapPanel().getSubnetMap().update(list);
	}

	
	/**
	 * Creates the filter view.
	 *
	 * @return the filter view
	 */
	private FilterView createFilterView() {
		String title = "Layer 3 Topology";
		String subTitle = "State - Campus Wide";
		final String[] headers = {"Views", "Filters", "Manager"};
		final FilterView filterView = new FilterView(title, subTitle, headers);
		/*integerService.getEvents(new AsyncCallback<List<Object>>() {

			@Override
			public void onSuccess(List<Object> result) {
				filterView.update(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});*/
		return filterView;
	}
	
	/**
	 * Creates the event view.
	 *
	 * @return the event view
	 */
	private EventView createEventView() {
		String title = "Events";
		final String[] headers = {"Type", "Severity", "Start Time", "Status", "Description"};
		final EventView eventView = new EventView(title, headers);
		/*integerService.getEvents(new AsyncCallback<List<Object>>() {

			@Override
			public void onSuccess(List<Object> result) {
				eventView.update(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});*/
		return eventView;
	}

	/**
	 * Enablecontained tree view.
	 *
	 * @param enable the enable
	 */
	public static void enablecontaineeTreeView(boolean enable) {
		eastSplitPanel.setWidgetHidden(containedTreeView, !enable);
	}
	
	/**
	 * Show contained tree view.
	 *
	 * @param se the se
	 */
	public static void showContainedTreeView(final ServiceElement se) {
		//containeeTreeView.updateTitle(se.getName());
		serviceElementMapSplitPanel.setWidgetHidden(containedSplitPanel, false);
		
		MainClient.integerService.getServiceElementByParentId(se.getID(), new AsyncCallback<ServiceElement[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error");
			}

			@Override
			public void onSuccess(ServiceElement[] serviceElements) {
				containedTreeView.updateTree(se.getName(), serviceElements);
			}
		});
	}


	public static void showServiceElementMap(Network network) {
		eastSplitPanel.setWidgetHidden(serviceElementMapSplitPanel, false);
		subnetPanel.updateSubnet(network);
	}
}
