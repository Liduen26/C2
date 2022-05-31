package cCarre.genmap.model;

import java.util.ArrayList;

import javax.tools.Tool;

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
	
	public Cell(Node cell) {
		super();
	}

	public Cell(int width, int x, int y) {
		super();
		this.width = width;
		this.x = x;
		this.y = y;
		
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
			
		case "departBtn":
			if(!ToolBar.isStartPlaced()) {
				// S'il n'y a pas encore de départ placé
				
				if(ToolBar.isEndPlaced() == false || ToolBar.getEndPlace() > this.x) {
					// Si la fin est bien a droite du start, ou pas placée
					Rectangle start = new Rectangle();
					start.setWidth(width);
					start.setHeight(width);
					start.setFill(Color.DARKGREEN);
					start.setId("start");
					
					this.getChildren().add(start);
					ToolBar.setStartPlaced(x);
					
				} else {
					occuped = false;
					
					Ebus.get().post(new PopupEvent("Attention !", "Le départ doit être placé à gauche de l'arrivée"));
				}
			} else {
				// Si un départ a déjà été placé 
				occuped = false;

				Ebus.get().post(new PopupEvent("Attention !", "Un départ a déjà été placé"));
			}
			break;
			
		case "arriveeBtn":
			if(!ToolBar.isEndPlaced()) {
				// S'il n'y a pas encore d'arrivée placée
				
				if(ToolBar.isStartPlaced() == false || ToolBar.getStartPlace() < this.x) {
					// Si le start est bien a gauche de la fin, ou pas placée
					Rectangle end = new Rectangle();
					end.setWidth(width);
					end.setHeight(width);
					end.setFill(Color.DARKRED);
					end.setId("end");
					
					this.getChildren().add(end);
					ToolBar.setEndPlaced(x);
					
				} else {
					occuped = false;
					
					Ebus.get().post(new PopupEvent("Attention !", "L'arrivée doit être placée à droite du départ"));
				}
			} else {
				// Si une arrivée a déjà été placée
				occuped = false;

				Ebus.get().post(new PopupEvent("Attention !", "Une arrivée a déjà été placée"));
			}
			break;
			
		default:
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
					// Vérifie si c'est le start ou le début qui a été delete
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
