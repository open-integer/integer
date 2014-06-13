package edu.harvard.integer.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import edu.harvard.integer.common.topology.ServiceElement;

public class ServiceElementDetailsTabPanel extends TabLayoutPanel {

	private ServiceElementGeneralPanel generalPanel = new ServiceElementGeneralPanel();
	
	public ServiceElementDetailsTabPanel() {
		super(2.0, Unit.EM);
		setAnimationDuration(500);
	    getElement().getStyle().setMargin(1.0, Unit.PX);
	    
	    // Attribute Tab
	    add(generalPanel, "General");
	    
	    // Attribute Tab
	    HTML attributesText = new HTML("Attribute list");
	    add(attributesText, "Attributes");
	    
	    // Mechanisms Tab
	    HTML mechanismsText = new HTML("Mechanism list");
	    add(mechanismsText, "Mechanisms");
	    
	    // Mechanisms Tab
	    HTML capabilitesText = new HTML("Capabilite list");
	    add(capabilitesText, "Capabilites");
	}

	public void update(ServiceElement se) {
		generalPanel.update(se);
	}
}
