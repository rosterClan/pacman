package pacman.model.level.observer;

/***
 * Subject that is being observed by LevelStateObserver
 */
public interface LevelStateSubject {
    void registerObserver(LevelStateObserver observer);
    void removeObserver(LevelStateObserver observer);
    void notifyObserversWithNumLives();
    void notifyObserversWithGameState();
    void notifyObserversWithScoreChange(int scoreChange);
}
