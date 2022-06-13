package cCarre.Menu;
import java.io.IOException;

import cCarre.AffichageMap.data.LevelData;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.view.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameMenuController {
	@FXML 
	public Button GoToBaseMenu;

	public void GoToBaseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("BaseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
	
	public void LaunchGame(ActionEvent event) throws IOException {
		// Définis la map à utiliser, attend un JSONArray
		Level.setJsonLevel(LevelData.getLevelInJSON(LevelData.LEVEL1));
		
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainMenu.class.getResource("../AffichageMap/view/mainLayout.fxml"));
		Pane BaseMenu = (Pane) loader.load();
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());

		// Show the scene containing the root layout.
        Scene scene = new Scene(BaseMenu);
        window.setScene(scene);
        
        MainController controller = loader.getController();
       
        // Chemin du fichier json (à faire)
        
		window.setMaximized(true);
		window.show();
		
		scene.setOnKeyPressed(e ->{
			controller.jump();
		});
	}
	public void GoToEditLevel(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("../genmap/view/genLayout.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
}