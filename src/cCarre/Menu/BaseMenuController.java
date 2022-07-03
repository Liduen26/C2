package cCarre.Menu;



import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.Scanner;


public class BaseMenuController{

@FXML public Button GoToGameMenu;
@FXML private ImageView imageView;

//Image img = new Image(getClass().getResource("@../../images/logoc².png").toExternalForm());


	public void GoToGameMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("GameMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.setHeight(1080);
		window.setWidth(1920);
		window.show();
		System.out.print("test1");
	}
	
	
	public void GoToShopMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("ShopMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
	
	public void GoToOptionsMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("OptionsMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
	
	public void GoToPauseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("PauseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
	
	public void GoToVoid(ActionEvent event) throws IOException {
		System.exit(0);
	}

/*    private class KeyHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent e) {
            KeyCode keyPressed = e.getCode();

            switch (keyPressed) {

            case W:
                System.out.print("W");
                break;
            case A:
            	System.out.print("A");		
                break;
            case S:
            	System.out.print("S");
                break;
            case D:
            	System.out.print("D");
                break;
            default:
                break;
            }
        }*/
	
/*	String key;
	Scanner input = new Scanner(System.in);
    for(int x = 0; x < Integer.MAX_VALUE; x++)
    {
        key = input.next();
        if(key.equals("w"))
        {
        	System.out.print("W");
        }
    }
}
*/

}




	

