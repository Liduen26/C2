package timeline;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class Controller {
	private MainTimeline mainApp;
	
    @FXML
    private Rectangle square;
    @FXML
    private Label fps;
    
    // Vars ---------------
    private boolean run;    

	public void setMainTimeline(MainTimeline main) {
		mainApp = main;
	}

	
	// Let's Code --------------------------------------------------------
	public void initialize() {
		long oldTime;
		long nowTime = System.nanoTime();
		long dt;
		
		run = true;
		while (run == true) {
			oldTime = nowTime;
			nowTime = System.nanoTime();
			dt = nowTime - oldTime;
			
			System.out.println(dt);
		}
		
	}
	
}