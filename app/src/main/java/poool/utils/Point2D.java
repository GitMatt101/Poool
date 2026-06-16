package poool.utils;

public class Point2D {
    
    private double x;
    private double y;

    public Point2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void sum(final Vector2D vector) {
        this.x += vector.getX();
        this.y += vector.getY();
    }

    public Vector2D sub(final Point2D point) {
        return new Vector2D(this.x - point.getX(), this.y - point.getY());
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}
