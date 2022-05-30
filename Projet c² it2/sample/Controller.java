package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    FileChooser fileChooser = new FileChooser();

    @FXML
    private TextArea textArea;
    
    //Added null check to check rather a file is picked or not
    @FXML
    void getText(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null){
            try {
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()){
                    textArea.appendText(scanner.nextLine() + "\n");
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
            saveSystem(file, textArea.getText());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("D:\\"));
    }

    public void saveSystem(File file, String content){
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(content);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}