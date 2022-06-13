package cCarre.genmap.model;

import java.util.ArrayList;

import cCarre.genmap.events.AddLengthGrilleEvent;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.PopupEvent;
import cCarre.genmap.events.RemoveLengthGrilleEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends Region {
	private boolean occuped = false;
	private int width;
	private int x, y;
	private Rectangle back;
	private char cellId;
	private Rectangle selection;
	private boolean selected = false;

	public Cell(Node cell) {
		super();
	}

	public Cell(int width, int x, int y) {
		super();
		this.width = width;
		this.x = x;
		this.y = y;
		this.cellId = '0';
		
		back = new Rectangle();
		back.setFill(Color.FLORALWHITE);
		back.setWidth(width);
		back.setHeight(width);
		back.setMouseTransparent(true);
		this.getChildren().add(back);
		
		this.setPrefWidth(width);
		this.setPrefHeight(width);
		
		selection = new Rectangle();
		selection.setStroke(Color.YELLOW);
		selection.setStrokeWidth(4);
		selection.setStrokeType(StrokeType.INSIDE);
		selection.setFill(Color.TRANSPARENT);
		selection.setWidth(width);
		selection.setHeight(width);
		selection.setOpacity(0);
		selection.setMouseTransparent(true);
		
		
		this.getChildren().add(selection);
		
		this.setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				e.setDragDetect(true);
				onPaint();
				
			} else if(e.getButton() == MouseButton.SECONDARY) {
				e.setDragDetect(true);
				erase();
				// Cause une erreur mais �a marche qu'averc �a }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}
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
		case '0':
			occuped = false;
			Rectangle vide = new Rectangle();
			vide.setWidth(width);
			vide.setHeight(width);
			vide.setFill(Color.WHITE);
			
			this.getChildren().add(vide);
			break;
			
		case '1': 
			Rectangle ground = new Rectangle();
			ground.setWidth(width);
			ground.setHeight(width);
			ground.setFill(Color.ROYALBLUE);
			
			this.getChildren().add(ground);
			break;
			
		case '2':
			// Ajoute un carr� blanc avant de mettre le triangle
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
			cellId = '0';
			occuped = false;
			break;
		}
		
		// Si la case a �t� peinte, on v�rifie si le x est sup au plus grand x, pour la taille de la grille 
		if(occuped) {
			if(ToolBar.getMostX() < x) {
				Ebus.get().post(new AddLengthGrilleEvent(x));
			}
		}
	}
	
	private void paint() {
		occuped = true;
		
		// Regarde quel item est s�lectionn� pour la peinture
		String item = ToolBar.getItem();
		
		switch (item) {
		case "groundBtn": 
			Rectangle ground = new Rectangle();
			ground.setWidth(width);
			ground.setHeight(width);
			ground.setFill(Color.ROYALBLUE);
			ground.setMouseTransparent(true);
			cellId = '1';
			
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
			triangle.setMouseTransparent(true);
			cellId = '2';

			this.getChildren().add(triangle);
			break;
			
		case "departBtn":
			if(!ToolBar.isStartPlaced()) {
				// S'il n'y a pas encore de d�part plac�
				
				if(ToolBar.isEndPlaced() == false || ToolBar.getEndPlace() > this.x) {
					// Si la fin est bien a droite du start, ou pas plac�e
					Rectangle start = new Rectangle();
					start.setWidth(width);
					start.setHeight(width);
					start.setFill(Color.DARKGREEN);
					start.setId("start");
					start.setMouseTransparent(true);
					cellId = '8';
					
					this.getChildren().add(start);
					ToolBar.setStartPlaced(x);
					
				} else {
					occuped = false;
					
					Ebus.get().post(new PopupEvent("Attention !", "Le d�part doit �tre plac� � gauche de l'arriv�e"));
				}
			} else {
				// Si un d�part a d�j� �t� plac� 
				occuped = false;

				Ebus.get().post(new PopupEvent("Attention !", "Un d�part � d�j� �t� plac�e"));
			}
			break;
			
		case "arriveeBtn":
			if(!ToolBar.isEndPlaced()) {
				// S'il n'y a pas encore d'arriv�e plac�e
				
				if(ToolBar.isStartPlaced() == false || ToolBar.getStartPlace() < this.x) {
					// Si le start est bien a gauche de la fin, ou pas plac�e
					Rectangle end = new Rectangle();
					end.setWidth(width);
					end.setHeight(width);
					end.setFill(Color.DARKRED);
					end.setId("end");
					end.setMouseTransparent(true);
					cellId = '9';
					
					this.getChildren().add(end);
					ToolBar.setEndPlaced(x);
					
				} else {
					occuped = false;
					
					Ebus.get().post(new PopupEvent("Attention !", "L'arriv�e doit �tre plac�e � droite du d�part"));
				}
			} else {
				// Si une arriv�e a d�j� �t� plac�e
				occuped = false;

				Ebus.get().post(new PopupEvent("Attention !", "Une arriv�e � d�j� �t� plac�e"));
			}
			break;
			
		case "test": 
			cellId = 's';
			break;
			
		default:
			cellId = 0;
			occuped = false;
			break;
		}
		
		// Si la case a �t� peinte, on v�rifie si le x est sup au plus grand x, pour la taille de la grille 
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
					// V�rifie si c'est le start ou le d�but qui a �t� delete
					ToolBar.setStartPlaced((node.getId() == "start" ) ? 0 : ToolBar.getStartPlace());
					ToolBar.setEndPlaced((node.getId() == "end" ) ? 0 : ToolBar.getEndPlace());
					
					cellId = '0';
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
	
	public char getCellId() {
		return cellId;
	}

	public void setCellId(char cellId) {
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
	
	public int getMyWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if(selected && !this.selected) {
			selection.setOpacity(1);
			selection.toFront();
			
		} else if(!selected && this.selected) {
			selection.setOpacity(0);
		}
		this.selected = selected;
	}
	
}
