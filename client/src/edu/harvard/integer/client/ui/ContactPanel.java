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

/**
 * The Class ContactPanel represents a panel to configure Contact object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ContactPanel extends FormPanel {

	/**
	 * Create a new ContactPanel.
	 */
	public ContactPanel() {
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(12, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("Name"));
		final TextBox nameTextBox = new TextBox();
		grid.setWidget(0, 1, nameTextBox);

		grid.setWidget(1, 0, new Label("Address 1"));
		final TextBox address1Box = new TextBox();
		grid.setWidget(1, 1, address1Box);
		
		grid.setWidget(2, 0, new Label("Address 2"));
		final TextBox address2Box = new TextBox();
		grid.setWidget(2, 1, address2Box);
		
		grid.setWidget(3, 0, new Label("City"));
		final TextBox cityBox = new TextBox();
		grid.setWidget(3, 1, cityBox);
		
		grid.setWidget(4, 0, new Label("State"));
		final ListBox stateListBox = new ListBox();
		stateListBox.setName("stateListBoxFormElement");
		stateListBox.addItem("MA", "maValue");
		stateListBox.addItem("ME", "meValue");
		stateListBox.addItem("NH", "nhValue");
		grid.setWidget(4, 1, stateListBox);
		
		grid.setWidget(5, 0, new Label("Zip"));
		final TextBox zipBox = new TextBox();
		grid.setWidget(5, 1, zipBox);
		
		grid.setWidget(6, 0, new Label("Type"));
		final ListBox typeListBox = new ListBox();
		typeListBox.addItem("Primary Provider", "primaryValue");
		typeListBox.addItem("Service Delivery", "deliveryValue");
		grid.setWidget(6, 1, typeListBox);

		grid.setWidget(7, 0, new Label("Primary Email"));
		final TextBox priEmailBox = new TextBox();
		grid.setWidget(7, 1, priEmailBox);
		
		grid.setWidget(8, 0, new Label("Secondary Email"));
		final TextBox secEmailBox = new TextBox();
		grid.setWidget(8, 1, secEmailBox);
		
		grid.setWidget(9, 0, new Label("Primary Phone"));
		final TextBox priPhoneBox = new TextBox();
		grid.setWidget(9, 1, priPhoneBox);
		
		grid.setWidget(10, 0, new Label("Secondary Phone"));
		final TextBox secPhoneBox = new TextBox();
		grid.setWidget(10, 1, secPhoneBox);
		
		grid.setWidget(11, 0, new Label("SMS Number"));
		final TextBox smsNumberBox = new TextBox();
		grid.setWidget(11, 1, smsNumberBox);
		
		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "150px");
		grid.getCellFormatter().setWidth(0, 1, "220px");

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
				Window.alert(event.getResults());
			}
		});
	}
}
