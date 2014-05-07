package edu.harvard.integer.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.client.IntegerServiceAsync;

public class DiscoveryRulePanel extends FormPanel {

	private final IntegerServiceAsync integerService = GWT.create(IntegerService.class);
	
	public DiscoveryRulePanel() {
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(2, 2);
		setWidget(grid);

		// Create a CheckBox widget to indicate it is a standard MIB
		grid.setWidget(0, 0, new Label("IP Address"));
		final TextBox ipTextBox = new TextBox();
		ipTextBox.setText("10.240.127.0");
		grid.setWidget(0, 1, ipTextBox);
		
		grid.setWidget(1, 0, new Label("Mask"));
		final TextBox maskTextBox = new TextBox();
		maskTextBox.setText("255.255.255.0");
		grid.setWidget(1, 1, maskTextBox);

		// You can use the CellFormatter to affect the layout of the grid's
		// cells.
		grid.getCellFormatter().setWidth(0, 0, "150px");
		grid.getCellFormatter().setWidth(0, 1, "250px");

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

		// Add an event handler to the form.
		addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(final SubmitEvent submitEvent) {
				final String address = ipTextBox.getText();
				final String mask = maskTextBox.getText();
				integerService.startDiscovery(address, mask, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Discovery failed");
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Discovery  completed");
					}
					
				});
			}
		});

		addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {

				Window.alert(event.getResults());
			}
		});
	}
}
