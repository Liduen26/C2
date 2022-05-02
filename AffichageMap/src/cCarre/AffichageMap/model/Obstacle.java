package cCarre.AffichageMap.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Obstacle extends Polygon{
	
	int elementSize;

	public Obstacle(int x, int y, int width, int height, Color color, AnchorPane rootLayout) {
		this.elementSize = width;
		createTriangle(x, y, width, height, color, rootLayout);
	}
	
    private void createTriangle(double x, double y, int w, int h, Color color, AnchorPane rootLayout) {
        Polygon entity = new Polygon();
        entity.getPoints().addAll(new Double[]{
                x+elementSize/2, y, 
                x, y+elementSize, 
                x+elementSize, y+elementSize, 
             }); 

        entity.setFill(color);
        
        rootLayout.getChildren().add(entity);
    }
}
