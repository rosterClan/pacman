package pacman.model.entity.dynamic.ghost.PositionGetter.concrete;
import java.util.*;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.NewGhost;
import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.Maze;

public class inkyPos implements PositionGetter {
    private Maze manager = Maze.instance;
    private Vector2D directionVectorCache = null; 
    private Ghost blinky = null;

    private void findBlinky() {
        if (blinky == null) {
            List<Renderable> ghosts = manager.getGhosts();
            for (Renderable ghostInstance : ghosts) {
                Ghost castGhost = (Ghost)ghostInstance;
                PositionGetter getterInstance = castGhost.getPosGetter();
                if (getterInstance.getClass() == blinkyPos.class) {
                    blinky = castGhost;
                    break;
                }
            }
        }
    }
 
    @Override
    public Vector2D getPosition(KinematicState player_pos, KinematicState ownPos) {
        this.findBlinky();
        if (blinky != null) {
            Vector2D curPos = player_pos.getPosition();
            Vector2D prevPos = player_pos.getPreviousPosition();
            Vector2D unitVector = curPos.vectorSubtraction(prevPos).getUnitVector();

            if (directionVectorCache == null) {
                directionVectorCache = unitVector;
            } else if (unitVector.getMagnitude() > 0) {
                directionVectorCache = unitVector;
            } else {
                unitVector = directionVectorCache;
            }
    
            Vector2D twoSpacesAhead = new Vector2D(curPos.getX()+((unitVector.getX()*2)*16), curPos.getY()+((unitVector.getY()*2)*16));
            Vector2D blinkyPos = blinky.getPosition();

            Vector2D destinationVector = twoSpacesAhead.vectorSubtraction(blinkyPos).multiplyScaler(2);
            return destinationVector;
        }

        return ownPos.getPosition();
    }
    
}
