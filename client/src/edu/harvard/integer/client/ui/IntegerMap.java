package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * The Class IntegerMap represents a map object of Integer.
 * This is a subclass class extended from com.emitrom.lienzo.client.core.shape.Layer.
 * It is able to display any number of service element by calculating the individual widget size.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class IntegerMap extends Layer {
	
	public static final int MAP_WIDTH = SystemSplitViewPanel.CONTENT_WIDTH;
	public static final int MAP_HEIGHT = SystemSplitViewPanel.CONTENT_HEIGHT;
	
	/** The Constant OFFSET_X. */
	public static final int OFFSET_X = 30;
	
	/** The Constant OFFSET_Y. */
	public static final int OFFSET_Y = 55;
	
	/** The Constant GRID_LAYOUT. */
	public static final int GRID_LAYOUT = 1;
	
	/** The Constant CIRCULAR_LAYOUT. */
	public static final int CIRCULAR_LAYOUT = 2;
	
	/** The layout_type. */
	protected int layout_type = CIRCULAR_LAYOUT;
	
	/** The icon_row_total. */
	private int icon_row_total;
	
	/** The icon_col_total. */
	protected int icon_col_total;
	
	/** The icon_width. */
	protected int icon_width = MAP_WIDTH / 5;
	
	protected int line_width = 3;
	
	/** The icon_height. */
	protected int icon_height;
	
	/** The selected element. */
	BaseEntity selectedEntity;
	
	/** The selected timestamp. */
	long selectedTimestamp;
	
	/** The entity map. */
	protected Map<ID, Point> entityMap = new HashMap<ID, Point>();
	
	protected List<Point> pointList = new ArrayList<Point>();
	
	/**
	 * Gets the selected element.
	 *
	 * @return the selected element
	 */
	public BaseEntity getSelectedEntity() {
		return selectedEntity;
	}
	
	/**
	 * Gets the selected timestamp.
	 *
	 * @return the selected timestamp
	 */
	public long getSelectedTimestamp() {
		return selectedTimestamp;
	}
	
	/**
	 * Gets the layout_type.
	 *
	 * @return the layout_type
	 */
	public int getLayout_type() {
		return layout_type;
	}

	/**
	 * Sets the layout_type.
	 *
	 * @param layout_type the new layout_type
	 */
	public void setLayout_type(int layout_type) {
		this.layout_type = layout_type;
	}

	/**
	 * init_layout method calculates the icon size to be displayed based on the given number of items to be displayed.
	 *
	 * @param total the total
	 */
	protected void init_layout(int total) {
		icon_row_total = (int) Math.ceil(Math.sqrt(total/2));
		icon_col_total = 2 * icon_row_total;
		
		if (icon_col_total != 0)
			icon_width = MAP_WIDTH / icon_col_total;
		
		if (layout_type == CIRCULAR_LAYOUT) {
			icon_width = icon_width / 2;
		}
		icon_height = icon_width;
		
		if (total > 50)
			line_width = 1;
		else if (total > 25)
			line_width = 2;
	}
	
	/**
	 * Calculate point.
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
	public Point calculatePoint(int total, int i) {
		if (layout_type == CIRCULAR_LAYOUT) 
			return calculateCircularLayoutPoint(total, i);
		
		return calculateLineLayoutPoint(total, i);
	}
	
	/**
	 * Calculate line layout point.
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
	public Point calculateLineLayoutPoint(int total, int i) {
		int col = i % icon_col_total;
		int row = i / icon_col_total;
		int x = col * icon_width*2 + OFFSET_X;
    	int y = row * icon_height*2 + OFFSET_Y;
		
		return new Point(x, y);
	}
	
	/**
	 * Calculate circular layout point.
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
	public Point calculateCircularLayoutPoint(int total, int i) {
		double height = MAP_HEIGHT - icon_height * 2 - OFFSET_Y;
        double width = MAP_WIDTH - icon_width * 2;
        double radius = 0.45 * (height < width ? height : width);
		double angle = (2 * Math.PI * i++) / total;
    	double x = Math.cos(angle) * radius + width / 2;
    	double y = Math.sin(angle) * radius + height / 2;
    	
    	return new Point(x, y);
	}

}
