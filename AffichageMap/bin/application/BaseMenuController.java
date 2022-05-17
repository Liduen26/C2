package application;



import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class BaseMenuController {

@FXML public Button GoToGameMenu;

	public void GoToGameMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("GameMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.show();
	}
	public void GoToShopMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("ShopMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.show();
	}
	
	public void GoToOptionsMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("OptionsMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.show();
	}
	
	
}
