package cCarre.genmap.model;

import javafx.scene.Parent;
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
		this.setPickOnBounds(false);
		this.getChildren().add(back);
		
//		this.setOnMousePressed(event -> {
//			this.setOnMouseMoved(e -> {
////				if(e.getButton() == MouseButton.PRIMARY) {
//					System.out.println("heyy " + x);
//					
////				}
//			});
//		});
		
		this.setOnMousePressed(e -> {
			ToolBar.setClick(true);
			e.setDragDetect(true);
		});
//		
//		this.setOnMouseMoved(e -> {
//			System.out.println("slt " + x);
//			ToolBar.isClick();
//		});
		
		this.setOnDragOver(e -> {
			System.out.println("hey");
		});
		
	}
	
	public void onPaint() {
		if(!occuped) {
			paint();
			System.out.println("paint " + x);
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
			
			break;
		}
		
	}

	private void erase() {
		// TODO Auto-generated method stub
		
	}
	
}
