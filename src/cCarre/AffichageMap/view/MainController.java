package cCarre.AffichageMap.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;

import cCarre.MainMenu;
import cCarre.AffichageMap.model.Coin;
import cCarre.AffichageMap.model.FinishBlock;
import cCarre.AffichageMap.model.Ground;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.model.Obstacle;
import cCarre.AffichageMap.model.Player;
import cCarre.Menu.GameMenuController;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.MoveGridEvent;
import cCarre.genmap.events.PlayerState;
import jaco.mp3.player.MP3Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.util.Duration;

public class MainController {
	private GameMenuController mainApp;

	private ArrayList<Ground> platforms = new ArrayList<Ground>();
	private ArrayList<Obstacle> triangles = new ArrayList<Obstacle>();
	private ArrayList<FinishBlock> finishBlocks = new ArrayList<FinishBlock>();
	private ArrayList<Coin> coins = new ArrayList<Coin>();

	int elementSize = 60;

	// Vars ---------------
	MP3Player gameMusic;
	MP3Player gameSound1;

	long oldTime;
	long newTime;
	double dt; //dt par sec
	double temps;
	int frame;
	long time;
	
	Timeline time1 = null;
	
	double vitesse;
	double distanceX;
	double distanceY;
	double verticalVelocity = 0;
	int spawnX, spawnY;
	
	int pieces = 0;
	
	boolean jump = false;
	boolean onGround = true;
	
	String level = "";
	boolean running = true;
	boolean newSpawn = false;
	
	boolean dead = false;
	private Rectangle ragdoll = null;
	
	Shape[][] mapRender = null;
	
	// Pour changer la vitesse
	int constV = 270; 
	int constGrav = 700;
	double jumpForce = 600;
	
	// taille de l'�cran
	private Rectangle2D screenBounds = Screen.getPrimary().getBounds();

	boolean edit = false;
	double toolBarHeight = 0;
	
	double vAnimDeath = 1000;

	@FXML
	private Player player;
	
	@FXML
	private Label Coin;

	@FXML
	private AnchorPane rootLayout;
	
	@FXML
	private void initialize() {
		// Adapte la vitesse et la gravit� et les �l�ments � la taille de l'�cran
		float varVit = (float)1920/constV;		
		constV = (int) ((int) screenBounds.getWidth()/varVit);
		
		float varGrav = (float)1920/constGrav;		
		constGrav = (int) ((int) screenBounds.getWidth()/varGrav);
		
		// Init la taille des case et la force du saut par rapport � la r�solution
		jumpForce = (int) (screenBounds.getWidth()/(1920/jumpForce));
		elementSize = (int) (screenBounds.getWidth()/(1920/elementSize));

		//init temps
		newTime = System.nanoTime();
		time = System.currentTimeMillis();
		
		Ebus.get().register(this);

		Level level = new Level();
		int levelLength = level.getLevelLength();
		int levelHeight = level.getLevelHeight();
		JSONObject Level = level.getLevel();
		JSONArray map = (JSONArray) Level.get("map");

		mapRender = new Shape[levelHeight][levelLength];
		
		for(int y = 0; y < levelHeight; y++) {
			for(int x = 0; x < levelLength; x++) {
				char text = (char) ((JSONArray) map.get(y)).get(x);

				switch(text) {
				case '0' :
					// c'est vide, c'est l'air
					mapRender[y][x] = null;
					break;
				case '1' :
					Ground platform = new Ground(x*elementSize, y*elementSize, elementSize, elementSize, Color.valueOf((String) ((JSONObject) Level.get("color")).get("ground")));
					
					// Ajout au tableau de rendu de la map
					mapRender[y][x] = platform;
					break;
				case '2' :
					Obstacle triangle = new Obstacle(x*elementSize, y*elementSize, elementSize, elementSize, Color.valueOf((String) ((JSONObject) Level.get("color")).get("obstacle")));

					// Ajout au tableau de rendu de la map
					mapRender[y][x] = triangle;
					break;
				case '3' :
					Coin coin = new Coin(x*elementSize + (elementSize / 4), y*elementSize + (elementSize / 4), elementSize / 2, elementSize / 2, Color.valueOf((String) ((JSONObject) Level.get("color")).get("coin")));

					// Ajout au tableau de rendu de la map
					mapRender[y][x] = coin;
					break;
				case '8' :
					if(!newSpawn) {
						spawnX = x * elementSize;
						spawnY = y * elementSize - 1;
					}
					break;
				case '9' :
					FinishBlock finishBlock = new FinishBlock(x*elementSize, y*elementSize, elementSize, elementSize, Color.GREEN);

					// Ajout au tableau de rendu de la map
					mapRender[y][x] = finishBlock;
					break;
				case 's' :
					// Test rapide de l'�diteur
					spawnX = x * elementSize;
					spawnY = y * elementSize - 1;
					newSpawn = true;
					break;
				}
			}
		}

		// pr�charge le spawn
		loadSpawn();
		
		// Charge le player
		player = new Player(spawnX, spawnY, elementSize, elementSize, Color.BLUE, rootLayout, constGrav, constV);
		
		// La cam�ra suit le joueur
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 300 && offset < level.getLevelWidth() - 300) {
                rootLayout.setLayoutX(-(offset - 300));
                Coin.setLayoutX(+(offset - 300));
                
                // Si le jeu vient de l'�diteur, transmet les coo � la grille
				Ebus.get().post(new MoveGridEvent(-(offset - 300)));
				
				
				// Update de l'affichage de la map
				double div = player.getTranslateX() / elementSize;
				double oldDiv = 0;
				if(div > oldDiv) {
					oldDiv = div;
					this.renderMap();
				}
            }
        });
		
        // Pause de 1s avant de lancer le jeu
 		PauseTransition delay = new PauseTransition(Duration.seconds(1));
 		delay.setOnFinished( event -> {
 			
 			loop(500); // Let's go into the GAME !
 		});
 		delay.play();
        
		
		loadCoin();
		
		// Cr�ation du cube d'anim de mort
		ragdoll = new Rectangle();
		ragdoll.setManaged(false);
		
		
		// Opacit� de base
		ragdoll.setOpacity(0.5);
		
		
	}

	/**
	 * Chef d'orchestre du jeu, c'est un boucle qui update @fps fois par seconde
	 * @param fps Le nombre d'update, et donc d'images par seconde
	 */
	private void loop(int fps) {
		time1 = new Timeline(new KeyFrame(Duration.millis(1000 / (fps - 2)), e -> {
			dt = affFPS();
			temps = dt / 1000000000; //dt par sec
			
			if(running) {
				double gravity = player.p2.distance(player.centreX, player.centreY) * 2;
				double jumpForce = 600;

				
				
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
				
				collisions(); // check les collision et la mort du joueur
				
				coinCollision(); // ramasse les coins si on passe dessus
				
				// Si le joueur touche la ligne d'arriv�e
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
				if(player.getTranslateY() > screenBounds.getHeight() - toolBarHeight) {
					player.death(spawnX, spawnY, rootLayout, Coin);
				}
				
				Coin.setText("Pieces : "+pieces);
				
			} else if (!running && dead){
				// Le joueur est mort
				double facteur = vAnimDeath * temps;
				
				ragdoll.setHeight(ragdoll.getHeight() + (facteur*2));
				ragdoll.setWidth(ragdoll.getWidth() + (facteur*2));
				ragdoll.setLayoutX(ragdoll.getLayoutX() - facteur);
				ragdoll.setLayoutY(ragdoll.getLayoutY() - facteur);
				
				ragdoll.setOpacity(ragdoll.getOpacity() - 0.0065);
			}
			
		}));

		time1.setCycleCount(Animation.INDEFINITE);
		time1.play();
	}
	
	
	/**
	 * Pr�charge le spawn de la map avant l'apparition du player, et r�initialise les liste de blocs
	 */
	private void loadSpawn() {
		// Chargement du spawn de la map
		
		double init = spawnX - elementSize * 6;
		double end = spawnX + screenBounds.getWidth();
		
		// Reset des listes
		rootLayout.getChildren().removeAll(platforms);
		rootLayout.getChildren().removeAll(triangles);
		rootLayout.getChildren().removeAll(finishBlocks);
		rootLayout.getChildren().removeAll(coins);
		
		platforms = new ArrayList<Ground>();
		triangles = new ArrayList<Obstacle>();
		finishBlocks = new ArrayList<FinishBlock>();
		coins = new ArrayList<Coin>();
		
		// lis la map de haut en bas, seulement les x dont il a besoin
		for(int y = 0; y < mapRender.length; y++) {
			for(int x = (int) Math.max(0, init); x < (int) Math.min(mapRender[0].length, end); x++) {
				
				if(mapRender[y][x] != null && ((x * elementSize) > init && (x * elementSize) < end)) {
					if(mapRender[y][x] instanceof Ground) {
						platforms.add((Ground) mapRender[y][x]);
						
					} else if(mapRender[y][x] instanceof Obstacle) {
						triangles.add((Obstacle) mapRender[y][x]);
						
					} else if(mapRender[y][x] instanceof Coin) {
						coins.add((Coin) mapRender[y][x]);
						
					} else if(mapRender[y][x] instanceof FinishBlock) {
						finishBlocks.add((FinishBlock) mapRender[y][x]);
					}
					
					rootLayout.getChildren().add(mapRender[y][x]);
				}
			}
		}
	}
	
	/**
	 * Supprime les blocs qui ne sont plus dans le champ, et affiche ceux qui y arrivent
	 */
	private void renderMap() {
		// R�cup�re la position du joueur et affiche uniquement la map dont il a besoin
		// Ajouter les blocs � leurs liste
		
		// constante de marges gauches et droites
		final int spaceLeft = 7;
		final int spaceRight = 3;
		
		double init = player.getTranslateX() - (elementSize * spaceLeft);
		double end = player.getTranslateX() + (screenBounds.getWidth() - (elementSize * spaceRight));
		
		
		// lis la map de haut en bas, seulement les x dont il a besoin
		for(int y = 0; y < mapRender.length; y++) {
			
			for(int x = Math.max(0, (int) (init / elementSize) - 4); x < Math.min(mapRender[0].length, end / elementSize); x++) {
				// Supprime ce qui est derriere
				if(rootLayout.getChildren().contains(mapRender[y][x])) {
					if(mapRender[y][x].getLayoutX() < init ) {
						
						// Suppr des listes de collisions
						if(mapRender[y][x] instanceof Ground) {
							platforms.remove((Ground) mapRender[y][x]);
							
						} else if(mapRender[y][x] instanceof Obstacle) {
							triangles.remove((Obstacle) mapRender[y][x]);
							System.out.println(((Obstacle) mapRender[y][x]));
							
						} else if(mapRender[y][x] instanceof Coin) {
							coins.remove((Coin) mapRender[y][x]);
							
						} else if(mapRender[y][x] instanceof FinishBlock) {
							finishBlocks.remove((FinishBlock) mapRender[y][x]);
						}
						
						// Suppr le visible
						rootLayout.getChildren().remove(mapRender[y][x]);
					}
				}
				
				// Ajoute les cases si besoin
				if(mapRender[y][x] != null && (mapRender[y][x].getLayoutX() > init) && (mapRender[y][x].getLayoutX() < player.getTranslateX() + end)) {
					if(!rootLayout.getChildren().contains(mapRender[y][x])) {
						
						// Suppr des listes de collisions
						if(mapRender[y][x] instanceof Ground) {
							platforms.add((Ground) mapRender[y][x]);
							
						} else if(mapRender[y][x] instanceof Obstacle) {
							triangles.add((Obstacle) mapRender[y][x]);
							
						} else if(mapRender[y][x] instanceof Coin) {
							coins.add((Coin) mapRender[y][x]);
							
						} else if(mapRender[y][x] instanceof FinishBlock) {
							finishBlocks.add((FinishBlock) mapRender[y][x]);
						}
						
						// Suppr le visible
						rootLayout.getChildren().add(mapRender[y][x]);
					}
				}
			}
		}
	}

	/**
	 * R�re les collisions et la mort du joueur
	 */
	private void collisions() {
		boolean death = false;
		
		if(platfollision() || triangleCollision()) {
			player.death(spawnX,spawnY, rootLayout, Coin);
		}
	}
	
	/**
	 * G�re la d�tection de la collision avec les plateformes, 
	 * tue si le player est sur le c�t�, au sol s'il est sur le dessus
	 */
	private boolean platfollision() {
		boolean collisionDetected = false;
		boolean ground = false;
		onGround = false;
		
		// Collisions au sol
		for (Shape platform : platforms) {
        	if (platform != player.playerRectangle) {
        		Shape intersect = Shape.intersect(player.playerRectangle, platform);
        		if (intersect.getBoundsInLocal().getHeight() != -1) {
        			if (intersect.getBoundsInLocal().getHeight() - toolBarHeight <= intersect.getBoundsInLocal().getWidth()) {
        				if(intersect.getBoundsInLocal().getMinY() - toolBarHeight > platform.getLayoutY()) {
        					System.out.println(toolBarHeight);
        					System.out.println(intersect.getBoundsInLocal().getMinY()- toolBarHeight);
        					System.out.println(platform.getLayoutY());
							// plafond -> MORT
	        				verticalVelocity = 0;
	        				collisionDetected = true;
	        				System.out.println("Ca c le plafond -------------------------------------------------------------------------------------");
						} else {
							// Sol
	        				
	        				player.setTranslateY(platform.getLayoutY() - (player.getHeight() - 0.0001));
//							System.out.println("Sol");
							verticalVelocity = 0;
							onGround = true;
						}
					} 
        		}
        	}
		}
		
		// Collisions cot�
		for (Shape platform : platforms) {
        	if (platform != player.playerRectangle) {
        		Shape intersect = Shape.intersect(player.playerRectangle, platform);
        		if (intersect.getBoundsInLocal().getHeight() != -1) {
        			if (intersect.getBoundsInLocal().getHeight() - toolBarHeight > intersect.getBoundsInLocal().getWidth()) {
						// Cot� -> MORT
						System.out.println("Ca c un bord -------------------------------------------------------------------------------------");
						verticalVelocity = 0;
						collisionDetected = true;
        			}
        		}
        	}
        }
		
		return collisionDetected;
		
	}

	/**
	 * Gestion de la collision avec les formes en triangles
	 */
	private boolean triangleCollision() {
		boolean collisionDetected = false;
		for (Shape triangle : triangles) {
			if (triangle != player.playerRectangle) {
				Shape intersect = Shape.intersect(player.playerRectangle, triangle);
				if (intersect.getBoundsInLocal().getWidth() != -1) {
					collisionDetected = true;
					System.out.println("Ca c un triangle -------------------------------------------------------------------------------------");
				}
			}
		}
		
		return collisionDetected;
	}
	
	
	/**
	 * Gestion de la collision avec un Coin (une pi�ce)
	 */
	private void coinCollision() {
		if(!edit) {
			// Check si le joueur touche une piece et change le statut de la piece
			for (Shape coin : coins) {
				if (coin != player.playerRectangle) {
					Shape intersect = Shape.intersect(player.playerRectangle, coin);
					if (intersect.getBoundsInLocal().getWidth() != -1) {
						coin.getProperties().put("alive", false);
					}
				}
			}
	
			// On supprime les coins ramass�s avec iterator car on ne peut pas delete quand on boucle sur la liste
			for (Iterator<Coin> it = coins.iterator(); it.hasNext(); ) {
				Node coin = it.next();
				if (!(Boolean)coin.getProperties().get("alive")) {
					it.remove();
					rootLayout.getChildren().remove(coin);
					pieces ++;
					saveCoin(pieces);
				}
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
	 * Calcule l'interval entre les frames, et affiche le nombre de fps calcul�
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

	public void startJump() {
		jump = true;
	}
	
	public void stopJump() {
		jump = false;
	}
	
	public double getSpeedPlayer() {
		return player.getSpeed();
	}


	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit, double height) {
		this.edit = edit;
		this.toolBarHeight = (edit == true) ? height : 0;
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

	public void setStop() {
		time1.stop();
	}
	
	
	@Subscribe
	public void setPlayerState(PlayerState e) {
		
		if(e.getState()) {
			// Le joueur respawn
			running = e.getState();
			dead = !e.getState();
			
			rootLayout.getChildren().remove(ragdoll);
			this.loadSpawn();
		} else {
			// Le joueur meurt
			running = e.getState();
			dead = !e.getState();
			
			// Placement du ragdoll de mort
			ragdoll.setWidth(player.getPlayerRectangle().getWidth() - (player.getPlayerRectangle().getWidth() / 2));
			ragdoll.setHeight(player.getPlayerRectangle().getHeight() - (player.getPlayerRectangle().getHeight() / 2));
			ragdoll.setLayoutX(player.getTranslateX() + player.getPlayerRectangle().getTranslateX() + (player.getPlayerRectangle().getWidth() / 4));
			ragdoll.setLayoutY(player.getTranslateY() + player.getPlayerRectangle().getTranslateY() + (player.getPlayerRectangle().getHeight() / 4));
			ragdoll.setFill(player.getPlayerRectangle().getFill());
			
			ragdoll.setOpacity(0.5);
//			System.out.println(rootLayout.getChildren().contains(ragdoll));
			rootLayout.getChildren().add(ragdoll);
			
			// Son de mort
//			String path = MainMenu.class.getResource("Minecraft-Death-Sound-Effect.mp3").getPath();
//			System.out.println(path);
			
			File file = new File("resources/Minecraft-Death-Sound-Effect_mp3cut.net.wav");
			System.out.println(file.exists());
			
			Media media = new Media(file.toURI().toString());
			System.out.println(media.getTracks());
			
			mediaPlayer = new MediaPlayer(media);
			System.out.println(mediaPlayer = new MediaPlayer(media));
			
			System.out.println(mediaPlayer.getStatus());
			mediaPlayer.play();
			
		}
	}
}
