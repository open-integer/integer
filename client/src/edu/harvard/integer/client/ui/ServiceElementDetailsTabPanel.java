package edu.harvard.integer.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import edu.harvard.integer.client.widget.HvGridValuesPanel;
import edu.harvard.integer.client.widget.HvTableViewPanel;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;

/**
 * The ServiceElementDetailsTabPanel class represents a panel to display all attributes of Service Element.
 * It includes General attribute tab, Value list tab, Capability tab and Mechanism tab
 * 
 * This is a subclass class extended from com.google.gwt.user.client.ui.TabLayoutPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ServiceElementDetailsTabPanel extends TabLayoutPanel {

	/** The Constant TABPANEL_WIDTH. */
	public static final int TABPANEL_WIDTH = 500;
	
	/** The Constant TABPANEL_HEIGHT. */
	public static final int TABPANEL_HEIGHT = 300;
	
	/** The headers. */
	final String[] headers = {"Name", "Description"};
	
	/** The general panel. */
	private ServiceElementGeneralPanel generalPanel = new ServiceElementGeneralPanel();
	
	/** The attribute values panel. */
	private HvGridValuesPanel attributeValuesPanel = new HvGridValuesPanel();
	
	/** The mechanism view panel. */
	private HvTableViewPanel mechanismViewPanel;
	
	/** The capability view panel. */
	private HvTableViewPanel capabilityViewPanel;
	
	/**
	 * Instantiates a new service element details tab panel.
	 */
	public ServiceElementDetailsTabPanel() {
		super(2.0, Unit.EM);
		setAnimationDuration(500);
	    //getElement().getStyle().setMargin(1.0, Unit.PX);
	    
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

	/**
	 * Update method will refresh the panel with the given ServiceElement object
	 *
	 * @param se the se
	 */
	public void update(ServiceElement se) {
		generalPanel.update(se);
		attributeValuesPanel.update(se);
		mechanismViewPanel.showIdList(se.getCapabilites());
		capabilityViewPanel.showIdList(se.getCapabilites());
	}
	
	/**
	 * Update method will refresh the category atttribute of GeneralPanel with the given ServiceElementType object.
	 *
	 * @param serviceElementType the service element type
	 */
	public void update(ServiceElementType serviceElementType) {
		generalPanel.update(serviceElementType);
	}
}
