package cCarre.genmap.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.json.JSONArray;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SaveController implements Initializable {

    FileChooser fileChooser = new FileChooser();
    int[][] tabLevel;

    private String contenu = "";
        
    //Added null check to check rather a file is picked or not
    @FXML
    void getText(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null){
            try {
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()){
                	contenu+=(scanner.nextLine() + "\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void save(MouseEvent event) {
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null){
            saveSystem(file, contenu);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Json file (*.json", "*.json");
    	fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(new File("D:\\"));
    }

    public void saveSystem(File file, String content){
        try {
        	PrintWriter printWriter = new PrintWriter(file);
        	
        	JSONArray tab2D = new JSONArray();
        	tab2D.put(tabLevel);
        	content += tab2D.toString();
            
            printWriter.write(content);
            printWriter.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
	public void setData(int[][] customMap) {
		tabLevel = customMap;
	}
}