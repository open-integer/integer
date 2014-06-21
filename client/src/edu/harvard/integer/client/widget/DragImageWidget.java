package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.resources.client.ImageResource;

import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.ui.ServiceElementWidget;
import edu.harvard.integer.client.utils.Coordinate;
import edu.harvard.integer.client.utils.LinePoints;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class DragImageWidget.
 */
public class DragImageWidget extends WidgetLayer {  
  
    /** The image width. */
    public static final int IMAGE_WIDTH = 60;  
    
    /** The image height. */
    public static final int IMAGE_HEIGHT = 60;  
    
    //public static final Color BANNER_COLOR = new Color(28, 90, 128);
      
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
    	addIndividualDevices(null, null);
    	addDeviceGroup(50, 300, "60 Oxford Street (60X)");
    	addDeviceGroup(500, 300, "One Summer Street (1SU)");
    }
    
    private void addDeviceGroup(int x, int y, String title) {
		Group group = new Group();
		group.setDraggable(true);
		group.setX(x).setY(y);
		addIndividualDevices(group, title);
		
		Rectangle rectangle = new Rectangle(400, 300, 7).setStrokeColor(ColorName.GRAY.getValue());
		group.add(rectangle);
		
		Rectangle topRec = new Rectangle(400, 30, 7).setFillColor(ColorName.AQUA.getValue()).setAlpha(0.2);
		group.add(topRec);
		
		add(group);
	}

	private void addIndividualDevices(Group group, String title) {
    	
        ImageResource[] images = 
        		{Resources.IMAGES.wirelessRoute128(),
        		 Resources.IMAGES.graySwitch(),
        		 Resources.IMAGES.wirelessRouter80(),
        		 Resources.IMAGES.greenRouter(),
        		 Resources.IMAGES.pcom(),
        		 Resources.IMAGES.wirelessRoute128(),
        		 Resources.IMAGES.graySwitch(),
        		 Resources.IMAGES.wirelessRouter80(),
        		};

        int i = 0;
        
        List<ServiceElementWidget> iconList = new ArrayList<ServiceElementWidget>();
        
        for (int row = 0; row < 2; row++) {
        	int x1 = 0;
            int y1 = 0;    
            
        	for (int col = 0; col < 3; col++) {
        		
	        	Picture picture = new Picture(images[i], IMAGE_WIDTH, IMAGE_HEIGHT, true, null);
	        	int x = col * IMAGE_WIDTH*2 + 30;
	        	int y = row * IMAGE_HEIGHT*2 + 30;
	        	ServiceElement serviceElement = new ServiceElement();
	        	serviceElement.setName("192.168.1."+i++);
	        	ServiceElementWidget icon = new ServiceElementWidget(picture, serviceElement, null);
	        	ServiceElementWidget last_icon = null;
	        	
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
	                line.setStrokeColor(ColorName.GREEN).setStrokeWidth(2).setFillColor(ColorName.GREEN);
	                
	                if (group != null)
	                	group.add(line);
	                else
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
        for (ServiceElementWidget icon : iconList) {
        	if (group == null)
            	add(icon);
        	else {
        		group.add(icon);
        		Text text = new Text(title, "Arial, sans-serif", 14);
        		text.setX(10).setY(20); // .setStrokeColor(BANNER_COLOR).setFillColor(BANNER_COLOR);
        		group.add(text);
        	}
        }
    }  
	
}
