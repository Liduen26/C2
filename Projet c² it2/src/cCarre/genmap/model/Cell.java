package cCarre.genmap.model;

import java.util.ArrayList;

import cCarre.genmap.events.AddLengthGrilleEvent;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.RemoveLengthGrilleEvent;
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
	private int cellId;

	public Cell(Node cell) {
		super();
	}

	public Cell(int width, int x, int y) {
		super();
		this.width = width;
		this.x = x;
		this.y = y;
		this.cellId = 0;
		
		back = new Rectangle();
		back.setFill(Color.FLORALWHITE);
		back.setWidth(width);
		back.setHeight(width);
		this.getChildren().add(back);
		
		
		this.setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				e.setDragDetect(true);
				onPaint();
			} else if(e.getButton() == MouseButton.SECONDARY) {
				e.setDragDetect(true);
				erase();
				// Cause une erreur mais ça marche qu'averc ça }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}
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
	
	private void onPaint() {
		if(!occuped) {
			paint();
		}
	}
	
	private void loadMapPaint() {
		occuped = true;
		
		switch (cellId) {
		case 0: 
			break;
		case 1: 
			Rectangle ground = new Rectangle();
			ground.setWidth(width);
			ground.setHeight(width);
			ground.setFill(Color.ROYALBLUE);
			
			this.getChildren().add(ground);
			break;
			
		case 2:
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
			cellId = 0;
			occuped = false;
			break;
		}
		
		// Si la case a été peinte, on vérifie si le x est sup au plus grand x, pour la taille de la grille 
		if(occuped) {
			if(ToolBar.getMostX() < x) {
				Ebus.get().post(new AddLengthGrilleEvent(x));
			}
		}
	}
	
	private void paint() {
		occuped = true;
		
		// Regarde quel item est sélectionné pour la peinture
		String item = ToolBar.getItem();
		
		switch (item) {
		case "groundBtn": 
			Rectangle ground = new Rectangle();
			ground.setWidth(width);
			ground.setHeight(width);
			ground.setFill(Color.ROYALBLUE);
			cellId = 1;
			
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
			cellId = 2;

			this.getChildren().add(triangle);
			break;
			
		default:
			cellId = 0;
			occuped = false;
			break;
		}
		
		// Si la case a été peinte, on vérifie si le x est sup au plus grand x, pour la taille de la grille 
		if(occuped) {
			if(ToolBar.getMostX() < x) {
				Ebus.get().post(new AddLengthGrilleEvent(x));
			}
		}
	}

	private void erase() {
		ArrayList<Node> toRem = new ArrayList<Node>();
		if(occuped) {
			for(Node node : this.getChildren()) {
				if(node != back) {
					cellId = 0;
					toRem.add(node);
				}
			}
			for(Node rem : toRem) {
				this.getChildren().remove(rem);
			}
			
			if(ToolBar.getMostX() == x) {
				Ebus.get().post(new RemoveLengthGrilleEvent(x));
			}
			occuped = false;
		}
	}
	
	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
