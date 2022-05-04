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
	long dt;
	double dp;
	int frame;
	long time;
	boolean jump = false;
	
	// init centre cube
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
		 centreX = square.getLayoutX() + square.getWidth() / 2;
		 centreY = square.getLayoutY() + square.getHeight() / 2;


		//init vecteur vitesse
		Point2D p1 = new Point2D.Double(centreX + 50, centreY);
		Line vVitesse = new Line(centreX,centreY,p1.getX(),p1.getY());
		vVitesse.setStroke(Color.RED);
		
		
		//init vecteur gravité
		Point2D p2 = new Point2D.Double(centreX, centreY + 50);
		Line vG = new Line(centreX,centreY,p2.getX(),p2.getY());
		vG.setStroke(Color.GREEN);

		rootLayout.getChildren().add(vVitesse);
		rootLayout.getChildren().add(vG);
		
		System.out.println("coo centre carée: " + centreX);
		dp = p1.distance(centreX,centreY);
		System.out.println("dp: "+dp);
		
		// init timeline	
		Timeline timel = new Timeline(new KeyFrame(Duration.millis(1000 / 60), e -> {
			// Calculs FPS
			frame++;
			oldTime = newTime;
			newTime = System.nanoTime();
			dt = newTime - oldTime;

			// faire avancer					
			square.setLayoutX(square.getLayoutX() + 1);
			vVitesse.setLayoutX(vVitesse.getLayoutX() + 1);
			vG.setLayoutX(vG.getLayoutX() + 1);
			
			
			// init centre cube
		 centreX = square.getLayoutX() + square.getWidth() / 2;
		 centreY = square.getLayoutY() + square.getHeight() / 2;

			p1.setLocation(p1.getX() + 1, centreY);
			p2.setLocation(centreX, p2.getY());
			System.out.println("coo J1: "+centreX + " // "+"next coo: " + p1.getX());
		//	System.out.println("coo centre carée: " + centreX);
			System.out.println("dp: "+dp);


			
			dp = p1.distance(centreX,centreY);
			System.out.println(dp);

			// faire sauter
			if(jump == true) {
				System.out.println("jump");	

			}

			// Affichage FPS
			if(System.currentTimeMillis() - time >= 1000) {
				fps.setText("FPS : " + frame);
				frame = 0;
				time = System.currentTimeMillis();				
			}
		}));

		//TL luncher
		timel.setCycleCount(Animation.INDEFINITE);
		timel.play();

	}

	public void jump() { jump = true; }
	// lie le taille vvitesse avec vitesse et temps avec distance via la liaison precedente


}