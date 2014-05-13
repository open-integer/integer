package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Picture;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvMapIconWidget;
import edu.harvard.integer.client.widget.WidgetLayer;
import edu.harvard.integer.common.topology.ServiceElement;

public class DeviceMap extends WidgetLayer {
	//private static final int DEVICE_COUNT = 20;
	public static final int START_DRAW_X = 30;
	public static final int START_DRAW_Y = 30;
	public static final int ICON_WIDTH = 60;
	public static final int ICON_HEIGHT = 60;
	
	private ServiceElement selectedElement;
	
	public DeviceMap(int width, int height) {
		super(width, height);
		//init();
	}

	/*private void init() {
		ServiceElement[] serviceElements = new ServiceElement[DEVICE_COUNT];
		for (int i = 0; i < DEVICE_COUNT; i++) {
			serviceElements[i] = new ServiceElement();
			serviceElements[i].setName("cisco."+i);
		}
		update(serviceElements);
	}*/

	public ServiceElement getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(ServiceElement selectedElement) {
		this.selectedElement = selectedElement;
	}

	public void update(ServiceElement[] result) {
		removeAll();
		
		int col = 0;
		int row = 0;
		for (final ServiceElement device : result) {
			int x = col * ICON_WIDTH*2 + START_DRAW_X;
        	int y = row * ICON_HEIGHT*2 + START_DRAW_Y;
        	
        	Picture picture = new Picture(Resources.IMAGES.pcom(), ICON_WIDTH, ICON_HEIGHT, true, null);
        	NodeMouseClickHandler mouseClickHandler = new NodeMouseClickHandler() {

        		@Override
        		public void onNodeMouseClick(NodeMouseClickEvent event) {
        			selectedElement = device;
        		}
        		
        	};
        	HvMapIconWidget icon = new HvMapIconWidget(picture, device, mouseClickHandler);
        	icon.draw(x, y);
        	
        	if (col < 5)
        		col++;
        	else {
        		col = 0;
        		row++;
        	}
        	add(icon);
		}
	}
	
	

}
