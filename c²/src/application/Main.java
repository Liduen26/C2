package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import test.adressApp.adress.view.RootLayoutController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
//        this.primaryStage.getIcons().add(new Image("file:ressources/images/21254.png"));

		initBaseMenu();
	}

public void initBaseMenu() {
	try {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/application/BaseMenu.fxml"));
		BaseMenu = (Pane) loader.load();

		// Show the scene containing the root layout.
		Scene scene = new Scene(BaseMenu);
		primaryStage.setScene(scene);

		primaryStage.show();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
}