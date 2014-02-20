package edu.harvard.integer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.harvard.integer.client.ui.MIBImportPanel;
import edu.harvard.integer.client.ui.ServiceElementTypePanel;
import edu.harvard.integer.client.widget.HuitDialogBox;
import edu.harvard.integer.client.widget.HuitFlexTable;
import edu.harvard.integer.common.snmp.MIBInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainClient implements EntryPoint {
	private Widget currentWidget;
	private HuitFlexTable flexTable;

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
			DOM.getElementById("ServiceElementType");
			Element element = (Element) Document.get().getElementById(
					"ServiceElementType");
			Anchor testAnchor = Anchor.wrap(element);
			testAnchor.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					String[] headers = {"Name", "IP Address", "Mac Address", "Type", "State", "SW/Version", "H/W Version"};
					flexTable = new HuitFlexTable(headers);
					
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

		Element element = (Element) Document.get().getElementById("Mib");
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
				flexTable = new HuitFlexTable(hearders);

				if (currentWidget != null)
					RootPanel.get("root").remove(currentWidget);
				currentWidget = flexTable.getVisualPanel();
				RootPanel.get("root").add(currentWidget);
			}
		});

		Element importElement = (Element) Document.get().getElementById(
				"MibImport");
		Anchor importAnchor = Anchor.wrap(importElement);
		importAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MIBImportPanel importPanel = new MIBImportPanel();
				HuitDialogBox importDialog = new HuitDialogBox("Import MIB",
						importPanel);
				importDialog.setSize("400px", "200px");
				importDialog.center();
				importDialog.show();
			}
		});
		
		Element serviceElementTypeElement = (Element) Document.get().getElementById(
				"Fault");
		Anchor serviceAnchor = Anchor.wrap(serviceElementTypeElement);
		serviceAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ServiceElementTypePanel addPanel = new ServiceElementTypePanel();
				HuitDialogBox addDialog = new HuitDialogBox("Add Service Element Type",
						addPanel);
				addDialog.setSize("400px", "500px");
				addDialog.center();
				addDialog.show();
			}
		});
		
		Element capabilityElement = (Element) Document.get().getElementById(
				"Capability");
		Anchor capabilityAnchor = Anchor.wrap(capabilityElement);
		capabilityAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ServiceElementTypePanel addPanel = new ServiceElementTypePanel();
				HuitDialogBox addDialog = new HuitDialogBox("Add Capability",
						addPanel);
				addDialog.setSize("400px", "200px");
				addDialog.center();
				addDialog.show();
			}
		});

	}
}
