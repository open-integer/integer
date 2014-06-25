/*
 * 
 */
package edu.harvard.integer.client.ui;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.client.IntegerServiceAsync;

/**
 * This MIBImportPanel class represents a panel to import MIB file.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class MIBImportPanel extends FormPanel {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final IntegerServiceAsync integerService = GWT
			.create(IntegerService.class);

	/**
	 * Create a new MibImportPanel.
	 */
	public MIBImportPanel() {
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		// Create a grid panel to hold all of the form widgets.
		Grid grid = new Grid(1, 2);
		setWidget(grid);

		// Create a FileUpload widget
		grid.setWidget(0, 0, new Label("Select MIB File"));
		final FileUploadExt upload = new FileUploadExt();

		upload.setName("uploadFormElement");
		grid.setWidget(0, 1, upload);

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
				// This event is fired just before the form is submitted. We can
				// take this opportunity to perform validation.
				final String filename = upload.getFilename();

				if (filename.length() == 0) {
					Window.alert("No File Specified!");
					submitEvent.cancel();
					return;
				}

				FileList files = upload.getFiles();

				File file = files.getItem(0);
				String fileType = file.getType();

				if (!fileType.startsWith("text") && !fileType.isEmpty()) {
					Window.alert("The file you selected \"" + filename + "\" is not a text file.");
					submitEvent.cancel();
					return;
				}

				// create file reader
				final FileReader reader = new FileReader();
				reader.addErrorHandler(new ErrorHandler() {

					@Override
					public void onError(ErrorEvent errEvent) {
						Window.alert("Import failed: " + errEvent.toString());
						submitEvent.cancel();
					}
				});

				reader.addLoadEndHandler(new LoadEndHandler() {

					@Override
					public void onLoadEnd(LoadEndEvent event) {
						final String mibContents = reader.getStringResult();
						integerService.mibImport(filename, mibContents,
								true,
								new AsyncCallback<String>() {
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the
										// user
										Window.alert("Import failed: "
												+ caught.getMessage() + "\n\n"
												+ "Mib file " + filename
												+ ": \n\n" + mibContents);
									}

									public void onSuccess(String result) {
										Window.alert("Import completed");
									}
								});
					}
				});

				reader.readAsText(file);
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
