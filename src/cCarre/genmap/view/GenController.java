package cCarre.genmap.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;

import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.view.MainController;
import cCarre.Menu.MainMenu;
import cCarre.genmap.MainGen;
import cCarre.genmap.events.AddLengthGrilleEvent;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.MoveGridEvent;
import cCarre.genmap.events.PopupEvent;
import cCarre.genmap.events.RemoveLengthGrilleEvent;
import cCarre.genmap.model.Cell;
import cCarre.genmap.model.ToolBar;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GenController {
	@SuppressWarnings("unused")
	private MainGen mainGen;
	
	// Blocs -------------------------
	@FXML
    private AnchorPane root;
	
	@FXML
    private HBox toolBar;

    @FXML
    private Button groundBtn;

    @FXML
    private Button obstacleBtn;
    
    @FXML
    private Button test;
    
    @FXML
    private HBox saveBar;
	
	// Vars --------------------------
    final int widthCell = (60 - 1);
    private GridPane grille;
	private double oldX;
	private double newX;
	private Rectangle2D screenBounds;
	private double playerSpeed = 0;
	double initialPtX = 0;
	double initialPtY = 0;
	
	Rectangle select;

	FileChooser fileChooser = new FileChooser();
	
	public void setMainGen(MainGen mainGen) {
		this.mainGen = mainGen;
	}
	
	@FXML
	private void initialize() {
		// Init de la grille ----------------------------------------------------------------------
		screenBounds = Screen.getPrimary().getBounds();

		// Se place dans disque D quand on save
//        fileChooser.setInitialDirectory(new File("C:\\"));
		
		double rWidth = screenBounds.getWidth() / widthCell;
		double rHeight = screenBounds.getHeight() / widthCell;
		
		grille = new GridPane();
		grille.setHgap(1);
		grille.setVgap(1);
		grille.setGridLinesVisible(true);
		
		// Remplissage de la grille
		for(int y = 0; y < rHeight; y++) {
			for(int x = 0; x < rWidth -2; x++) {
				Cell cell = new Cell(widthCell, x, y);
				
				grille.add(cell, x, y);
			}
		}
		root.getChildren().add(grille);
		
		// Gï¿½re le depl de la grille ac le clic molette
		handleMouseEvents();
		
		// Permet a cette classe de s'abonner ï¿½ des events 
		Ebus.get().register(this);
		
		
		// Tracking des btns de la toolBar --------------------------------------------------------
		for(Node btn : toolBar.getChildren()) {
			btn.setOnMouseClicked(e -> {
				final Node btnAct = (Node) e.getSource();
				String id = btnAct.getId();
				
				ToolBar.setItem(id);
			});
		}
		
		select = new Rectangle();
		select.setFill(Color.RED);
		select.setOpacity(0.2);
		select.setFocusTraversable(true);
	}
	
	// QuickTest ----------------------------------------------------------------------------------
	@FXML
    void handleTest(ActionEvent event) throws IOException {
		boolean go = true;
		ToolBar.setItem("test");
		
		
		if(go) {
			// Charge la map
			JSONArray mapGen = new JSONArray();
			
			System.out.println(mapGen);
			
			// Dï¿½sactive tout les btns de la toolbar et change le retour<
			toolBar.setDisable(true);
			test.setDisable(true);
			saveBar.setDisable(true);
			
			// Met le focus sur l'anchorPane pour ne pas appuyer sur un btn, et pour permettre l'event keyPressed du saut
			root.requestFocus();
			
			// Dï¿½finis la map ï¿½ utiliser, attend un JSONArray
			Level.setJsonLevel(mapGen);
			
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainMenu.class.getResource("../AffichageMap/view/mainLayout.fxml"));
			AnchorPane game = (AnchorPane) loader.load();
			
			// met le jeu par dessus la grille
			root.getChildren().add(game);
			
			MainController controller = loader.getController();
			playerSpeed = controller.getSpeedPlayer();
			controller.setEdit(true);
			
			root.setOnKeyPressed(e ->{
				controller.jump();
			});
			// /!\ Penser ï¿½ remove l'event sur le btn return /!\	
		}
    }
	
	@Subscribe
	private void gridGameMoving(MoveGridEvent e) {
		grille.setLayoutX(e.getX());
		System.out.println(grille.getLayoutX()+"\n");
	}
	
	
	
	// Grile dynamique, PAS TOUCHER !!!! ----------------------------------------------------------------------------------------
	
	/**
	 * Dï¿½placement de la grille avec le clic molette
	 */
	private void handleMouseEvents() {
		// Event qui attendent le drag de la fenï¿½tre ----------------------------------------------
		grille.setOnMousePressed(e -> {
			// Dï¿½but / init
			if(e.getButton() == MouseButton.MIDDLE) {
				// Dï¿½placement de la grille
				e.setDragDetect(true);
				newX = e.getSceneX();
				
			} else if(e.getButton() == MouseButton.PRIMARY && ToolBar.getItem().equals("select")) {
				Cell c = null;
				System.out.println(e.getTarget());
				if(e.getTarget() instanceof Cell) {
					c = (Cell) e.getTarget();
					System.out.println(c.isSelected());
					
					if(c.isSelected()) {
						// Depl de la selection
						
					} 
				} else {
					// Zone de sï¿½lection
					
					this.unselect();
					select.setLayoutX(e.getX());
					select.setLayoutY(e.getY());
					select.setWidth(0);
					select.setHeight(0);
					initialPtX = e.getX() + grille.getLayoutX();
					initialPtY = e.getY();
					
					root.getChildren().add(select);
				}
			}
		});
		
		grille.setOnMouseDragged(e -> {
			// Dï¿½placement de la souris
			if(e.getButton() == MouseButton.MIDDLE) {
				// Si on drag avec le clic molette .... ->
				double mouseX = e.getSceneX();
				double delta = 0;
				
				oldX = newX;
				newX = mouseX;
				delta = newX - oldX;
				
//				System.out.println("old : " + oldX + " / new : " + newX + " / delta : " + delta);
//				System.out.println(-mostRight +" / " + grille.getLayoutX());
//				System.out.println(grille.getLayoutX());
				
				// Dï¿½place uniqument si c'est pas < ï¿½ 0
				if((grille.getLayoutX() + delta) < 0 && (grille.getLayoutX() + delta) > -((widthCell + 1) * ToolBar.getMostX()) + (widthCell / 2)) {
					grille.setLayoutX(grille.getLayoutX() + delta);
				}
				
			} else if(e.getButton() == MouseButton.PRIMARY && ToolBar.getItem().equals("select")) {
				Cell c = new Cell((Node) e.getTarget());
				if(e.getTarget() instanceof Cell) {
					c = (Cell) e.getTarget();
				}
				
				if(c.isSelected()) {
					// Depl de la selï¿½ction
//					System.out.println(c.isSelected());
					
				} else {
					// Zone de sï¿½lection
					switch (ToolBar.getItem()) {
					case "select":
						double deltaX = (e.getX() + grille.getLayoutX()) - initialPtX;
					    double deltaY = e.getY() - initialPtY;

					    if(deltaX < 0) {
					        select.setLayoutX(e.getX() + grille.getLayoutX());
					        select.setWidth(-deltaX);
					    } else {
					        select.setLayoutX(initialPtX);
					        select.setWidth((e.getX() + grille.getLayoutX()) - initialPtX);
					    }

					    if(deltaY < 0) {
					        select.setLayoutY( e.getY());
					        select.setHeight(-deltaY);
					    } else {
					        select.setLayoutY(initialPtY);
					        select.setHeight(e.getY() - initialPtY);
					    }
						
						break;
					}
				}
			}
		});
		
		grille.setOnMouseReleased(e -> {
			// Relachement du clic
			if(e.getButton() == MouseButton.PRIMARY && ToolBar.getItem().equals("select")) {
				// Regarde toutes les cases 
				for(Node cell : grille.getChildren()) {
					Cell c = new Cell(cell);
					if(cell instanceof Cell) {
						c = (Cell) cell;
					}
					
					// Cherche les cellules dans la zone de selection
					if(((c.getX()+1) * widthCell) > select.getLayoutX() && (c.getX() * widthCell) < (select.getLayoutX() + select.getWidth()) 
					&& ((c.getY()+1) * widthCell) > select.getLayoutY() && (c.getY() * widthCell) < (select.getLayoutY() + select.getHeight())) {
						// Si y a pas que le background, alors 
						if(c.getChildrenUnmodifiable().size() > 2) {
							c.setSelected(true);
						}							
					}
				}
				
				root.getChildren().remove(select);
			}
		});
		
	}
	
	/**
	 * Dï¿½selectionne toutes les cases de la grille
	 */
	private void unselect() {
		for(Node cell : grille.getChildren()) {
			Cell c;
			if(cell instanceof Cell) {
				c = (Cell) cell;
			} else {
				c = new Cell(cell);
			}
			
			if(c.isSelected()) {
				c.setSelected(false);
 			}
		}
	}

	
	
	// Ecoute le bus d'ï¿½vent pour savoir si la taille de la grille doit changer -------------------
	/**
	 * Gï¿½re l'ajout de colonnes ï¿½ la grille, se dï¿½clenche via l'event bus
	 * @param e l'event auquel il est abonnï¿½
	 */
	@Subscribe
	private void handleAddLenght(AddLengthGrilleEvent e) {
		int deltaX = e.getX() - ToolBar.getMostX();
		ToolBar.setMostX(e.getX());
		
		// Crï¿½e des colonne de grille, autant de fois que le delta nouveau-ancien
		int nCol = 0;
		int nRow = 0;

		for(int j = 0; j < deltaX; j++) {
			// Regarde le num de col et de ligne de la derniï¿½re cellule
			Cell c = (Cell) grille.getChildren().get(grille.getChildren().size() - 1);
			
			nCol = c.getX();
			nRow = c.getY();
//			System.out.println(nCol + " / " + nRow);
			
			// Ajout de colonnes --------------------------------------------------
			for(int i = 0; i <= nRow ;i++) {
				Cell cells = new Cell(widthCell, nCol + 1, i);
				grille.addColumn(nCol + 1, cells);			
			}
		}
	}
	
	/**
	 * Gï¿½re la suppression de colonnes ï¿½ la grille, se dï¿½lcnche via l'event bus
	 * @param e l'event auquel il est abonnï¿½
	 */
	@Subscribe
	private void handleRemoveLenght(RemoveLengthGrilleEvent e) {
		int x = 0;
		// Regarde toutes les cases pour dï¿½finir quelle colonne est la derniï¿½req
		for(Node cell : grille.getChildren()) {
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			
			// Si y a pas que le background, alors on change le mostX
			if(c.getChildrenUnmodifiable().size() > 2) {
				x = (c.getX() > x) ? c.getX() : x;
			}
		}
		int deltaX = x - ToolBar.getMostX();
		ToolBar.setMostX(x);
		
		// Dï¿½cale la cam si y a plus assez de cases peintes dans le champ
		if(grille.getLayoutX() < -((widthCell + 1) * ToolBar.getMostX()) + (widthCell / 2)) {
			double x2 = -((widthCell + 1) * ToolBar.getMostX()) + (widthCell / 2);
			x2 = (x2 >= 0) ? 0 : x2;

			grille.setLayoutX(x2);
		}
		
		// Obtention du nbr de colonnes et lignes
		Node cells = grille.getChildren().get(grille.getChildren().size() - 1);
		Cell c1 = new Cell(cells);
		if(cells instanceof Cell) {
			c1 = (Cell) cells;
		}
		int nCol = c1.getX();
		int nRow = c1.getY();

		// Supprime une colones de grille
		ArrayList<Cell> toRem = new ArrayList<Cell>();
		
		for(Node cell : grille.getChildren()) {
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			
			// suppr via les y
			if(c.getX() >= nCol + deltaX + 1) {
				toRem.add(c);
			}
		}
		for(Cell rem : toRem) {
			grille.getChildren().remove(rem);
		}
	}
	
	// btn Retour ---------------------------------------------------------------------------------
	public void GoToBaseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("../../Menu/BaseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setHeight(500);
		window.setWidth(600);
		window.show();
	}
	
	@Subscribe
	public void myPopup(PopupEvent e) {
		// Tailles max
		int width = 400;
		int height = 200;
		
		// Crï¿½ation de la vBox, et set de set propriï¿½tï¿½s et css
		VBox popup = new VBox();
		popup.setPrefWidth(width);
		popup.setMaxHeight(height);
		popup.setLayoutX((screenBounds.getWidth() / 2) - (popup.getPrefWidth()/ 2));
		popup.setLayoutY((screenBounds.getHeight() / 4) * 3 - 50);
		popup.setAlignment(Pos.CENTER);
		popup.setStyle("-fx-opacity: 0.8; -fx-padding: 20px; -fx-background-color: white; -fx-border-color: red; -fx-border-radius: 20px;");
		
		// Labels de titre et de contenu
		Label title = new Label();
		title.setText(e.getTitle());
		
		Label text = new Label();
		text.setText(e.getText());
		
		// implï¿½mentation des labels ï¿½ la popup, et elle-mï¿½me au root
		popup.getChildren().addAll(title, text);
		root.getChildren().add(popup);
		
		// Pause de 2s, puis fait disparaitre la popup
		PauseTransition delay = new PauseTransition(Duration.seconds(2));
		delay.setOnFinished( event -> root.getChildren().remove(popup));
		delay.play();
	}
	
	
	// -------------------------- PARTIE DEDIEE A LA SAVE ----------------------------------------------
	public void GoToSave(ActionEvent event) throws IOException, ParseException {
		// Load (mais n'affiche pas) la page fxml dï¿½diï¿½e ï¿½ la save pour ensuite envoyer la map ï¿½ la classe SaveController
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Save.fxml"));
		Parent root = (Parent) loader.load();
		
		// Envoie la map ( chargï¿½e par getCustomMap() ) ï¿½ la classe SaveController ï¿½ travers la fonction setData() dans SaveController qui rï¿½cupï¿½re la map et lance la save()
		SaveController sc = loader.getController();
		sc.setData(getCustomMap());
	}

    public void loadMap() throws FileNotFoundException, IOException, ParseException {
    	JSONParser parser = new JSONParser();

    	// Demande ï¿½ l'utilisateur de choisir un fichier
        File file = fileChooser.showOpenDialog(new Stage());
    	FileReader reader = new FileReader(file);

    	JSONArray element1 = (JSONArray) parser.parse(reader); // parse
        JSONArray element2 = (JSONArray) element1.get(0); // element1 recupere la map        
        
        // Créer un tableau 2D pour exploiter la map choisie
    	char[][] tabMap = new char[element1.size()][element2.size()];
    	
    	// Remplit le tableau 2D
        for (int i = 0; i < element1.size(); ++i) {
            element2 = (JSONArray) element1.get(i); // passe ï¿½ la prochaine ligne
            for(int j = 0; j < element2.size(); ++j) {
                System.out.print(element2.get(j));
                
                int iO = ((Long) element2.get(j)).intValue();
                char cO = (char) (iO + '0');
                tabMap[i][j] = cO;
                //tabMap[i][j] = (char) ((Long) element2.get(j)).intValue();
            }
            System.out.println(""); // saute une ligne
        }
        
		// Remplit la grille avec la map chargée
		for(Node cell : grille.getChildren()) {
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			if(c.getY() == element1.size()-1 && c.getX() == element2.size()-1){
				System.out.println("STOOOOOOOOOOOOOOOPPPPPPPPPPPPP");
				break;
			}
			c.setCellId(tabMap[c.getY()][c.getX()]);
			c.loadMapPaint();
		}
    }
	
	private char[][] getCustomMap() {
		// Obtention du nbr de colonnes et lignes
		Node cells = grille.getChildren().get(grille.getChildren().size() - 1);
		Cell c1 = new Cell(cells);
		if(cells instanceof Cell) {
			c1 = (Cell) cells;
		}
		int nCol = c1.getX();
		int nRow = c1.getY();
		
		//Crï¿½ation du tableau 2D
		char[][] cellTab = new char[nRow+1][nCol+1];
//		System.out.println(" MAX : "+nCol+", "+nRow);
		
		// remplit le tableau 2D
		for(Node cell : grille.getChildren()) {
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			cellTab[c.getY()][c.getX()] = c.getCellId();
		}
		
		return cellTab;
	}
}