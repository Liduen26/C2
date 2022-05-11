package cCarre.genmap.view;

import cCarre.genmap.MainGen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GenController {
	private MainGen mainGen;
	
	@FXML
    private AnchorPane root;
	
	private boolean drag;
	Timeline timel;
	
	public void setMainGen(MainGen mainGen) {
		this.mainGen = mainGen;
	}
	
	@FXML
	private void initialize() {
		System.out.println("init");
		
		int widthCell = 40;
		
		double rWidth = 1920 / widthCell;
		double rHeight = 1000 / widthCell;
		System.out.println(rHeight);
		
		GridPane grille = new GridPane();
		grille.setHgap(widthCell);
		grille.setVgap(widthCell);
		grille.setGridLinesVisible(true);
		
		root.getChildren().add(grille);
		
		for(int y = 0; y < rHeight; y++) {
			for(int x = 0; x < rWidth; x++) {
				Rectangle r = new Rectangle();
				grille.add(r, x, y);
			}
		}
		
		
		// Event qui attendent le drag de la fenètre
		grille.setOnMousePressed(e -> {
			handleDrag(e);
		});
		grille.setOnMouseReleased(e -> {
			drag = false;
			drag();
		});
		
		
	}
	
	private void handleDrag(MouseEvent e) {
		if(e.getButton() == MouseButton.MIDDLE) {
			drag = true;
			drag();
		} 
	}
	
	public void drag() {
		System.out.println("heyy");
		timel = new Timeline(new KeyFrame(Duration.millis(1000 / 142), e -> {
			if(drag == true) {
				System.out.println("ui");
				
			} 
		}));
		timel.setCycleCount(Timeline.INDEFINITE);
		timel.play();
		
	}
	public void dragOff() {
		drag = false;
	}
}
