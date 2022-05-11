package cCarre.genmap.model;

public class ToolBar {
	private static String item;
	
	// Objet servant à récupérer les état des btns 
	public ToolBar() {
		
	}
	
	public static void setItem(String id) {
		item = id;
	}
	
	public static String getItem() {
		if(item == null) {
			return "rien";
		} 
		
		return item;			
	}
}
