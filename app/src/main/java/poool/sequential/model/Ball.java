package poool.sequential.model;

import poool.sequential.model.board.Board;
import poool.utils.Boundary;
import poool.utils.Globals;
import poool.utils.Point2D;
import poool.utils.Vector2D;

public class Ball {

    private final Point2D position;
    private final Vector2D velocity;
    private final double radius;
    private final double mass;

    public Ball(final Point2D position, final double radius, final double mass) {
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.radius = radius;
        this.mass = mass;
    }

    public Ball(final Point2D position, final Vector2D velocity, final double radius, final double mass) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.mass = mass;
    }

    public Point2D getPosition() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }
    
    public void setVelocity(final double x, final double y) {
        this.velocity.setX(x);
        this.velocity.setY(y);
    }

    public double getRadius() {
        return this.radius;
    }

    public double getMass() {
        return this.mass;
    }

    public void updateState(final double deltaTime) {
        final double speed = this.velocity.getAbsolute();
        if (speed > Globals.MIN_VELOCITY) {
            final double deltaTimeScaled = deltaTime * Globals.MIN_VELOCITY;
            final double decrease = Globals.FRICTION_COEFFICIENT * deltaTimeScaled;
            final double coefficient = Math.max(0, speed - decrease) / speed;
            this.velocity.mul(coefficient);
            final Vector2D velocityScaled = new Vector2D(this.velocity.getX(), this.velocity.getY());
            velocityScaled.mul(deltaTimeScaled);
            this.position.sum(velocityScaled);
            this.applyBoundaryConstraints();
        } else {
            // Reset velocity to (0,0)
            this.velocity.mul(0);
        }
    }

    private void applyBoundaryConstraints() {
        final Boundary bounds = Board.getBounds();
        if (this.position.getX() + radius >= bounds.x1()) {
            this.velocity.invertX();
        } else if (this.position.getX() - radius <= bounds.x0()) {
            this.velocity.invertX();
        } else if (this.position.getY() + radius >= bounds.y1()) {
            this.velocity.invertY();
        } else if (this.position.getY() - radius <= bounds.y0()) {
            this.velocity.invertY();
        }
    }
    
}
