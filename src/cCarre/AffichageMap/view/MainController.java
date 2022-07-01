package cCarre.AffichageMap.view;

import java.io.File;
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
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.MoveGridEvent;
import cCarre.genmap.events.PlayerState;
import cCarre.genmap.model.ToolBar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.util.Duration;

public class MainController {
	private ArrayList<Ground> platforms = new ArrayList<Ground>();
	private ArrayList<Obstacle> triangles = new ArrayList<Obstacle>();
	private ArrayList<FinishBlock> finishBlocks = new ArrayList<FinishBlock>();
	private ArrayList<Coin> coins = new ArrayList<Coin>();

	int elementSize = 60;

	// Vars ---------------
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
	
	MediaPlayer mediaPlayer = null;
	MediaPlayer musicPlayer = null;
	String nameMusic = "Projet64_2.wav";
	
	boolean newGround = false;
	boolean oldGround = false;
	
	// Pour changer la vitesse
	int constV = 430; 
	int constGrav = 900;
	double jumpForce = 1300;
	
	// taille de l'écran
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
		// Met la couleur sur le début du niveau
	    rootLayout.setStyle("-fx-background-color: "+ToolBar.getBackgroundColor());
		// Adapte la vitesse et la gravitï¿½ et les ï¿½lï¿½ments ï¿½ la taille de l'ï¿½cran
		float varVit = (float)1920/constV;		
		constV = (int) ((int) screenBounds.getWidth()/varVit);
		
		float varGrav = (float)1920/constGrav;		
		constGrav = (int) ((int) screenBounds.getWidth()/varGrav);
		
		// Init la taille des case et la force du saut par rapport ï¿½ la rï¿½solution
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
				System.out.println(((JSONArray) map.get(y)).get(x).getClass());
				char text = '0';
				
				if(((JSONArray) map.get(y)).get(x) instanceof java.lang.Long) {
					int text1 = ((Long) ((JSONArray) map.get(y)).get(x)).intValue();
					text = (char) (text1 + '0');
					System.out.println(text);
					
				} else {
					text = (char) ((JSONArray) map.get(y)).get(x);
				}
				

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
					// Test rapide de l'ï¿½diteur
					spawnX = x * elementSize;
					spawnY = y * elementSize - 1;
					newSpawn = true;
					break;
				}
			}
		}

		// prï¿½charge le spawn
		loadSpawn();
		
		// Charge le player
		player = new Player(spawnX, spawnY, elementSize, elementSize, Color.BLUE, rootLayout, constGrav, constV);
		
		// La camï¿½ra suit le joueur
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 300 && offset < level.getLevelWidth() - 300) {
                rootLayout.setLayoutX(-(offset - 300));
                Coin.setLayoutX(+(offset - 300));
                
        		// Adapte la taille de l'achor pane au niveau joué, puis change la background color
                rootLayout.resize((levelLength+25)*elementSize, (levelHeight+6)*elementSize);
        	    rootLayout.setStyle("-fx-background-color: "+ToolBar.getBackgroundColor());

                // Si le jeu vient de l'ï¿½diteur, transmet les coo ï¿½ la grille
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
		
        // Let's go into the GAME !
		loop(500); 

        // Joue la musique
		playMusic();

		// Charge le fichier des coins
		loadCoin();
		
		// Crï¿½ation du cube d'anim de mort
		ragdoll = new Rectangle();
		ragdoll.setManaged(false);
		
		// Opacitï¿½ de base
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
						playSound("Jump.wav", 1);
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
	            		stopMusic();
	            	}
	            }
	            
				
				
				Coin.setText("Pieces : "+pieces);
				
				//Son landing
				oldGround = newGround;
				newGround = playerOnGround();
				if(!oldGround && newGround) {
					playSound("Land.wav", 0.5);
				}
				
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
	 * Prï¿½charge le spawn de la map avant l'apparition du player, et rï¿½initialise les liste de blocs
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
		// constante de marges gauches et droites
		final int spaceLeft = 7;
		final int spaceRight = 4;
		
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
	 * Rï¿½re les collisions et la mort du joueur
	 */
	private void collisions() {
		if(platfollision() || triangleCollision()) {
			player.death(spawnX, spawnY, rootLayout, Coin);
		}
		// meurt quand tombe dans le vide
		if(player.getTranslateY() > screenBounds.getHeight() - toolBarHeight) {
			player.death(spawnX, spawnY, rootLayout, Coin);
			playSound("Roblox-Death-Sound-cut.wav", 4);
		}
	}
	
	/**
	 * Gï¿½re la dï¿½tection de la collision avec les plateformes, 
	 * tue si le player est sur le cï¿½tï¿½, au sol s'il est sur le dessus
	 */
	private boolean platfollision() {
		boolean collisionDetected = false;
		onGround = false;
		
		// Collisions au sol
		for (Shape platform : platforms) {
        	if (platform != player.playerRectangle) {
        		Shape intersect = Shape.intersect(player.playerRectangle, platform);
        		if (intersect.getBoundsInLocal().getHeight() != -1) {
        			if (intersect.getBoundsInLocal().getHeight() - toolBarHeight <= intersect.getBoundsInLocal().getWidth()) {
        				if(intersect.getBoundsInLocal().getMinY() - toolBarHeight > platform.getLayoutY()) {
							// plafond -> MORT
	        				verticalVelocity = 0;
	        				collisionDetected = true;
	        				System.out.println("Ca c le plafond -------------------------------------------------------------------------------------");
	        				playSound("Minecraft-Death-Sound-cut.wav", 5);
						} else {
							if(intersect.getBoundsInLocal().getMaxY() - toolBarHeight != platform.getLayoutY()) {
//								System.out.println(intersect.getBoundsInLocal().getMaxY()- toolBarHeight);
//								System.out.println(platform.getLayoutY() + "\n");
								
							}
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
		
		// Collisions cotï¿½
		for (Shape platform : platforms) {
        	if (platform != player.playerRectangle) {
        		Shape intersect = Shape.intersect(player.playerRectangle, platform);
        		if (intersect.getBoundsInLocal().getHeight() != -1) {
        			if (intersect.getBoundsInLocal().getHeight() - toolBarHeight > intersect.getBoundsInLocal().getWidth()) {
						// Cotï¿½ -> MORT
						System.out.println("Ca c un bord -------------------------------------------------------------------------------------");
						verticalVelocity = 0;
						collisionDetected = true;
						playSound("Minecraft-Death-Sound-cut.wav", 5);
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
					playSound("Roblox-Death-Sound-cut.wav", 4);
				}
			}
		}
		
		return collisionDetected;
	}
	
	
	/**
	 * Gestion de la collision avec un Coin (une piï¿½ce)
	 */
	private void coinCollision() {
		if(!edit) {
			// Check si le joueur touche une piece et change le statut de la piece
			for (Shape coin : coins) {
				if (coin != player.playerRectangle) {
					Shape intersect = Shape.intersect(player.playerRectangle, coin);
					if (intersect.getBoundsInLocal().getWidth() != -1) {
						coin.getProperties().put("alive", false);
						playSound("Coin.wav", 2);
					}
				}
			}
	
			// On supprime les coins ramassï¿½s avec iterator car on ne peut pas delete quand on boucle sur la liste
			for (Iterator<Coin> it = coins.iterator(); it.hasNext(); ) {
				Shape coin = it.next();
				if (!(Boolean)coin.getProperties().get("alive")) {
					it.remove();
					rootLayout.getChildren().remove(coin);
					pieces ++;
					
					// Supprime le coin du tableau de rendu de la map
					if(coin instanceof Coin) {
						Coin c = (Coin) coin;
						int x = (int) Math.round(c.getLayoutX() / elementSize);
						int y = (int) Math.round(c.getLayoutY() / elementSize);
						
						mapRender[y][x] = null;
					}
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
			e.printStackTrace();
		} finally {
 
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
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
		stopMusic();
		
	}
	
	
	@Subscribe
	public void setPlayerState(PlayerState e) {
		
		if(e.getState()) {
			// Sauf si la timeline est stop,
			if(time1.getStatus() != Animation.Status.STOPPED) {
				// Le joueur respawn
				running = e.getState();
				dead = !e.getState();
				
				playMusic();
				this.loadSpawn();
			}
			
			rootLayout.getChildren().remove(ragdoll);
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
			rootLayout.getChildren().add(ragdoll);
			
			saveCoin(pieces);
			stopMusic();
			
		}
	}
	
	
	/**
	 * Fais jouer un son se trouvant dans le dossier resources/audio/
	 * @param name Le nom du fichier (avec l'extension)
	 * @param volume Le volume de 0 à 10
	 */
	private void playSound(String name, double volume) {
		File file = new File("resources/audio/" + name);
		
		Media media = new Media(file.toURI().toString());
		
		mediaPlayer = new MediaPlayer(media);
		
		mediaPlayer.setVolume(volume / 10);
		mediaPlayer.play();
	}
	
	private void playMusic() {
		File file = new File("resources/audio/" + nameMusic);
		
		Media media = new Media(file.toURI().toString());
		
		musicPlayer = new MediaPlayer(media);
		
		musicPlayer.setVolume(1.5 / 10);
		musicPlayer.play();
	}
	
	private void stopMusic() {
		
		musicPlayer.stop();
		
	}
}
