package pacman.model.engine.observer;

/***
 * Observer for GameStateSubject
 */
public interface GameStateObserver {
    void updateGameState(GameState gameState);
}
