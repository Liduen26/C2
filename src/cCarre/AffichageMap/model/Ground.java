package cCarre.AffichageMap.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ground extends Rectangle{

	public Ground(int x, int y, int width, int height, Color color) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setFill(color);
        this.getProperties().put("alive", true);
	}
}
