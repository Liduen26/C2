package timeline;

import java.awt.geom.Point2D;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller {
	private MainTimeline mainApp;

	@FXML
	private Rectangle square;
	@FXML
	private Label fps;
	@FXML
	private AnchorPane rootLayout;

	// Vars ---------------
	long oldTime;
	long newTime;
	double dt; //dt par sec
	double temps;
	int frame;
	long time;
	boolean jump = false;
	double vitesse;
	double distanceX;
	double distanceY;
	double verticalVelocity = 0;
	
	//pour changer la vitsse
	int constV = 30; 
	final int constGrav = 100;
	
	// init var centre cube
	double centreX ;
	double centreY;


	public void setMainTimeline(MainTimeline main) {
		mainApp = main;
	}


	// Let's Code --------------------------------------------------------
	public void initialize() {
		//init temp
		newTime = System.nanoTime();
		time = System.currentTimeMillis();
		

		// init centre cube
		centreX = square.getTranslateX() + square.getWidth() / 2;
		centreY = square.getTranslateY() + square.getHeight() / 2;

		//init vecteur vitesse
		Point2D p1 = new Point2D.Double(centreX + constV, centreY);
		Line vVitesse = new Line(centreX,centreY,p1.getX(),p1.getY());
		vVitesse.setStroke(Color.RED);
		
		//init vecteur gravit�
		Point2D p2 = new Point2D.Double(centreX, centreY + constGrav);
		Line vG = new Line(centreX,centreY,p2.getX(),p2.getY());
		vG.setStroke(Color.PURPLE);

		//init vecteur vertical
		Point2D p3 = new Point2D.Double(centreX, centreY);
		Line vVert = new Line(centreX,centreY,p3.getX(),p3.getY());
		vVert.setStroke(Color.GREEN);


		rootLayout.getChildren().add(vVitesse);
		rootLayout.getChildren().add(vG);
		rootLayout.getChildren().add(vVert);

		
		// init timeline	
		Timeline timel = new Timeline(new KeyFrame(Duration.millis(1000 / 142), e -> {
			// init centre cube
			centreX = square.getTranslateX() + square.getWidth() / 2;
			centreY = square.getTranslateY() + square.getHeight() / 2;
			
			
			double gravity = p2.distance(centreX, centreY);
			gravity = 200;

			double jumpForce = 150;
			
			
			dt = affFPS();
			temps = dt / 1000000000; //dt par sec
		
			// distanceX vect entre centre du joueur et le point (vitesse)
			vitesse = p1.distance(centreX,centreY) * 4;
			
			
			// Est-ce que le cube est au sol ?
			if(square.getTranslateY() >= 432) {
				verticalVelocity = 0;
				
				// Saut si oui
				if(jump == true) {
					verticalVelocity = jumpForce;
					jump = false; 
				}
			} else {
				verticalVelocity -= gravity * temps;
			}

			System.out.println(verticalVelocity);
			distanceX = vitesse * temps;
			distanceY = verticalVelocity * temps;

			// Met a jour les position
			depl(vVitesse, vG, vVert, p1, p2, p3);

			dontGoOut();
			
		}));

		//TL launcher
		timel.setCycleCount(Animation.INDEFINITE);
		timel.play();

	}
	
	
	private void dontGoOut() {
		if(square.getTranslateX() >= 800 || square.getTranslateX() <= 0) {
			constV = constV * -1;
			// marche po :(
		}	
	}


	private void depl(Line vVitesse, Line vG, Line vVert, Point2D p1, Point2D p2, Point2D p3) {
		// D�placements X			
		square.setTranslateX(square.getTranslateX() + distanceX);
		vVitesse.setTranslateX(vVitesse.getTranslateX() + distanceX);
		vG.setTranslateX(vG.getTranslateX() + distanceX);
		vVert.setTranslateX(vVert.getTranslateX() + distanceX);

		
		// D�placements Y
		square.setTranslateY(square.getTranslateY() - distanceY);
		vVitesse.setTranslateY(vVitesse.getTranslateY() - distanceY);
		vG.setTranslateY(vG.getTranslateY() - distanceY);
		vVert.setStartY(centreY);
		vVert.setEndY(p3.getY());

		
		//actualiser position des points 
		p1.setLocation(p1.getX() + distanceX, centreY);
		p2.setLocation(p2.getX() + distanceX, centreY + constGrav);
		p3.setLocation(p3.getX() + distanceX, centreY - verticalVelocity);
	}
	
	private double affFPS () {
		// Calculs FPS
		frame++;
		oldTime = newTime;
		newTime = System.nanoTime();
		dt = newTime - oldTime;
		
		// Affichage FPS
		if(System.currentTimeMillis() - time >= 1000) {
			fps.setText("FPS : " + frame);
			frame = 0;
			time = System.currentTimeMillis();				
		} 
		
		return dt;
	}

	public void jump() { jump = true; }


}