package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.client.utils.HvLink;
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
	
	/** The Constant HALF_PI. */
	public static final double HALF_PI = Math.PI / 2;
	
	/** The Constant ONE_AND_HALF_PI. */
	public static final double ONE_AND_HALF_PI = Math.PI + HALF_PI;
	
	/** The Constant DOUBLE_PI. */
	public static final double DOUBLE_PI = Math.PI + Math.PI;
	
	/** The Constant MAP_WIDTH. */
	public static final int MAP_WIDTH = SystemSplitViewPanel.CONTENT_WIDTH;
	
	/** The Constant MAP_HEIGHT. */
	public static final int MAP_HEIGHT = SystemSplitViewPanel.CONTENT_HEIGHT -100;
	
	/** The Constant LAYOUT_CENTER. */
	public static final int LAYOUT_CENTER = 1;
	
	/** The Constant LAYOUT_LEFT. */
	public static final int LAYOUT_LEFT = 2;
	
	/** The Constant LAYOUT_RIGHT. */
	public static final int LAYOUT_RIGHT = 3;
	
	/** The Constant OFFSET_X. */
	public static final int OFFSET_X = 30;
	
	/** The Constant OFFSET_Y. */
	public static final int OFFSET_Y = 30;
	
	/** The Constant GRID_LAYOUT. */
	public static final int GRID_LAYOUT = 1;
	
	/** The Constant CIRCULAR_LAYOUT. */
	public static final int CIRCULAR_LAYOUT = 2;
	
	/** The Constant ELLIPSE_LAYOUT. */
	public static final int ELLIPSE_LAYOUT = 3;
	
	/** The layout_type. */
	protected int layout_type = CIRCULAR_LAYOUT;
	
	/** The layout_position. */
	protected int layout_position = LAYOUT_CENTER;
	
	/** The icon_row_total. */
	private int icon_row_total;
	
	/** The icon_col_total. */
	protected int icon_col_total;
	
	/** The icon_width. */
	protected int icon_width = MAP_WIDTH / 5;
	
	/** The icon_height. */
	protected int icon_height;
	
	/** The line_width. */
	protected int line_width = 3;
	
	/** The circle radius of Circular Layout **/
	protected double radius;
	
	/** The semi length of x-axis **/
	protected double a;
	
	/** The semi length of y-axis **/
	protected double b;
	
	/** The square of semi length of x-axis **/
	protected double aa;
	
	/** The product of semi length of x-axis and semi length of y-axis **/
	protected double ab;
	
	/** The square of semi length of y-axis **/
	protected double bb;
	
	/** The x-axis value of original point **/
	protected double original_x;
	
	/** The y-axis value of original point **/
	protected double original_y;
	
	/** The selected element. */
	protected BaseEntity selectedEntity;
	
	/** The selected timestamp. */
	protected long selectedTimestamp;
	
	/** The entity map. */
	protected Map<ID, Point> entityMap = new HashMap<ID, Point>();
	
	/** The service element icon map. */
	protected Map<ID, ServiceElementWidget> iconMap = new HashMap<ID, ServiceElementWidget>();
	
	/** The list of Link in map **/
	protected List<HvLink> linkList = new ArrayList<HvLink>();
	
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
		int layout_width = getLayoutWidth();
		int layout_height = getLayoutHeight();
		
		int sqrt_total = (int) (2 * Math.sqrt(total)) + 2;
		int dw = layout_width / sqrt_total;
		int dh = layout_height / sqrt_total;
		icon_width = dh < dw ? dh : dw;
		icon_height = icon_width;
		
		if (layout_type == ELLIPSE_LAYOUT ||
			layout_type == CIRCULAR_LAYOUT) {
			double width = layout_width - icon_width * 2 - OFFSET_X;
			double height = layout_height - icon_height * 2 - OFFSET_Y;
	        
			radius = 0.45 * (height < width ? height : width);
			
			original_x = width / 2;
			original_y = height / 2;
			
			if (layout_position == LAYOUT_RIGHT)
				original_x += width;
			
			a = (MAP_WIDTH - icon_width * 2) * 0.45 - OFFSET_X;
			b = (MAP_HEIGHT - icon_height * 2) / 3 - OFFSET_Y;
			aa = a * a;
			ab = a * b;
			bb = b * b;
		}
		else {
			icon_row_total = (int) Math.ceil(Math.sqrt(total/2));
			icon_col_total = 2 * icon_row_total;
		}
		
		
		if (total > 50)
			line_width = 1;
		else if (total > 25)
			line_width = 2;
	}
	
	/**
	 * Gets the layout width.
	 * It returns MAP_WIDTH 	if layout_position is CENTER
	 * 			  MAP_WIDTH/2 	if layout_position is LEFT or RIGHT
	 *
	 * @return the layout width
	 */
	private int getLayoutWidth() {
		int layout_width = MAP_WIDTH;
		if (layout_position == LAYOUT_LEFT ||
			layout_position == LAYOUT_RIGHT	) {
			layout_width = MAP_WIDTH / 2;
		}
		return layout_width;
	}
	
	/**
	 * Gets the layout height.
	 *
	 * @return the layout height
	 */
	private int getLayoutHeight() {
		// always MAP_HEIGHT for now
		return MAP_HEIGHT;
	}
	
	/**
	 * Calculate point at up-left corner of the icon
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
	public Point calculatePoint(int total, int i, double angle) {
		if (layout_type == CIRCULAR_LAYOUT) 
			return calculateCircularLayoutPoint(angle);
		else if (layout_type == ELLIPSE_LAYOUT)
			return calculateEllipseLayoutPoint(angle);
		
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
	 * It supports 3 layout positions: CENTER, LEFT and RIGHT.
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
	public Point calculateCircularLayoutPoint(double angle) {		
    	double x = Math.cos(angle) * radius + original_x;
    	double y = Math.sin(angle) * radius + original_y;
    	
    	return new Point(x, y);
	}
	
	public Point calculateEllipseLayoutPoint(double angle) {
		double tan = Math.tan(angle);
		double tantan = tan * tan;
		double sqrt = Math.sqrt(bb + aa * tantan);
		
    	double x0 = ab / sqrt;
    	double y0 = x0 * tan;
    	
    	if (angle > HALF_PI && angle < ONE_AND_HALF_PI) {
    		x0 = -x0;
    		y0 = -y0;
    	}
    	double x = x0 + a + OFFSET_X;
    	double y = y0 + b + OFFSET_Y;
    	
    	return new Point(x, y);
	}
	
	public Point getCenterPoint() {
		double x = a + OFFSET_X;
    	double y = b + OFFSET_Y;
    	
    	return new Point(x, y);
	}

}
