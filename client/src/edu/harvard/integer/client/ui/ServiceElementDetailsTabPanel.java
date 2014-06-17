package edu.harvard.integer.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import edu.harvard.integer.client.widget.HvGridValuesPanel;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;

public class ServiceElementDetailsTabPanel extends TabLayoutPanel {

	final String[] headers = {"Name", "Description"};
	
	private ServiceElementGeneralPanel generalPanel = new ServiceElementGeneralPanel();
	private HvGridValuesPanel attributeValuesPanel = new HvGridValuesPanel();
	private HvTableViewPanel mechanismViewPanel;
	private HvTableViewPanel capabilityViewPanel;
	
	public ServiceElementDetailsTabPanel() {
		super(2.0, Unit.EM);
		setAnimationDuration(500);
	    getElement().getStyle().setMargin(1.0, Unit.PX);
	    
	    // General Tab
	    add(generalPanel, "General");
	    
	    // Attribute Tab
	    add(attributeValuesPanel, "Attributes");
	    
	    // Mechanisms Tab
	    mechanismViewPanel = new HvTableViewPanel(null, headers);
	    add(mechanismViewPanel, "Mechanisms");
	    
	    // Capabilities Tab
		capabilityViewPanel = new HvTableViewPanel(null, headers);
	    add(capabilityViewPanel, "Capabilites");
	}

	public void update(ServiceElement se) {
		generalPanel.update(se);
		attributeValuesPanel.update(se);
		mechanismViewPanel.showIdList(se.getCapabilites());
		capabilityViewPanel.showIdList(se.getCapabilites());
	}
	
	public void update(ServiceElementType serviceElementType) {
		generalPanel.update(serviceElementType);
	}
}
