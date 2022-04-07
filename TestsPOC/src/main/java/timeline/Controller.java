package timeline;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller {

    @FXML
    private Rectangle square;
    
    private double cox = 0;
    private double time = 0;
    
    private MainTimeline mainApp;
    private double yBase = 0;
    private boolean jump = false;
    private boolean grav = false;
	
	public void setMainTimeline(MainTimeline main) {
		mainApp = main;
	}
	
	public void initialize() {
		yBase = square.getLayoutY();
		Timeline timel = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				square.setLayoutX(square.getLayoutX() + 0.1);
				time = time + 0.01;
				if(jump == true) {
					jump();
					
				} else if(grav == true) {
					gravity();
					
				}
			}
		}));
		
		timel.setCycleCount(Animation.INDEFINITE);
		timel.play();
	}
	
	public void jumpOn() {
		jump = true;
		cox = square.getLayoutX();
		time = 0;
		yBase = square.getLayoutY();
		System.out.println(cox);
	}
	
	public void jump() {
		System.out.println(time);
		square.setLayoutY(yBase - (-Math.pow(time, 2) + (4 * time)) * 0.5);
		System.out.println(square.getLayoutY());
		
		if(time >= 2) {
			jump = false;
			System.out.println("stopJump");
			gravityOn();
		}
	}
	
	public void gravityOn() {
		grav = true;
		cox = square.getLayoutX();
		time = 0;
	}
	
	public void gravity() {
		square.setLayoutY(square.getLayoutY() + 0.5);
		
		if(square.getLayoutY() >= yBase) {
			grav = false;
		}
		
	}
}