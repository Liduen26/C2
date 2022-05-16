package cCarre.genmap.model;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Parent {
	private boolean occuped = false;
	private int width;
	private int x, y;
	
	public Cell(int width, int x, int y) {
		this.width = width;
		
		Rectangle back = new Rectangle();
		back.setFill(Color.FLORALWHITE);
		back.setWidth(width);
		back.setHeight(width);
//		back.setPickOnBounds(false);
//		this.setPickOnBounds(true);
		this.getChildren().add(back);
		
		
		this.setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				System.out.println("slt");
				onPaint();
			}
		});
		
		this.setOnDragDetected(e -> {
			this.startFullDrag();
		});
		
		this.setOnMouseDragOver (e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				System.out.println("hey" + x);
				onPaint();
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
			Rectangle obst = new Rectangle();
			obst.setWidth(width);
			obst.setHeight(width);
			obst.setFill(Color.RED);
			
			this.getChildren().add(obst);
			break;
			
		default:
			occuped = false;
			break;
		}
		
	}

	private void erase() {
		// TODO Auto-generated method stub
		
	}
	
}
