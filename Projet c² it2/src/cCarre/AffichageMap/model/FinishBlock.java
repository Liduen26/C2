package cCarre.AffichageMap.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FinishBlock extends Rectangle{

	public FinishBlock(int x, int y, int width, int height, Color color, AnchorPane rootLayout) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setFill(color);
        this.getProperties().put("alive", true);

        rootLayout.getChildren().add(this);
	}
	
    
}
