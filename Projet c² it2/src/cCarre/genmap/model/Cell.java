package cCarre.genmap.model;

import java.util.ArrayList;

import cCarre.genmap.events.AddLengthGrilleEvent;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.PopupEvent;
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
				// Cause une erreur mais ï¿½a marche qu'averc ï¿½a }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}
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
	
	public void loadMapPaint() {
		occuped = true;
		
		switch (cellId) {
		case 0:
			Rectangle vide = new Rectangle();
			vide.setWidth(width);
			vide.setHeight(width);
			vide.setFill(Color.WHITE);
			
			this.getChildren().add(vide);
			break;
			
		case 1: 
			Rectangle ground = new Rectangle();
			ground.setWidth(width);
			ground.setHeight(width);
			ground.setFill(Color.ROYALBLUE);
			
			this.getChildren().add(ground);
			break;
			
		case 2:
			// Ajoute un carré blanc avant de mettre le triangle
			Rectangle vide2 = new Rectangle();
			vide2.setWidth(width);
			vide2.setHeight(width);
			vide2.setFill(Color.WHITE);
			this.getChildren().add(vide2);
			
			// Ajoute le triangle
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
		
		// Si la case a ï¿½tï¿½ peinte, on vï¿½rifie si le x est sup au plus grand x, pour la taille de la grille 
		if(occuped) {
			if(ToolBar.getMostX() < x) {
				Ebus.get().post(new AddLengthGrilleEvent(x));
			}
		}
	}
	
	private void paint() {
		occuped = true;
		
		// Regarde quel item est sï¿½lectionnï¿½ pour la peinture
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
			
		case "departBtn":
			if(!ToolBar.isStartPlaced()) {
				// S'il n'y a pas encore de dï¿½part placï¿½
				
				if(ToolBar.isEndPlaced() == false || ToolBar.getEndPlace() > this.x) {
					// Si la fin est bien a droite du start, ou pas placï¿½e
					Rectangle start = new Rectangle();
					start.setWidth(width);
					start.setHeight(width);
					start.setFill(Color.DARKGREEN);
					start.setId("start");
					
					this.getChildren().add(start);
					ToolBar.setStartPlaced(x);
					
				} else {
					occuped = false;
					
					Ebus.get().post(new PopupEvent("Attention !", "Le dï¿½part doit ï¿½tre placï¿½ ï¿½ gauche de l'arrivï¿½e"));
				}
			} else {
				// Si un dï¿½part a dï¿½jï¿½ ï¿½tï¿½ placï¿½ 
				occuped = false;

				Ebus.get().post(new PopupEvent("Attention !", "Un dï¿½part a dï¿½jï¿½ ï¿½tï¿½ placï¿½"));
			}
			break;
			
		case "arriveeBtn":
			if(!ToolBar.isEndPlaced()) {
				// S'il n'y a pas encore d'arrivï¿½e placï¿½e
				
				if(ToolBar.isStartPlaced() == false || ToolBar.getStartPlace() < this.x) {
					// Si le start est bien a gauche de la fin, ou pas placï¿½e
					Rectangle end = new Rectangle();
					end.setWidth(width);
					end.setHeight(width);
					end.setFill(Color.DARKRED);
					end.setId("end");
					
					this.getChildren().add(end);
					ToolBar.setEndPlaced(x);
					
				} else {
					occuped = false;
					
					Ebus.get().post(new PopupEvent("Attention !", "L'arrivï¿½e doit ï¿½tre placï¿½e ï¿½ droite du dï¿½part"));
				}
			} else {
				// Si une arrivï¿½e a dï¿½jï¿½ ï¿½tï¿½ placï¿½e
				occuped = false;

				Ebus.get().post(new PopupEvent("Attention !", "Une arrivï¿½e a dï¿½jï¿½ ï¿½tï¿½ placï¿½e"));
			}
			break;
			
		default:
			cellId = 0;
			occuped = false;
			break;
		}
		
		// Si la case a ï¿½tï¿½ peinte, on vï¿½rifie si le x est sup au plus grand x, pour la taille de la grille 
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
					// Vï¿½rifie si c'est le start ou le dï¿½but qui a ï¿½tï¿½ delete
					ToolBar.setStartPlaced((node.getId() == "start" ) ? 0 : ToolBar.getStartPlace());
					ToolBar.setEndPlaced((node.getId() == "end" ) ? 0 : ToolBar.getEndPlace());
					
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
	@Override
	public String toString() {
		return super.toString() + " / x: " + this.x + " / y: " + this.y;
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
