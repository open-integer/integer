package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;

/**
 * The Class HvCheckBoxListPanel.
 */
public class HvCheckBoxListPanel extends Grid {
	
	/** The checkboxes. */
	private List<CheckBox> checkboxes = new ArrayList<CheckBox>();
	
	/** The row total. */
	private int rowTotal;
	
	/** The column total. */
	private int columnTotal;
	
	/** The cur row. */
	private int curRow = -1;
	
	/** The cur column. */
	private int curColumn = -1;
	
	/**
	 * Instantiates a new hv check box list panel.
	 *
	 * @param row the row
	 * @param col the col
	 */
	public HvCheckBoxListPanel(int row, int col) {
		super(row, col);
		rowTotal = row;
		columnTotal = col;
	}
	
	/**
	 * Adds the item.
	 *
	 * @param title the title
	 */
	public void addItem(String title) {
		curColumn++;
		
		if (checkboxes.size()%columnTotal == 0) {
			curRow++;
			curColumn = 0;
		}
		
		CheckBox checkbox = new CheckBox(title);
		checkboxes.add(checkbox);
		setWidget(curRow, curColumn, checkbox);
			
	}
}
