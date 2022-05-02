package cCarre.AffichageMap.view;

import java.util.ArrayList;

import cCarre.AffichageMap.Main;
import cCarre.AffichageMap.model.Coin;
import cCarre.AffichageMap.model.Ground;
import cCarre.AffichageMap.model.Level;
import cCarre.AffichageMap.model.Obstacle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


public class MainController {
	
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> triangles = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();
    
    final int elementSize = 60;

	private Main mainApp;
	
	@FXML
	private AnchorPane rootLayout;
	
	@FXML
	private void initialize() {
		Level level = new Level();
		int levelLength = level.getLevelLength();
		int levelHeight = level.getLevelHeight();
		char[][] Level = level.getLevel();
		
		
		for(int y = 0; y < levelHeight; y++) {
			for(int x= 0; x < levelLength; x++) {
				
				switch(Level[y][x]) {
					case '0' :
						// ici c'est vide
						break;
					case '1' :
						Ground platform = new Ground(x*elementSize, y*elementSize, elementSize, elementSize, Color.GREEN, rootLayout);
                        platforms.add(platform);
						break;
					case '2' :
                        Obstacle triangle = new Obstacle(x*elementSize, y*elementSize, elementSize, elementSize, Color.RED, rootLayout);
                        triangles.add(triangle);
						break;
					case '3' :
						Coin coin = new Coin(x*elementSize, y*elementSize, elementSize, elementSize, Color.YELLOW, rootLayout);
						coins.add(coin);
						break;
				}
			}
		}
        Ground player = new Ground(5, 600, elementSize, elementSize, Color.BLUE, rootLayout);
	}
	
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

}
