package pacman.model.entity.dynamic.ghost.PositionGetter.concrete;

import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

public class pinkyPos implements PositionGetter {
    private Vector2D directionVectorCache = null; 

    @Override
    public Vector2D getPosition(KinematicState player_pos, KinematicState ownPos) {
        Vector2D curPos = player_pos.getPosition();
        Vector2D prevPos = player_pos.getPreviousPosition();
        
        Vector2D directionVector = curPos.vectorSubtraction(prevPos);
        Vector2D unitVector = directionVector.getUnitVector();

        //If the magitude is 0, that means pacman is in a corner not moving. Therefore, its kind of hard to
        //figure out where '4' positions ahead is. So, we cache valid direction vectors and we can just refer to that
        //in such cases. Otherwise, this ghoasts method of movement doesn't make any sense. 
        if (directionVectorCache == null) {
            directionVectorCache = unitVector;
        } else if (unitVector.getMagnitude() > 0) {
            directionVectorCache = unitVector;
        } else {
            directionVector = directionVectorCache;
        }

        return new Vector2D(curPos.getX()+((directionVector.getX()*4)*16), curPos.getY()+((directionVector.getY()*4)*16));
    }
    
}
