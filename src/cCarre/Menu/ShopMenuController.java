package cCarre.Menu;

import java.io.IOException;

import cCarre.AffichageMap.model.Player;
import cCarre.AffichageMap.view.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ShopMenuController {
	
	 Color color = Color.BLUE;
	
	@FXML public Button GoToBaseMenu;
	public void GoToBaseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("BaseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}

	public void ChangeColorToBlue(ActionEvent event) throws IOException{
		System.out.println("Blue");
		Color color = Color.BLUE;
	}
	
	public void ChangeColorToRed(ActionEvent event) throws IOException{
		System.out.println("Red");
		Color color = Color.RED;
	}
	
	public void ChangeColorToGreen(ActionEvent event) throws IOException{
		System.out.println("Green");
		Color color = Color.GREEN;
	}
	
	public void ChangeColorToYellow(ActionEvent event) throws IOException{
		System.out.println("Yellow");
		Color color = Color.YELLOW;
	}

	public Color Getcolor() {		
		return color;
	}
	
}

