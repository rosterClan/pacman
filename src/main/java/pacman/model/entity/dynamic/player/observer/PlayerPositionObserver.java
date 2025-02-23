package pacman.model.entity.dynamic.player.observer;

import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.KinematicStateImpl.KinematicStateBuilder;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface PlayerPositionObserver {
    void update(KinematicState position);
}
