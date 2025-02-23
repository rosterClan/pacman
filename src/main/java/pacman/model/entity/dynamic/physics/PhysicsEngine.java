package pacman.model.entity.dynamic.physics;

import java.util.HashMap;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.NewGhost;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.maze.Maze;
import pacman.model.maze.MazeCreator;

/**
 * Primitive PhysicsEngine implementation.
 */
public class PhysicsEngine {

    public static void defaultStepBack(DynamicEntity a) {
        Vector2D aPreviousPosition = a.getPositionBeforeLastUpdate();
        a.setPosition(aPreviousPosition);
    }

    public static void resolveCollision(DynamicEntity a, StaticEntity b) {
        if (b.canPassThrough()) {
            return;
        }

        if (a instanceof Controllable) {
            defaultStepBack(a);
        }
    }
}

