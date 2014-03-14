package edu.harvard.integer.client.widget;

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
		this.setDraggable(true);
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
