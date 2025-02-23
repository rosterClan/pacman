package pacman.model.entity.dynamic;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.level.Level;

import java.util.Set;

public interface DynamicEntity extends Renderable {
    double COLLISION_OFFSET = 4;

    void update();

    Vector2D getPositionBeforeLastUpdate();

    void setPosition(Vector2D position);

    boolean collidesWith(Renderable renderable);

    void collideWith(Level level, Renderable renderable);

    void setPossibleDirections(Set<Direction> possibleDirections);

    Direction getDirection();

    Vector2D getCenter();
}