package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class HuitFlexTable extends FlexTable {
	private static final int HeaderRowIndex = 0;
	
	private String[] headers;
	private ScrollPanel scrollPanel;

	public HuitFlexTable(String[] headers) {
		setWidth("100%");
		setCellSpacing(0);
		addStyleName("flexTable");
		
		this.headers = headers;
		addHeader();
		
		scrollPanel = new ScrollPanel(this);
		scrollPanel.setSize("1000px", "500px");
	}

	public Widget getVisualPanel() {
		return scrollPanel;
	}

	public void applyDataRowStyles() {
		HTMLTable.RowFormatter rf = getRowFormatter();

		for (int row = 1; row < getRowCount(); ++row) {
			if ((row % 2) != 0) {
				rf.addStyleName(row, "FlexTable-OddRow");
			} else {
				rf.addStyleName(row, "FlexTable-EvenRow");
			}
		}
	}
	
	public void addHeader() {
		insertRow(0);
		getRowFormatter().addStyleName(HeaderRowIndex, "FlexTable-Header");

		for (String columnHeader : headers)
			addColumn(columnHeader);

		getRowFormatter().addStyleName(0, "flexTableHeader");
	}
	
	public void clean() {
		removeAllRows();
		addHeader();
	}
	
	public void addColumn(Object columnHeading) {
	    Widget widget = createCellWidget(columnHeading);
	    int cell = getCellCount(HeaderRowIndex);
	    setWidget(HeaderRowIndex, cell, widget);
	}
	
	private Widget createCellWidget(Object cellObject) {
	    Widget widget = null;

	    if (cellObject instanceof Widget)
	      widget = (Widget) cellObject;
	    else
	      widget = new Label(cellObject.toString());

	    return widget;
	}
	
	int rowIndex = 1;

	public void addRow(Object[] cellObjects) {

		for (int cell = 0; cell < cellObjects.length; cell++) {
			Widget widget = createCellWidget(cellObjects[cell]);
			setWidget(rowIndex, cell, widget);
		}
		rowIndex++;
	}
}
