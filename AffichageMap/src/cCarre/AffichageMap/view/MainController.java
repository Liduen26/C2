package cCarre.AffichageMap.view;

import java.util.ArrayList;
import java.util.Iterator;

import cCarre.AffichageMap.Main;
import cCarre.AffichageMap.model.Coin;
import cCarre.AffichageMap.model.FinishLine;
import cCarre.AffichageMap.model.Ground;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.model.Obstacle;
import cCarre.AffichageMap.model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


public class MainController {
	
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();
    private ArrayList<Node> finishLines = new ArrayList<Node>();
    private ArrayList<Shape> nodes; // Pour les triangles

    final int elementSize = 60;
    Player player;
    int spawnX, spawnY;

	// DELETE
    boolean running = true;
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
        nodes = new ArrayList<>();
        
		for(int y = 0; y < levelHeight; y++) {
			for(int x= 0; x < levelLength; x++) {
				
				switch(Level[y][x]) {
					case '0' :
						// ici c'est vide
						break;
					case '1' :
						Ground platform = new Ground(x*elementSize, y*elementSize, elementSize, elementSize, Color.BROWN, rootLayout);
                        platforms.add(platform);
						break;
					case '2' :
                        Obstacle triangle = new Obstacle(x*elementSize, y*elementSize, elementSize, elementSize, Color.RED, rootLayout);
                        nodes.add(triangle);
						break;
					case '3' :
						Coin coin = new Coin(x*elementSize+10, y*elementSize+10, 40, 40, Color.YELLOW, rootLayout);
						coins.add(coin);
						break;
					case '8' :
						spawnX = x*elementSize;
						spawnY = y*elementSize-1;
						break;
					case '9' :
                        FinishLine finishBlock = new FinishLine(x*elementSize, y*elementSize, elementSize, elementSize, Color.GREEN, rootLayout);
                        finishLines.add(finishBlock);
						break;
				}
			}
		}
        player = new Player(spawnX, spawnY, elementSize, elementSize, Color.BLUE, rootLayout);
        nodes.add(player);
        
        // NOUVEAU
        for (Shape block : nodes) {
            setDragListeners(block);
          }
        rootLayout.getChildren().addAll(nodes);
        // NOUVEAU
        
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 300 && offset < level.getLevelWidth() - 300) {
                rootLayout.setLayoutX(-(offset - 300));
            }
        });

        Timeline time1 = new Timeline(new KeyFrame(Duration.millis(16), e -> {
        	if(running == true) {
                checkShapeIntersection(player);
            	update();
        	}
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
    		// Le joueur est sur le sol
	    	if (playerOnGround()) {
	        	canJump = true;
	        	jump = false;
	        	player.setTranslateY(player.getTranslateY()-2);
	         }
	        // Le joueur jump
	        if(jump == true && player.getTranslateY() > beforeJump-jumpHeight) {
	        	player.setTranslateY(player.getTranslateY()-1);
	        }
	        // Le joueur est en l'air
	        else{
	        	jump = false;
	        	if(!playerOnGround()) {
	        		player.setTranslateY(player.getTranslateY()+1);
	        	}
        	beforeJump = player.getTranslateY();
	        }
	        // Le joueur sort de la map
	        if(player.getTranslateY() > 1000) {
	        	death();
	        }
        }
    }
    
    // Joueur sur le sol
    public boolean playerOnGround() {
        for (Node platform : platforms) {
            if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
            	if(player.getTranslateY()+player.getHeight() >= platform.getTranslateY()){
                	return true;
                }
            }
        }
    return false;
    }

	private void update() {
		
        for (Node finishBlock : finishLines) {
            if (player.getBoundsInParent().intersects(finishBlock.getBoundsInParent())) {
            	System.out.println("VICTOIRE");
            	running = false;
            }
        }
		
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                coin.getProperties().put("alive", false);
            }
        }
        
        // On supprime les coins ramass�s avec iterator car on ne peut pas delete quand on boucle sur la liste
        for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                rootLayout.getChildren().remove(coin);
            }
        }
        
		// le joueur avance toujours
        movePlayerX(6);
        movePlayerY(10);
    }
	
	public void death() {
		player.setTranslateX(spawnX);
		player.setTranslateY(spawnY);
    	rootLayout.setLayoutX(-(player.getTranslateX())); // TP la cam�ra au d�but du jeu
	}
    
	// DELETE
    public void jumpPlayer() {
    	if(canJump) {
    		jump = true;
    		canJump = false;
    	}
    }
    
    // -------------------------- Drag and Drop (pour les tests) -------------------------- 
    public void setDragListeners(final Shape block) {
        final Delta dragDelta = new Delta();

        block.setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = block.getLayoutX() - mouseEvent.getSceneX();
            dragDelta.y = block.getLayoutY() - mouseEvent.getSceneY();
            block.setCursor(Cursor.NONE);
          }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            block.setCursor(Cursor.HAND);
          }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            block.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            block.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            checkShapeIntersection(block);
          }
        });
      }
    // -------------------------- Drag and Drop (pour les tests) -------------------------- 

    // Collisions avec les obstacles (triangles)
    private void checkShapeIntersection(Shape block) {
        boolean collisionDetected = false;
        for (Shape static_bloc : nodes) {
          if (static_bloc != block) {

            Shape intersect = Shape.intersect(block, static_bloc);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
              collisionDetected = true;
            }
          }
        }

        if (collisionDetected) {
          death();
        }
      }
    
    class Delta { double x, y; }
   
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

}
