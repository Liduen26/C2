package cCarre.genmap.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
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
    
    void load2() throws FileNotFoundException, IOException, ParseException {
    	JSONParser parser = new JSONParser();
    	JSONArray jsonMap;
    	
    	Object obj =(new FileReader("D:\\pop.json"));
    	
    	
    	/*
        JSONObject jsonObject = (JSONObject) obj;

        System.out.println(jsonObject.get(0));
        
        JSONArray solutions = (JSONArray) jsonObject.get(0);

        Iterator iterator = solutions.iterator();
        System.out.println("Premiere ligne : ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next());
        }
        */
    }
    void load3() {
    	/*
	private static JSONArray jsonMap;
	
		this.idLevel = 0;
		this.levelLength = jsonMap.getJSONArray(0).length();
		this.levelWidth = this.levelLength * 60;
		this.totalCoin = 0;
		this.levelHeight = jsonMap.length();
		this.LevelMap = new char[this.levelHeight][this.levelLength];
		
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelLength; j++) {
            	LevelMap[i][j] = (char) jsonMap.getJSONArray(i).get(j);
            }
        }
        */
    }

    @FXML
    void save(MouseEvent event) {
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null){
            saveSystem2(file, contenu);
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
    
    public void saveSystem2(File file, String content){
        try {
        	PrintWriter printWriter = new PrintWriter(file);
        	
        	JSONArray tab2D = new JSONArray();
        	tab2D.put(tabLevel);
        	content += tab2D.toString();
 
            //JSONArray element = (JSONArray) tab2D.getJSONArray(0).get(0); // Get all JSONArray data
            
            tab2D.write(printWriter);
            System.out.println("Bonjour");
            //printWriter.write(content);
            
            printWriter.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
	public void setData(int[][] customMap) {
		// Récupère la map
		tabLevel = customMap;
		// Lance la save
		save(null);
	}
}