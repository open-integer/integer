package edu.harvard.integer.client.widget;

import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.emitrom.lienzo.shared.core.types.TextAlign;

public class HvMapIconWidget extends Group {

	private Picture picture;
	private String title;
	
	public HvMapIconWidget(Picture picture, String title) {
		this.picture = picture;
		this.title = title;
		
		setDraggable(true);
		setListening(true);

		final HvMapIconPopup popup = new HvMapIconPopup(title, "Ok", "Cambridge");
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
	}
	
	public void draw(int x, int y) {
		picture.setX(x).setY(y).onLoad(new PictureLoadedHandler() {

			@Override
			public void onPictureLoaded(Picture picture) {
				add(picture);
				HvMapIconWidget.this.getLayer().draw();
			}
			
		});
		
		Text text = new Text(title, "oblique normal bold", 24);
		text.setX(x).setY(y+110).setTextAlign(TextAlign.LEFT).setFillColor(ColorName.DARKBLUE.getValue()).setScale(0.5);
		add(text);
	}

}
