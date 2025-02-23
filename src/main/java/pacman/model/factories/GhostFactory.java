package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;

import pacman.model.entity.dynamic.ghost.NewGhost;
import pacman.model.entity.dynamic.ghost.PositionGetter.PositionGetter;
import pacman.model.entity.dynamic.ghost.PositionGetter.concrete.*;
import pacman.model.entity.dynamic.ghost.ghostState.*;
import pacman.model.entity.dynamic.ghost.ghostState.states.*;


import java.util.*;

public class GhostFactory implements RenderableFactory {
        private static final int LEFT_X_POSITION_OF_MAP = 0;
        private static final int RIGHT_X_POSITION_OF_MAP = 448;
        private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
        private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

        private static final Map<Character, Image> IMAGES = new HashMap<>();
        static {
                IMAGES.put(RenderableType.BLINKY, new Image("maze/ghosts/blinky.png"));
                IMAGES.put(RenderableType.INKY, new Image("maze/ghosts/inky.png"));
                IMAGES.put(RenderableType.CLYDE, new Image("maze/ghosts/clyde.png"));
                IMAGES.put(RenderableType.PINKY, new Image("maze/ghosts/pinky.png"));
                IMAGES.put('l', new Image("maze/ghosts/frightened.png"));
        }
        private Image image;
        private Image frightenedImage; 

        private static final Map<Character, Vector2D> targetCorners = new HashMap<>();
        static {
                targetCorners.put(RenderableType.BLINKY, new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP));
                targetCorners.put(RenderableType.INKY, new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP));
                targetCorners.put(RenderableType.CLYDE, new Vector2D(LEFT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP));
                targetCorners.put(RenderableType.PINKY, new Vector2D(LEFT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP));
        }       
                private final Vector2D corner;

        private static final Map<Character, PositionGetter> algorithms = new HashMap<>();
        static {
                algorithms.put(RenderableType.BLINKY, new blinkyPos());
                algorithms.put(RenderableType.INKY, new pinkyPos());
                algorithms.put(RenderableType.CLYDE, new clydePos());
                algorithms.put(RenderableType.PINKY, new pinkyPos());
        }
        private final PositionGetter ghostType;


        private int getRandomNumber(int min, int max) {
                return (int) ((Math.random() * (max - min)) + min);
        }

        public GhostFactory(char renderableType) {
                this.corner = targetCorners.get(renderableType);
                this.ghostType = algorithms.get(renderableType);
                this.image = IMAGES.get(renderableType);
                this.frightenedImage = IMAGES.get("1");
        }
        

        @Override
        public Renderable createRenderable(Vector2D position) {
        try {
                position = position.add(new Vector2D(4, -4));
                BoundingBox boundingBox = new BoundingBoxImpl(position,this.image.getHeight(),this.image.getWidth());
                KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder().setPosition(position).build();

                NewGhost nGhost = new NewGhost();

                ghostState scatterState = new scatter(this.image, boundingBox, kinematicState, GhostMode.SCATTER, this.corner, ghostType, nGhost);
                ghostState chaseState = new chase(this.image, boundingBox, kinematicState, GhostMode.CHASE, this.corner, ghostType, nGhost);
                ghostState frightenedState = new frightened(new Image("maze/ghosts/frightened.png"), boundingBox, kinematicState, GhostMode.FRIGHTENED, this.corner, ghostType, nGhost);
                ghostState paralyzedState = new paralyzed(this.image, boundingBox, kinematicState, GhostMode.PARALYZED, this.corner, ghostType, nGhost);

                nGhost.addState(GhostMode.SCATTER, scatterState);
                nGhost.addState(GhostMode.CHASE, chaseState);
                nGhost.addState(GhostMode.FRIGHTENED, frightenedState);
                nGhost.addState(GhostMode.PARALYZED, paralyzedState);

                return (Renderable)nGhost;
        } catch (Exception e) {
                throw new ConfigurationParseException(String.format("Invalid ghost configuration | %s ", e));
        }
    }


}
