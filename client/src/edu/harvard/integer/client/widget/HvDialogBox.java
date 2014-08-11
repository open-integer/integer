/*
 * 
 */
package edu.harvard.integer.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class HuitDialogBox represents a dialog box for generic use.
 * This is a subclass class extended from com.google.gwt.user.client.ui.DialogBox.
 * It includes the following panels:
 *     a formPanel - content panel
 *     a buttonPanel - action button panel including leftButtonPanel and rightButtonPanel
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvDialogBox extends DialogBox {
	
	/** The form panel. */
	private Panel panel;
	
	/** The button panel. */
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	
	/** The left button panel. */
	private HorizontalPanel leftButtonPanel = new HorizontalPanel();
	
	/** The right button panel. */
	private HorizontalPanel rightButtonPanel = new HorizontalPanel();
	
	/** The close button. */
	private Button closeButton = new Button("Close");
	
	/** The ok button. */
	private Button okButton = new Button("OK");

	/**
	 * Creates a new HvDialogBox instance.
	 *
	 * @param title the title
	 * @param formPanel the form panel
	 */
	public HvDialogBox(String title, Panel panel) {
		this(title, panel, false, false);
	}
	
	/**
	 * Creates a new HvDialogBox instance.
	 *
	 * @param title the title
	 * @param formPanel the form panel
	 * @param autoHide the auto hide
	 * @param modal the modal
	 */
	public HvDialogBox(String title, Panel panel, boolean autoHide, boolean modal) {
		super(autoHide, modal);
		this.panel = panel;
		setText(title);
		setGlassEnabled(false);
	    setAnimationEnabled(true);

		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setStyleName("dialogVPanel");

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
	    
	    
		dialogContents.add(panel);
		dialogContents.add(buttonPanel);
		
	}
	
	/** The close handler. */
	private ClickHandler closeHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide();
		}
	};
	
	/** The ok handler. */
	private ClickHandler okHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			
			hide();
		}
	};
	
	/**
	 * Enable ok button.
	 *
	 * @param enable the enable
	 */
	public void enableOkButton(boolean enable) {
		okButton.setVisible(enable);
	}
	
	/**
	 * Enable close button.
	 *
	 * @param enable the enable
	 */
	public void enableCloseButton(boolean enable) {
		closeButton.setVisible(enable);
	}
}
