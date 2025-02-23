package pacman.model.level;

import pacman.model.entity.Renderable;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.observer.LevelStateSubject;

import java.util.List;

/**
 * The base interface for a Pac-Man level.
 */
public interface Level extends LevelStateSubject {
    List<Renderable> getRenderables();

    void tick();

    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    boolean isPlayer(Renderable renderable);

    boolean isCollectable(Renderable renderable);

    void collect(Collectable collectable);

    int getNumLives();

    void handleFrightened();

    void completeFrightened();

    boolean isLevelFinished();

    int getPoints();

    void handleLoseLife();

    void handleGameEnd();

    void resetModifyer();
}
