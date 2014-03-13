package edu.harvard.integer.client.widget;

import java.awt.Color;

import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import com.google.gwt.resources.client.ImageResource;

import edu.harvard.integer.client.resources.Resources;

/**
 * The Class DragImageWidget.
 */
public class DragImageWidget extends WidgetLayer {  
  
    /** The image width. */
    private final int IMAGE_WIDTH = 80;  
    
    /** The image height. */
    private final int IMAGE_HEIGHT = 80;  
      
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

        PictureLoadedHandler onLoad = new PictureLoadedHandler()  
        {  
            public void onPictureLoaded(Picture picture)  
            {  
                add(picture);  
                DragImageWidget.this.draw();  
            }  
        };  
          
        int i = 0;
        for (int row = 0; row < 3; row++) {
        	for (int col = 0; col < 6; col++) {
	        	Picture picture = new Picture(images[i], IMAGE_WIDTH, IMAGE_HEIGHT, true, null);
	        	int x = col * IMAGE_WIDTH*2 + 50;
	        	int y = row * IMAGE_HEIGHT*2 + 50;
	        	picture.setDraggable(true).setX(x).setY(y).onLoad(onLoad);
	        	
	        	i++;
	        	Text text = new Text("192.168.100." + i, "oblique normal bold", 24);
				text.setX(x).setY(y+IMAGE_HEIGHT+30).setTextAlign(TextAlign.LEFT).setFillColor(ColorName.DARKBLUE.getValue()).setScale(0.5);
				add(text);
        	}
        }
        
    }  
}
