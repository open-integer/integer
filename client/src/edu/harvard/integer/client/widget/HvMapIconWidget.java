package edu.harvard.integer.client.widget;

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

import edu.harvard.integer.client.ui.SystemSplitViewPanel;
import edu.harvard.integer.client.utils.Coordinate;
import edu.harvard.integer.client.utils.LinePoints;
import edu.harvard.integer.common.topology.ServiceElement;

public class HvMapIconWidget extends Group implements NodeMouseClickHandler, NodeDragStartHandler, NodeDragMoveHandler, NodeDragEndHandler {

	private Picture picture;
	private ServiceElement serviceElement;
	private List<LinePoints> lineConnectorList = new ArrayList<LinePoints>(); 
	private List<LinePoints> dragLineConnectorList = new ArrayList<LinePoints>();
	private NodeMouseClickHandler clickHandler;
	private boolean highlighted = false;
	
	public HvMapIconWidget(Picture picture, ServiceElement serviceElement, NodeMouseClickHandler clickHandler) {
		this.picture = picture;
		this.serviceElement = serviceElement;
		this.clickHandler = clickHandler;
		
		setDraggable(true);
		setListening(true);

		final HvMapIconPopup popup = new HvMapIconPopup(serviceElement.getName(), "Ok", "Cambridge");
		final int dw = (int)picture.getClippedImageDestinationWidth()*3/2;
		final int dh = (int)picture.getClippedImageDestinationHeight()/2;
		
		
		picture.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {  
            
			@Override
			public void onNodeMouseEnter(NodeMouseEnterEvent event) {
				popup.setPopupPosition(event.getX()+dw, event.getY()+dh);
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
	
	public void draw(int x, int y) {
		/*center_x = x + DragImageWidget.IMAGE_WIDTH/2;
		center_y = y + DragImageWidget.IMAGE_HEIGHT/2;*/
		
		picture.setStrokeColor(ColorName.CYAN).setStrokeWidth(3).setX(x).setY(y).onLoad(new PictureLoadedHandler() {

			@Override
			public void onPictureLoaded(Picture picture) {
				add(picture);
				HvMapIconWidget.this.getLayer().draw();
			}
			
		});
		
		Text text = new Text(serviceElement.getName(), "oblique normal bold", 20);
		text.setX(x).setY(y+DragImageWidget.IMAGE_HEIGHT+16).setTextAlign(TextAlign.LEFT).setFillColor(ColorName.DARKBLUE.getValue()).setScale(0.5);
		add(text);
	}
	
	public void addLineConnector(LinePoints lc) {
		lineConnectorList.add(lc);
	}

	@Override
	public void onNodeDragEnd(NodeDragEndEvent event) {
		removeDragLines(event.getX(), event.getY());
	}

	@Override
	public void onNodeDragMove(NodeDragMoveEvent event) {
		moveDragLines(event.getX(), event.getY());
	}

	@Override
	public void onNodeDragStart(NodeDragStartEvent event) {
		addDragLines(event.getX(), event.getY());
	}
	
	private void addDragLines(int cur_x, int cur_y) {
		Coordinate curPoint = new Coordinate(cur_x, cur_y);
		
		for (LinePoints lineConnector : lineConnectorList) {
			Coordinate otherPoint = lineConnector.getEndPoint();
			Line line = lineConnector.getLine();
			line.setVisible(false);
			
			Line newLine = new Line(cur_x, cur_y, otherPoint.getX(), otherPoint.getY());
			newLine.setStrokeColor(ColorName.LIGHTGRAY).setStrokeWidth(2).setFillColor(ColorName.LIGHTPINK);  	
			
			if (newLine.getParent() == null) {  
                getViewport().getDraglayer().add(newLine);  
                newLine.moveToBottom();  
            } 
			newLine.setVisible(true);
			
			// save lines being dragged
			LinePoints newLineConnector = new LinePoints(newLine, curPoint, otherPoint);
			dragLineConnectorList.add(newLineConnector);
		}
	}
	
	private void moveDragLines(int cur_x, int cur_y) {
		Point2D cur_point = new Point2D(cur_x, cur_y);
		updateDragLines(cur_point, dragLineConnectorList, true, false);
	}

	private void removeDragLines(int cur_x, int cur_y) {
		Point2D cur_point = new Point2D(cur_x, cur_y);
		updateDragLines(cur_point, dragLineConnectorList, false, false);
		updateDragLines(cur_point, lineConnectorList, true, true);
	}
	
	private void updateDragLines(Point2D cur_point, List<LinePoints> lines, boolean visible, boolean draw) {
		for (LinePoints linePoints : lines) {
			Coordinate otherPoint = linePoints.getEndPoint();
			Point2D other_point = new Point2D(otherPoint.getX(), otherPoint.getY());
			Line cur_line = linePoints.getLine();
			cur_line.setPoints(new Point2DArray(cur_point, other_point));
			cur_line.setVisible(visible);
			
			if (draw)
				cur_line.getScene().draw();
		}
	}

	@Override
	public void onNodeMouseClick(NodeMouseClickEvent event) {
		if (clickHandler != null)
			clickHandler.onNodeMouseClick(event);
		// SystemSplitViewPanel.showContaineeView(serviceElement);
		
		setHighLighted(true); // highlighted whenever it gets clicked for now
	}
	
	public void setHighLighted(boolean highlighted) {
		this.highlighted = highlighted;
		drawHighLights(highlighted);
	}
	
	public boolean getHighLighted() {
		return highlighted;
	}
	
	private void drawHighLights(boolean highLight) {
		ColorName highLightColor = highLight ? ColorName.DARKBLUE : ColorName.WHITE;
		picture.setShadow(new Shadow(highLightColor, 3,3,3)).getLayer().draw();
	}
}
