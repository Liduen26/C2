package timeline;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainTimeline extends Application {
	
	Stage primaryStage;
	private AnchorPane mainLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TestTimeline");
		
		// Initialisation et ouverture de la fenètre
		initMainLayout();
		
		
	}
	
	public void initMainLayout() {
		try {
			// Chargement du layout principal
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainTimeline.class.getResource("GameTest.fxml"));
			mainLayout = (AnchorPane) loader.load();
			
			// Affichage de la scène contenant le layout précédemment chargé
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			
			// Mise en relation avec le controller
			Controller controller = loader.getController();
			controller.setMainTimeline(this);
			
			primaryStage.show();
			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if(event.getCode() == KeyCode.SPACE) {
						System.out.println("space");
						controller.jump();
					} else if(event.getCode() == KeyCode.Z) {
							System.out.println("Z");
						
					}
				};
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
