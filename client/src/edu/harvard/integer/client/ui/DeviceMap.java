package edu.harvard.integer.client.ui;

import com.emitrom.lienzo.client.core.shape.Picture;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvMapIconWidget;
import edu.harvard.integer.client.widget.WidgetLayer;
import edu.harvard.integer.common.topology.ServiceElement;

public class DeviceMap extends WidgetLayer {
	public static final int START_DRAW_X = 30;
	public static final int START_DRAW_Y = 30;
	public static final int ICON_WIDTH = 60;
	public static final int ICON_HEIGHT = 60;
	Picture picture = new Picture(Resources.IMAGES.pcom(), ICON_WIDTH, ICON_HEIGHT, true, null);
	
	public DeviceMap(int width, int height) {
		super(width, height);
		init();
	}

	private void init() {	
	}

	public void update(ServiceElement[] result) {
		
		int col = 0;
		int row = 0;
		for (ServiceElement device : result) {
			int x = col * ICON_WIDTH*2 + START_DRAW_X;
        	int y = row * ICON_HEIGHT*2 + START_DRAW_Y;
        	
        	HvMapIconWidget icon = new HvMapIconWidget(picture, device.getName());
        	icon.draw(x, y);
        	
        	if (col < 5)
        		col++;
        	else {
        		col = 0;
        		row++;
        	}
		}
	}

}
