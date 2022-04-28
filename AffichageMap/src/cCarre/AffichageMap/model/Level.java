package cCarre.AffichageMap.model;

import cCarre.AffichageMap.data.LevelData;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Level {
	private final int idLevel;
	private int levelLength;
	private int totalCoin;
	private int levelHeight;
	private char[][] Level;
	
	public Level() {
		this.idLevel = 0;
		this.levelLength = LevelData.LEVEL1[0].length();
		this.totalCoin = 0;
		this.levelHeight = LevelData.LEVEL1.length;
		this.Level = new char[this.levelHeight][this.levelLength];
		System.out.println(this.levelHeight+" "+this.levelLength);
		
        for (int i = 0; i < levelHeight; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < levelLength; j++) {
            	
        		System.out.println(i);
        		System.out.println(j);
        		System.out.println(line.charAt(j));
            	Level[j][i] = line.charAt(j);
            	
            	System.out.println(line.charAt(j));
            }
        }
		
		
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
