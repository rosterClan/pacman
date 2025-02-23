package pacman.model.entity.dynamic.ghost.ghostState;

import java.util.*;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.NewGhost;
import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.ghost.ghostState.states.frightened;
import pacman.model.entity.dynamic.ghost.momento.ghostMementoImp;
import pacman.model.entity.dynamic.ghost.momento.memento;
import pacman.model.entity.dynamic.ghost.momento.originator;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.KinematicStateImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.time.LocalDateTime;
import java.time.Duration;

public abstract class ghostState implements Ghost, originator {
    protected int minimumDirectionCount = 8;
    protected Layer layer = Layer.FOREGROUND;
    protected Image image;

    protected GhostMode ghostMode;

    protected Direction currentDirection;
    protected Set<Direction> possibleDirections;
    protected double speed; 
    protected NewGhost client; 
    protected Level curLevel; 

    protected PositionGetter posGetter; 

    protected LocalDateTime futureTime; 
    protected LocalDateTime currentTime;

    protected int nodeLength;
    protected int currentDirectionCount = 0;

    protected KinematicState kinematicState;
    protected KinematicState playerPosition;
    protected BoundingBox boundingBox;

    protected Vector2D junctionTile; 
    protected Vector2D targetLocation;
    protected Vector2D startingPosition;
    protected Vector2D targetCorner;
    protected Vector2D prevLoc; 

    public ghostState(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, PositionGetter posGetter, NewGhost client) {
        this.posGetter = posGetter;
        this.client = client;
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;

        this.prevLoc = new Vector2D(Double.MAX_VALUE, Double.MAX_VALUE); 
        this.speed = 0;
        this.nodeLength = 0; 
        this.currentTime = LocalDateTime.now();
        this.junctionTile = null;
        this.curLevel = curLevel;
    }

    @Override
    public PositionGetter getPosGetter() {
        return this.posGetter;
    }

    public void setLevel(Level level) {
        this.curLevel = level; 
    }

    @Override
    public memento produceStateMemento() {
        return new ghostMementoImp(this.kinematicState, this.boundingBox, this.possibleDirections, this.junctionTile);
    }

    @Override
    public void setStateMemento(memento state) {
        this.kinematicState = state.getKinematicState();
        this.boundingBox = state.getBoundingBox();
        this.possibleDirections = state.getDirections();
        this.junctionTile = null;
        this.futureTime = null;
    }
    
    private Vector2D getJunction(Vector2D tilePos) {
        HashMap<String, Boolean> paths = Maze.instance.getPathItems();
        
        Vector2D directionPos = Direction.getIntDirection(this.kinematicState.getDirection());
        Vector2D positiveAdjacent = directionPos.abs().vectorSubtraction(new Vector2D(1, 1));
        Vector2D negitiveAdjacent = positiveAdjacent.multiplyScaler(-1);

        Vector2D iterator = tilePos.vectorAddition(directionPos); 
        while (paths.containsKey(iterator.toIntString())) {
            Vector2D bottom = iterator.vectorAddition(negitiveAdjacent);
            Vector2D top = iterator.vectorAddition(positiveAdjacent);
            if (paths.containsKey(bottom.toIntString())) {
                break; 
            } else if (paths.containsKey(top.toIntString())){
                break; 
            }
            iterator = iterator.vectorAddition(directionPos);
        }
        Vector2D junction = iterator;
        return junction;
    }

    private Direction getNextDirection(Vector2D curTilePos, Vector2D targetTilePos, Direction curDirection) {
        HashMap<String, Boolean> paths = Maze.instance.getPathItems();
        Direction bestDirection = null; 
        double bDist = Double.MAX_VALUE;

        Vector2D curDirectionVector = Direction.getIntDirection(curDirection);
        Vector2D oppositeDirectionVector = curDirectionVector.multiplyScaler(-1);

        for (Direction dir : Direction.values()) {
            Vector2D directionVector = Direction.getIntDirection(dir);
            Vector2D posCandidate = curTilePos.vectorAddition(directionVector);
            if (paths.containsKey(posCandidate.toIntString()) && !directionVector.equals(oppositeDirectionVector)) {
                double dist = Vector2D.calculateEuclideanDistance(posCandidate, targetTilePos);
                if (dist < bDist) {
                    bDist = dist; 
                    bestDirection = dir;
                }
            }
        }
        
        if (bestDirection == null) {
            return Direction.getDirection((int)oppositeDirectionVector.getX(), (int)oppositeDirectionVector.getY());
        }
        return bestDirection;
    }

    private void printGameState(Vector2D junction, Vector2D tilePos) {
        HashMap<String, Boolean> paths = Maze.instance.getPathItems();
        String fullMap = "";
        for (int heightIdx = 0; heightIdx < (int)576/16; heightIdx++) {
            StringBuilder printItem = new StringBuilder();
            for (int widthIdx = 0; widthIdx < (int)448/16; widthIdx++) {
                if (widthIdx == junction.getX() && heightIdx == junction.getY()) {
                    printItem.append(" ▦");
                } else if (widthIdx == tilePos.getX() && heightIdx == tilePos.getY()) {
                    printItem.append(" ■");
                } else if (paths.containsKey(Maze.formatCoordinates(widthIdx, heightIdx))) {
                    printItem.append(" 1");
                } else {
                    printItem.append("  ");
                }
            }
            fullMap += printItem.toString() + "\n";
        }
        System.out.println(fullMap);
    }

    private void configNextDir(Direction dir) {
        switch (dir) {
            case UP:
                this.kinematicState.up();
                break;
            case DOWN:
                this.kinematicState.down();
                break;
            case LEFT:
                this.kinematicState.left();
                break;
            case RIGHT:
                this.kinematicState.right();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void update() {
        this.timeCounter();
        Vector2D realPos = this.kinematicState.getPosition();
        Vector2D tilePos = Maze.getTileCords(realPos, this.getWidth(), this.getHeight());
        Vector2D targetTilePos = Maze.getTileCords(this.getTargetLocation(), this.getWidth(), this.getHeight());

        if (this.junctionTile == null) {
            this.junctionTile = getJunction(tilePos);
        }
        Vector2D realJunctionPos = Maze.getRealCords(this.junctionTile, -4, -7); 

        if (realJunctionPos.vectorSubtraction(realPos).getMagnitude() > realJunctionPos.vectorSubtraction(this.prevLoc).getMagnitude()) {
            configNextDir(getNextDirection(tilePos, targetTilePos, this.kinematicState.getDirection()));
            this.kinematicState.setSpeed(0);
            this.prevLoc = new Vector2D(Double.MAX_VALUE, Double.MAX_VALUE);
            this.junctionTile = null;
        } else {
            this.prevLoc = realPos;
            //this.printGameState(this.junctionTile, tilePos);
            this.kinematicState.setSpeed(speed);
            this.kinematicState.update();
            this.boundingBox.setTopLeft(this.kinematicState.getPosition());
        } 
    }

    private void timeCounter() {
        this.currentTime = LocalDateTime.now();
        if (this.futureTime != null) {
            Duration duration = Duration.between(this.currentTime, this.futureTime);
            if (duration.isNegative() || duration.isZero()) {
                this.futureTime = null;
                this.stateTransfer();
            }
        } else {
            this.futureTime = LocalDateTime.now().plusSeconds(this.nodeLength);
        }
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder().setPosition(startingPosition).build();
        this.boundingBox.setTopLeft(startingPosition);
        this.junctionTile = null;
        this.currentDirectionCount = minimumDirectionCount;
    }

    public void setClient(NewGhost ghostClient) {
        this.client = ghostClient;
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.client.setGhostMode(ghostMode);
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.client.setSpeeds(speeds);
    }

    public void setSpeed(Double speed) {
        this.speed = speed; 
    }

    public void setNodeLength(Integer nodeLength) {
        this.nodeLength = nodeLength;
    }

    @Override
    public void update(KinematicState position) {
        this.playerPosition = position;
    }
    
    public KinematicState getKinematicState() {
        return this.kinematicState;
    }

    public Set<Direction> getPossibleDirections() {
        return this.possibleDirections;
    }

    public BoundingBox getBounding() {
        return this.boundingBox;
    }

    @Override
    abstract public void collideWith(Level level, Renderable renderable);
    abstract protected Vector2D getTargetLocation();
    abstract protected void stateTransfer();
}
