package edu.harvard.integer.client.ui;


import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvMapIconPopup;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class ServiceElementMap represents a map object of Integer.
 * This is a subclass class extended from IntegerMap.
 * It is able to display any number of service element by calculating the individual widget size.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class SubnetMap extends IntegerMap {

	public void updateNetwork(Network network) {
		entityMap.clear();
		removeAll();
		
		if (network.getLowerNetworks() == null || network.getLowerNetworks().isEmpty())
			updateServiceElements(network.getServiceElements(), LAYOUT_CENTER);
		else {
			updateServiceElements(network.getServiceElements(), LAYOUT_LEFT);
			updateLowerNetworks(network.getLowerNetworks(), LAYOUT_RIGHT);
		}
		
		drawLinks(network.getInterDeviceLinks());
	}

	/**
	 * Update method will refresh the panel with the given list of ServiceElement objects.
	 *
	 * @param list the result
	 */
	public void updateServiceElements(List<ServiceElement> list, int layout_position) {
		this.layout_position = layout_position;
		init_layout(list.size());
		
		int i = 0;
		ImageResource image = Resources.IMAGES.graySwitch();
		
		for (final ServiceElement entity : list) {
			Point point = calculatePoint(list.size(), i++);
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
	
	private void updateLowerNetworks(List<Network> list, int layout_position) {
		this.layout_position = layout_position;
		init_layout(list.size());
		
		int i = 0;
		ImageResource image = Resources.IMAGES.network();
		
		for (final Network entity : list) {
			Point point = calculatePoint(list.size(), i++);
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
	
	/**
	 * Draw links.
	 *
	 * @param list the links
	 */
	private void drawLinks(List<InterDeviceLink> list) {
		for (final InterDeviceLink link : list) {
			ID id1 = link.getSourceServiceElementId();
			ID id2 = link.getDestinationServiceElementId();
			Point p1 = entityMap.get(id1);
			Point p2 = entityMap.get(id2);
			
			if (p1 == null || p2 == null)
				continue;
			
			// draw line between p1 and p2
			drawLink(link, p1, p2);
		}
	}
	
	/**
	 * Draw link.
	 *
	 * @param link the link
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	private void drawLink(final InterDeviceLink link, Point p1, Point p2) {
		final HvMapIconPopup tooltip = new HvMapIconPopup();
		
		double x1 = p1.getX() + icon_width/2;
		double y1 = p1.getY() + icon_height/2;
		double x2 = p2.getX() + icon_width/2;
		double y2 = p2.getY() + icon_height/2;
		
		Line line = new Line(x1, y1, x2, y2);
		ColorName colorName = ColorName.BLUE;
//		if (linkStatus == null)
//			colorName = ColorName.GREY;
//		else if (linkStatus.equalsIgnoreCase("up"))
//			colorName = ColorName.GREEN;
//		else if (linkStatus.equalsIgnoreCase("down"));
//			colorName = ColorName.RED;
			
        line.setStrokeColor(colorName).setStrokeWidth(line_width).setFillColor(colorName);
        
        line.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {  
            
			@Override
			public void onNodeMouseEnter(NodeMouseEnterEvent event) {
				int x = SystemSplitViewPanel.WESTPANEL_WIDTH + event.getX() + 15;
				int y = 100 + event.getY() - 15;

				tooltip.setPopupPosition(x, y);
				tooltip.update(link.getName(), "Not availabel");
				tooltip.show();
			}  
        });  
  
		line.addNodeMouseExitHandler(new NodeMouseExitHandler() {

			@Override
			public void onNodeMouseExit(NodeMouseExitEvent event) {
				tooltip.hide();
			}  
            
        });
		
		line.addNodeMouseClickHandler(new NodeMouseClickHandler() {

			@Override
			public void onNodeMouseClick(NodeMouseClickEvent event) {
				LinkDetailsPanel detailsPanel = new LinkDetailsPanel();
				detailsPanel.update(link);
				HvDialogBox detailsDialog = new HvDialogBox("Link Details", detailsPanel);
				detailsDialog.enableOkButton(false);
				detailsDialog.setSize("400px", "150px");
				detailsDialog.center();
				detailsDialog.show();
			}
			
		});
        
        add(line);
	}

}
