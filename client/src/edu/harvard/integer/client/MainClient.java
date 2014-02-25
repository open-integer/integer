package edu.harvard.integer.client;

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
import com.google.gwt.user.client.ui.Widget;

import edu.harvard.integer.client.ui.CapabilityPanel;
import edu.harvard.integer.client.ui.MIBImportPanel;
import edu.harvard.integer.client.ui.MechanismPanel;
import edu.harvard.integer.client.ui.ServiceElementPanel;
import edu.harvard.integer.client.ui.ServiceElementTypePanel;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvFlexTable;
import edu.harvard.integer.common.snmp.MIBInfo;

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
		try {
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
		} catch (AssertionError e) {
			Window.alert("AssertionError");
		}

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

		// Mechanism
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
