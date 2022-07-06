package cCarre.AffichageMap.view;

import java.io.IOException;

import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.RestartGameEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PauseMenuController {
	
	@FXML public Button GoToGameMenu;

	public void GoToBaseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("../../Menu/GameMenu2.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
	@FXML
	public void GoToGameAgain(ActionEvent event) throws IOException {
		Ebus.get().post(new RestartGameEvent());
		System.out.println("reprendre le jeu");
	}
}
