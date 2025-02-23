package pacman.model.entity.dynamic.ghost.momento;

public interface originator {
    public memento produceStateMemento();
    public void setStateMemento(memento state);
}
