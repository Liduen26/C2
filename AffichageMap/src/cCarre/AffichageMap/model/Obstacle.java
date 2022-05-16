package cCarre.AffichageMap.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Obstacle extends Polygon{
	
	int elementSize;

	public Obstacle(int x, int y, int width, int height, Color color, AnchorPane rootLayout) {
		this.elementSize = width;     
		this.getPoints().addAll(new Double[]{
                (double) (x+elementSize/2), (double) y, 
                (double) x, (double) (y+elementSize), 
                (double) (x+elementSize), (double) (y+elementSize), 
             }); 

        this.setFill(color);
        
        rootLayout.getChildren().add(this);
	}
}
