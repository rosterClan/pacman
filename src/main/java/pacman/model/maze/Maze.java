package pacman.model.maze;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.factories.RenderableType;

import java.util.*;


/**
 * Stores and manages the renderables for the Pac-Man game
 */
public class Maze {
    public static final int RESIZING_FACTOR = 16;
    public static final Maze instance = new Maze();
    private static final int MAX_CENTER_DISTANCE = 4;
    private final List<Renderable> renderables;
    private final List<Renderable> ghosts;
    private final List<Renderable> pellets;

    private final HashMap<String, Boolean> isWall;
    private HashMap<String, Boolean> isPath;
    private final HashMap<String, BoundingBox> all;

    private Renderable pacman;
    private int numLives;

    private Maze() {
        this.renderables = new ArrayList<>();
        this.ghosts = new ArrayList<>();
        this.pellets = new ArrayList<>();

        this.isWall = new HashMap<>();
        this.isPath = new HashMap<>();
        this.all = new HashMap<>();
    }

    public static String formatCoordinates(int x, int y) {
        return String.format("(%d, %d)", x, y);
    }

    public static boolean isAtIntersection(Set<Direction> possibleDirections) {
        if (possibleDirections.contains(Direction.LEFT) || possibleDirections.contains(Direction.RIGHT)) {
            return possibleDirections.contains(Direction.UP) ||
                    possibleDirections.contains(Direction.DOWN);
        }

        return false;
    }

    private HashMap<String, Boolean> traverseGraph(int x, int y, HashMap<String, Boolean> knownItems) {
        int xIdx = 0;
        int yIdx = 0; 

        knownItems.put(Maze.formatCoordinates(x, y), true);
        for (xIdx = -1; xIdx <= 1; xIdx++) {
            String key = Maze.formatCoordinates(x+xIdx, y+yIdx);
            if (this.isPath.containsKey(key) && !knownItems.containsKey(key)) {
                knownItems = this.traverseGraph(x+xIdx, y+yIdx, knownItems);
            }
        }

        xIdx = 0; 
        for (yIdx = -1; yIdx <= 1; yIdx++) {
            String key = Maze.formatCoordinates(x+xIdx, y+yIdx);
            if (this.isPath.containsKey(key) && !knownItems.containsKey(key)) {
                knownItems = this.traverseGraph(x+xIdx, y+yIdx, knownItems);
            }
        }

        return knownItems;
    }

    public void removeMiscPaths(int width, int height) {
        Renderable player = this.getControllable();
        Vector2D playerTile = Maze.getTileCords(player.getPosition(), player.getWidth(), player.getHeight());
        HashMap<String, Boolean> realPaths = this.traverseGraph((int)playerTile.getX(), (int)playerTile.getY(), new HashMap<String, Boolean>());
        this.isPath = realPaths;
    }

    public void addRenderable(Renderable renderable, char renderableType, int x, int y) {
        boolean isWall = false; 
        if (renderable != null) {
            if (renderableType == RenderableType.PACMAN) {
                this.pacman = renderable;
            } else if (renderableType == RenderableType.BLINKY || renderableType == RenderableType.INKY || renderableType == RenderableType.CLYDE || renderableType == RenderableType.PINKY) {
                this.ghosts.add(renderable);
            } else if (renderableType == RenderableType.PELLET || renderableType == RenderableType.POWER_PELLET) {
                this.pellets.add(renderable);
            } else {
                this.isWall.put(formatCoordinates(x, y), true);
                isWall = true;
            }
            this.all.put(formatCoordinates(x, y), renderable.getBoundingBox());
            this.renderables.add(renderable);
        }
        if (!isWall) {
            this.isPath.put(formatCoordinates(x, y), true);
        }
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public Renderable getControllable() {
        return pacman;
    }

    public List<Renderable> getGhosts() {
        return ghosts;
    }

    public List<Renderable> getPellets() {
        return pellets;
    }

    public HashMap<String, Boolean> getPathItems() {
        return this.isPath;
    }


    //Converts the screen coordinate system to the internal coordinate system. 
    public static Vector2D getTileCords(Vector2D item, double width, double height) {
        int xTile = (int) Math.floor((item.getX()+(width/2)) / MazeCreator.RESIZING_FACTOR);
        int yTile = (int) Math.floor((item.getY()+(height/2)) / MazeCreator.RESIZING_FACTOR);
        return new Vector2D(xTile, yTile);
    }

    //Convcerts the internal coordinate system to an approximate real screen coordinate
    public static Vector2D getRealCords(Vector2D item, double width, double height) {
        double xPos = ((MazeCreator.RESIZING_FACTOR * item.getX()) + (width/2));
        double yPos = ((MazeCreator.RESIZING_FACTOR * item.getY()) + (height/2));
        return new Vector2D(xPos, yPos);
    }

    private int getCenterOfTile(int index) {
        return index * MazeCreator.RESIZING_FACTOR + MazeCreator.RESIZING_FACTOR / 2;
    }

    //This is no longer used by ghosts, they contain their own system for navigation. However, this is still used by Pacman. I think. 
    public void updatePossibleDirections(DynamicEntity dynamicEntity) {
        int xTile = (int) Math.floor(dynamicEntity.getCenter().getX() / MazeCreator.RESIZING_FACTOR);
        int yTile = (int) Math.floor(dynamicEntity.getCenter().getY() / MazeCreator.RESIZING_FACTOR);

        Set<Direction> possibleDirections = new HashSet<>();

        if (Math.abs(getCenterOfTile(xTile) - dynamicEntity.getCenter().getX()) < MAX_CENTER_DISTANCE &&
                Math.abs(getCenterOfTile(yTile) - dynamicEntity.getCenter().getY()) < MAX_CENTER_DISTANCE) {

            String aboveCoordinates = formatCoordinates(xTile, yTile - 1);
            if (isWall.get(aboveCoordinates) == null) {
                possibleDirections.add(Direction.UP);
            }

            String belowCoordinates = formatCoordinates(xTile, yTile + 1);
            if (isWall.get(belowCoordinates) == null) {
                possibleDirections.add(Direction.DOWN);
            }

            String leftCoordinates = formatCoordinates(xTile - 1, yTile);
            if (isWall.get(leftCoordinates) == null) {
                possibleDirections.add(Direction.LEFT);
            }

            String rightCoordinates = formatCoordinates(xTile + 1, yTile);
            if (isWall.get(rightCoordinates) == null) {
                possibleDirections.add(Direction.RIGHT);
            }
        } else {
            possibleDirections.add(dynamicEntity.getDirection());
            possibleDirections.add(dynamicEntity.getDirection().opposite());
        }

        dynamicEntity.setPossibleDirections(possibleDirections);
    }

    public int getNumLives() {
        return numLives;
    }

    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }

    public void reset() {
        for (Renderable renderable : renderables) {
            renderable.reset();
        }
    }
}
