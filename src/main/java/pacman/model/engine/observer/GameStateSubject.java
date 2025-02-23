package pacman.model.engine.observer;

public interface GameStateSubject {
    void registerObserver(GameStateObserver observer);
    void notifyObserversWithGameState();
}
