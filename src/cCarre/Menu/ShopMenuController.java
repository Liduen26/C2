package cCarre.Menu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ShopMenuController {
	
	int pieces;


	@FXML public Button GoToBaseMenu;
	public void GoToBaseMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("BaseMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		
		window.setScene(tableViewScene);
		window.setMaximized(true);
		window.show();
	}
		
	public void jsonColorChange (String color) {
		JSONObject colorwriter = new JSONObject();
		colorwriter.put("variable", color);
		
		try (FileWriter file = new FileWriter("./Color.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(colorwriter.toJSONString()); 
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void loadCoin() {
		
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader("pieces.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            
            // Cast JSON file
            JSONObject JsonCoin = (JSONObject) obj;
            
            pieces = ((Long) JsonCoin.get("nbrsCoin")).intValue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
	
	@SuppressWarnings("unchecked")
	public void saveCoin() {
		FileWriter file = null;
		JSONObject obj = new JSONObject();
		obj.put("nbrsCoin", new Integer(pieces));
		
		try {
			file =new FileWriter("./pieces.json");
			file.write(obj.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	@FXML
	private void initialize() {
		loadCoin();
	}
	
	private boolean canBuy() {
		if(pieces>=15) {
			pieces -=15;
			saveCoin();
			return true;
		}
		return false;
	}
	
	public void ChangeColorToBlue(ActionEvent event) throws IOException{
		if(canBuy()) {
			jsonColorChange("blue");
		}

	}
	
	public void ChangeColorToRed(ActionEvent event) throws IOException{
		if(canBuy()) {
			jsonColorChange("red");
		}
	}
	
	public void ChangeColorToGreen(ActionEvent event) throws IOException{
		if(canBuy()) {
			jsonColorChange("green");
		}
	}
	
	public void ChangeColorToYellow(ActionEvent event) throws IOException{
		if(canBuy()) {
			jsonColorChange("yellow");
		}
	}


}

