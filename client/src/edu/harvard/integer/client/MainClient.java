package edu.harvard.integer.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.harvard.integer.client.ui.CalendarPolicyPanel;
import edu.harvard.integer.client.ui.CapabilityPanel;
import edu.harvard.integer.client.ui.CapabilityView;
import edu.harvard.integer.client.ui.ContactPanel;
import edu.harvard.integer.client.ui.DiscoveryRuleView;
import edu.harvard.integer.client.ui.IpTopologySeedView;
import edu.harvard.integer.client.ui.LocationPanel;
import edu.harvard.integer.client.ui.MIBImportPanel;
import edu.harvard.integer.client.ui.MechanismPanel;
import edu.harvard.integer.client.ui.OragnizationPanel;
import edu.harvard.integer.client.ui.RolePanel;
import edu.harvard.integer.client.ui.ServiceElementPanel;
import edu.harvard.integer.client.ui.ServiceElementTypePanel;
import edu.harvard.integer.client.ui.SnmpGlobalReadCredentialView;
import edu.harvard.integer.client.ui.StartDiscoveryPanel;
import edu.harvard.integer.client.ui.StatusPanel;
import edu.harvard.integer.client.ui.SystemSplitViewPanel;
import edu.harvard.integer.client.ui.UserPanel;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvFlexTable;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SnmpGlobalReadCredential;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * This is the entry point classes define <code>onModuleLoad()</code> of Integer Web Client.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class MainClient implements EntryPoint {
	
	/** The Constant WINDOW_WIDTH. */
	public static final int WINDOW_WIDTH = 1200;
	
	/** The Constant WINDOW_HEIGHT. */
	public static final int WINDOW_HEIGHT = 900;
	
	/** The Constant SPLITTER_SIZE. */
	public static final int SPLITTER_SIZE = 12;
	
	/** The current widget. */
	public static Widget currentWidget = null;
	
	/** The flex table. */
	private HvFlexTable flexTable;
	
	/** The status panel. */
	public static StatusPanel statusPanel = new StatusPanel();
	
	private boolean initWindow = true;
	private boolean initResize = true;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	public static final IntegerServiceAsync integerService = GWT
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
		
		// Location
		createAddLocationLink();
		
		// Organization
		createAddOrganizationLink();
		
		// Calendar Policy
		createAddCalendarPolicyLink();
		
		// Role
		createAddRoleLink();
		
		// Contact
		createAddContactLink();
		
		// User
		createAddUserLink();
		
		// Start Discovery
		createStartDiscoveryLink();
		
		// DiscoveryRules
		createDiscoveryRulesLink();
		
		// IpTopologySeeds
		createIpTopologySeedsLink();
		
		// SnmpGlobalReadCredentials
		createSnmpGlobalReadCredentials();
		
		currentWidget = new SystemSplitViewPanel();
		RootPanel.get("root").add(currentWidget);
		
		// Status Bar
		RootPanel.get("status").add(statusPanel);
		
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				Scheduler.get().scheduleDeferred(
		                new Scheduler.ScheduledCommand() {
		                    public void execute() {
		                    	if (initWindow) {
		                    		int height = Window.getClientHeight();
		                    		int contentHeight = height - 150;
		                    		RootPanel.get("root").setHeight(contentHeight+"px");
			                    	currentWidget.setSize("100%", contentHeight+"px");
		                    		initWindow = false;
		                    		return;
		                    	}
		                    	
		                    	if (initResize) {
		                    		initResize = false;
		                    		RootPanel.get("root").setHeight("100%");
			                    	currentWidget.setSize("100%", "100%");
		                    	}
		                    }
		                });
			}

		});
	}

	/**
	 * Creates the system page link.
	 */
	private void createSystemPageLink() {
		Element element = (Element) Document.get().getElementById("home");
		Anchor testAnchor = Anchor.wrap(element);
		testAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);
				
				currentWidget = new SystemSplitViewPanel(); // createHomePage();
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	/**
	 * Creates the view imported mibs link.
	 */
	private void createViewImportedMibsLink() {
		Element element = (Element) Document.get().getElementById("mibs");
		Anchor testAnchor = Anchor.wrap(element);
		testAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				integerService.getImportedMibs(new AsyncCallback<MIBInfo[]>() {

					@Override
					public void onFailure(Throwable caught) {
						MainClient.statusPanel.showAlert("Failed to get all mib modules");
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
	
	/**
	 * Creates the view service element types link.
	 */
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
	
	/**
	 * Creates the view capabilities link.
	 */
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
	
	/**
	 * Creates the add service element type link.
	 */
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
	
	/**
	 * Creates the import mib link.
	 */
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
	
	/**
	 * Creates the adddd service element link.
	 */
	private void createAddddServiceElementLink() {
		Element serviceElementElement = (Element) Document.get().getElementById(
				"addServiceElement");
		Anchor serviceAnchor = Anchor.wrap(serviceElementElement);
		serviceAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ServiceElement se = new ServiceElement();
				ServiceElementPanel addPanel = new ServiceElementPanel(se);
				HvDialogBox addDialog = new HvDialogBox("Add Service Element",
						addPanel);
				addDialog.setSize("450px", "520px");
				addDialog.center();
				addDialog.show();
			}
		});
	}
	
	/**
	 * Creates the add capability link.
	 */
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
	
	/**
	 * Creates the add mechanism link.
	 */
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
	
	/**
	 * Creates the add location link.
	 */
	private void createAddLocationLink() {
		Element element = (Element) Document.get().getElementById(
				"addLocation");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LocationPanel panel = new LocationPanel();
				HvDialogBox dialog = new HvDialogBox("Add Location",
						panel);
				dialog.setSize("400px", "200px");
				dialog.center();
				dialog.show();
			}
		});
	}
	
	/**
	 * Creates the add organization link.
	 */
	private void createAddOrganizationLink() {
		Element element = (Element) Document.get().getElementById(
				"addOrganization");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				OragnizationPanel panel = new OragnizationPanel();
				HvDialogBox dialog = new HvDialogBox("Add Oragnization",
						panel);
				dialog.setSize("400px", "200px");
				dialog.center();
				dialog.show();
			}
		});
	}
	
	/**
	 * Creates the add calendar policy link.
	 */
	private void createAddCalendarPolicyLink() {
		Element element = (Element) Document.get().getElementById(
				"addCalendar");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CalendarPolicyPanel panel = new CalendarPolicyPanel();
				HvDialogBox dialog = new HvDialogBox("Add Calendar Policy",
						panel);
				dialog.setSize("400px", "200px");
				dialog.center();
				dialog.show();
			}
		});
	}

	/**
	 * Creates the add role link.
	 */
	private void createAddRoleLink() {
		Element element = (Element) Document.get().getElementById(
				"addRole");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RolePanel panel = new RolePanel();
				HvDialogBox dialog = new HvDialogBox("Add Role",
						panel);
				dialog.setSize("400px", "200px");
				dialog.center();
				dialog.show();
			}
		});
	}
	
	/**
	 * Creates the add contact link.
	 */
	private void createAddContactLink() {
		Element element = (Element) Document.get().getElementById(
				"addContact");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ContactPanel panel = new ContactPanel();
				HvDialogBox dialog = new HvDialogBox("Add Contact",
						panel);
				dialog.setSize("400px", "300px");
				dialog.center();
				dialog.show();
			}
		});
	}

	/**
	 * Creates the add user link.
	 */
	private void createAddUserLink() {
		Element element = (Element) Document.get().getElementById(
				"addUser");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UserPanel panel = new UserPanel();
				HvDialogBox dialog = new HvDialogBox("Add User",
						panel);
				dialog.setSize("400px", "300px");
				dialog.center();
				dialog.show();
			}
		});
	}
	
	/**
	 * Creates the start discovery link.
	 */
	private void createStartDiscoveryLink() {
		Element element = (Element) Document.get().getElementById(
				"startDiscovery");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				StartDiscoveryPanel panel = new StartDiscoveryPanel();
				HvDialogBox dialog = new HvDialogBox("Start Discovery",
						panel);
				dialog.setSize("400px", "250px");
				dialog.center();
				dialog.show();
			}
		});
	}

	/**
	 * Creates the discovery rules link.
	 */
	private void createDiscoveryRulesLink() {
		Element element = (Element) Document.get().getElementById("discoveryRules");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String title = "Discovery Rules";
				final String[] headers = {"Name", "Description", "Type", "IP Topology Seeds Number", "Created", "Modified"};
				final int[] columnWidthes = {100, 150, 100, 80, 100, 100};
				
				final DiscoveryRuleView view = new DiscoveryRuleView(title, headers);
				view.setColumnsWidth(columnWidthes);
				
				integerService.getAllDiscoveryRules(new AsyncCallback<DiscoveryRule[]>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(DiscoveryRule[] result) {
						view.update(result);
					}
					
				});

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);

				currentWidget = view;
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	/**
	 * Creates the ip topology seeds link.
	 */
	private void createIpTopologySeedsLink() {
		Element element = (Element) Document.get().getElementById("ipTopologySeeds");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String title = "IP Topology Seeds";
				final String[] headers = {"Name", "Description", "Subnet", "Radius", "Discovery Timeout", "Discovery Retries", "Topology Timeout", "Topology Retries", "Initial Gateway"};
				final int[] columnWidthes = {100, 150, 100, 100, 80,  80, 80, 80, 120};
				
				final IpTopologySeedView ipTopologySeedView = new IpTopologySeedView(title, headers);
				ipTopologySeedView.setColumnsWidth(columnWidthes);
				integerService.getAllIpTopologySeeds(new AsyncCallback<IpTopologySeed[]>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(IpTopologySeed[] result) {
						ipTopologySeedView.update(result);
					}
					
				});

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);

				currentWidget = ipTopologySeedView;
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
	/**
	 * Creates the snmp global read credentials.
	 */
	private void createSnmpGlobalReadCredentials() {
		Element element = (Element) Document.get().getElementById("snmpGlobalReadCredentials");
		Anchor anchor = Anchor.wrap(element);
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String title = "SNMP Global Read Credentials";
				final String[] headers = {"Name", "Credentials", "Alternate Port List"};
				final int[] columnWidthes = {150, 300, 150};
				
				final SnmpGlobalReadCredentialView view = new SnmpGlobalReadCredentialView(title, headers);
				view.setColumnsWidth(columnWidthes);
				integerService.getAllGlobalCredentails(new AsyncCallback<SnmpGlobalReadCredential[]>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(SnmpGlobalReadCredential[] result) {
						view.update(result);
					}
					
				});

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);

				currentWidget = view;
				RootPanel.get("root").add(currentWidget);
			}
		});
	}
	
}
