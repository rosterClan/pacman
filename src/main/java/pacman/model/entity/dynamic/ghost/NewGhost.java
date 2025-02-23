package pacman.model.entity.dynamic.ghost;

import java.util.*;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.ghost.ghostState.ghostState;
import pacman.model.entity.dynamic.ghost.momento.caretaker;
import pacman.model.entity.dynamic.ghost.momento.memento;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.level.Level;

public class NewGhost implements Ghost, PlayerPositionObserver, caretaker {
    private ghostState state; 
    private HashMap<GhostMode, ghostState> states; 
    private memento ghostState; 
    private Level curLevels; 

    public NewGhost() {
        this.states = new HashMap<>();
        this.ghostState = null;
        this.curLevels = null;
    }

    @Override
    public PositionGetter getPosGetter() {
        return this.state.getPosGetter();
    }
    
    public ghostState getSetState() {
        return this.state;
    }

    /*Setters and Getters*/
    @Override
    public void saveState(memento state) {
        this.ghostState = state; 
    }
    @Override
    public void restoreState() {
        this.state.setStateMemento(this.ghostState);
    }

    public void removeState(GhostMode mode) {
        this.states.remove(mode);
    }
    public void addState(GhostMode mode, ghostState state) {
        if (mode == GhostMode.SCATTER) {
            this.state = state;
        }
        this.states.put(mode, state);
        this.setLevel(curLevels);
    }
    public ghostState getState(GhostMode mode) {
        if (this.states.containsKey(mode)) {
            return this.states.get(mode);
        }
        return null; 
    }
    @Override
    public void setGhostMode(GhostMode ghostMode) {
        if (this.states.containsKey(ghostMode)) {
            this.saveState(this.state.produceStateMemento());
            this.state = this.states.get(ghostMode);
            this.restoreState();
        }
    }
    ///

    @Override
    public void update() {
        this.state.update();
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.state.getPositionBeforeLastUpdate();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.state.setPosition(position);
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return this.state.collidesWith(renderable);
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        this.state.collideWith(level, renderable);
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.state.setPossibleDirections(possibleDirections);
    }

    @Override
    public Direction getDirection() {
        return this.state.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return this.state.getCenter();
    }

    @Override
    public Image getImage() {
        return this.state.getImage();
    }

    @Override
    public double getWidth() {
        return this.state.getWidth();
    }

    @Override
    public double getHeight() {
        return this.state.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return this.state.getPosition();
    }

    @Override
    public Layer getLayer() {
        return this.state.getLayer();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.state.getBoundingBox();
    }

    @Override
    public void reset() {
        this.state.reset();
    }

    @Override
    public void update(KinematicState position) {
        this.state.update(position);
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        for (GhostMode mode : speeds.keySet()) {
            if (this.states.containsKey(mode)) {
                this.states.get(mode).setSpeed(speeds.get(mode));
            }
        }
    }

    public void setNodeLengths(Map<GhostMode, Integer> nodeLengths) {
        for (GhostMode mode : nodeLengths.keySet()) {
            if (this.states.containsKey(mode)) {
                this.states.get(mode).setNodeLength(nodeLengths.get(mode));
            }
        }
    }

    @Override
    public void setLevel(Level level) {
        this.curLevels = level; 
        for (GhostMode ghost : this.states.keySet()) {
            this.states.get(ghost).setLevel(level);
        }
    }

}
