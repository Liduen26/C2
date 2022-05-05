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
	double distance;
	double verticalVelocity = 0;
	
	//pour changer la vitsse
	final int constV = 30; 
	
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
		
		//init vecteur gravité
		Point2D p2 = new Point2D.Double(centreX, centreY + 50);
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
			double jumpForce = 40;
			
			
			dt = affFPS();
			temps = dt / 1000000000; //dt par sec
		
					
			// faire sauter
			if(jump == true) {
				verticalVelocity = jumpForce;
				jump = false; 
			}
					
			
			// distance vect entre centre du joueur et le point (vitesse)
			vitesse = p1.distance(centreX,centreY) * 4;
			distance = vitesse * temps;
			
			// Déplacements X			
			square.setTranslateX(square.getTranslateX() + distance);
			vVitesse.setTranslateX(vVitesse.getTranslateX() + distance);
			vG.setTranslateX(vG.getTranslateX() + distance);
			//vVert.setTranslateX(vVert.getTranslateX() + distance);
			vVert.setTranslateX(vVert.getTranslateX()+ distance);
			//vVert.setTranslateY(vVert.getTranslateY());

			
			// Déplacements Y
//			square.setTranslateX(square.getTranslateX() + distance);
//			vVitesse.setTranslateX(vVitesse.getTranslateX() + distance);
//			vG.setTranslateX(vG.getTranslateX() + distance);
			vVert.setEndY(p3.getY());

			
			//actualiser position des points 
			p1.setLocation(p1.getX() + distance, centreY);
			p2.setLocation(centreX, p2.getY() + distance);
			p3.setLocation(centreX, centreY - verticalVelocity);
			
//			System.out.println("coordonée: " + centreX);
//			System.out.println("distance: "+ distance);
//			System.out.println("Temps : " + temps + "\n dt : " + dt + "\n");
//			System.out.println("coo Y sqr: "+centreY + " coo Y pt: " + p3.getY() + " Vertvelo: " + verticalVelocity);
//			System.out.println("x : " + vVert.getTranslateX() + " / vvert y : " + vVert.getTranslateY()+ "coo sqr Y: "+ square.getTranslateY());

					
			
			
			
		}));

		//TL luncher
		timel.setCycleCount(Animation.INDEFINITE);
		timel.play();

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