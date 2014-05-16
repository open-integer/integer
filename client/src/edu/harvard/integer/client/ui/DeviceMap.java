package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Picture;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvMapIconWidget;
import edu.harvard.integer.common.topology.ServiceElement;

public class DeviceMap extends Layer {
	public static final int OFFSET_X = 30;
	public static final int OFFSET_Y = 30;
	
	private int icon_row_total;
	private int icon_col_total;
	private int icon_width;
	private int icon_height;
	
	private ServiceElement selectedElement;
	private long selectedTimestamp;
	
	public DeviceMap() {
		//super(width, height);
		//demo();
	}

	public void demo(int total) {
		ServiceElement[] serviceElements = new ServiceElement[total];
		for (int i = 0; i < total; i++) {
			serviceElements[i] = new ServiceElement();
			serviceElements[i].setName("cisco."+i);
		}
		update(serviceElements);
	}

	public ServiceElement getSelectedElement() {
		return selectedElement;
	}
	
	public long getSelectedTimestamp() {
		return selectedTimestamp;
	}

	private void init_layout(int total) {
		icon_row_total = (int) Math.ceil(Math.sqrt(total/2));
		icon_col_total = 2 * icon_row_total;
		icon_width = SystemSplitViewPanel.CONTENT_WIDTH / (2 * icon_col_total);
		icon_height = icon_width;
	}

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
        	HvMapIconWidget icon = new HvMapIconWidget(picture, device, mouseClickHandler);
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
