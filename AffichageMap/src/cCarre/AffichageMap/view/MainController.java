package cCarre.AffichageMap.view;

import java.util.ArrayList;

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
import javafx.scene.Node;
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
    boolean canJump = true;
    double jumpHeight = 150;
    private boolean jump = false;
    double ground = 599;
    double beforeJump = ground - jumpHeight;

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
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    boolean rightBorder = player.getTranslateX() >= ((platform.getTranslateX() + elementSize) - player.getWidth());
                    boolean leftBorder = player.getTranslateX() <= (platform.getTranslateX() + player.getWidth());
                    
                    if (rightBorder || leftBorder) {
                    	System.out.println("BOOOOM");
                    	death();
                        return;
                    }
                }
            }
            player.setTranslateX(player.getTranslateX()+1);
        }
    }
    // DELETE
    private void movePlayerY(int value) {
    	for (int i = 0; i < Math.abs(value); i++) {
	        for (Node platform : platforms) {
	            if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
	                    boolean onGround = player.getTranslateY()+player.getHeight() >= platform.getTranslateY();
	                    if (onGround) {
	                    	canJump = true;
	                    	jump = false;
	                    	player.setTranslateY(player.getTranslateY()-2);
	                    }
	            }
	        }
	        // Si le joueur appuie sur sauter
	        if(jump == true && player.getTranslateY() > beforeJump-jumpHeight) {
	        	player.setTranslateY(player.getTranslateY()-1);
	        }
	        else{
	        	jump = false;
	        	player.setTranslateY(player.getTranslateY()+1);
	        	beforeJump = player.getTranslateY();
	        }
	        // Si il tombe de la map
	        if(player.getTranslateY() > 1000) {
	        	death();
	        }
        }
    }
    
	private void update() {
		// le joueur avance toujours
        movePlayerX(6);
        // DELETE
        movePlayerY(10);
    }
	
	public void death() {
		player.setTranslateX(0);
		player.setTranslateY(599);
	}
    
	// DELETE
    public void jumpPlayer() {
    	if(canJump) {
    		jump = true;
    		canJump = false;
    	}
    }
   
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

}
