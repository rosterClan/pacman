package pacman.model.engine;

import pacman.model.engine.observer.GameStateSubject;
import pacman.model.entity.Renderable;
import pacman.model.level.observer.LevelStateObserver;

import java.util.List;


/**
 * The base interface for interacting with the Pac-Man model
 */
public interface GameEngine extends GameStateSubject {
    List<Renderable> getRenderables();

    void startGame();

    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void tick();

    void registerLevelStateObserver(LevelStateObserver observer);
}
