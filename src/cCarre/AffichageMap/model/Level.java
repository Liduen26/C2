package cCarre.AffichageMap.model;

import org.json.simple.JSONArray;

public class Level {
	private final int idLevel;
	private int levelLength;
	private int totalCoin;
	private int levelWidth;
	private int levelHeight;
	
	private static JSONArray jsonMap;
	
	public Level() {
		System.out.println(jsonMap.get(1));
		this.idLevel = 0;
		this.levelLength = ((JSONArray) jsonMap.get(0)).size();
		this.levelWidth = this.levelLength * 60;
		this.totalCoin = 0;
		this.levelHeight = jsonMap.size();
	}
	
	public int getLevelWidth(){
		return levelWidth;
	}

	public JSONArray getLevel() {
		return jsonMap;
	}

	public int getLevelHeight() {
		return levelHeight;
	}

	public void setLevelHeight(int levelHeight) {
		this.levelHeight = levelHeight;
	}

	public int getLevelLength() {
		return levelLength;
	}

	public void setLevelLength(int levelLength) {
		this.levelLength = levelLength;
	}

	public int getTotalCoin() {
		return totalCoin;
	}

	public void setTotalCoin(int totalCoin) {
		this.totalCoin = totalCoin;
	}

	public int getIdLevel() {
		return idLevel;
	}
	
	public static JSONArray setJsonLevel() {
		return jsonMap;
	}
	
	/**
	 * M�thode servant � d�finir le level � utiliser, � set avant d'instancier Level (et donc le contrller du jeu)
	 * @param json Le JSONArray de la map a utiliser
	 */
	public static void setJsonLevel(JSONArray json) {
		Level.jsonMap = json;
	}
}
