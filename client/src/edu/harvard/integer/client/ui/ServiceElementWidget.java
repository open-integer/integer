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
import com.google.gwt.touch.client.Point;

import edu.harvard.integer.client.utils.LinePoints;
import edu.harvard.integer.client.widget.HvMapIconPopup;
import edu.harvard.integer.common.BaseEntity;
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
	
	/** The line connector list. */
	private List<LinePoints> linePointList = new ArrayList<LinePoints>(); 
	
	/** The drag line connector list. */
	private List<LinePoints> dragLinePointList = new ArrayList<LinePoints>();
	
	/** The click handler. */
	private NodeMouseClickHandler clickHandler;
	
	/** The highlighted. */
	private boolean highlighted = false;
	
	/** The clipped image width. */
	private int clippedImageWidth;
	
	/** The clipped image height. */
	private int clippedImageHeight;
	
	/** The half of icon_width. */
	private double half_icon_width;
	
	/** The half of icon_height. */
	private double half_icon_height;
	
	/** The font size. */
	private int fontSize ;
	
	/** The label below offset. */
	private int labelBelowOffset;
	
	/**
	 * Constructor class of HvServiceElementWidget.
	 *
	 * @param picture the picture
	 * @param serviceElement the service element
	 * @param clickHandler the click handler
	 */
	public ServiceElementWidget(Picture picture, BaseEntity entity, NodeMouseClickHandler clickHandler) {
		this.picture = picture;
		this.entity = entity;
		this.clickHandler = clickHandler;
		
		setDraggable(true);
		setListening(true);

		final HvMapIconPopup popup = new HvMapIconPopup(entity.getName(), "");
		clippedImageWidth = (int)picture.getClippedImageDestinationWidth();
		clippedImageHeight = (int)picture.getClippedImageDestinationHeight();
		half_icon_width = clippedImageWidth/2;
		half_icon_height = clippedImageHeight/2;
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
	 * Draw the widget at position of (x, y)
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void draw(int x, int y) {
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
	 * Adds the line connector between the points
	 *
	 * @param lc the lc
	 */
	public void addLineConnector(LinePoints linePoints) {
		linePointList.add(linePoints);
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeDragEndHandler#onNodeDragEnd(com.emitrom.lienzo.client.core.event.NodeDragEndEvent)
	 */
	@Override
	public void onNodeDragEnd(NodeDragEndEvent event) {
		removeDragLines(event.getX(), event.getY());
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeDragMoveHandler#onNodeDragMove(com.emitrom.lienzo.client.core.event.NodeDragMoveEvent)
	 */
	@Override
	public void onNodeDragMove(NodeDragMoveEvent event) {
		moveDragLines(event.getX(), event.getY());
	}

	/* (non-Javadoc)
	 * @see com.emitrom.lienzo.client.core.event.NodeDragStartHandler#onNodeDragStart(com.emitrom.lienzo.client.core.event.NodeDragStartEvent)
	 */
	@Override
	public void onNodeDragStart(NodeDragStartEvent event) {
		addDragLines(event.getX(), event.getY());
	}
	
	/**
	 * Adds the drag lines at position of (x, y)
	 *
	 * @param cur_x the cur_x
	 * @param cur_y the cur_y
	 */
	private void addDragLines(int cur_x, int cur_y) {
		
		for (LinePoints linePoints : linePointList) {
			Point otherPoint = linePoints.getEndPoint();
			Line line = linePoints.getLine();
			line.setVisible(false);
			
			Line newLine = new Line(cur_x, cur_y, otherPoint.getX()+half_icon_width, otherPoint.getY()+half_icon_height);
			newLine.setStrokeColor(ColorName.LIGHTGRAY).setStrokeWidth(2).setFillColor(ColorName.LIGHTPINK);  	
			
			if (newLine.getParent() == null) {  
                getViewport().getDraglayer().add(newLine);  
                newLine.moveToBottom();  
            } 
			newLine.setVisible(true);
			
			// save current point at top-left corner of the icon
			Point curPoint = new Point(cur_x - half_icon_width, cur_y - half_icon_height);
			
			// save lines being dragged
			LinePoints newLinePoints = new LinePoints(newLine, curPoint, otherPoint);
			dragLinePointList.add(newLinePoints);
		}
	}
	
	/**
	 * Move drag lines to position of (x, y)
	 *
	 * @param cur_x the cur_x
	 * @param cur_y the cur_y
	 */
	private void moveDragLines(int cur_x, int cur_y) {
		Point2D cur_point = new Point2D(cur_x, cur_y);
		updateDragLines(cur_point, dragLinePointList, true, false);
	}

	/**
	 * Removes the drag lines from position of (x, y)
	 *
	 * @param cur_x the cur_x
	 * @param cur_y the cur_y
	 */
	private void removeDragLines(int cur_x, int cur_y) {
		Point2D cur_point = new Point2D(cur_x, cur_y);
		updateDragLines(cur_point, dragLinePointList, false, false);
		updateDragLines(cur_point, linePointList, true, true);
	}
	
	/**
	 * Update drag lines.
	 *
	 * @param cur_point the cur_point
	 * @param lines the lines
	 * @param visible the visible
	 * @param draw the draw
	 */
	private void updateDragLines(Point2D cur_point, List<LinePoints> lines, boolean visible, boolean draw) {
		for (LinePoints linePoints : lines) {
			Point otherPoint = linePoints.getEndPoint();
			Point2D other_point = new Point2D(otherPoint.getX() + half_icon_width, otherPoint.getY() + half_icon_height);
			Line cur_line = linePoints.getLine();
			cur_line.setPoints(new Point2DArray(cur_point, other_point));
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
		if (clickHandler != null)
			clickHandler.onNodeMouseClick(event);
		
		if (entity instanceof Network)
			SystemSplitViewPanel.showServiceElementMap((Network)entity);
		else if (entity instanceof ServiceElement)
			SystemSplitViewPanel.showContainedTreeView((ServiceElement)entity);
		
		setHighLighted(true); // highlighted whenever it gets clicked for now
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
}
