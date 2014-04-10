package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Arrow;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.shared.core.types.ArrowType;
import com.emitrom.lienzo.shared.core.types.Color;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.resources.client.ImageResource;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.utils.Coordinate;
import edu.harvard.integer.client.utils.LinePoints;

/**
 * The Class DragImageWidget.
 */
public class DragImageWidget extends WidgetLayer {  
  
    /** The image width. */
    public static final int IMAGE_WIDTH = 80;  
    
    /** The image height. */
    public static final int IMAGE_HEIGHT = 80;  
      
    /**
     * Instantiates a new drag image widget.
     *
     * @param width the width
     * @param height the height
     */
    public DragImageWidget(int width, int height) {  
        super(width, height);  
        init();  
    }  
  
    /**
     * Inits the.
     */
    private void init() {  
    	//setDraggable(true);
    	
        ImageResource[] images = 
        		{Resources.IMAGES.wirelessRoute128(),
        		 Resources.IMAGES.graySwitch(),
        		 Resources.IMAGES.wirelessRouter80(),
        		 Resources.IMAGES.greenRouter(),
        		 Resources.IMAGES.pcom(),
        		 Resources.IMAGES.wirelessRoute128(),
        		 Resources.IMAGES.graySwitch(),
        		 Resources.IMAGES.wirelessRouter80(),
        		 Resources.IMAGES.greenRouter(),
        		 Resources.IMAGES.pcom(),
        		 Resources.IMAGES.wirelessRoute128(),
        		 Resources.IMAGES.graySwitch(),
        		 Resources.IMAGES.wirelessRouter80(),
        		 Resources.IMAGES.greenRouter(),
        		 Resources.IMAGES.pcom(),
        		 Resources.IMAGES.wirelessRouter80(),
        		 Resources.IMAGES.greenRouter(),
        		 Resources.IMAGES.pcom(),
        		 Resources.IMAGES.wirelessRoute128(),
        		 Resources.IMAGES.graySwitch(),
        		 Resources.IMAGES.wirelessRouter80(),
        		 Resources.IMAGES.greenRouter(),
        		 Resources.IMAGES.pcom(),
        		};

        int i = 0;
        
        List<HvMapIconWidget> iconList = new ArrayList<HvMapIconWidget>();
        
        for (int row = 0; row < 3; row++) {
        	int x1 = 0;
            int y1 = 0;
            
            
        	for (int col = 0; col < 6; col++) {
        		
	        	Picture picture = new Picture(images[i], IMAGE_WIDTH, IMAGE_HEIGHT, true, null);
	        	int x = col * IMAGE_WIDTH*2 + 50;
	        	int y = row * IMAGE_HEIGHT*2 + 50;
	        	
	        	HvMapIconWidget icon = new HvMapIconWidget(picture, "192.168.100."+i++);
	        	HvMapIconWidget last_icon = null;
	        	
	        	if (col > 0) {
	        		last_icon = iconList.get(iconList.size()-1);
	        	}
	        	
	        	icon.draw(x, y);
	        	
	        	iconList.add(icon);
	        	
	        	// line starts and ends at center of icon
	        	int cur_x = x + IMAGE_WIDTH/2;
	        	int cur_y = y + IMAGE_HEIGHT/2;
	        	if (x1 != 0 && y1 != 0) {
	        		Line line = new Line(x1,y1, cur_x, cur_y);  
	                line.setStrokeColor(Color.getRandomHexColor()).setStrokeWidth(2).setFillColor(Color.getRandomHexColor());  
	                add(line);
	                
	                // add line to iconWidget
	                Coordinate prePoint = new Coordinate(x1, y1);
	                Coordinate curPoint = new Coordinate(cur_x, cur_y);
	                LinePoints lineConnector = new LinePoints(line, curPoint, prePoint);
	                icon.addLineConnector(lineConnector);

	                // add connector to previous icon
	                LinePoints pre_lineCoordinate = new LinePoints(line, prePoint, curPoint);
	                
	                if (last_icon != null)
	                	last_icon.addLineConnector(pre_lineCoordinate);
	        	}
	        	
	        	x1 = cur_x;
	        	y1 = cur_y;

        	}
        }
        
        // add icons on top
        for (HvMapIconWidget icon : iconList) {
        	add(icon);
        }
        
    }  
    
    public static class DragHandle extends Group implements NodeDragStartHandler, NodeDragMoveHandler, NodeDragEndHandler  
    {  
        private Arrow arrow, dragArrow;  
        private boolean start;  
          
        public DragHandle(String text, boolean start, Arrow arrow, Arrow dragArrow)  
        {  
            Circle c = new Circle(3);  
            c.setFillColor(ColorName.BLACK.getColor().setA(0.5));  
            add(c);  
              
            Text t = new Text(text, "Arial, sans-serif", 10);  
            t.setX(-10).setY(15);  
            t.setFillColor(ColorName.BLACK);  
            add(t);  
              
            this.arrow = arrow;  
            this.dragArrow = dragArrow;  
            this.start = start;  
              
            setDraggable(true);  
            addNodeDragStartHandler(this);  
            addNodeDragMoveHandler(this);  
            addNodeDragEndHandler(this);  
        }  
  
        @Override
		public void onNodeDragEnd(NodeDragEndEvent event) {
			if (start)  
                dragArrow.setStart(new Point2D(event.getX(), event.getY()));  
            else  
                dragArrow.setEnd(new Point2D(event.getX(), event.getY()));  
  
            dragArrow.setVisible(false);  
              
            arrow.setStart(dragArrow.getStart());  
            arrow.setEnd(dragArrow.getEnd());  
            arrow.setVisible(true);  
              
            arrow.getScene().draw(); 
		}

		@Override
		public void onNodeDragMove(NodeDragMoveEvent event) {
			if (start)  
                dragArrow.setStart(new Point2D(event.getX(), event.getY()));  
            else  
                dragArrow.setEnd(new Point2D(event.getX(), event.getY())); 
		}

		@Override
		public void onNodeDragStart(NodeDragStartEvent event) {
			if (dragArrow.getParent() == null)  
            {  
                getViewport().getDraglayer().add(dragArrow);  
                dragArrow.moveToBottom();  
            }  
              
            arrow.setVisible(false);  
            if (start)  
                dragArrow.setStart(new Point2D(event.getX(), event.getY()));  
            else  
                dragArrow.setEnd(new Point2D(event.getX(), event.getY()));  
              
            dragArrow.setVisible(true);
		}  
    }  
    
    
  
    public void update(int baseWidth, int headWidth, int arrowAngle, int baseAngle, ArrowType arrowType) {  
    	Arrow[] arrows = new Arrow[2];  
        for (int i = 0; i < 2; i++)  
        {  
            Arrow a = arrows[i];  
            a.setBaseWidth(baseWidth);  
            a.setHeadWidth(headWidth);  
            a.setArrowAngle(arrowAngle);  
            a.setBaseAngle(baseAngle);  
            a.setArrowType(arrowType);  
        }  
        getScene().draw();  
    }
}
