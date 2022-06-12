package cCarre.AffichageMap.model;

import org.json.JSONArray;

public class Level {
	private final int idLevel;
	private int levelLength;
	private int totalCoin;
	private int levelWidth;
	private int levelHeight;
	private char[][] LevelMap;
	
	private static JSONArray jsonMap;
	
	

	public Level() {
		this.idLevel = 0;
		this.levelLength = jsonMap.getJSONArray(0).length();
		this.levelWidth = this.levelLength * 60;
		this.totalCoin = 0;
		this.levelHeight = jsonMap.length();
		this.LevelMap = new char[this.levelHeight][this.levelLength];
		
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelLength; j++) {
            	LevelMap[i][j] = (char) jsonMap.getJSONArray(i).get(j);
            }
        }
	}
	
	public int getLevelWidth(){
		return levelWidth;
	}

	public char[][] getLevel() {
		return LevelMap;
	}

	public void setLevel(char[][] level) {
		LevelMap = level;
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
	 * Méthode servant à définir le level à utiliser, à set avant d'instancier Level (et donc le contrller du jeu)
	 * @param json Le JSONArray de la map a utiliser
	 */
	public static void setJsonLevel(JSONArray json) {
		Level.jsonMap = json;
	}
}
