package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class ServiceElementMap represents a map object of Integer.
 * This is a subclass class extended from com.emitrom.lienzo.client.core.shape.Layer.
 * It is able to display any number of service element by calculating the individual widget size.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ServiceElementMap extends Layer {
	
	/** The Constant OFFSET_X. */
	public static final int OFFSET_X = 30;
	
	/** The Constant OFFSET_Y. */
	public static final int OFFSET_Y = 30;
	
	/** The Constant GRID_LAYOUT. */
	public static final int GRID_LAYOUT = 1;
	
	/** The Constant CIRCULAR_LAYOUT. */
	public static final int CIRCULAR_LAYOUT = 2;
	
	/** The layout_type. */
	private int layout_type = CIRCULAR_LAYOUT;
	
	/** The icon_row_total. */
	private int icon_row_total;
	
	/** The icon_col_total. */
	private int icon_col_total;
	
	/** The icon_width. */
	private int icon_width = SystemSplitViewPanel.CONTENT_WIDTH / 5;
	
	/** The icon_height. */
	private int icon_height;
	
	/** The selected element. */
	private ServiceElement selectedElement;
	
	/** The selected timestamp. */
	private long selectedTimestamp;
	
	/**
	 * Gets the selected element.
	 *
	 * @return the selected element
	 */
	public ServiceElement getSelectedElement() {
		return selectedElement;
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
	 * Instantiates a new service element map.
	 */
	public ServiceElementMap() {
		init(18);
	}
	
	/**
	 * Inits the.
	 *
	 * @param count the count
	 */
	private void init(int count) {
		ServiceElement[] serviceElements = new ServiceElement[count];
		for (int i = 0; i < count; i++) {
			serviceElements[i] = new ServiceElement();
			serviceElements[i].setName("cisco."+i);
		}
		update(serviceElements);
	}

	/**
	 * init_layout method calculates the icon size to be displayed based on the given number of items to be displayed.
	 *
	 * @param total the total
	 */
	private void init_layout(int total) {
		icon_row_total = (int) Math.ceil(Math.sqrt(total/2));
		icon_col_total = 2 * icon_row_total;
		
		if (icon_col_total != 0)
			icon_width = SystemSplitViewPanel.CONTENT_WIDTH / (2 * icon_col_total);
		
		if (layout_type == CIRCULAR_LAYOUT) {
			icon_width = icon_width / 2;
		}
		icon_height = icon_width;
	}

	/**
	 * Update method will refresh the panel with the given list of ServiceElement objects.
	 *
	 * @param result the result
	 */
	public void update(ServiceElement[] result) {
		removeAll();
		init_layout(result.length);
		
		int i = 0;
		for (final ServiceElement device : result) {
			Point point = calculatePoint(result.length, i++);
        	
        	Picture picture = new Picture(Resources.IMAGES.pcom(), icon_width, icon_height, true, null);
        	NodeMouseClickHandler mouseClickHandler = new NodeMouseClickHandler() {

        		@Override
        		public void onNodeMouseClick(NodeMouseClickEvent event) {
        			selectedElement = device;
        			selectedTimestamp = System.currentTimeMillis();
        		} 		
        	};
        	ServiceElementWidget icon = new ServiceElementWidget(picture, device, mouseClickHandler);
        	icon.draw((int)point.getX(), (int)point.getY());
        	
        	add(icon);
		}
	}
	
	public Point calculatePoint(int total, int i) {
		if (layout_type == CIRCULAR_LAYOUT) 
			return calculateCircularLayoutPoint(total, i);
		
		return calculateLineLayoutPoint(total, i);
	}
	
	public Point calculateLineLayoutPoint(int total, int i) {
		int col = i % icon_col_total;
		int row = i / icon_col_total;
		int x = col * icon_width*2 + OFFSET_X;
    	int y = row * icon_height*2 + OFFSET_Y;
		
		return new Point(x, y);
	}
	
	public Point calculateCircularLayoutPoint(int total, int i) {
		double height = SystemSplitViewPanel.CONTENT_HEIGHT - icon_height * 2;
        double width = SystemSplitViewPanel.CONTENT_WIDTH - icon_width * 2;
        double radius = 0.45 * (height < width ? height : width);
		double angle = (2 * Math.PI * i++) / total;
    	double x = Math.cos(angle) * radius + width / 2;
    	double y = Math.sin(angle) * radius + height / 2 - OFFSET_Y;
    	
    	return new Point(x, y);
	}

}
