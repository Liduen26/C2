package cCarre.Menu;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class MainMenu extends Application {

	private Stage primaryStage;
	private Pane BaseMenu;
	@FXML private ImageView imageView;
	// ... AFTER THE OTHER VARIABLES ...
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("C²");
		
		// Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:///C:/Users/amaur/eclipse-workspace/c/c²/c²/ressources/images/clogo.png"));

		initBaseMenu();
	}

	public void initBaseMenu() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainMenu.class.getResource("/cCarre/Menu/BaseMenu.fxml"));
			BaseMenu = (Pane) loader.load();
	
			// Show the scene containing the root layout.
			File file = new File("../../images/logoc².png");
	        Image image = new Image(file.toURI().toString());
	        imageView = new ImageView(image);
	        
			Scene scene = new Scene(BaseMenu);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void mainv2(String[] args) {
		launch(args);
	}
}