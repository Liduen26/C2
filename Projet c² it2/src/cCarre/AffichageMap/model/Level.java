package cCarre.AffichageMap.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONString;

import cCarre.AffichageMap.data.LevelData;

public class Level {
	private final int idLevel;
	private int levelLength;
	private int totalCoin;
	private int levelWidth;
	private int levelHeight;
	private char[][] Level;
	
	public Level(JSONArray json) {
		
		System.out.println(json.getJSONArray(0).length());
		System.out.println(LevelData.LEVEL1[0].length());
		this.idLevel = 0;
		this.levelLength = json.getJSONArray(0).length();
		this.levelWidth = this.levelLength * 60;
		this.totalCoin = 0;
		this.levelHeight = json.length();
		this.Level = new char[this.levelHeight][this.levelLength];
		
        for (int i = 0; i < levelHeight; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < levelLength; j++) {
            	Level[i][j] = line.charAt(j);
            }
        }
	}
	
	public int getLevelWidth(){
		return levelWidth;
	}

	public char[][] getLevel() {
		return Level;
	}

	public void setLevel(char[][] level) {
		Level = level;
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
}
