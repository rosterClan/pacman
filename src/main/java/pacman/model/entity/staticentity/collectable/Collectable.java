package pacman.model.entity.staticentity.collectable;

import pacman.model.entity.Renderable;

/**
 * Represents a collectable entity in the Pac-Man Game.
 * The collection of a collectable rewards the player with points;
 */
public interface Collectable extends Renderable {
    void collect();

    boolean isCollectable();

    int getPoints();

    boolean isPowerPellet();
}
