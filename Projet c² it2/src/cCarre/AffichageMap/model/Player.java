package cCarre.AffichageMap.model;

import java.awt.geom.Point2D;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Player extends Parent{
	
	public double centreX, centreY;
	
	public Rectangle playerRectangle;
	
	public Point2D p1, p2, p3;
	
	public Line vVitesse, vG, vVert;
	
	int height,width;
	
	int constGrav;

	
	
	public void SetColor(Color color) {
		playerRectangle.setFill(color);
	}
	
	public Player(int x, int y, int width, int height, Color color, AnchorPane rootLayout, int constGrav, int constV) {
		this.setWidth(width);
		this.setHeight(height);
		
		this.height = height;
		this.width = width;
		this.constGrav = constGrav;
		
		playerRectangle = new Rectangle();
		
		this.setTranslateX(x);
		this.setTranslateY(y);
		playerRectangle.setWidth(width);
		playerRectangle.setHeight(height);
		playerRectangle.setFill(color);
		playerRectangle.getProperties().put("alive", true);
        
		this.getChildren().add(playerRectangle);
		
		// init centre cube
		centreX = playerRectangle.getWidth() / 2;
		centreY = playerRectangle.getHeight() / 2;
		
		//init point
		Point2D p1 = new Point2D.Double(centreX + constV, centreY);
		Line vVitesse = new Line(centreX,centreY,p1.getX(),p1.getY());
		vVitesse.setStroke(Color.RED);
		this.p1 = p1;
		this.vVitesse = vVitesse;
		
		Point2D p2 = new Point2D.Double(centreX, centreY + constGrav);
		Line vG = new Line(centreX,centreY,p2.getX(),p2.getY());
		vG.setStroke(Color.PURPLE);
		this.p2 = p2;
		this.vG = vG;

		Point2D p3 = new Point2D.Double(centreX, centreY);
		Line vVert = new Line(centreX,centreY,p3.getX(),p3.getY());
		vVert.setStroke(Color.GREEN);

		this.p3 = p3;
		this.vVert = vVert;

		// Ajout des vecteurs au joueur
		this.getChildren().add(vVitesse);
		this.getChildren().add(vG);
		this.getChildren().add(vVert);
        
        rootLayout.getChildren().add(this);
	}
	
	public void depl(double distanceX, double distanceY, double jumpForce, double verticalVelocity) {
		this.setTranslateX(this.getTranslateX() + distanceX);
		this.setTranslateY(this.getTranslateY() - distanceY);
		
		vVitesse.setStartX(centreX);
		vVitesse.setEndX(p1.getX());
		
		vG.setStartY(centreY);
		vG.setEndY(p2.getY());
		
		vVert.setStartY(centreY);
		vVert.setEndY(p3.getY());
		
		p1.setLocation(p1.getX(), centreY);
		p2.setLocation(p2.getX(), centreY + constGrav);
		p3.setLocation(p3.getX(), centreY - verticalVelocity);
	}
	
	public void tp(double X, double Y){
		this.setTranslateX(X);
		this.setTranslateY(Y);
		
		vVitesse.setStartX(centreX);
		vVitesse.setEndX(p1.getX());
		
		vG.setStartY(centreY);
		vG.setEndY(p2.getY());
		
		vVert.setStartY(centreY);
		vVert.setEndY(p3.getY());
	}
	
	public void death(double spawnX, double spawnY, AnchorPane rootLayout){
		tp(spawnX, spawnY);
    	rootLayout.setLayoutX(-(getTranslateX())); // TP la caméra au début du jeu
	}

	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getWidth() {
		return width;
	}



	public void setWidth(int width) {
		this.width = width;
	}
}
