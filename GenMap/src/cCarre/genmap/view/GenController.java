package cCarre.genmap.view;

import cCarre.genmap.MainGen;
import javafx.fxml.FXML;

public class GenController {
	private MainGen mainGen;
	
	@FXML
	private void initialize() {
		System.out.println("init");

	}
	
	public void setMainGen(MainGen mainGen) {
		this.mainGen = mainGen;
	}
}
