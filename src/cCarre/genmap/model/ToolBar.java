package cCarre.genmap.model;

import cCarre.genmap.events.Ebus;
import javafx.scene.paint.Color;
import cCarre.genmap.events.AddLengthGrilleEvent;

public final class ToolBar {
	private static String item;
	private static boolean test = false;
	private static boolean click;
	private static int mostX;
	private static int startPlaced = 0;
	private static int endPlaced = 0;
	private static Color groundColor = null;
	private static Color obstacleColor = null;
	private static Color coinColor = null;

	public ToolBar() {}
	
	// Paserelle d'infos --------------------------------------------------------------------------
	
	

	// Objet servant � r�cup�rer les �tat des btns ------------------------------------------------
	
	
	
	
	// Getter - Setters ---------------------------------------------------------------------------
	public static void setItem(String id) {
		item = id;
	}
	
	public static String getItem() {
		if(item == null) {
			return "rien";
		} 
		return item;			
	}
	
	public static boolean isClick() {
		return click;
	}

	public static void setClick(boolean click) {
		ToolBar.click = click;
	}

	public static int getMostX() {
		return mostX;
	}

	public static void setMostX(int mostX) {
		ToolBar.mostX = mostX;
	}
	
	public static boolean isStartPlaced() {
		return (startPlaced == 0) ? false : true;
	}

	public static void setStartPlaced(int startPlaced) {
		ToolBar.startPlaced = startPlaced;
	}
	
	public static int getStartPlace() {
		return startPlaced;
	}

	public static boolean isEndPlaced() {
		return (endPlaced == 0) ? false : true;
	}

	public static void setEndPlaced(int endPlaced) {
		ToolBar.endPlaced = endPlaced;
	}
	
	public static int getEndPlace() {
		return endPlaced;
	}
	
	// Couleurs
	
	public static Color getGroundColor() {
		return groundColor;
	}

	public static void setGroundColor(Color groundColor) {
		ToolBar.groundColor = groundColor;
	}

	public static Color getObstacleColor() {
		return obstacleColor;
	}

	public static void setObstacleColor(Color obstacleColor) {
		ToolBar.obstacleColor = obstacleColor;
	}

	public static Color getCoinColor() {
		return coinColor;
	}

	public static void setCoinColor(Color coinColor) {
		ToolBar.coinColor = coinColor;
	}
}
