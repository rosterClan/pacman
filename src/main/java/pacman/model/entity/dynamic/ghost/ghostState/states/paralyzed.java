package pacman.model.entity.dynamic.ghost.ghostState.states;

import java.util.Map;

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
import pacman.model.level.Level;

public class paralyzed extends ghostState{
    public paralyzed(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, PositionGetter posGetter, NewGhost client) {
        super(image, boundingBox, kinematicState, ghostMode, targetCorner, posGetter, client);
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (renderable instanceof Controllable) {
            level.handleLoseLife();
        }
    }

    @Override
    protected Vector2D getTargetLocation() {
        if (this.playerPosition == null) {
            return this.startingPosition;
        }
        return this.playerPosition.getPosition(); 
    }
    
    @Override
    protected void stateTransfer() {
        this.futureTime = null; 
        this.client.setGhostMode(GhostMode.SCATTER);
    }
    
}
