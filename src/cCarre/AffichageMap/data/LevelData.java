package cCarre.AffichageMap.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LevelData {

    public static final String[] LEVEL1 = new String[] {
        "0000000000000000000000000000000000000000000000000000009",
        "0000000000000000000000000000000000000000000000000000009",
        "0000000000000000000000000000000000000000000000000000009",
        "0000000000000000000000000000000000000000000000000000009",
        "0000000000000000000000000000000000000000000000000000009",
        "0000000010000110000000000000000000010000110000000000009",
        "0001110000000000000000000000001110000000000000000000009",
        "0000000000003300000000000000000000000003300000000000009",
        "0000000000011110000000000000000000000011110000000000009",
        "0000000300000000000000000000000000300000000000000000009",
        "8000000100200000000023000110000000100200000000023000119",
        "1111111100111100011111001111111111100111100011111001119"
    };

    public static final String[] LEVEL2 = new String[] {
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000000000000000000000000000",
		"0000010000000000000020000000",
		"0800010000000000003111000110",
		"1111111111111111111111001111"
    };
    
    /**
     * Passe � la moulinette le vieux format de map et le transformen en JSONArray
     * @param level La map en String[]
     * @return Un JSONArry de la map
     */
    @SuppressWarnings("unchecked")
	public static JSONObject getLevelInJSON(String[] level) {
    	int levelHeight = level.length;
    	int levelWidth = level[0].length();
    	char[][] levelTab = new char[levelHeight][levelWidth];
    	JSONArray map = new JSONArray();
    	
    	for (int y = 0; y < levelHeight; y++) {
            String line = level[y];
            JSONArray lineJSON = new JSONArray();
            map.add(lineJSON);
            for (int x = 0; x < levelWidth; x++) {
            	lineJSON.add(line.charAt(x));
            }
        }
    	
    	JSONObject json = new JSONObject();
    	
    	JSONObject colorObject = new JSONObject();
    	colorObject.put("ground","#A52A2A");	// Brown
    	colorObject.put("obstacle","#FF00FF");	// Red
    	colorObject.put("coin","#FFFF00"); 		// Yellow
    	
    	json.put("map", map);
    	json.put("color", colorObject);
    	
    	return json;
    }
}
