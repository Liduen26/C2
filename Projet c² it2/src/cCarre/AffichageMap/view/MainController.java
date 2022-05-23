package cCarre.AffichageMap.view;

import java.util.ArrayList;
import java.util.Iterator;

import cCarre.AffichageMap.Main;
import cCarre.AffichageMap.model.Coin;
import cCarre.AffichageMap.model.FinishBlock;
import cCarre.AffichageMap.model.Ground;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.model.Obstacle;
import cCarre.AffichageMap.model.Player;
import cCarre.Menu.GameMenuController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


public class MainController {


	private GameMenuController mainApp;

	private ArrayList<Shape> platforms = new ArrayList<Shape>();
	private ArrayList<Shape> triangles = new ArrayList<Shape>();
	private ArrayList<Shape> finishBlocks = new ArrayList<Shape>();
	private ArrayList<Shape> coins = new ArrayList<Shape>();

	final int elementSize = 60;


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
	boolean onGround = false;
	
	boolean canJump = false;
	int spawnX, spawnY;
	boolean running = true;
	//pour changer la vitsse
	int constV = 270; 
	final int constGrav = 700;

	public void setMainApp(GameMenuController gameMenuController) {
		this.mainApp = gameMenuController;
	}

	@FXML
	private Player player;

	@FXML
	private AnchorPane rootLayout;

	@FXML
	private void initialize() {


		//init temps
		newTime = System.nanoTime();
		time = System.currentTimeMillis();


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
					Ground platform = new Ground(x*elementSize, y*elementSize, elementSize, elementSize, Color.BROWN, rootLayout);
					platforms.add(platform);
					break;
				case '2' :
					Obstacle triangle = new Obstacle(x*elementSize, y*elementSize, elementSize, elementSize, Color.RED, rootLayout);
					triangles.add(triangle);
					break;
				case '3' :
					Coin coin = new Coin(x*elementSize+15, y*elementSize+15, 30, 30, Color.YELLOW, rootLayout);
					coins.add(coin);
					break;
				case '8' :
					spawnX = x*elementSize;
					spawnY = y*elementSize-1;
					break;
				case '9' :
					FinishBlock finishBlock = new FinishBlock(x*elementSize, y*elementSize, elementSize, elementSize, Color.GREEN, rootLayout);
					finishBlocks.add(finishBlock);
					break;
				}
			}
		}
		player = new Player(spawnX, spawnY, elementSize, elementSize, Color.BLUE, rootLayout, constGrav, constV);
		
		// La caméra suit le joueur
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 300 && offset < level.getLevelWidth() - 300) {
                rootLayout.setLayoutX(-(offset - 300));
            }
        });
		
		//player.setFill(Color.PURPLE);
		Timeline time1 = new Timeline(new KeyFrame(Duration.millis(1000 / 142), e -> {
			
			if(running) {
				
			double gravity = player.p2.distance(player.centreX, player.centreY) * 2;
			double jumpForce = 600;

			dt = affFPS();
			temps = dt / 1000000000; //dt par sec

			// distanceX vect entre centre du joueur et le point (vitesse)
			vitesse = player.p1.distance(player.centreX,player.centreY);
			
			// Est-ce que le cube est au sol ?
			if(playerOnGround() == true) {
				verticalVelocity = 0;
				
				// Saut si oui
				if(jump == true) {
					verticalVelocity = jumpForce;
					jump = false; 
				}
			} else {
				verticalVelocity -= gravity * temps;
			}

			distanceX = vitesse * temps;
			distanceY = verticalVelocity * temps;

			// Met a jour les position
			player.depl(distanceX, distanceY, jumpForce, verticalVelocity);
			
			platfCollision(); // Check plateforme collision
			triangleCollision();// Check triangle collision
			coinCollision();// Check coin collision
			
			// Si le joueur touche la ligne d'arrivée
            boolean collisionDetected = false;
            for (Shape finishBlock : finishBlocks) {
            	if (finishBlock != player.playerRectangle) {

            		Shape intersect = Shape.intersect(player.playerRectangle, finishBlock);
            		if (intersect.getBoundsInLocal().getWidth() != -1) {
            			collisionDetected = true;
            		}
            	}

            	if (collisionDetected) {
            		running = false;
            	}
            }
            
			// meurt quand tombe dans le vide
			if(player.getTranslateY()>800) {
				player.death(spawnX, spawnY, rootLayout);
			}
			// Empeche de charger un saut pendant un saut
			if(jump == true) {
				canJump = false;
			}
		}
			
			
	        for(Shape block : platforms){
	            setDragListeners(block);
	        }
	        
	        
	        
		}));

		time1.setCycleCount(Animation.INDEFINITE);
		time1.play();
	}

	private void platfCollision() {
		onGround = false;
		for (Shape platform : platforms) {
        	if (platform != player.playerRectangle) {
        		Shape intersect = Shape.intersect(player.playerRectangle, platform);
        		if (intersect.getBoundsInLocal().getHeight() != -1) {
        			System.out.println("oui");
        			// Collision cotée
        			if(player.getTranslateY()+45>platform.getTranslateY()) {
        				verticalVelocity = 0;
        				player.death(spawnX,spawnY, rootLayout);
        			}
        			// sol
        			else {
        				player.setTranslateY(platform.getTranslateY()-player.getHeight());
        				verticalVelocity = 0;
        				onGround = true;
        				canJump = true;
        			}
        		}
        	}
        }
	}
	
    private void triangleCollision() {
        boolean collisionDetected = false;
        for (Shape triangle : triangles) {
          if (triangle != player.playerRectangle) {
            Shape intersect = Shape.intersect(player.playerRectangle, triangle);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
              collisionDetected = true;
            }
          }
        }
        if (collisionDetected) {
			player.death(spawnX,spawnY, rootLayout);
        }
      }
    
    private void coinCollision() {
    	// Check si le joueur touche une piece et change le statut de la piece
        for (Shape coin : coins) {
          if (coin != player.playerRectangle) {
            Shape intersect = Shape.intersect(player.playerRectangle, coin);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
            	coin.getProperties().put("alive", false);
            }
          }
        }
        
        // On supprime les coins ramassés avec iterator car on ne peut pas delete quand on boucle sur la liste
        for (Iterator<Shape> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                rootLayout.getChildren().remove(coin);
            }
        }
      }

	public boolean playerOnGround() {
		return onGround;
	}

	private double affFPS () {
		// Calculs FPS
		frame++;
		oldTime = newTime;
		newTime = System.nanoTime(); 
		dt = newTime - oldTime;

		// Affichage FPS
		if(System.currentTimeMillis() - time >= 1000) {
			//fps.setText("FPS : " + frame);
			//System.out.println("FPS : " + frame);
			frame = 0;
			time = System.currentTimeMillis();				
		} 

		return dt;
	}

	public void jump() {
		if(jump == false && canJump == true) {
			jump = true;
			canJump = false;
		}
		 
	}
	
	
	// Drag and drop pour tests (a DELETE)
    public void setDragListeners(final Shape block) {
        final Delta dragDelta = new Delta();

        block.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = block.getTranslateX() - mouseEvent.getSceneX();
                dragDelta.y = block.getTranslateY() - mouseEvent.getSceneY();
                block.setCursor(Cursor.NONE);
            }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                block.setCursor(Cursor.HAND);
            }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                block.setTranslateX(mouseEvent.getSceneX() + dragDelta.x);
                block.setTranslateY(mouseEvent.getSceneY() + dragDelta.y);
            }
        });
    }
     // Pour drag and drop (a DELETE)
    class Delta {
        double x, y;
    }

}
