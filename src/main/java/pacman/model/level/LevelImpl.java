package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.observer.GameState;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.NewGhost;
import pacman.model.entity.dynamic.ghost.ghostState.states.frightened;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.observer.LevelStateObserver;
import pacman.model.maze.Maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level {

    private static final int START_LEVEL_TIME = 100;
    private final Maze maze;
    private final List<LevelStateObserver> observers;
    private List<Renderable> renderables;
    private Controllable player;
    private List<NewGhost> ghosts;
    private int tickCount;
    private int numLives;
    private int points;
    private GameState gameState;
    private List<Renderable> collectables;
    private int scoreMultiplyer;

    public LevelImpl(JSONObject levelConfiguration, Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.observers = new ArrayList<>();
        this.gameState = GameState.READY;
        this.points = 0;
        this.scoreMultiplyer = 0; 

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();

        // Set up player
        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) maze.getControllable();
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        // Set up ghosts
        this.ghosts = maze.getGhosts().stream().map(element -> (NewGhost) element).collect(Collectors.toList());
        Map<GhostMode, Double> ghostSpeeds = levelConfigurationReader.getGhostSpeeds();
        Map<GhostMode, Integer> ghostNodeLengths = levelConfigurationReader.getGhostModeLengths();

        for (NewGhost ghost : this.ghosts) {
            player.registerObserver(ghost);
            ghost.setSpeeds(ghostSpeeds);
            ghost.setNodeLengths(ghostNodeLengths);
            ghost.setGhostMode(GhostMode.SCATTER);
            ghost.setLevel(this);
        }
        
        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());

    }

    public void resetModifyer() {
        this.scoreMultiplyer = 0;
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(Collectors.toList());
    }

    @Override
    public void tick() {
        if (this.gameState != GameState.IN_PROGRESS) {

            if (tickCount >= START_LEVEL_TIME) {
                setGameState(GameState.IN_PROGRESS);
                tickCount = 0;
            }

        } else {

            if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
                this.player.switchImage();
            }

            List<DynamicEntity> dynamicEntities = getDynamicEntities();
            for (DynamicEntity dynamicEntity : dynamicEntities) {
                maze.updatePossibleDirections(dynamicEntity);
                dynamicEntity.update();
            }

            for (int i = 0; i < dynamicEntities.size(); ++i) {
                DynamicEntity dynamicEntityA = dynamicEntities.get(i);

                // handle collisions between dynamic entities
                for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                    DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                    if (dynamicEntityA.collidesWith(dynamicEntityB) || dynamicEntityB.collidesWith(dynamicEntityA)) {
                        dynamicEntityB.collideWith(this, dynamicEntityA);
                        dynamicEntityA.collideWith(this, dynamicEntityB);
                    }
                }

                // handle collisions between dynamic entities and static entities
                for (StaticEntity staticEntity : getStaticEntities()) {
                    if (dynamicEntityA.collidesWith(staticEntity)) {
                        dynamicEntityA.collideWith(this, staticEntity);
                        PhysicsEngine.resolveCollision(dynamicEntityA, staticEntity);
                    }
                }
            }
        }
        tickCount++;
    }

    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public void collect(Collectable collectable) {
        int localPoints = 0; 
        if (collectable instanceof frightened) {
            this.scoreMultiplyer++; 
            localPoints += (Math.pow(2, this.scoreMultiplyer) * collectable.getPoints());
        } else {
            localPoints += collectable.getPoints();
            this.collectables.remove(collectable);
        }
        notifyObserversWithScoreChange(localPoints);
    }

    @Override
    public void handleLoseLife() {
        if (gameState == GameState.IN_PROGRESS) {
            for (DynamicEntity dynamicEntity : getDynamicEntities()) {
                dynamicEntity.reset();
                if (dynamicEntity instanceof NewGhost) {
                    ((NewGhost)dynamicEntity).setGhostMode(GhostMode.SCATTER);
                }
            }
            setNumLives(numLives - 1);
            setGameState(GameState.READY);
            tickCount = 0;
        }
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public void registerObserver(LevelStateObserver observer) {
        this.observers.add(observer);
        observer.updateNumLives(this.numLives);
        observer.updateGameState(this.gameState);
    }

    @Override
    public void removeObserver(LevelStateObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserversWithNumLives() {
        for (LevelStateObserver observer : observers) {
            observer.updateNumLives(this.numLives);
        }
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
        notifyObserversWithGameState();
    }

    @Override
    public void notifyObserversWithGameState() {
        for (LevelStateObserver observer : observers) {
            observer.updateGameState(gameState);
        }
    }

    public void notifyObserversWithScoreChange(int scoreChange) {
        for (LevelStateObserver observer : observers) {
            observer.updateScore(scoreChange);
        }
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
        notifyObserversWithNumLives();
    }

    @Override
    public void handleGameEnd() {
        this.renderables.removeAll(getDynamicEntities());
    }

    @Override
    public void handleFrightened() {
        List<Renderable> ghostList = this.maze.getGhosts();
        for (Renderable item : ghostList) {
            NewGhost newItem = (NewGhost)item;
            newItem.setGhostMode(GhostMode.FRIGHTENED);
        }
    }

    @Override
    public void completeFrightened() {
        List<Renderable> ghostList = this.maze.getGhosts();
        for (Renderable item : ghostList) {
            NewGhost newItem = (NewGhost)item;
            newItem.setGhostMode(GhostMode.SCATTER);
        }
    }
}
