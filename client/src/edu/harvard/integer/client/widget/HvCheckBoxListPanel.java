package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;

public class HvCheckBoxListPanel extends Grid {
	
	private List<CheckBox> checkboxes = new ArrayList<CheckBox>();
	private int rowTotal;
	private int columnTotal;
	private int curRow = -1;
	private int curColumn = -1;
	
	public HvCheckBoxListPanel(int row, int col) {
		super(row, col);
		rowTotal = row;
		columnTotal = col;
	}
	
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
