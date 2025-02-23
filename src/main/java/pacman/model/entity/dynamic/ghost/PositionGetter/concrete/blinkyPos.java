package pacman.model.entity.dynamic.ghost.PositionGetter.concrete;

import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

public class blinkyPos implements PositionGetter {
    @Override
    public Vector2D getPosition(KinematicState playerPos, KinematicState ownPos) {
        return playerPos.getPosition();
    }
    
}
