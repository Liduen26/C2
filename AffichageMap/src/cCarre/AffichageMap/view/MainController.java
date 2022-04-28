package cCarre.AffichageMap.view;

import cCarre.AffichageMap.Main;
import cCarre.AffichageMap.model.Level;
import javafx.fxml.FXML;

public class MainController {
	
	private Main mainApp;
	
	@FXML
	private void initialize() {
		Level level = new Level();
		int levelLength = level.getLevelLength();
		int levelHeight = level.getLevelHeight();
		char[][] Level = level.getLevel();
		
		for(int y = 0; y < levelLength; y++) {
			for(int x= 0; x < levelHeight; x++) {
				
				switch(Level[x][y]) {
					case '0' :
						System.out.print("0");
						break;
					case '1' :
						System.out.print("1");
						break;
				}
				
			}
			System.out.println("");
		}
	}
	
	
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

}
