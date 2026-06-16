package poool.utils;

public class Vector2D {

    private double x;
    private double y;

    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(final double val) {
        this.x = val;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double val) {
        this.y = val;
    }

    public double getAbsolute() {
        return Math.hypot(this.x, this.y);
    }

    public void mul(final double coefficient) {
        this.x *= coefficient;
        this.y *= coefficient;
    }

    public void invertX() {
        this.x = -this.x;
    }

    public void invertY() {
        this.y = -this.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}
