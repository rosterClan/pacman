package pacman.model.entity.dynamic.ghost.PositionGetter.concrete;

import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

public class clydePos implements PositionGetter {

    @Override
    public Vector2D getPosition(KinematicState player_pos, KinematicState ownPos) {
        if (Vector2D.calculateEuclideanDistance(player_pos.getPosition(), ownPos.getPosition()) < 8*16) {
            return player_pos.getPosition();
        }
        return ownPos.getPosition();
    }
    
}
