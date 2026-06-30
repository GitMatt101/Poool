package poool.model;

public class BallViewInfo {
    
    private final Point2D pos;
    private final double radius;

    public BallViewInfo(final Point2D pos, final double radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public Point2D pos() {
        return this.pos;
    }

    public double radius() {
        return this.radius;
    }

}
