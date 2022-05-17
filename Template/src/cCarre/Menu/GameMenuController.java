package cCarre.Menu;



import java.io.IOException;

import cCarre.AffichageMap.view.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class GameMenuController {
@FXML public Button GoToBaseMenu;

	public void GoToBaseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("BaseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.show();
	}
	
	public void LaunchGame(ActionEvent event) throws IOException {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainMenu.class.getResource("../AffichageMap/view/mainLayout.fxml"));
		Pane BaseMenu = (Pane) loader.load();
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());

		// Show the scene containing the root layout.
        Scene scene = new Scene(BaseMenu);
        window.setScene(scene);
        
        MainController controller = loader.getController();
        controller.setMainApp(this);
        
		window.setMaximized(true);
		window.setHeight(1080);
		window.setWidth(1920);
		
		window.show();
		
		scene.setOnKeyPressed(e ->{
			controller.jump();
		});
	}
}