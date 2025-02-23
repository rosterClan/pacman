package pacman.model.factories;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;

/**
 * Concrete renderable factory for Pellet objects
 */
public class PelletFactory implements RenderableFactory {
    private int NUM_POINTS = 50;
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;

    private static final Map<Character, Image> PELLET_IMAGES = new HashMap<>();
    static {
        PELLET_IMAGES.put(RenderableType.PELLET, new Image("maze/pellet.png"));
        PELLET_IMAGES.put(RenderableType.POWER_PELLET, new Image("maze/power_pellet.png"));
    }
    private Image image; 
    private boolean isPowerPellet;

    public PelletFactory(char renderableType) {
        this.image = PELLET_IMAGES.get(renderableType);
        this.isPowerPellet = (renderableType == RenderableType.POWER_PELLET);
        if (this.isPowerPellet) {
            NUM_POINTS = 50;
        } else {
            NUM_POINTS = 10;
        }
    }

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            BoundingBox boundingBox = new BoundingBoxImpl(position, image.getHeight(), image.getWidth());
            return new Pellet(boundingBox, layer, image, NUM_POINTS, this.isPowerPellet);
        } catch (Exception e) {
            throw new ConfigurationParseException(String.format("Invalid pellet configuration | %s", e));
        }
    }
}
