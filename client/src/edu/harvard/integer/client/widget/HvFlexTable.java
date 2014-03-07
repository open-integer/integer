package edu.harvard.integer.client.widget;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class HvFlexTable.
 */
public class HvFlexTable extends FlexTable {
	
	/** The Constant HeaderRowIndex. */
	private static final int HeaderRowIndex = 0;
	
	/** The headers. */
	private String[] headers;
	
	/** The scroll panel. */
	private ScrollPanel scrollPanel;

	/**
	 * Instantiates a new hv flex table.
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

	/**
	 * Apply data row styles.
	 */
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
	 * Clean.
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
	    else
	      widget = new Label(cellObject.toString());

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
}
