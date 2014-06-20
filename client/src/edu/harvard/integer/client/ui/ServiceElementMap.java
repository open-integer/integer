package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Picture;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvServiceElementWidget;
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
	 * Init_layout.
	 *
	 * @param total the total
	 */
	private void init_layout(int total) {
		icon_row_total = (int) Math.ceil(Math.sqrt(total/2));
		icon_col_total = 2 * icon_row_total;
		
		if (icon_col_total != 0)
			icon_width = SystemSplitViewPanel.CONTENT_WIDTH / (2 * icon_col_total);
		
		icon_height = icon_width;
	}

	/**
	 * Update.
	 *
	 * @param result the result
	 */
	public void update(ServiceElement[] result) {
		removeAll();
		init_layout(result.length);
		
		int col = 0;
		int row = 0;
		for (final ServiceElement device : result) {
			int x = col * icon_width*2 + OFFSET_X;
        	int y = row * icon_height*2 + OFFSET_Y;
        	
        	Picture picture = new Picture(Resources.IMAGES.pcom(), icon_width, icon_height, true, null);
        	NodeMouseClickHandler mouseClickHandler = new NodeMouseClickHandler() {

        		@Override
        		public void onNodeMouseClick(NodeMouseClickEvent event) {
        			selectedElement = device;
        			selectedTimestamp = System.currentTimeMillis();
        		}
        		
        	};
        	HvServiceElementWidget icon = new HvServiceElementWidget(picture, device, mouseClickHandler);
        	icon.draw(x, y);
        	
        	if (col < icon_col_total)
        		col++;
        	else {
        		col = 0;
        		row++;
        	}
        	add(icon);
		}
	}

}
