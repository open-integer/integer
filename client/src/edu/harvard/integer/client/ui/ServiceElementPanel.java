/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.client.IntegerServiceAsync;

/**
 * This class represents a form panel for importing MIB file.
 *
 * @author jhuang
 */
public class ServiceElementPanel extends FormPanel {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final IntegerServiceAsync integerService = GWT
			.create(IntegerService.class);

	/**
	 * Create a new MibImportPanel.
	 */
	public ServiceElementPanel() {
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(11, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		final TextBox nameTextBox = new TextBox();
		grid.setWidget(0, 1, nameTextBox);

		grid.setWidget(1, 0, new Label("Description"));
		final TextBox descTextBox = new TextBox();
		grid.setWidget(1, 1, descTextBox);
		
		grid.setWidget(2, 0, new Label("Primary Location"));
		final TextBox locationTextBox = new TextBox();
		grid.setWidget(2, 1, locationTextBox);

		grid.setWidget(3, 0, new Label("Operational Control ID"));
		final TextBox controlIdTextBox = new TextBox();
		grid.setWidget(3, 1, controlIdTextBox);
		
		grid.setWidget(4, 0, new Label("Criticality"));
		final TextBox criticalityTextBox = new TextBox();
		grid.setWidget(4, 1, criticalityTextBox);
		
		grid.setWidget(5, 0, new Label("Parent"));
		final ListBox parentBox = new ListBox();
		grid.setWidget(5, 1, parentBox);
		
		grid.setWidget(6, 0, new Label("Network Layer"));
		final ListBox networkLayerListBox = new ListBox();
		networkLayerListBox.setName("networkLayerBoxFormElement");
		networkLayerListBox.addItem("Layer 1", "layer1Value");
		networkLayerListBox.addItem("layer 2", "layer2Value");
		networkLayerListBox.addItem("layer 3", "layer3Value");
		grid.setWidget(6, 1, networkLayerListBox);
		
		grid.setWidget(7, 0, new Label("Capability Ordered List"));
		final ListBox capabilitiesListBox = new ListBox(true);
		capabilitiesListBox.setVisibleItemCount(3);
		capabilitiesListBox.setName("capabilitiesListBoxFormElement");
		capabilitiesListBox.addItem("Capability 1", "capability1Value");
		capabilitiesListBox.addItem("Capability 2", "capability2Value");
		capabilitiesListBox.addItem("Capability 3", "capability3Value");
		capabilitiesListBox.addItem("Capability 4", "capability4Value");
		grid.setWidget(7, 1, capabilitiesListBox);
		
		grid.setWidget(8, 0, new Label("Credentials"));
		final ListBox credentialsListBox = new ListBox();
		credentialsListBox.setVisibleItemCount(2);
		credentialsListBox.setName("credentialsListBoxFormElement");
		credentialsListBox.addItem("Credential 1", "cred1Value");
		credentialsListBox.addItem("Credential 2", "cred2Value");
		credentialsListBox.addItem("Credential 3", "cred3Value");
		grid.setWidget(8, 1, credentialsListBox);
		
		grid.setWidget(9, 0, new Label("Domain ID List"));
		final ListBox domainListBox = new ListBox();
		domainListBox.setName("domainListBoxFormElement");
		domainListBox.setVisibleItemCount(2);
		domainListBox.addItem("Domain 1", "domain1Value");
		domainListBox.addItem("Domain 2", "domain2Value");
		domainListBox.addItem("Domain 3", "domain3Value");
		grid.setWidget(9, 1, domainListBox);
		
		grid.setWidget(10, 0, new Label("Mechanism List"));
		final ListBox mechanismListBox = new ListBox();
		mechanismListBox.setName("mechanismListBoxFormElement");
		mechanismListBox.setVisibleItemCount(2);
		mechanismListBox.addItem("Mechanism 1", "Mechanism1Value");
		mechanismListBox.addItem("Mechanism 2", "Mechanism2Value");
		mechanismListBox.addItem("Mechanism 3", "Mechanism3Value");
		grid.setWidget(10, 1, mechanismListBox);

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "150px");
		grid.getCellFormatter().setWidth(0, 1, "200px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

		// Add an event handler to the form.
		addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(final SubmitEvent submitEvent) {
				Window.alert("Under construction ...");
			}
		});

		addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {

				// When the form submission is successfully completed, this
				// event is fired. Assuming the service returned a response of
				// type text/html, we can get the result text here (see the
				// FormPanel documentation for further explanation).
				Window.alert(event.getResults());
			}
		});
	}
}
