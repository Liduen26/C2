package cCarre.genmap.model;

import cCarre.genmap.events.Ebus;
import cCarre.genmap.events.AddLengthGrilleEvent;

public class ToolBar {
	private static String item;
	private static boolean click;
	private static int mostX;
	public ToolBar() {}
	
	// Paserelle d'infos --------------------------------------------------------------------------
	
	

	// Objet servant à récupérer les état des btns ------------------------------------------------
	
	
	
	
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
		
		Ebus.get().post(new AddLengthGrilleEvent(mostX));
	}
}
