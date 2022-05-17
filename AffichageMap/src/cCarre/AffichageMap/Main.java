package cCarre.AffichageMap;
	
import javafx.event.EventHandler;
import java.io.IOException;

import cCarre.AffichageMap.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	private Stage primaryStage;
	private AnchorPane mainLayout;
	
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AffichageMap");
        this.primaryStage.setMaximized(true);

        initMainLayout();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initMainLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/mainLayout.fxml"));
            mainLayout = (AnchorPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            
            MainController controller = loader.getController();
            controller.setMainApp(this);

            
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.SPACE) {
                        System.out.println("space");
                        controller.jump();
                    } else if(event.getCode() == KeyCode.Z) {
                        System.out.println("z");
                    }
                };
            });

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
