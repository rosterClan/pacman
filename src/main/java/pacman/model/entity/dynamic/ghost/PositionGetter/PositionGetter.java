package pacman.model.entity.dynamic.ghost.PositionGetter;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface PositionGetter {
    public Vector2D getPosition(KinematicState player_pos, KinematicState ownPos);
}
