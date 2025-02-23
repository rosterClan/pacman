package pacman.model.entity.dynamic.ghost.momento;

import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.*;

public interface memento {
    public KinematicState getKinematicState();
    public BoundingBox getBoundingBox();
    public Set<Direction> getDirections();
    public Vector2D getTargetJunction();
}
