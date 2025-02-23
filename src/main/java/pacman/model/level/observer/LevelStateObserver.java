package pacman.model.level.observer;

import pacman.model.engine.observer.GameState;

public interface LevelStateObserver {
    void updateNumLives(int numLives);
    void updateGameState(GameState gameState);
    void updateScore(int scoreChange);
}
