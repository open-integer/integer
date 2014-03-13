package edu.harvard.integer.client.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class HvListBoxPanel.
 */
public class HvListBoxPanel extends ListBox {

	/** The this panel. */
	private VerticalPanel thisPanel = new VerticalPanel();
	
	/** The button panel. */
	private FlowPanel buttonPanel = new FlowPanel();
	
	/** The add button. */
	private HvLittelSquareButton addButton = new HvLittelSquareButton("+");
	
	/** The delete button. */
	private HvLittelSquareButton deleteButton = new HvLittelSquareButton("-");
	
	/** The up button. */
	private HvLittelSquareButton upButton = new HvLittelSquareButton("^");
	
	/** The down button. */
	private HvLittelSquareButton downButton = new HvLittelSquareButton("v");
	
	/**
	 * Instantiates a new hv list box panel.
	 */
	public HvListBoxPanel() {
		super();
		init();
	}
	
	/**
	 * Instantiates a new hv list box panel.
	 *
	 * @param isMultipleSelect the is multiple select
	 */
	public HvListBoxPanel(boolean isMultipleSelect) {
		super(isMultipleSelect);
		init();
	}
	
	/**
	 * Instantiates a new hv list box panel.
	 *
	 * @param element the element
	 */
	public HvListBoxPanel(Element element) {
		super(element);
		init();
	}
	
	/**
	 * Inits the.
	 */
	private void init() {
		upButton.setVisible(false);
		downButton.setVisible(false);
		
		buttonPanel.setStyleName("littleButtonPanel");
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		//buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			
		thisPanel.setWidth("100%");
		thisPanel.setSpacing(0);
		thisPanel.setStyleName("flexListBoxPanel");
		thisPanel.add(this);
		thisPanel.add(buttonPanel);
	}
	
	/**
	 * Gets the visual panel.
	 *
	 * @return the visual panel
	 */
	public Widget getVisualPanel() {
		return thisPanel;
	}

	/**
	 * Sets the up down button visible.
	 *
	 * @param visible the new up down button visible
	 */
	public void setUpDownButtonVisible(boolean visible) {
		upButton.setVisible(visible);
		downButton.setVisible(visible);
	}
	
}