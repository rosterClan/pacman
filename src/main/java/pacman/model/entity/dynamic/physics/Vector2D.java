package pacman.model.entity.dynamic.physics;

/**
 * Utility object for 2D coordinates.
 * <p>
 * All state is immutable.
 */
public class Vector2D {

    public static final Vector2D ZERO = new Vector2D(0, 0);
    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2D other) {
        return (other.getX() == this.x) && (other.getY() == this.y);
    }

    public Vector2D abs() {
        return new Vector2D(Math.abs(this.x), Math.abs(this.y));
    }

    public Vector2D vectorAddition(Vector2D vec) {
        return new Vector2D(this.x + vec.getX(), this.y + vec.getY());
    }

    public Vector2D getUnitVector() {
        double magnitude = this.getMagnitude();
        return new Vector2D(this.x / magnitude, this.y / magnitude);
    }

    public static double calculateEuclideanDistance(Vector2D vector2d, Vector2D vector2d2) {
        double xDistance = vector2d2.getX() - vector2d.getX();
        double yDistance = vector2d2.getY() - vector2d.getY();
        return Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
    }

    public Vector2D vectorSubtraction(Vector2D otherVector) {
        double xDirection = this.x - otherVector.getX();
        double yDirection = this.y - otherVector.getY();
        return new Vector2D(xDirection, yDirection);
    }

    public Vector2D multiplyScaler(int val) {
        return new Vector2D(this.x*val, this.y*val);
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.getX(), this.y + v.getY());
    }

    public boolean isLeftOf(double x) {
        return this.x < x;
    }

    public boolean isRightOf(double x) {
        return this.x > x;
    }

    public boolean isAbove(double y) {
        return this.y < y;
    }

    public boolean isBelow(double y) {
        return this.y > y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public String toIntString() {
        return "(" + String.valueOf((int)this.x) + ", " + String.valueOf((int)this.y) + ")";
    }
}
