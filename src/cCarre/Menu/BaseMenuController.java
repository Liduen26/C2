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
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;



public class BaseMenuController {

@FXML public Button GoToGameMenu;
@FXML private ImageView imageView;

MediaPlayer mediaPlayer;

//Image img = new Image(getClass().getResource("@../../images/logoc�.png").toExternalForm());


	public void GoToGameMenu(ActionEvent event) throws IOException {
		playSound("Click_Menus.wav");
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("GameMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(tableViewScene);
		window.setFullScreen(true);
		window.setHeight(1080);
		window.setWidth(1920);
		window.show();

	}
	
	
	public void GoToShopMenu(ActionEvent event) throws IOException {
		playSound("Click_Menus.wav");
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("ShopMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setFullScreen(true);
		window.show();
	}
	
	public void GoToOptionsMenu(ActionEvent event) throws IOException {
		playSound("Click_Menus.wav");
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("OptionsMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setFullScreen(true);
		window.show();
	}
	
	public void GoToVoid(ActionEvent event) throws IOException {
		System.exit(0);
	}
	
	/**
	 * Fais jouer un son se trouvant dans le dossier resources/audio/
	 * @param name Le nom du fichier (avec l'extension)
	 * @param volume Le volume de 0 � 10
	 */
	private void playSound(String name) {
		File file = new File("resources/audio/" + name);
		
		Media media = new Media(file.toURI().toString());
		
		mediaPlayer = new MediaPlayer(media);
		
		mediaPlayer.setVolume(5.0 / 10);
		mediaPlayer.play();
	}
}
