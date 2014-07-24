package edu.harvard.integer.client.ui;

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

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.utils.HvLink;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvMapIconPopup;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.InterNetworkLink;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;

/**
 * The Class NetworkMap represents a map object of Integer.
 * This is a subclass class extended from IntegerMap.
 * It is able to display any number of service element by calculating the individual widget size.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class NetworkMap extends IntegerMap {
	
	/**
	 * Update network information including network lkist and link list.
	 *
	 * @param networkkInfo the networkk info
	 */
	public void updateNetworkInformation(NetworkInformation networkkInfo) {
		layout_type = ELLIPSE_LAYOUT;
		
		int netSize = networkkInfo.getNetworks().length;
		int linkSize = networkkInfo.getLinks().length;
		String text = "showing network: " + netSize + " subnets, " + linkSize + " links";
		MainClient.statusPanel.updateStatus(text);
		entityMap.clear();
		removeAll();
		updateNetworks(networkkInfo.getNetworks());
		drawLinks(networkkInfo.getLinks());
	}

	/**
	 * Update method will refresh the panel with the given list of ServiceElement objects.
	 *
	 * @param result the result
	 */
	private void updateNetworks(Network[] result) {
		int n = result.length;
		init_layout(n);
		
		int i = 0;
		ImageResource image = Resources.IMAGES.network();
		double angle = 0;
		double increment = DOUBLE_PI / n;
		
		for (final Network network : result) {
			Point point = calculatePoint(n, i++, angle);
			entityMap.put(network.getID(), point);
			
        	Picture picture = new Picture(image, icon_width, icon_height, true, null);
        	NodeMouseClickHandler mouseClickHandler = new NodeMouseClickHandler() {

        		@Override
        		public void onNodeMouseClick(NodeMouseClickEvent event) {
        			selectedEntity = network;
        			selectedTimestamp = System.currentTimeMillis();
        		} 		
        	};
        	ServiceElementWidget icon = new ServiceElementWidget(picture, network, mouseClickHandler);
        	icon.draw((int)point.getX(), (int)point.getY());
        	
        	add(icon);
        	iconMap.put(network.getID(), icon);
        	
        	angle += increment;
		}
	}
	
	/**
	 * Draw links.
	 *
	 * @param links the links
	 */
	private void drawLinks(InterNetworkLink[] links) {
		for (final InterNetworkLink link : links) {
			ID id1 = link.getSourceNetworkId();
			ID id2 = link.getDestinationNetworkId();
			
			if (id1.equals(id2))
				continue;
			
			Point p1 = entityMap.get(id1);
			Point p2 = entityMap.get(id2);
			
			if (p1 == null || p2 == null)
				continue;
			
			// draw line between p0 and p
			Line line = drawLink(link, p1, p2);
			
			ServiceElementWidget icon1 = iconMap.get(id1);
			ServiceElementWidget icon2 = iconMap.get(id2);
			
			// save line (source: p1, destination: p2) to icon1
			HvLink link1 = new HvLink(line, p1, p2, icon1, icon2);
            icon1.addLink(link1);
            
            // save line (source: p2, destination: p1) to icon2
            HvLink link2 = new HvLink(line, p2, p1, icon2, icon1);
            icon2.addLink(link2);
		}
	}
	
	/**
	 * Draw link.
	 *
	 * @param link the link
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	private Line drawLink(final InterNetworkLink link, Point p1, Point p2) {
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
				int x = SystemSplitViewPanel.WESTPANEL_WIDTH + event.getX();
				int y = 200 + event.getY();

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
        return line;
	}

}
