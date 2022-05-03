package timeline;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
	int frame;
	long time;
	boolean jump = false;

	public void setMainTimeline(MainTimeline main) {
		mainApp = main;
	}

	
	// Let's Code --------------------------------------------------------
	public void initialize() {
	//init temp
		newTime = System.nanoTime();
		time = System.currentTimeMillis();
	// init centre cube
		
		double centreX = square.getLayoutX() + square.getWidth() / 2;
		double centreY = square.getLayoutY() + square.getHeight() / 2;
		System.out.println("centreX : " + square.getLayoutX() + " / " + centreX);
		
		
	//init vecteur
		Point2D p1 = new Point2D(square.getLayoutX() + 50, centreY);
		Line vVitesse = new Line(centreX,centreY,p1.getX(),p1.getY());
		vVitesse.setStroke(Color.RED);
		
		Point2D p2 = new Point2D(centreX, square.getLayoutY() + 50);
		Line vG = new Line(centreX,centreY,p2.getX(),p2.getY());
		vG.setStroke(Color.GREEN);
			
		rootLayout.getChildren().add(vVitesse);
		rootLayout.getChildren().add(vG);
		

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
			

			// faire sauter
			if(jump == true) {
				System.out.println("jump");	
				
			}
			
			// Affichage FPS
			if(System.currentTimeMillis() - time > 1000) {
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

	
}