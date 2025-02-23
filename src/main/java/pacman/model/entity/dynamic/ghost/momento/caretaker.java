package pacman.model.entity.dynamic.ghost.momento;

public interface caretaker {
    public void saveState(memento state);
    public void restoreState();
}
