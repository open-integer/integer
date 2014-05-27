package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.mediator.EventFilter;
import com.emitrom.lienzo.client.core.mediator.MousePanMediator;
import com.emitrom.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvIconButton;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.topology.CriticalityEnum;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.ServiceElement;

public class SystemSplitViewPanel extends SplitLayoutPanel {
	public static final int SPLITTER_SIZE = 4;
	public static final int CONTENT_WIDTH = 1200;
	public static final int CONTENT_HEIGHT = 600;
	public static final int WESTPANEL_WIDTH = 250;
	
	public static SplitLayoutPanel westPanel = null;
	public static DockPanel eastPanel = null;
	public static SplitLayoutPanel eastSplitPanel = null;
	
	public static final String title = "Device Children";
	public static final String[] headers = {"Name", "Status", "Description"};
	public static ContaineeTreeView containeeTreeView = null;
	
	public static HvIconButton detailsButton = new HvIconButton("Summary");

	public static ServiceElement selectedElement;

	private LienzoPanel networkPanel = new LienzoPanel(CONTENT_WIDTH, CONTENT_HEIGHT);

	public SystemSplitViewPanel() {
		super(SPLITTER_SIZE);
		
		final DeviceMap deviceMap = new DeviceMap();
		deviceMap.demo(10);

		MainClient.integerService.getTopLevelElements(new AsyncCallback<ServiceElement[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to receive Devices from Integer");
			}

			@Override
			public void onSuccess(ServiceElement[] result) {
				deviceMap.update(result);
			}
		});
		
        networkPanel.add(deviceMap);
        
        networkPanel.getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.ANY));
        networkPanel.getViewport().pushMediator(new MousePanMediator(EventFilter.BUTTON_RIGHT));
        
        LienzoPanel.enableWindowMouseWheelScroll(true);
        
        setSize("100%", MainClient.WINDOW_HEIGHT+"px");
		
		FilterView filterView = createFilterView();
		westPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		westPanel.addSouth(filterView, 200);
		westPanel.add(createNetworkTreePanel());
		westPanel.setWidgetToggleDisplayAllowed(filterView, true);

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
				if (deviceMap.getSelectedTimestamp() > containeeTreeView.getSelectedTimestamp())
					selectedElement = deviceMap.getSelectedElement();
				else
					selectedElement = containeeTreeView.getSelectedServiceElement();
				
				MainClient.integerService.getDeviceDetails(selectedElement.getID(), new AsyncCallback<DeviceDetails>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to receive Devices from Integer");
					}

					@Override
					public void onSuccess(DeviceDetails deviceDetails) {
						DeviceDetailsPanel detailsPanel = new DeviceDetailsPanel(selectedElement.getName(), deviceDetails);
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
		
		containeeTreeView = new ContaineeTreeView(title, headers);
		eastSplitPanel.addEast(containeeTreeView, 300);
		eastSplitPanel.setWidgetHidden(containeeTreeView, true);
		eastSplitPanel.setWidgetToggleDisplayAllowed(containeeTreeView, true);
		eastSplitPanel.add(networkPanel);
		
		eastPanel.add(mapToolbarPanel, DockPanel.NORTH);
		eastPanel.add(eastSplitPanel, DockPanel.CENTER);
		eastPanel.add(eventView, DockPanel.SOUTH);
		
		FilterPanel filterPanel = new FilterPanel(getDemoFilter());
		addWest(filterPanel, WESTPANEL_WIDTH);
		setWidgetToggleDisplayAllowed(filterPanel, true);
		
		add(eastPanel);
		
	}
	
	private Filter getDemoFilter() {
		Filter filter = new Filter();
		List<ID> ids = new ArrayList<ID>();
		ids.add(new ID(1L, "Load Balancers/Round Robin", new IDType("Technology")));
		ids.add(new ID(2L, "Load Balancers/Dynamic Ratio", new IDType("Technology")));
		ids.add(new ID(3L, "Load Balancers/Fastest", new IDType("Technology")));
		ids.add(new ID(4L, "Load Balancers/Least", new IDType("Technology")));
		ids.add(new ID(5L, "Routers/BGP", new IDType("Technology")));
		ids.add(new ID(6L, "Routers/OSPF", new IDType("Technology")));
		filter.setTechnologies(ids);
		
		List<ID> providerIds = new ArrayList<ID>();
		providerIds.add(new ID(11L, "Cisco", new IDType("Technology")));
		providerIds.add(new ID(12L, "Lucent", new IDType("Technology")));
		providerIds.add(new ID(13L, "Juniper", new IDType("Technology")));
		filter.setProviders(providerIds);
		
		List<CriticalityEnum> criticalities = new ArrayList<CriticalityEnum>();
		for (CriticalityEnum e : CriticalityEnum.values()) {
			criticalities.add(e);
		}
		filter.setCriticalities(criticalities );
		
		List<ID> locationIds = new ArrayList<ID>();
		locationIds.add(new ID(31L, "Boston", new IDType("Technology")));
		locationIds.add(new ID(32L, "Cambridge", new IDType("Technology")));
		locationIds.add(new ID(33L, "New York", new IDType("Technology")));
		filter.setLocations(locationIds);
		
		List<ID> serviceIds = new ArrayList<ID>();
		serviceIds.add(new ID(41L, "Internet", new IDType("Technology")));
		serviceIds.add(new ID(42L, "Video", new IDType("Technology")));
		serviceIds.add(new ID(43L, "Wireless", new IDType("Technology")));
		filter.setServices(serviceIds);
		
		List<ID> organizationIds = new ArrayList<ID>();
		organizationIds.add(new ID(51L, "Harvard University", new IDType("Technology")));
		organizationIds.add(new ID(52L, "Boston University", new IDType("Technology")));
		organizationIds.add(new ID(53L, "Northeastern University", new IDType("Technology")));
		filter.setOrginizations(organizationIds);
		
		return filter;
	}
	
	private VerticalPanel createNetworkTreePanel() {
		VerticalPanel treePanel = new VerticalPanel();
		Tree staticTree = createStaticTree();
	    staticTree.setAnimationEnabled(true);
	    staticTree.ensureDebugId("cwTree-staticTree");
	    ScrollPanel staticTreeWrapper = new ScrollPanel(staticTree);
	    staticTreeWrapper.ensureDebugId("cwTree-staticTree-Wrapper");
	    staticTreeWrapper.setSize("250px", "500px");
	    
	    treePanel.add(staticTree);
	    
	    return treePanel;
	}
	
	private Tree createStaticTree() {
	    // Create the tree
	    String[] networks = {"Cambridge Campus", "Allston Campus", "Longwood Medical", };
	    String[] subnetworks = {"192.168.1.", "192.168.2.", "192.168.3.", };
	    
	    Tree tree = new Tree();
	    tree.setAnimationEnabled(true);
	    TreeItem root = new TreeItem();
	    root.setText("Physical Network");

	    final TreeItem deviceNode = root.addTextItem("Discovered Devices");
	    SelectionHandler<TreeItem> handler = new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				if(event.getSelectedItem() == deviceNode) {
					
		            networkPanel.removeAll();

		            final DeviceMap deviceMap = new DeviceMap();
		            deviceMap.demo(100);
		            
		            networkPanel.add(deviceMap);
		            networkPanel.getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.ANY));
		            
		            MainClient.integerService.getTopLevelElements(new AsyncCallback<ServiceElement[]>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Failed to receive Devices from Integer");
						}

						@Override
						public void onSuccess(ServiceElement[] result) {
							deviceMap.update(result);
						}
					});
		        }
			}
	    	
	    };
		tree.addSelectionHandler(handler);
	    
	    int i = 1;
	    for (String network : networks) {
		    TreeItem cambridgeNet = root.addTextItem(network);
		    for (String subnet : subnetworks) {
		    	cambridgeNet.addTextItem(subnet+i++);
			}
	    }

	    root.setState(true);
	    tree.addItem(root);
	    return tree;
	}
	
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

	public static void enablecontaineeTreeView(boolean enable) {
		eastSplitPanel.setWidgetHidden(containeeTreeView, !enable);
	}
	
	public static void showContaineeTreeView(final ServiceElement se) {
		//containeeTreeView.updateTitle(se.getName());
		eastSplitPanel.setWidgetHidden(containeeTreeView, false);
		
		MainClient.integerService.getServiceElementByParentId(se.getID(), new AsyncCallback<ServiceElement[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error");
			}

			@Override
			public void onSuccess(ServiceElement[] serviceElements) {
				/*if (serviceElements == null || serviceElements.length == 0) {
					serviceElements = new ServiceElement[9];
					for (int i = 0; i < serviceElements.length; i++) {
						serviceElements[i] = new ServiceElement();
						serviceElements[i].setName("fake-se" + (i+1));
					}
				}
				*/
				containeeTreeView.updateTree(se.getName(), serviceElements);
			}
		});
		
		ServiceElement[] serviceElements = new ServiceElement[39];
		for (int i = 0; i < serviceElements.length; i++) {
			serviceElements[i] = new ServiceElement();
			serviceElements[i].setName("fake-se" + (i+1));
		}
	
		containeeTreeView.updateTree(se.getName(), serviceElements);
		
	}
}
