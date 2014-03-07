package edu.harvard.integer.client.widget;

import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Picture;

import edu.harvard.integer.client.resources.Resources;

/**
 * The Class DragImageWidget.
 */
public class DragImageWidget extends WidgetLayer {  
  
    /** The image width. */
    private final int IMAGE_WIDTH = 100;  
    
    /** The image height. */
    private final int IMAGE_HEIGHT = 100;  
      
    /** The alain avatar. */
    private Picture alainAvatar;  
    
    /** The alfredo avatar. */
    private Picture alfredoAvatar;  
    
    /** The david avatar. */
    private Picture davidAvatar;  
    
    /** The dean avatar. */
    private Picture deanAvatar;  
    
    /** The enno avatar. */
    private Picture ennoAvatar;  
      
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
          
        final int x = width/2;  
        final int y = height/2;  
          
        PictureLoadedHandler onLoad = new PictureLoadedHandler()  
        {  
            public void onPictureLoaded(Picture picture)  
            {  
                add(picture);  
                DragImageWidget.this.draw();  
            }  
        };  
          
        alainAvatar = new Picture(Resources.IMAGES.wirelessRoute128(), IMAGE_WIDTH, IMAGE_HEIGHT, true, null);  
        alainAvatar.setDraggable(true).setX(x - IMAGE_WIDTH*2).setY(y - IMAGE_HEIGHT).onLoad(onLoad);  
          
        alfredoAvatar = new Picture(Resources.IMAGES.graySwitch(), IMAGE_WIDTH, IMAGE_HEIGHT, true, null);  
        alfredoAvatar.setDraggable(true).setX(x - IMAGE_WIDTH*2).setY(y).onLoad(onLoad);  
          
        davidAvatar = new Picture(Resources.IMAGES.wirelessRouter80(), IMAGE_WIDTH, IMAGE_HEIGHT, true, null);  
        davidAvatar.setDraggable(true).setX(x + IMAGE_WIDTH).setY(y - IMAGE_HEIGHT).onLoad(onLoad);  
          
        deanAvatar = new Picture(Resources.IMAGES.greenRouter(), IMAGE_WIDTH, IMAGE_HEIGHT, true, null);  
        deanAvatar.setDraggable(true).setX(x - IMAGE_WIDTH/2).setY(y - IMAGE_HEIGHT/2).onLoad(onLoad);  
          
        ennoAvatar = new Picture(Resources.IMAGES.pcom(), IMAGE_WIDTH, IMAGE_HEIGHT, true, null);  
        ennoAvatar.setDraggable(true).setX(x + IMAGE_WIDTH).setY(y).onLoad(onLoad);  
    }  
}
