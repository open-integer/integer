package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.client.core.types.Shadow;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.touch.client.Point;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.client.utils.HvLink;
import edu.harvard.integer.client.widget.HvDialogBox;
import edu.harvard.integer.client.widget.HvMapIconPopup;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.topology.MapItemPosition;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The ServiceElementWidget class represents a Service Element Widget object displaying on the screen.
 * This is a subclass class extended from com.emitrom.lienzo.client.core.shape.Group.
 * It includes element icon image, multiple lines and text label so that they can be moved around together by mouse
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ServiceElementWidget extends Group implements NodeMouseClickHandler, NodeDragStartHandler, NodeDragMoveHandler, NodeDragEndHandler {

	/** The picture. */
	private Picture picture;
	
	/** The service element. */
	private BaseEntity entity;
	
	/** The map item position. */
	private MapItemPosition mapItemPosition;
	
	/** The point position of this widget */
	private Point point;
	
	/** The list of associated link. */
	private final List<HvLink> linkList = new ArrayList<HvLink>(); 
	
	/** The list of link to be dragged. */
	private final List<HvLink> dragLinkList = new ArrayList<HvLink>();
	
	/** The subnetPanel. */
	private SubnetPanel subnetPanel;
	
	/** The highlighted. */
	private boolean highlighted = false;
	
	/** The clipped image width. */
	private int clippedImageWidth;
	
	/** The clipped image height. */
	private int clippedImageHeight;
	
	/** The font size. */
	private int fontSize ;
	
	/** The label below offset. */
	private int labelBelowOffset;
	
	/**
	 * Constructor class of HvServiceElementWidget.
	 *
	 * @param picture the picture
	 * @param entity the entity
	 * @param mapItemPosition 
	 * @param subnetPanel the click handler
	 */
	public ServiceElementWidget(Picture picture, BaseEntity entity, MapItemPosition mapItemPosition, SubnetPanel subnetPanel) {
		this.picture = picture;
		this.entity = entity;
		this.mapItemPosition = mapItemPosition;
		this.subnetPanel = subnetPanel;
		
		setDraggable(true);
		setListening(true);

		final HvMapIconPopup popup = new HvMapIconPopup(entity.getName(), "");
		clippedImageWidth = (int)picture.getClippedImageDestinationWidth();
		clippedImageHeight = (int)picture.getClippedImageDestinationHeight();
		fontSize = (int) Math.ceil(clippedImageWidth * 2 / 9);
		labelBelowOffset = clippedImageHeight + (int) Math.ceil(clippedImageHeight * 15 / 90);
		
		picture.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {  
            
			@Override
			public void onNodeMouseEnter(NodeMouseEnterEvent event) {
				int x = SystemSplitViewPanel.WESTPANEL_WIDTH + event.getX() + 15;
				int y = 100 + event.getY() - 15;
				popup.setPopupPosition(x, y);
				popup.show();
			}  
        });  
  
		picture.addNodeMouseExitHandler(new NodeMouseExitHandler() {

			@Override
			public void onNodeMouseExit(NodeMouseExitEvent event) {
				popup.hide();
			}  
            
        });  
		
		addNodeDragStartHandler(this);  
        addNodeDragMoveHandler(this);  
        addNodeDragEndHandler(this);
        addNodeMouseClickHandler(this);
	}
	
	/**
	 * Draw the widget at position of (x, y).
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void draw(int x, int y) {
		point = new Point(x, y);
		
		picture.setStrokeColor(ColorName.CYAN).setStrokeWidth(3).setX(x).setY(y).onLoad(new PictureLoadedHandler() {

			@Override
			public void onPictureLoaded(Picture picture) {
				add(picture);
				ServiceElementWidget.this.getLayer().draw();
			}
			
		});
		
		Text text = new Text(entity.getName(), "oblique normal bold", fontSize);
		text.setX(x).setY(y+labelBelowOffset).setTextAlign(TextAlign.LEFT).setFillColor(ColorName.DARKBLUE.getValue()).setScale(0.5);
		add(text);
	}
	
	/**
	 * Adds the line connector between the points.
	 *
	 * @param link the link
	 */
	public void addLink(HvLink link) {
		linkList.add(link);
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeDragEndHandler#onNodeDragEnd(com.emitrom.lienzo.client.core.event.NodeDragEndEvent)
	 */
	@Override
	public void onNodeDragEnd(NodeDragEndEvent event) {
		// update the point position
		point = new Point(event.getX(), event.getY());
		removeDraggedLinks(point);
		
		// clear to finish
		clearDragLinks();
		
		mapItemPosition.setXposition(event.getX());
		mapItemPosition.setYposition(event.getY());
		
		MainClient.integerService.updateMapItemPosition(mapItemPosition, new AsyncCallback<Void>()  {

			@Override
			public void onFailure(Throwable caught) {	
				MainClient.statusPanel.updateStatus("Failed to save position for " + entity.getName());
			}

			@Override
			public void onSuccess(Void result) {
				MainClient.statusPanel.updateStatus("New position saved for " + entity.getName());
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeDragMoveHandler#onNodeDragMove(com.emitrom.lienzo.client.core.event.NodeDragMoveEvent)
	 */
	@Override
	public void onNodeDragMove(NodeDragMoveEvent event) {
		point = new Point(event.getX(), event.getY());
		moveDragLines(point);
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeDragStartHandler#onNodeDragStart(com.emitrom.lienzo.client.core.event.NodeDragStartEvent)
	 */
	@Override
	public void onNodeDragStart(NodeDragStartEvent event) {
		point = new Point(event.getX(), event.getY());
		addDraggingLinks(point);
	}
	
	private void clearDragLinks() {
		for (HvLink link : dragLinkList) {
			getViewport().getDraglayer().remove(link.getLine());
		}
		dragLinkList.clear();
	}
	
	/**
	 * Adds the dragging lines at center of icon.
	 *
	 * @param cur_x the cur_x
	 * @param cur_y the cur_y
	 */
	private void addDraggingLinks(Point point) {
		
		for (HvLink link : linkList) {
			ID startId = link.getStartWidget().getEntity().getID();
			ID endId = link.getEndWidget().getEntity().getID();
			
			// ignore the link which startId is not this entity id
			if (!entity.getID().equals(startId) || startId.equals(endId))
				continue;
			
			Point otherPoint = link.getEndPoint();
			Line line = link.getLine();
			ServiceElementWidget endWidget = link.getEndWidget();
			otherPoint = endWidget.getPoint();
			
			line.setVisible(false);
			
			Line newLine = new Line(point.getX(), point.getY(), otherPoint.getX(), otherPoint.getY());
			newLine.setStrokeColor(ColorName.LIGHTGRAY).setStrokeWidth(2).setFillColor(ColorName.LIGHTGRAY);  	
			
			if (newLine.getParent() == null) {  
                getViewport().getDraglayer().add(newLine);  
                newLine.moveToBottom();  
            } 
			newLine.setVisible(true);
			
			// save lines being dragged
			HvLink newLink = new HvLink(newLine, point, otherPoint, this, endWidget);
			dragLinkList.add(newLink);
			
			// update other end point for this widget
			endWidget.updateLinkEndPoint(this);
		}
	}
	
	/**
	 * Update link to the given end point widget.
	 *
	 * @param widget the widget
	 */
	private void updateLinkEndPoint(ServiceElementWidget widget) {
		Point2D widgetPoint = new Point2D(widget.getPoint().getX(), widget.getPoint().getY());
		
		for (HvLink link : linkList) {
			if (link.hasWidget(widget)) {
				Point2D curPoint = new Point2D(point.getX(), point.getY());
				
				Line cur_line = link.getLine();
				cur_line.setPoints(new Point2DArray(widgetPoint, curPoint));
				break;
			}
		}
	}
	
	/**
	 * Move drag lines to the given point.
	 *
	 * @param Point the point
	 */
	private void moveDragLines(Point point) {
		//Point cur_point = new Point(cur_x, cur_y);
		updateDraggingLinks(point, dragLinkList, true, false);
	}

	/**
	 * Removes the dragged links at center of icons.
	 *
	 * @param Point the point
	 */
	private void removeDraggedLinks(Point point) {
		updateDraggingLinks(point, dragLinkList, false, false);
		updateDraggingLinks(point, linkList, true, true);
	}
	
	/**
	 * Update the dragging links at center of icons.
	 *
	 * @param point the point
	 * @param links the links
	 * @param visible the visible
	 * @param draw the draw
	 */
	private void updateDraggingLinks(Point point, List<HvLink> links, boolean visible, boolean draw) {
		Point2D point2d = new Point2D(point.getX(), point.getY());
		for (HvLink link : links) {
			ServiceElementWidget otherEndWidget = link.getEndWidget();
			Point2D other_point = new Point2D(otherEndWidget.getPoint().getX(), otherEndWidget.getPoint().getY());
			Line cur_line = link.getLine();
			cur_line.setPoints(new Point2DArray(point2d, other_point));
			cur_line.setVisible(visible);
			
			if (draw)
				cur_line.getScene().draw();
		}
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeMouseClickHandler#onNodeMouseClick(com.emitrom.lienzo.client.core.event.NodeMouseClickEvent)
	 */
	@Override
	public void onNodeMouseClick(NodeMouseClickEvent event) {
		
		if (entity instanceof Network)
			SystemSplitViewPanel.showServiceElementMap((Network)entity);
		else if (entity instanceof ServiceElement) {
			subnetPanel.setSelectedEntity(entity);
			ServiceElement serviceElement = (ServiceElement)entity;
			
			ServiceElementTreeDetailsPanel detailsPanel = new ServiceElementTreeDetailsPanel(serviceElement);
			final HvDialogBox detailsDialog = new HvDialogBox(serviceElement.getName(), detailsPanel);
			detailsDialog.enableOkButton(false);

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			    public void execute() {
			    	detailsDialog.center();
			    	detailsDialog.show();
			    }
			});
						
		}
		setHighLighted(true); // highlighted whenever it gets clicked for now
	}
	
	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public BaseEntity getEntity() {
		return entity;
	}
	
	/**
	 * Sets the high lighted.
	 *
	 * @param highlighted the new high lighted
	 */
	public void setHighLighted(boolean highlighted) {
		this.highlighted = highlighted;
		drawHighLights(highlighted);
	}
	
	/**
	 * Gets the high lighted.
	 *
	 * @return the high lighted
	 */
	public boolean getHighLighted() {
		return highlighted;
	}
	
	/**
	 * Draw high lights.
	 *
	 * @param highLight the high light
	 */
	private void drawHighLights(boolean highLight) {
		ColorName highLightColor = highLight ? ColorName.DARKBLUE : ColorName.WHITE;
		picture.setShadow(new Shadow(highLightColor, 3,3,3)).getLayer().draw();
	}

	/**
	 * Gets the list of associated links.
	 *
	 * @return the link list
	 */
	public List<HvLink> getLinkList() {
		return linkList;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

}
