package cCarre.Menu;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ShopMenuController {
	
	 Color color = Color.BLUE;
	 MediaPlayer mediaPlayer;
	
	@FXML public Button GoToBaseMenu;
	public void GoToBaseMenu(ActionEvent event) throws IOException {
		playSound("Click_Menus.wav");
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("BaseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}

	public void ChangeColorToBlue(ActionEvent event) throws IOException{
		playSound("Buy.wav");
		System.out.println("Blue");
		Color color = Color.BLUE;
	}
	
	public void ChangeColorToRed(ActionEvent event) throws IOException{
		playSound("Buy.wav");
		System.out.println("Red");
		Color color = Color.RED;
	}
	
	public void ChangeColorToGreen(ActionEvent event) throws IOException{
		playSound("Buy.wav");
		System.out.println("Green");
		Color color = Color.GREEN;
	}
	
	public void ChangeColorToYellow(ActionEvent event) throws IOException{
		playSound("Buy.wav");
		System.out.println("Yellow");
		Color color = Color.YELLOW;
	}

	public Color Getcolor() {		
		return color;
	}
	
	/**
	 * Fais jouer un son se trouvant dans le dossier resources/audio/ 
	 * @param name Le nom du fichier (avec l'extension)
	 * @param volume Le volume de 0 à 10
	 */
	private void playSound(String name) {
		File file = new File("resources/audio/" + name);
		
		Media media = new Media(file.toURI().toString());
		
		mediaPlayer = new MediaPlayer(media);
		
		mediaPlayer.setVolume(7.0 / 10);
		mediaPlayer.play();
	}
}

