package pacman.model.entity.dynamic.physics;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public static Vector2D getIntDirection(Direction dir) {
        switch (dir) {
            case UP:
                return new Vector2D(0, -1);
            case DOWN:
                return new Vector2D(0, 1);
            case LEFT:
                return new Vector2D(-1, 0);
            case RIGHT:
                return new Vector2D(1, 0);
            default:
                return null;
        }
    }

    public static Direction getDirection(int x, int y) {
        if (-1 <= x && x <= 1 && -1 <= y && y <= 1) {
            if (x != 0 && y == 0) {
                if (x == -1) {
                    return LEFT;  
                } else if (x == 1) {
                    return RIGHT; 
                }
            } else if (x == 0 && y != 0) {
                if (y == -1) {
                    return UP; 
                } else if (y == 1) {
                    return DOWN; 
                }
            }
        }
        return null;
    }
}
