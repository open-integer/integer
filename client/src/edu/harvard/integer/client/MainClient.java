package edu.harvard.integer.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.harvard.integer.client.ui.MIBImportPanel;
import edu.harvard.integer.client.widget.HuitDialogBox;
import edu.harvard.integer.client.widget.HuitFlexTable;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.shared.FieldVerifier;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainClient implements EntryPoint {
	private Widget currentWidget;
	private HuitFlexTable flexTable;
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private final IntegerServiceAsync integerService = GWT
			.create(IntegerService.class);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final Button sendButton = new Button("Go");
		final TextBox nameField = new TextBox();
		nameField.setText("");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

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
							String moduleName = ""+mibInfo.getName() +"Name";
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
				importDialog.setSize("400px", "400px");
				importDialog.center();
				importDialog.show();
			}
		});

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
