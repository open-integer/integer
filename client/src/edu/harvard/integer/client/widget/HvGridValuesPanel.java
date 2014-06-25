package edu.harvard.integer.client.widget;

import java.util.List;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class HvGridValuesPanel represents a panel showing the list of attribute value
 * This is a subclass class extended from com.google.gwt.user.client.ui.FormPanel.
 * It includes 
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvGridValuesPanel extends FormPanel{
	
	/** The grid. */
	private Grid grid = new Grid(0, 2);
	
	/**
	 * Creates a new HvGridValuesPanel instance.
	 */
	public HvGridValuesPanel() {
		ScrollPanel scrollPanel = new ScrollPanel(grid);
		scrollPanel.setHeight("100%");
		setWidget(scrollPanel);
	}
	
	/**
	 * Updates attribute value with given service element
	 *
	 * @param se the se
	 */
	public void update(ServiceElement se) {
		List<ManagementObjectValue> values = se.getAttributeValues();
		int rows = values.size();
		grid.resizeRows(rows);
		
		for (int i = 0; i < rows; i++) {
			ManagementObjectValue value = values.get(i);
			String name = value.getManagementObject().getName();
			HTML label = new HTML(name);
			label.addStyleName("gwt-label");
			
			HTML html = new HTML();
			grid.setWidget(i, 0, label);
			grid.setWidget(i, 1, html);
			grid.getCellFormatter().setWidth(i, 0, "200px");
			
			Object v = value.getValue();
			if (v instanceof String)
				html.setHTML((String)v);
			else if (v instanceof Integer)
				html.setHTML(""+v);
		}
	}	

}
