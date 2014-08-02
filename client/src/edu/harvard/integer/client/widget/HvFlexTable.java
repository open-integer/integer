package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class HvFlexTable represents a table with headers and scroll bar.
 * This is a subclass class extended from com.google.gwt.user.client.ui.FlexTable.
 * It includes 
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvFlexTable extends FlexTable {
	
	/** The Constant HeaderRowIndex. */
	private static final int HeaderRowIndex = 0;
	
	private int highlightedRow = -1;
	
	/** The headers. */
	private String[] headers;
	
	/** The scroll panel. */
	private ScrollPanel scrollPanel;

	/**
	 * Create a new HvFlexTable instance.
	 *
	 * @param headers the headers
	 */
	public HvFlexTable(String[] headers) {
		setWidth("100%");
		setCellSpacing(0);
		addStyleName("flexTable");
		
		this.headers = headers;
		addHeader();
		
		scrollPanel = new ScrollPanel(this);
		scrollPanel.setSize("1000px", "500px");
	}

	/**
	 * Gets the visual panel.
	 *
	 * @return the visual panel
	 */
	public Widget getVisualPanel() {
		return scrollPanel;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setSize(java.lang.String, java.lang.String)
	 */
	public void setSize(String width, String height) {
		scrollPanel.setSize(width, height);
	}

	/**
	 * Apply data row styles.
	 */
	public void applyDataRowStyles() {
		for (int row = 1; row < getRowCount(); ++row)
			resetRowBackground(row);
	}
	
	/**
	 * Adds the header.
	 */
	public void addHeader() {
		insertRow(0);
		getRowFormatter().addStyleName(HeaderRowIndex, "FlexTable-Header");

		for (String columnHeader : headers)
			addColumn(columnHeader);

		getRowFormatter().addStyleName(0, "flexTableHeader");
	}
	
	/**
	 * Clean up
	 */
	public void clean() {
		removeAllRows();
		addHeader();
	}
	
	/**
	 * Adds the column.
	 *
	 * @param columnHeading the column heading
	 */
	public void addColumn(Object columnHeading) {
	    Widget widget = createCellWidget(columnHeading);
	    int cell = getCellCount(HeaderRowIndex);
	    setWidget(HeaderRowIndex, cell, widget);
	}
	
	/**
	 * Creates the cell widget.
	 *
	 * @param cellObject the cell object
	 * @return the widget
	 */
	private Widget createCellWidget(Object cellObject) {
	    Widget widget = null;

	    if (cellObject instanceof Widget)
	      widget = (Widget) cellObject;
	    else if (cellObject != null)
	      widget = new Label(cellObject.toString());
	    else
	    	widget = new Label("N/A");

	    return widget;
	}
	
	/** The row index. */
	int rowIndex = 1;

	/**
	 * Adds the row.
	 *
	 * @param cellObjects the cell objects
	 */
	public void addRow(Object[] cellObjects) {

		for (int cell = 0; cell < cellObjects.length; cell++) {
			Widget widget = createCellWidget(cellObjects[cell]);
			setWidget(rowIndex, cell, widget);
		}
		rowIndex++;
	}
	
	public void setHighlighted(int row) {
		if (highlightedRow > -1) {
			getRowFormatter().removeStyleName(row, "FlexTable-Highlighted");
			resetRowBackground(highlightedRow);
		}
		
		getRowFormatter().addStyleName(row, "FlexTable-Highlighted");
		
		highlightedRow = row;
	}
	
	private void resetRowBackground(int row) {
		HTMLTable.RowFormatter rf = getRowFormatter();
		
		if ((row % 2) != 0) {
			rf.setStyleName(row, "FlexTable-OddRow");
		} else {
			rf.setStyleName(row, "FlexTable-EvenRow");
		}
	}
}
