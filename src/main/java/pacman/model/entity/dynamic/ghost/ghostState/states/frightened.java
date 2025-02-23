package pacman.model.entity.dynamic.ghost.ghostState.states;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.NewGhost;
import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.ghost.ghostState.ghostState;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;
import java.util.Random;

public class frightened extends ghostState implements Collectable {
    private Random randomNumberGenerator = null;

    public frightened(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, PositionGetter posGetter, NewGhost client) {
        super(image, boundingBox, kinematicState, ghostMode, targetCorner, posGetter, client);
        this.randomNumberGenerator = new Random();
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (renderable instanceof Controllable) {
            this.reset();
            this.client.setGhostMode(GhostMode.PARALYZED);
        }
    }

    @Override
    protected Vector2D getTargetLocation() {
        if (this.randomNumberGenerator == null) {
            return this.targetCorner;
        }
        return new Vector2D(this.randomNumberGenerator.nextInt(1001) - 500, this.randomNumberGenerator.nextInt(1001) - 500);
    }
    
    @Override
    protected void stateTransfer() {
        this.curLevel.resetModifyer();
        this.client.setGhostMode(GhostMode.SCATTER);
        if (this.curLevel != null) {
            this.curLevel.completeFrightened();
        }
    }

    @Override
    public void collect() {}

    @Override
    public boolean isCollectable() {
        return true; 
    }

    @Override
    public int getPoints() {
        return 100; 
    }

    @Override
    public boolean isPowerPellet() {
        return false; 
    }
    
}
