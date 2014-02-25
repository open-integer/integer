package edu.harvard.integer.client.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HvListBoxPanel extends ListBox {

	private VerticalPanel thisPanel = new VerticalPanel();
	private FlowPanel buttonPanel = new FlowPanel();
	private HvLittelSquareButton addButton = new HvLittelSquareButton("+");
	private HvLittelSquareButton deleteButton = new HvLittelSquareButton("-");
	private HvLittelSquareButton upButton = new HvLittelSquareButton("^");
	private HvLittelSquareButton downButton = new HvLittelSquareButton("v");
	
	public HvListBoxPanel() {
		super();
		init();
	}
	
	public HvListBoxPanel(boolean isMultipleSelect) {
		super(isMultipleSelect);
		init();
	}
	
	public HvListBoxPanel(Element element) {
		super(element);
		init();
	}
	
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
	
	public Widget getVisualPanel() {
		return thisPanel;
	}

	public void setUpDownButtonVisible(boolean visible) {
		upButton.setVisible(visible);
		downButton.setVisible(visible);
	}
	
}
