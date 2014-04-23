package edu.harvard.integer.client.ui;

import java.util.List;

import com.emitrom.lienzo.client.core.mediator.EventFilter;
import com.emitrom.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.widget.DragImageWidget;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;

public class SystemSplitViewPanel extends SplitLayoutPanel {
	private static final int SPLITTER_SIZE = 3;
	private static final int CONTENT_WIDTH = 950;
	private static final int CONTENT_HEIGHT = 550;
	private static final int WIDGET_WIDTH = 90;
	private static final int WIDGET_HEIGHT = 50;
	
	LienzoPanel networkPanel = new LienzoPanel(CONTENT_WIDTH, CONTENT_HEIGHT);

	public SystemSplitViewPanel() {
		super(SPLITTER_SIZE);

        DragImageWidget dragImageWidget = new DragImageWidget(WIDGET_WIDTH, WIDGET_HEIGHT);
        networkPanel.add(dragImageWidget);
        
        networkPanel.getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.ANY));
        LienzoPanel.enableWindowMouseWheelScroll(true);
        
		setSize(MainClient.WINDOW_WIDTH+"px", MainClient.WINDOW_HEIGHT+"px");
		SplitLayoutPanel westPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		SplitLayoutPanel eastPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		
		FilterView filterView = createFilterView();
		westPanel.addSouth(filterView, 200);
		westPanel.add(createNetworkTreePanel());
		westPanel.setWidgetToggleDisplayAllowed(filterView, true);

		EventView eventView = createEventView();
		eastPanel.addSouth(eventView, 150);
		eastPanel.add(networkPanel);
		eastPanel.setWidgetToggleDisplayAllowed(eventView, true);
		
		addWest(westPanel, 250);
		add(eastPanel);
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
		            
		            final DeviceMap deviceMap = new DeviceMap(WIDGET_WIDTH, WIDGET_HEIGHT);
		            
		            networkPanel.add(deviceMap);
		            
		            MainClient.integerService.getAllServiceElementManagementObjects(new AsyncCallback<List<ServiceElementManagementObject>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Failed to receive Devices from Integer");
						}

						@Override
						public void onSuccess(List<ServiceElementManagementObject> result) {
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
}
