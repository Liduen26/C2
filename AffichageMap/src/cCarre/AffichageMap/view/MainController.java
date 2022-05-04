package cCarre.AffichageMap.view;

import java.util.ArrayList;
import java.util.HashMap;

import cCarre.AffichageMap.Main;
import cCarre.AffichageMap.model.Coin;
import cCarre.AffichageMap.model.Ground;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.model.Obstacle;
import cCarre.AffichageMap.model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class MainController {
	
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> triangles = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();
    
    final int elementSize = 60;
    Player player;
    
	// DELETE
    private boolean jump = false;
    double beforeJump;
    

	private Main mainApp;
	
	@FXML
	private AnchorPane rootLayout;
	
	@FXML
	private void initialize() {
		Level level = new Level();
		int levelLength = level.getLevelLength();
		int levelHeight = level.getLevelHeight();
		char[][] Level = level.getLevel();
		
		for(int y = 0; y < levelHeight; y++) {
			for(int x= 0; x < levelLength; x++) {
				
				switch(Level[y][x]) {
					case '0' :
						// ici c'est vide
						break;
					case '1' :
						Ground platform = new Ground(x*elementSize, y*elementSize, elementSize, elementSize, Color.GREEN, rootLayout);
                        platforms.add(platform);
						break;
					case '2' :
                        Obstacle triangle = new Obstacle(x*elementSize, y*elementSize, elementSize, elementSize, Color.RED, rootLayout);
                        triangles.add(triangle);
						break;
					case '3' :
						Coin coin = new Coin(x*elementSize, y*elementSize, elementSize, elementSize, Color.YELLOW, rootLayout);
						coins.add(coin);
						break;

				}
			}
		}
        player = new Player(5, 599, elementSize, elementSize, Color.BLUE, rootLayout);
        player.setFill(Color.PURPLE);
        
        Timeline time1 = new Timeline(new KeyFrame(Duration.millis(20), e -> {
        	update();
        }));
        
        time1.setCycleCount(Animation.INDEFINITE);
        time1.play();
	}

    private void movePlayerX(int value) {
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
	                if (player.getLayoutX() + elementSize >= platform.getTranslateX()-1) {
	                    player.setLayoutX(0);
	                    return;
	                }
            	}
            }
            player.setLayoutX(player.getLayoutX()+1);
        }
    }
    
	private void update() {
		// le joueur avance toujours
        movePlayerX(2);        
        
        if(jump == true && player.getLayoutY() > beforeJump-100) {
        	System.out.println("PLAYER = "+player.getLayoutY());
        	System.out.println("beforeJump = "+(beforeJump-50));
        	player.setLayoutY(player.getLayoutY()-1);
        }
        else
        	if(player.getTranslateY() != 599){
        	System.out.println(player.getTranslateY());
        	jump = false;
        	player.setLayoutY(player.getTranslateY()+1);
        	beforeJump = player.getTranslateY();
        }
    }
	
    
	// DELETE
    public void jumpPlayer() {
    	jump = true;
    }
   
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

}
