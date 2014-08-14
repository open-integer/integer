package edu.harvard.integer.client.ui;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.utils.HvLink;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvMapIconPopup;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.MapItemPosition;
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

	/** The subnet panel. */
	private SubnetPanel subnetPanel;
	
	/** The diff networks map. */
	protected Map<ID, Point> diffNetworksMap = new HashMap<ID, Point>();
	
	/**
	 * Instantiates a new subnet map.
	 *
	 * @param parentPanel the parent panel
	 */
	public SubnetMap(SubnetPanel parentPanel) {
		subnetPanel = parentPanel;
	}

	/**
	 * Update network.
	 *
	 * @param network the network
	 */
	public void updateNetwork(final Network network) {
		entityMap.clear();
		diffNetworksMap.clear();
		positionMap.clear();
		iconMap.clear();
		removeAll();
		
		MainClient.integerService.getPositionsByNetwork(network.getID(), new AsyncCallback<MapItemPosition[]>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(MapItemPosition[] positions) {
				if (positions != null && positions.length > 0)
					for (MapItemPosition position : positions)
						positionMap.put(position.getItemId(), position);
				
				if (network.getLowerNetworks() == null || network.getLowerNetworks().isEmpty())
					updateServiceElements(network.getID(), network.getServiceElements(), LAYOUT_CENTER);
				else {
					updateServiceElements(network.getID(), network.getServiceElements(), LAYOUT_LEFT);
					updateLowerNetworks(network.getID(), network.getLowerNetworks(), LAYOUT_RIGHT);
				}
				
				updateInterDeviceDiffNetworks(network.getID(), network.getInterDeviceLinks());
				
				drawLinks(network.getInterDeviceLinks());
				
				drawServiceElements(network.getServiceElements());
			}
			
		});
		
		
	}

	/**
	 * Update method will refresh the panel with the given list of ServiceElement objects.
	 * @param networkId 
	 *
	 * @param positions the positions
	 * @param layout_position the layout_position
	 */
	public void updateServiceElements(ID networkId, List<ServiceElement> list, int layout_position) {
		this.layout_position = layout_position;
		
		int total = list.size();
		init_layout(total);
		
		int i = 0;
		ImageResource image = Resources.IMAGES.defaultDevice();
		double angle = 0;
		double increment = DOUBLE_PI / total;
		Point point;
		
		for (final ServiceElement serviceElement : list) {
			MapItemPosition mapItemPosition = positionMap.get(serviceElement.getID());
			
			if (mapItemPosition != null)
				point = new Point(mapItemPosition.getXposition().doubleValue(), mapItemPosition.getYposition().doubleValue());
			else {
				point = calculatePoint(total, i, angle);
				mapItemPosition = new MapItemPosition();
				mapItemPosition.setMapId(networkId);
				mapItemPosition.setItemId(serviceElement.getID());
			}

			i++;
			
			entityMap.put(serviceElement.getID(), point);
			
			if (serviceElement.getIconName() == null || serviceElement.getIconName().equalsIgnoreCase("unknown"))
				image = Resources.IMAGES.unknown();
			else if (serviceElement.getIconName().equalsIgnoreCase("server"))
				image = Resources.IMAGES.server();
			else if (serviceElement.getIconName().equalsIgnoreCase("router"))
				image = Resources.IMAGES.router();
			
        	Picture picture = new Picture(image, icon_width, icon_height, true, null);

        	ServiceElementWidget icon = new ServiceElementWidget(picture, serviceElement, mapItemPosition, subnetPanel);
        	iconMap.put(serviceElement.getID(), icon);
        	
        	angle += increment;
		}
	}
	
	/**
	 * Draw service elements.
	 *
	 * @param serviceElements the service elements
	 */
	private void drawServiceElements(List<ServiceElement> serviceElements) {
		for (ServiceElement serviceElement : serviceElements) {
			ServiceElementWidget icon = iconMap.get(serviceElement.getID());
			Point point = entityMap.get(serviceElement.getID());
			icon.draw((int)point.getX(), (int)point.getY());
			add(icon);
		}
	}
	
	/**
	 * Update lower networks.
	 *
	 * @param list the list
	 * @param layout_position the layout_position
	 */
	private void updateLowerNetworks(ID parentNetworkId, List<Network> list, int layout_position) {
		this.layout_position = layout_position;
		init_layout(list.size());
		
		int i = 0;
		ImageResource image = Resources.IMAGES.network();
		double angle = 0;
		double increment = DOUBLE_PI / list.size();
		Point point = null;
		
		for (final Network network : list) {
			MapItemPosition mapItemPosition = positionMap.get(network.getID());
			
			if (mapItemPosition != null)
				point = calculatePoint(list.size(), i++, angle);
			else {
				mapItemPosition = new MapItemPosition();
				mapItemPosition.setMapId(parentNetworkId);
				mapItemPosition.setItemId(network.getID());
			}
			
			// Point point = calculatePoint(list.size(), i++, angle);
			entityMap.put(network.getID(), point);
			
        	Picture picture = new Picture(image, icon_width, icon_height, true, null);
        	NodeMouseClickHandler mouseClickHandler = new NodeMouseClickHandler() {

        		@Override
        		public void onNodeMouseClick(NodeMouseClickEvent event) {
        			selectedEntity = network;
        			selectedTimestamp = System.currentTimeMillis();
        		} 		
        	};
        	ServiceElementWidget icon = new ServiceElementWidget(picture, network, mapItemPosition, null);

        	add(icon);
        	iconMap.put(network.getID(), icon);
        	
        	angle += increment;
		}
	}
	
	/**
	 * Update inter device diff networks.
	 *
	 * @param list the list
	 */
	private void updateInterDeviceDiffNetworks(ID parentNetworkId, List<InterDeviceLink> list) {
		layout_type = ELLIPSE_LAYOUT;
		
		// find all the networks not in this subnet
		int counterDiffNetwork = 0;
		for (final InterDeviceLink link : list) {
			ID id1 = link.getSourceServiceElementId();
			ID id2 = link.getDestinationServiceElementId();
			Point p1 = entityMap.get(id1);
			Point p2 = entityMap.get(id2);
			
			if (p1 == null || p2 == null) 
				counterDiffNetwork++;
		}

		int i = 0;
		ImageResource image = Resources.IMAGES.network();
		double angle = 0;
		double increment = DOUBLE_PI / list.size();
			
		for (final InterDeviceLink link : list) {
			ID id1 = link.getSourceServiceElementId();
			ID id2 = link.getDestinationServiceElementId();
			Point p1 = entityMap.get(id1);
			Point p2 = entityMap.get(id2);
			Point point = null;
			ID networkId = null;
			
			final ServiceElement fakeSe = new ServiceElement();
			
			if (p1 == null || p2 == null) {
				point = calculatePoint(counterDiffNetwork, i++, angle);
				fakeSe.setIdentifier((long) i);
				
				if (p1 == null) {
					networkId = link.getSourceNetworkId();
					fakeSe.setName(id1.getName());
					fakeSe.setIdentifier(id1.getIdentifier());
				}
				else {
					networkId = link.getDestinationNetworkId();
					fakeSe.setName(id2.getName());
					fakeSe.setIdentifier(id2.getIdentifier());
				}
				
				if (networkId != null)
					entityMap.put(networkId, point);
				else {
					// fake for test
					entityMap.put(fakeSe.getID(), point);
				}
					
			}
			else
				continue;
			
        	Picture picture = new Picture(image, icon_width, icon_height, true, null);

        	ServiceElementWidget icon = new ServiceElementWidget(picture, fakeSe, null, null);
        	icon.draw((int)point.getX(), (int)point.getY());
        	
        	add(icon);
        	iconMap.put(fakeSe.getID(), icon);
        	
        	angle += increment;
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
			
			if (id1.equals(id2))
				continue;
			
			Point p1 = entityMap.get(id1);
			Point p2 = entityMap.get(id2);
			
			if (p1 == null || p2 == null)
				continue;
			
			// draw line between p1 and p2
			Line line = drawLink(link, p1, p2);
			
			ServiceElementWidget icon1 = iconMap.get(id1);
			ServiceElementWidget icon2 = iconMap.get(id2);
			
			if (icon1 == null || icon2 == null)
				continue;
			
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
	 * @return the line
	 */
	private Line drawLink(final InterDeviceLink link, Point p1, Point p2) {
		final HvMapIconPopup tooltip = new HvMapIconPopup();
		
		double x1 = p1.getX() + icon_width/2;
		double y1 = p1.getY() + icon_height/2;
		double x2 = p2.getX() + icon_width/2;
		double y2 = p2.getY() + icon_height/2;
		
		Line line = new Line(x1, y1, x2, y2);
		ColorName colorName = ColorName.BLUE;
			
        line.setStrokeColor(colorName).setStrokeWidth(line_width).setFillColor(colorName);
        
        line.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {  
            
			@Override
			public void onNodeMouseEnter(NodeMouseEnterEvent event) {
				int x = SystemSplitViewPanel.WESTPANEL_WIDTH + event.getX() + 15;
				int y = 100 + event.getY() - 15;

				tooltip.setPopupPosition(x, y);
				tooltip.update(link.getName(), "Not available");
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
