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
import edu.harvard.integer.client.widget.HvListBoxPanel;

/**
 * This class represents a form panel for importing MIB file.
 *
 * @author jhuang
 */
public class ServiceElementTypePanel extends FormPanel {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final IntegerServiceAsync integerService = GWT
			.create(IntegerService.class);

	/**
	 * Create a new MibImportPanel.
	 */
	public ServiceElementTypePanel() {
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(7, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		final TextBox nameTextBox = new TextBox();
		grid.setWidget(0, 1, nameTextBox);

		grid.setWidget(1, 0, new Label("Model"));
		final TextBox modelTextBox = new TextBox();
		grid.setWidget(1, 1, modelTextBox);
		
		grid.setWidget(2, 0, new Label("Vendor"));
		final TextBox vendorTextBox = new TextBox();
		grid.setWidget(2, 1, vendorTextBox);

		grid.setWidget(3, 0, new Label("Firmware"));
		final TextBox firmwareTextBox = new TextBox();
		grid.setWidget(3, 1, firmwareTextBox);
		
		grid.setWidget(4, 0, new Label("Element Type"));
		final ListBox elementTypeListBox = new ListBox();
		//elementTypeListBox.setWidth("100%");
		elementTypeListBox.setName("listBoxFormElement");
		elementTypeListBox.addItem("Device", "deviceValue");
		elementTypeListBox.addItem("Port", "portValue");
		elementTypeListBox.addItem("Server", "serverValue");
		grid.setWidget(4, 1, elementTypeListBox);
		
		grid.setWidget(5, 0, new Label("Feature Set"));
		final HvListBoxPanel featureSetListBox = new HvListBoxPanel(true);
		featureSetListBox.setVisibleItemCount(5);
		featureSetListBox.setName("featureSetListBoxFormElement");
		featureSetListBox.addItem("Feature 1", "snmpValue");
		featureSetListBox.addItem("Feature 2", "snmpValue");
		featureSetListBox.addItem("Feature 3", "snmpValue");
		featureSetListBox.addItem("Feature 4", "snmpValue");
		grid.setWidget(5, 1, featureSetListBox.getVisualPanel());
		
		grid.setWidget(6, 0, new Label("Capability List"));
		final HvListBoxPanel capabilitiesListBox = new HvListBoxPanel(true);
		capabilitiesListBox.setVisibleItemCount(5);
		capabilitiesListBox.setName("capabilitiesListBoxFormElement");
		capabilitiesListBox.addItem("Capability 1", "snmpValue");
		capabilitiesListBox.addItem("Capability 2", "snmpValue");
		capabilitiesListBox.addItem("Capability 3", "snmpValue");
		capabilitiesListBox.addItem("Capability 4", "snmpValue");
		grid.setWidget(6, 1, capabilitiesListBox.getVisualPanel());

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "180px");
		grid.getCellFormatter().setWidth(0, 1, "250px");

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
