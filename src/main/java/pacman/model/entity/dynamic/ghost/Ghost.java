package pacman.model.entity.dynamic.ghost;

import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.level.Level;

import java.util.Map;

/**
 * Represents Ghost entity in Pac-Man Game
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver {
    void setSpeeds(Map<GhostMode, Double> speeds);
    void setGhostMode(GhostMode ghostMode);
    void setLevel(Level level);
    PositionGetter getPosGetter();
}
