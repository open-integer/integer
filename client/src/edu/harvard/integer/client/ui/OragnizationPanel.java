/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.client.widget.HvListBoxPanel;

/**
 * The Class OragnizationPanel represents a panel to configure Oragnization object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class OragnizationPanel extends FormPanel {

	/**
	 * Create a new OragnizationPanel.
	 */
	public OragnizationPanel() {
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(3, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		final TextBox nameTextBox = new TextBox();
		grid.setWidget(0, 1, nameTextBox);
		
		grid.setWidget(1, 0, new Label("Type"));
		final ListBox typeListBox = new ListBox();
		typeListBox.setName("typeListBoxFormElement");
		typeListBox.addItem("Provider", "maValue");
		typeListBox.addItem("Sub-unit Provider", "meValue");
		typeListBox.addItem("Consumer", "nhValue");
		grid.setWidget(1, 1, typeListBox);

		grid.setWidget(2, 0, new Label("Location List"));
		final HvListBoxPanel locationListBoxPanel = new HvListBoxPanel(true);
		locationListBoxPanel.setVisibleItemCount(5);
		locationListBoxPanel.setName("locationListBoxFormElement");
		locationListBoxPanel.addItem("Location 1", "locationValue");
		locationListBoxPanel.addItem("Location 2", "locationValue");
		locationListBoxPanel.addItem("Location 3", "locationValue");
		grid.setWidget(2, 1, locationListBoxPanel.getVisualPanel());

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "150px");
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
