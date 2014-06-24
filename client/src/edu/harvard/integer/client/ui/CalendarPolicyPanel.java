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
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.client.widget.HvListBoxPanel;

/**
 * The Class CalendarPolicyPanel represents a panel to configure calendar policy object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class CalendarPolicyPanel extends FormPanel {

	/**
	 * Create a new CalendarPolicyPanel.
	 */
	public CalendarPolicyPanel() {
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
		
		grid.setWidget(1, 0, new Label("Description"));
		final TextBox descTextBox = new TextBox();
		grid.setWidget(1, 1, descTextBox);

		grid.setWidget(2, 0, new Label("Calendar List"));
		final HvListBoxPanel calendarListBoxPanel = new HvListBoxPanel(true);
		calendarListBoxPanel.setVisibleItemCount(5);
		calendarListBoxPanel.setName("calendarListBoxPanel");
		calendarListBoxPanel.addItem("06:00 AM Jan 20, 2014", "calendar1Value");
		calendarListBoxPanel.addItem("07:00 AM Feb 21, 2014", "calendar2Value");
		calendarListBoxPanel.addItem("09:00 AM March 22, 2014", "calendar3Value");
		grid.setWidget(2, 1, calendarListBoxPanel.getVisualPanel());

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
				Window.alert(event.getResults());
			}
		});
	}
}
