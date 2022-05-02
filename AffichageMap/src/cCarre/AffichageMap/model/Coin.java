package cCarre.AffichageMap.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Coin extends Rectangle{

	public Coin(int x, int y, int width, int height, Color color, AnchorPane rootLayout) {
		createCoin(x, y, width, height, color, rootLayout);
	}
	
    private void createCoin(int x, int y, int w, int h, Color color, AnchorPane rootLayout) {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);

        rootLayout.getChildren().add(entity);
    }
}
