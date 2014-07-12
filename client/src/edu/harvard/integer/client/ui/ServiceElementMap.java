package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvMapIconPopup;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.InterNetworkLink;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;
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
	private BaseEntity selectedEntity;
	
	/** The selected timestamp. */
	private long selectedTimestamp;
	
	/** The entity map. */
	private Map<ID, Point> entityMap = new HashMap<ID, Point>();
	
	private List<Point> pointList = new ArrayList<Point>();
	
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
	 * Instantiates a new service element map.
	 */
	public ServiceElementMap() {
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
	 * Inits the network.
	 *
	 * @param count the count
	 */
	private void initNetwork(int count) {
		Network[] netowrks = new Network[count];
		for (int i = 0; i < count; i++) {
			netowrks[i] = new Network();
			netowrks[i].setName("harvard-"+i);
		}
		update(netowrks);
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
	 * Update network information including network lkist and link list
	 *
	 * @param networkkInfo the networkk info
	 */
	public void updateNetworkInformation(NetworkInformation networkkInfo) {
		update(networkkInfo.getNetworks());
		drawLinks(networkkInfo.getLinks());
	}

	/**
	 * Update method will refresh the panel with the given list of ServiceElement objects.
	 *
	 * @param result the result
	 */
	public void update(BaseEntity[] result) {
		entityMap.clear();
		removeAll();
		init_layout(result.length);
		
		// === testing only first 4 points ==
		int N = 4;
		init_layout(N);
		// ==================================
		
		int i = 0;
		ImageResource image = Resources.IMAGES.graySwitch();
		
		for (final BaseEntity entity : result) {
			// Point point = calculatePoint(result.length, i++);
			// === test only first 4 points ====
			if (i >= N)
				break;
			// ==================================
			
			Point point = calculatePoint(N, i++);
			entityMap.put(entity.getID(), point);
			pointList.add(point);
			// ==================================
			
			if (entity instanceof ServiceElement)
				image = Resources.IMAGES.pcom();
			else if (entity instanceof Network) {
				image = Resources.IMAGES.network();
				Network network = (Network)entity;
				network.getInterDeviceLinks();
				network.getLowerNetworks();
				network.getServiceElements();
			}
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
	 * @param links 
	 */
	private void drawLinks(InterNetworkLink[] links) {
		// draw fake links for demo
		Point p1 = pointList.get(0);
		Point p2 = pointList.get(1);
		Point p3 = pointList.get(2);
		Point p4 = pointList.get(3);
		InterNetworkLink link1 = new InterNetworkLink();
		link1.setName("link-1-2");
		InterNetworkLink link2 = new InterNetworkLink();
		link2.setName("link-2-4");
		InterNetworkLink link3 = new InterNetworkLink();
		link3.setName("link-3-4");
		
		drawLink(link1, p1, p2);
		drawLink(link2, p2, p4);
		drawLink(link3, p3, p4);
		
		// draw links
		for (final InterNetworkLink link : links) {
			ID id1 = link.getSourceNetworkId();
			ID id2 = link.getDestinationNetworkId();
			p1 = entityMap.get(id1);
			p2 = entityMap.get(id2);
			
			if (p1 == null || p2 == null)
				continue;
			
			// draw line between p0 and p
			drawLink(link, p1, p2);
		}
	}
	
	private void drawLink(final InterNetworkLink link, Point p1, Point p2) {
		final HvMapIconPopup tooltip = new HvMapIconPopup();
		
		double x1 = p1.getX() + icon_width/2;
		double y1 = p1.getY() + icon_height/2;
		double x2 = p2.getX() + icon_width/2;
		double y2 = p2.getY() + icon_height/2;
		
		Line line = new Line(x1, y1, x2, y2);
		ColorName colorName = ColorName.BLUE;
		if (link.getName().equalsIgnoreCase("link-2-4"))
			colorName = ColorName.RED;
		else
			colorName = ColorName.GREEN;
//		if (linkStatus == null)
//			colorName = ColorName.GREY;
//		else if (linkStatus.equalsIgnoreCase("up"))
//			colorName = ColorName.GREEN;
//		else if (linkStatus.equalsIgnoreCase("down"));
//			colorName = ColorName.RED;
			
        line.setStrokeColor(ColorName.GREEN).setStrokeWidth(3).setFillColor(colorName);
        
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
				LinkDetailsPanel detailsPanel = new LinkDetailsPanel(link);
				HvDialogBox detailsDialog = new HvDialogBox("Link Details", detailsPanel);
				detailsDialog.enableOkButton(false);
				detailsDialog.setSize("400px", "150px");
				detailsDialog.center();
				detailsDialog.show();
			}
			
		});
        
        add(line);
	}
	
	/**
	 * Calculate point.
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
	public Point calculatePoint(int total, int i) {
		if (layout_type == CIRCULAR_LAYOUT) 
			return calculateCircularLayoutPoint(total, i);
		
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
	 *
	 * @param total the total
	 * @param i the i
	 * @return the point
	 */
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
