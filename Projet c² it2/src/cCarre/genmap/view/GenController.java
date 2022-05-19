package cCarre.genmap.view;

import java.io.IOException;

import com.google.common.eventbus.Subscribe;

import cCarre.genmap.MainGen;
import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.AddLengthGrilleEvent;
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
    private GridPane grille;
	private double oldX;
	private double newX;
	private double mostRight;
	
	public void setMainGen(MainGen mainGen) {
		this.mainGen = mainGen;
	}
	
	@FXML
	private void initialize() {
		// Init de la grille (fait ï¿½ la va-vite, faudra amï¿½liorer toute cette merde ;D) -------
		final int widthCell = 60;
		
		double rWidth = 1920 / widthCell;
		double rHeight = 1000 / widthCell;
		
		grille = new GridPane();
		grille.setHgap(1);
		grille.setVgap(1);
		grille.setGridLinesVisible(true);
		
		root.getChildren().add(grille);
		
		for(int y = 0; y < rHeight; y++) {
			for(int x = 0; x < rWidth -2; x++) {
				Cell cell = new Cell(widthCell, x, y);
				
				grille.add(cell, x, y);
			}
		}
		
		
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
				
				// Déplace uniqument si c'est pas < à 0
				if((grille.getLayoutX() + delta) < 0 && (grille.getLayoutX() + delta) > -mostRight) {
					grille.setLayoutX(grille.getLayoutX() + delta);
				}
			}
		});
		
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
	
	
	// Ecoute le bus d'évent pour savoir si la taille de la grille doit changer -------------------
	@Subscribe
	private void handleAddLenght(AddLengthGrilleEvent e) {
		System.out.println(e.getX());
	}
	
	@Subscribe
	private void handleRemoveLenght(RemoveLengthGrilleEvent e) {
		int x = 0;
		for(Node cell : grille.getChildren()) {
			Parent p = (Parent) cell;
			Cell c = (Cell) p;
			if(!c.getChildrenUnmodifiable().isEmpty()) {
				x = (c.getX() > x) ? c.getX() : x;
			}
		}
		
		System.out.println(x);
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
}