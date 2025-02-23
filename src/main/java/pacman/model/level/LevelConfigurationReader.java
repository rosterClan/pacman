package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.model.entity.dynamic.ghost.GhostMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to read JSONObject to retrieve level configuration details
 */
public class LevelConfigurationReader {

    private final JSONObject levelConfiguration;

    public LevelConfigurationReader(JSONObject levelConfiguration) {
        this.levelConfiguration = levelConfiguration;
    }

    public double getPlayerSpeed() {
        return ((Number) levelConfiguration.get("pacmanSpeed")).doubleValue();
    }

    public Map<GhostMode, Integer> getGhostModeLengths() {
        Map<GhostMode, Integer> ghostModeLengths = new HashMap<>();
        JSONObject modeLengthsObject = (JSONObject) levelConfiguration.get("modeLengths");
        ghostModeLengths.put(GhostMode.CHASE, ((Number) modeLengthsObject.get("chase")).intValue());
        ghostModeLengths.put(GhostMode.SCATTER, ((Number) modeLengthsObject.get("scatter")).intValue());
        ghostModeLengths.put(GhostMode.FRIGHTENED, ((Number) modeLengthsObject.get("frightened")).intValue());
        ghostModeLengths.put(GhostMode.PARALYZED, 5); //I feel like this should be in the file, so I'm going to pretend that it was. 
        return ghostModeLengths;
    }

    public Map<GhostMode, Double> getGhostSpeeds() {
        Map<GhostMode, Double> ghostSpeeds = new HashMap<>();
        JSONObject ghostSpeed = (JSONObject) levelConfiguration.get("ghostSpeed");
        ghostSpeeds.put(GhostMode.CHASE, ((Number) ghostSpeed.get("chase")).doubleValue());
        ghostSpeeds.put(GhostMode.SCATTER, ((Number) ghostSpeed.get("scatter")).doubleValue());
        ghostSpeeds.put(GhostMode.FRIGHTENED, ((Number) ghostSpeed.get("frightened")).doubleValue());
        ghostSpeeds.put(GhostMode.PARALYZED, 0.0);
        return ghostSpeeds;
    }
}
