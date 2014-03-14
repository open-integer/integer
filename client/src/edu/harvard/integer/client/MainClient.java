package edu.harvard.integer.client;

import java.util.List;

import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.harvard.integer.client.ui.CapabilityPanel;
import edu.harvard.integer.client.ui.CapabilityView;
import edu.harvard.integer.client.ui.EventView;
import edu.harvard.integer.client.ui.FilterView;
import edu.harvard.integer.client.ui.MIBImportPanel;
import edu.harvard.integer.client.ui.MechanismPanel;
import edu.harvard.integer.client.ui.ServiceElementPanel;
import edu.harvard.integer.client.ui.ServiceElementTypePanel;
import edu.harvard.integer.client.widget.DragImageWidget;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvFlexTable;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.topology.Capability;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainClient implements EntryPoint {
	private Widget currentWidget;
	private HvFlexTable flexTable;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final IntegerServiceAsync integerService = GWT
			.create(IntegerService.class);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// view imported mib files
		createViewImportedMibsLink();

		// import mib
		createImportMibLink();
		
		// ServiceElementTypes
		createViewServiceElementTypesLink();

		// Add Service Element Type
		createAddServiceElementTypeLink();
		
		// Add Service Element
		createAddddServiceElementLink();
		
		// Add Capability
		createAddCapabilityLink();

		// Capabilities
		createViewCapabilitiesLink();

		// Mechanism
		createAddMechanismLink();
		
		// System
		createSystemPageLink();
		
		currentWidget = createHomePage();
		RootPanel.get("root").add(currentWidget);
		
	}
	
	private void createSystemPageLink() {
		Element element = (Element) Document.get().getElementById("system");
		Anchor testAnchor = Anchor.wrap(element);
		testAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);
				
				currentWidget = createHomePage();
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	private SplitLayoutPanel createHomePage() {
		LienzoPanel networkPanel = new LienzoPanel(950, 550);

        DragImageWidget dragImageWidget = new DragImageWidget(90, 50);
        networkPanel.add(dragImageWidget);
        
		SplitLayoutPanel systemPanel = new SplitLayoutPanel(5);
		systemPanel.setSize("1200px", "700px");
		SplitLayoutPanel westPanel = new SplitLayoutPanel(3);
		SplitLayoutPanel eastPanel = new SplitLayoutPanel(3);
		
		FilterView filterView = createFilterView();
		westPanel.addSouth(filterView, 200);
		westPanel.add(createNetworkTreePanel());
		westPanel.setWidgetToggleDisplayAllowed(filterView, true);

		EventView eventView = createEventView();
		eastPanel.addSouth(eventView, 150);
		eastPanel.add(networkPanel);
		eastPanel.setWidgetToggleDisplayAllowed(eventView, true);
		
		systemPanel.addWest(westPanel, 250);
		systemPanel.add(eastPanel);
		
		return systemPanel;
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
	
	private void createViewImportedMibsLink() {
		Element element = (Element) Document.get().getElementById("mibs");
		Anchor testAnchor = Anchor.wrap(element);
		testAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				integerService.getImportedMibs(new AsyncCallback<MIBInfo[]>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to get all mib modules");
					}

					@Override
					public void onSuccess(MIBInfo[] result) {
						if (result == null || result.length == 0)
							return;
						
						flexTable.clean();
						
						for (MIBInfo mibInfo : result) {
							String moduleName = ""+mibInfo.getName();
							String lastUpdate = ""+mibInfo.getModule().getLastUpdated();
							String oid = ""+mibInfo.getModule().getOid();
							String description = ""+mibInfo.getModule().getDescription();
							String vendor = ""+mibInfo.getVendor();
							
							Object[] rowData = { moduleName, lastUpdate, oid, description, vendor};
							flexTable.addRow(rowData);
						}
						flexTable.applyDataRowStyles();
					}
					
				});

				String[] hearders = {"Module", "Last Updated", "OID", "Description", "Vendor"};
				flexTable = new HvFlexTable(hearders);

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);
				currentWidget = flexTable.getVisualPanel();
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	private void createViewServiceElementTypesLink() {
		Element element = (Element) Document.get().getElementById(
				"serviceElementTypes");
		Anchor testAnchor = Anchor.wrap(element);
		testAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String[] headers = {"Name", "IP Address", "Mac Address", "Type", "State", "SW/Version", "H/W Version"};
				flexTable = new HvFlexTable(headers);
				
				int numRows = 50;

				for (int row = 1; row < numRows; row++) {
					Object[] rowData = { "Huit " + row,
							"192.168.55." + row,
							"255.255.255.255.100." + row, "Fiber " + row,
							"Normal" + row, "sw" + row, "hw" + row };
					flexTable.addRow(rowData);
				}
				flexTable.applyDataRowStyles();

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);
				currentWidget = flexTable.getVisualPanel();
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	private void createViewCapabilitiesLink() {
		Element capElement = (Element) Document.get().getElementById("capabilities");
		Anchor capAnchor = Anchor.wrap(capElement);
		capAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String title = "Capabilities";
				final String[] headers = {"Name", "Description", "FCAPS", "Service Elements"};
				final CapabilityView capacityView = new CapabilityView(title, headers);
				integerService.getCapabilities(new AsyncCallback<List<Capability>>() {

					@Override
					public void onSuccess(List<Capability> result) {
						capacityView.update(result);
					}

					@Override
					public void onFailure(Throwable caught) {
					}
					
				});

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);

				currentWidget = capacityView;
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	private void createAddServiceElementTypeLink() {
		Element serviceElementTypeElement = (Element) Document.get().getElementById(
				"addServiceElementType");
		Anchor serviceElementAnchor = Anchor.wrap(serviceElementTypeElement);
		serviceElementAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ServiceElementTypePanel addPanel = new ServiceElementTypePanel();
				HvDialogBox addDialog = new HvDialogBox("Add Service Element Type",
						addPanel);
				addDialog.setSize("450px", "480px");
				addDialog.center();
				addDialog.show();
			}
		});
	}
	
	private void createImportMibLink() {
		Element importElement = (Element) Document.get().getElementById(
				"importMib");
		Anchor importAnchor = Anchor.wrap(importElement);
		importAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MIBImportPanel importPanel = new MIBImportPanel();
				HvDialogBox importDialog = new HvDialogBox("Import MIB",
						importPanel);
				importDialog.setSize("500px", "200px");
				importDialog.center();
				importDialog.show();
			}
		});
	}
	
	private void createAddddServiceElementLink() {
		Element serviceElementElement = (Element) Document.get().getElementById(
				"addServiceElement");
		Anchor serviceAnchor = Anchor.wrap(serviceElementElement);
		serviceAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ServiceElementPanel addPanel = new ServiceElementPanel();
				HvDialogBox addDialog = new HvDialogBox("Add Service Element",
						addPanel);
				addDialog.setSize("450px", "520px");
				addDialog.center();
				addDialog.show();
			}
		});
	}
	
	private void createAddCapabilityLink() {
		Element capabilityElement = (Element) Document.get().getElementById(
				"addCapability");
		Anchor capabilityAnchor = Anchor.wrap(capabilityElement);
		capabilityAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CapabilityPanel addPanel = new CapabilityPanel();
				HvDialogBox addDialog = new HvDialogBox("Add Capability",
						addPanel);
				addDialog.setSize("400px", "200px");
				addDialog.center();
				addDialog.show();
			}
		});
	}
	
	private void createAddMechanismLink() {
		Element mechanismElement = (Element) Document.get().getElementById(
				"addMechanism");
		Anchor mechanismAnchor = Anchor.wrap(mechanismElement);
		mechanismAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MechanismPanel addPanel = new MechanismPanel();
				HvDialogBox addDialog = new HvDialogBox("Add Mechanism",
						addPanel);
				addDialog.setSize("400px", "200px");
				addDialog.center();
				addDialog.show();
			}
		});
	}
}
