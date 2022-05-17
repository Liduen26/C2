package cCarre.Menu;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class Main extends Application {

	private Stage primaryStage;
	private Pane BaseMenu;
	// ... AFTER THE OTHER VARIABLES ...

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("C�");

		// Set the application icon.
		this.primaryStage.getIcons().add(new Image("file:///C:/Users/amaur/eclipse-workspace/c/c�/c�/ressources/images/clogo.png"));

		initBaseMenu();
	}

public void initBaseMenu() {
	try {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/cCarre/Menu/BaseMenu.fxml"));
		BaseMenu = (Pane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(BaseMenu);
			primaryStage.setScene(scene);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}