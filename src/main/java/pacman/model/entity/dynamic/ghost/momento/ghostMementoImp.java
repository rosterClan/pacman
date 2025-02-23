package pacman.model.entity.dynamic.ghost.momento;

import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.*;

public class ghostMementoImp implements memento {
    private KinematicState kinematicState;
    private BoundingBox boundingBox; 
    private Set<Direction> direction; 
    private Vector2D targetJunction; 

    public ghostMementoImp(KinematicState kinState, BoundingBox boundbox, Set<Direction> direction, Vector2D targetJunction) {
        this.kinematicState = kinState;
        this.boundingBox = boundbox;
        this.direction = direction;
        this.targetJunction = targetJunction;
    }

    @Override
    public KinematicState getKinematicState() {
        return this.kinematicState;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public Set<Direction> getDirections() {
        return this.direction;
    }

    @Override
    public Vector2D getTargetJunction() {
        return this.targetJunction;
    }

}
