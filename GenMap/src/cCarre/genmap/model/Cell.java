package cCarre.genmap.model;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Cell extends Parent {
	private boolean occuped = false;
	private int width;
	private int x, y;
	private Rectangle back;
	
	public Cell(int width, int x, int y) {
		this.width = width;
		
		back = new Rectangle();
		back.setFill(Color.FLORALWHITE);
		back.setWidth(width);
		back.setHeight(width);
//		back.setPickOnBounds(false);
//		this.setPickOnBounds(true);
		this.getChildren().add(back);
		
		
		this.setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				e.setDragDetect(true);
				onPaint();
			} else if(e.getButton() == MouseButton.SECONDARY) {
				e.setDragDetect(true);
				erase();
				this.startFullDrag();
			}
		});
		this.setOnDragDetected(e -> {
			this.startFullDrag();
		});
		this.setOnMouseDragOver (e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				onPaint();
			} else if(e.getButton() == MouseButton.SECONDARY) {
				erase();
			}
		});
	}
	
	public void onPaint() {
		if(!occuped) {
			paint();
		}
	}
	
	private void paint() {
		occuped = true;
		
		String item = ToolBar.getItem();
		
		switch (item) {
		case "groundBtn": 
			Rectangle ground = new Rectangle();
			ground.setWidth(width);
			ground.setHeight(width);
			ground.setFill(Color.ROYALBLUE);
			
			this.getChildren().add(ground);
			break;
			
		case "obstacleBtn":
			Polygon triangle = new Polygon();
			triangle.getPoints().addAll(new Double[]{
	                (double) (width / 2), (double) 0, 
	                (double) 0, (double) (width), 
	                (double) (width), (double) (width), 
	             });
			triangle.setFill(Color.RED);
			
			this.getChildren().add(triangle);
			break;
			
		default:
			occuped = false;
			break;
		}
		
	}

	private void erase() {
		ArrayList<Node> toRem = new ArrayList<Node>();
		if(occuped) {
			for(Node node : this.getChildren()) {
				if(node != back) {
					toRem.add(node);
				}
			}
			for(Node rem : toRem) {
				this.getChildren().remove(rem);
			}
			occuped = false;
		}
	}
	
}
