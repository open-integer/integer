package edu.harvard.integer.client.ui;


import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class ServiceElementMap represents a map object of Integer.
 * This is a subclass class extended from IntegerMap.
 * It is able to display any number of service element by calculating the individual widget size.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ServiceElementMap extends IntegerMap {

	/**
	 * Update method will refresh the panel with the given list of ServiceElement objects.
	 *
	 * @param list the result
	 */
	public void update(List<ServiceElement> list) {
		entityMap.clear();
		removeAll();
		init_layout(list.size());
		
		// === testing only first 4 points ==
		int N = 10;
		init_layout(N);
		// ==================================
		
		int i = 0;
		ImageResource image = Resources.IMAGES.graySwitch();
		
		for (final ServiceElement entity : list) {
			
			// === test only first 4 points ====
			if (i >= N)
				break;
			
			Point point = calculatePoint(N, i++);
			// Point point = calculatePoint(result.length, i++);
			entityMap.put(entity.getID(), point);
			pointList.add(point);
			
			image = Resources.IMAGES.pcom();
			
        	Picture picture = new Picture(image, icon_width, icon_height, true, null);
        	NodeMouseClickHandler mouseClickHandler = new NodeMouseClickHandler() {

        		@Override
        		public void onNodeMouseClick(NodeMouseClickEvent event) {
        			selectedEntity = entity;
        			selectedTimestamp = System.currentTimeMillis();
        		} 		
        	};
        	ServiceElementWidget icon = new ServiceElementWidget(picture, entity, mouseClickHandler);
        	icon.draw((int)point.getX(), (int)point.getY());
        	
        	add(icon);
		}
	}

}
