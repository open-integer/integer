package edu.harvard.integer.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author jhuang
 *
 */
public class HuitDialogBox extends DialogBox {
	private FormPanel formPanel;
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel leftButtonPanel = new HorizontalPanel();
	private HorizontalPanel rightButtonPanel = new HorizontalPanel();
	
	private Button closeButton = new Button("Close");
	private Button okButton = new Button("OK");

	public HuitDialogBox(String title, FormPanel formPanel) {
		this.formPanel = formPanel;
		setText(title);
		setGlassEnabled(true);
	    setAnimationEnabled(true);

		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setStyleName("dialogVPanel");
		dialogContents.setSize("400px", "400px");

	    setWidget(dialogContents);

		// Add a close button at the bottom of the dialog
	    closeButton.addClickHandler(closeHandler);
	    okButton.addClickHandler(okHandler);

	    buttonPanel.setStyleName("dialogVPanel");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	    buttonPanel.setWidth("100%");
	    
	    buttonPanel.setSpacing(10);
	    
	    buttonPanel.add(leftButtonPanel);
	    buttonPanel.add(rightButtonPanel);
	    
	    leftButtonPanel.setWidth("45%");
	    rightButtonPanel.setWidth("55%");
	    
	    rightButtonPanel.add(okButton);
	    rightButtonPanel.add(closeButton);
	    
	    rightButtonPanel.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    rightButtonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    
		dialogContents.add(formPanel);
		dialogContents.add(buttonPanel);
		
	}
	
	private ClickHandler closeHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide();
		}
	};
	
	private ClickHandler okHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (formPanel != null)
				formPanel.submit();
		}
	};
}
