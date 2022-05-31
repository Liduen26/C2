package cCarre.genmap.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.eventbus.Subscribe;

import cCarre.genmap.MainGen;
import cCarre.genmap.events.AddLengthGrilleEvent;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.RemoveLengthGrilleEvent;
import cCarre.genmap.model.Cell;
import cCarre.genmap.model.ToolBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
	
	// Vars --------------------------
    final int widthCell = 60;
    private GridPane grille;
	private double oldX;
	private double newX;
	private ArrayList<Cell> cellList = new ArrayList<Cell>();

	FileChooser fileChooser = new FileChooser();
	
	public void setMainGen(MainGen mainGen) {
		this.mainGen = mainGen;
	}
	
	@FXML
	private void initialize() {
		// Se place dans disque D quand on save
        fileChooser.setInitialDirectory(new File("D:\\"));
        
		// Init de la grille (fait ï¿½ la va-vite, faudra amï¿½liorer toute cette merde ;D) -------
		double rWidth = 1920 / widthCell;
		double rHeight = 1000 / widthCell;
		
		grille = new GridPane();
		grille.setHgap(1);
		grille.setVgap(1);
		grille.setGridLinesVisible(true);
		
		// Remplissage de la grille
		for(int y = 0; y < rHeight; y++) {
			for(int x = 0; x < rWidth -2; x++) {
				Cell cell = new Cell(widthCell, x, y);
				cellList.add(cell);
				grille.add(cell, x, y);
			}
		}
		root.getChildren().add(grille);
		
		// Event qui attendent le drag de la fenètre ----------------------------------------------
		grille.setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.MIDDLE) {
				e.setDragDetect(true);
				newX = e.getSceneX();
			}
		});
		grille.setOnMouseDragged(e -> {
			// Si on drag avec le clic molette .... ->
			if(e.getButton() == MouseButton.MIDDLE) {
				double mouseX = e.getSceneX();
				double delta = 0;
							
				oldX = newX;
				newX = mouseX;
				delta = newX - oldX;
				
//				System.out.println("old : " + oldX + " / new : " + newX + " / delta : " + delta);
//				System.out.println(-mostRight +" / " + grille.getLayoutX());
//				System.out.println(grille.getLayoutX());
				
				// Déplace uniqument si c'est pas < à 0
				if((grille.getLayoutX() + delta) < 0 && (grille.getLayoutX() + delta) > -((widthCell + 1) * ToolBar.getMostX()) + (widthCell / 2)) {
					grille.setLayoutX(grille.getLayoutX() + delta);
				}
			}
		});
		
		// Permet a cette classe de s'abonner à des events 
		Ebus.get().register(this);
		
		
		// Tracking des btns de la toolBar --------------------------------------------------------
		for(Node btn : toolBar.getChildren()) {
			btn.setOnMouseClicked(e -> {
				final Node btnAct = (Node) e.getSource();
				String id = btnAct.getId();
				
				ToolBar.setItem(id);
				ToolBar.getItem();
			});
		}
	}
	
	@FXML
    private void handleSaving(ActionEvent event) {
		
    }
	
	
	
	
	// Grile dynamique, PAS TOUCHER !!!! ----------------------------------------------------------------------------------------
	
	// Ecoute le bus d'évent pour savoir si la taille de la grille doit changer -------------------
	@Subscribe
	private void handleAddLenght(AddLengthGrilleEvent e) {
		int deltaX = e.getX() - ToolBar.getMostX();
		ToolBar.setMostX(e.getX());
		
		// Crée des colonne de grille, autant de fois que le delta nouveau-ancien
		int nCol = 0;
		int nRow = 0;

		for(int j = 0; j < deltaX; j++) {
			// Regarde le num de col et de ligne de la dernière cellule
			Node cell = grille.getChildren().get(grille.getChildren().size() - 1);
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			
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
	
	@Subscribe
	private void handleRemoveLenght(RemoveLengthGrilleEvent e) {
		int x = 0;
		// Regarde toutes les cases pour définir quelle colonne est la dernièreq
		for(Node cell : grille.getChildren()) {
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			
			// Si y a pas que le background, alors on change le mostX
			if(c.getChildrenUnmodifiable().size() > 1) {
				x = (c.getX() > x) ? c.getX() : x;
			}
		}
		int deltaX = x - ToolBar.getMostX();
		ToolBar.setMostX(x);
		
		// Décale la cam si y a plus assez de cases peintes dans le champ
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
	
	
	// -------------------------- PARTIE DEDIEE A LA SAVE ----------------------------------------------
	public void GoToSave(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Save.fxml"));
		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("My Window");
		stage.show();
		
		SaveController sc = loader.getController();
		sc.setData(getCustomMap());
		/*
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Save.fxml"));
		Parent tableViewParent = (Parent)loader.load();
		SaveController sc = loader.getController();
		sc.setData(getCustomMap());
		
		Scene tableViewScene = new Scene(tableViewParent);
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setHeight(500);
		window.setWidth(600);
		window.show();
		*/
	}
	
	private int[][] getCustomMap() {
		// Obtention du nbr de colonnes et lignes
		Node cells = grille.getChildren().get(grille.getChildren().size() - 1);
		Cell c1 = new Cell(cells);
		if(cells instanceof Cell) {
			c1 = (Cell) cells;
		}
		int nCol = c1.getX();
		int nRow = c1.getY();
		
		//Création du tableau 2D
		int[][] cellTab = new int[nRow+1][nCol+1];
		System.out.println(" MAX : "+nCol+", "+nRow);
		
		// remplit le tableau 2D
		for(Node cell : grille.getChildren()) {
			Cell c = new Cell(cell);
			if(cell instanceof Cell) {
				c = (Cell) cell;
			}
			System.out.println(c.getX()+", "+c.getY());
			cellTab[c.getY()][c.getX()] = c.getCellId();
		}
		
		return cellTab;
	}
}
	