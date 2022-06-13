package cCarre.genmap.model;

import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.AddLengthGrilleEvent;

public class ToolBar {
	private static String item;
	private static boolean test = false;
	private static boolean click;
	private static int mostX;
	private static int startPlaced = 0;
	private static int endPlaced = 0;
	private static boolean preTest = false;
	

	public ToolBar() {}
	

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
	
	public static boolean isPreTest() {
		return preTest;
	}

	public static void setPreTest(boolean preTest) {
		ToolBar.preTest = preTest;
	}
}
