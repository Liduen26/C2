package cCarre.AffichageMap.view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;

import cCarre.AffichageMap.model.Coin;
import cCarre.AffichageMap.model.FinishBlock;
import cCarre.AffichageMap.model.Ground;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.model.Obstacle;
import cCarre.AffichageMap.model.Player;
import cCarre.Menu.GameMenuController;
import cCarre.Menu.MainMenu;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.MoveGridEvent;
import cCarre.genmap.events.RestartGameEvent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
	int pieces = 0;
	boolean canJump = false;
	int spawnX, spawnY;
	boolean running = true;
	String level = "";
	Pane game = null;

	
	// Pour changer la vitsse
	int constV = 270; 
	final int constGrav = 700;
	
	boolean edit = false;

	@FXML
	private Player player;
	
	@FXML
	private Label Coin;

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
		JSONArray Level = level.getLevel();

		for(int y = 0; y < levelHeight; y++) {
			for(int x = 0; x < levelLength; x++) {
				char text = (char) ((JSONArray) Level.get(y)).get(x);
				
				switch(text) {
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
					Coin coin = new Coin(x*elementSize + (elementSize / 4), y*elementSize + (elementSize / 4), elementSize / 2, elementSize / 2, Color.YELLOW, rootLayout);
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
				case 's' :
					// Test rapide de l'éditeur
					spawnX = x*elementSize;
					spawnY = y*elementSize-1;
					break;
				}
			}
		}
		Color color = Color.BLUE;
		//JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader("./Color.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONObject ColorList = (JSONObject) obj;
            ColorList.get("variable");
            color = parseColor((String) ColorList.get("variable"));
            System.out.println(color);

            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

		player = new Player(spawnX, spawnY, elementSize, elementSize, color, rootLayout, constGrav, constV);
		
		// La camï¿½ra suit le joueur
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 300 && offset < level.getLevelWidth() - 300) {
                rootLayout.setLayoutX(-(offset - 300));
                Coin.setLayoutX(+(offset - 300));
                
                // Si le jeu vient de l'ï¿½diteur, transmet les coo ï¿½ la grille
				Ebus.get().post(new MoveGridEvent(-(offset - 300)));
            }
        });
		
		loop(500); // Let's go into the GAME !
		loadCoin();
		Ebus.get().register(this);
	}
	
	private Color parseColor(String colors)
	{
		Color color = Color.BLUE; 
		switch (colors) {
			case "blue":
				color = Color.BLUE;
				break;
				
			case "red":
				color = Color.RED;
				break;
				
			case "green":
				color = Color.GREEN;
				break;
				
			case "yellow":
				color = Color.YELLOW;
				break;
		}
	return color;
	}

	/**
	 * Chef d'orchestre du jeu, c'est un boucle qui update @fps fois par seconde
	 * @param fps Le nombre d'update, et donc d'images par seconde
	 */
	private void loop(int fps) {
		Timeline time1 = new Timeline(new KeyFrame(Duration.millis(1000 / (fps - 2)), e -> {
			
			dt = affFPS();
			temps = dt / 1000000000; //dt par sec
			
			if(running) {
				double gravity = player.p2.distance(player.centreX, player.centreY) * 2;
				double jumpForce = 600;
	
				//dt = affFPS();
				//temps = dt / 1000000000; //dt par sec
	
				// distanceX vect entre centre du joueur et le point (vitesse)
				vitesse = player.p1.distance(player.centreX, player.centreY);
				
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
				
				// Si le joueur touche la ligne d'arrivï¿½e
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
					player.death(spawnX, spawnY, rootLayout, Coin);
				}
				// Empeche de charger un saut pendant un saut
				if(jump == true) {
					canJump = false;
				}						
			}
			Coin.setText("Pieces : "+pieces);
		}));

		time1.setCycleCount(Animation.INDEFINITE);
		time1.play();
	}
	
	/**
	 * Gï¿½re la dï¿½tection de la collision avec les plateformes, 
	 * tue si le player est sur le cï¿½tï¿½, au sol s'il est sur le dessus
	 */
	private void platfCollision() {
		onGround = false;
		for (Shape platform : platforms) {
        	if (platform != player.playerRectangle) {
        		Shape intersect = Shape.intersect(player.playerRectangle, platform);
        		if (intersect.getBoundsInLocal().getHeight() != -1) {
        			if (intersect.getBoundsInLocal().getHeight() <= intersect.getBoundsInLocal().getWidth()) {
						
						if(intersect.getBoundsInLocal().getMinY() > platform.getTranslateY()) {
							// plafond -> MORT
	        				verticalVelocity = 0;
	        				player.death(spawnX,spawnY, rootLayout, Coin);
						} else {
							// AU sol
							player.setTranslateY(platform.getTranslateY() - (player.getHeight() - 0.0001));
								
							verticalVelocity = 0;
							onGround = true;
							canJump = true;
						}
					} else {
						// Coté -> MORT
						verticalVelocity = 0;
						player.death(spawnX,spawnY, rootLayout, Coin);
        			}
        		}
        	}
        }
	}

	/**
	 * Gestion de la collision avec les formes en triangles
	 */
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
			player.death(spawnX,spawnY, rootLayout, Coin);
		}
	}
	
	
	/**
	 * Gestion de la collision avec un Coin (une piï¿½ce)
	 */
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

		// On supprime les coins ramassï¿½s avec iterator car on ne peut pas delete quand on boucle sur la liste
		for (Iterator<Shape> it = coins.iterator(); it.hasNext(); ) {
			Node coin = it.next();
			if (!(Boolean)coin.getProperties().get("alive")) {
				it.remove();
				rootLayout.getChildren().remove(coin);
				pieces ++;
				saveCoin(pieces);
			}
		}
	}
	
	/**
	 * @return SI le joueur est au sol ou pas
	 */
	public boolean playerOnGround() {
		return onGround;
	}

	/**
	 * Calcule l'interval entre les frames, et affiche le nombre de fps calculï¿½
	 * @return L'interval entre chaque frame, en nanosecondes
	 */
	private double affFPS () {
		// Calculs FPS
		frame++;
		oldTime = newTime;
		newTime = System.nanoTime(); 
		dt = newTime - oldTime;

		// Affichage FPS
		if(System.currentTimeMillis() - time >= 1000) {
			//fps.setText("FPS : " + frame);
			System.out.println("FPS : " + frame);
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
	public void pause() throws IOException {
		System.out.println("menu pause");
		if (running) {
			running = false;
			
			FXMLLoader gameLoader = new FXMLLoader();
			gameLoader.setLocation(MainMenu.class.getResource("../AffichageMap/view/PauseMenu.fxml"));
			game = (Pane) gameLoader.load();
			game.setLayoutX(-rootLayout.getLayoutX());
			// Met le jeu par dessus la grille
			rootLayout.getChildren().add(game);
		}else {
			running = true;
			rootLayout.getChildren().remove(game);
		}
		
	}
	@Subscribe
	public void restart(RestartGameEvent e) {
		running = true;
		rootLayout.getChildren().remove(game);
	}
	public double getSpeedPlayer() {
		return player.getSpeed();
	}


	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public void setMap(String string) {
		// TODO Auto-generated method stub
		this.level = string;
	}
	
	@SuppressWarnings("unchecked")
	public void saveCoin(int pieces) {
		FileWriter file = null;
		JSONObject obj = new JSONObject();
		obj.put("nbrsCoin", new Integer(pieces));
		
		try {
			file =new FileWriter("./pieces.json");
			file.write(obj.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
 
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }	
	}
	
	public void loadCoin() {
		
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader("pieces.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            
            // Cast JSON file
            JSONObject JsonCoin = (JSONObject) obj;
            
            pieces = ((Long) JsonCoin.get("nbrsCoin")).intValue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}

}
