package edu.harvard.integer.client.widget;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.gwt.core.client.GWT;

import edu.harvard.integer.client.utils.Util;

public class DraggableShapesWidget extends WidgetLayer {  
  
    public DraggableShapesWidget(int width, int height) {  
        super(width, height);  
        init();  
    }  
  
    private void init() {  
  
        final int total = GWT.isProdMode() ? 1000 : 40;  
  
        for (int i = 0; i < total; i++) {  
  
            final Circle circle = new Circle(10);  
            circle.setX(Util.generateValueWithinBoundary(width, 125)).setY(Util.generateValueWithinBoundary(height, 125))  
                    .setStrokeColor(Color.getRandomHexColor()).setStrokeWidth(2).setFillColor(Color.getRandomHexColor()).setDraggable(true);  
            add(circle);  
  
        }  
  
  
    }  
}
