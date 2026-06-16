package poool.sequential.model.board;

import java.util.List;

import poool.sequential.model.Ball;
import poool.utils.Boundary;
import poool.utils.Globals;

public class Board {

    private final Ball player;
    private final Ball opponent;
    private final List<Ball> balls;
    private static Boundary bounds;

    public Board(final BoardConfiguration configuration) {
        this.player = configuration.getPlayerBall();
        this.opponent = configuration.getOpponentBall();
        this.balls = configuration.getBalls();
        bounds = configuration.getBounds();
    }

    public Ball getPlayerBall() {
        return this.player;
    }

    public Ball getOpponentBall() {
        return this.opponent;
    }

    public List<Ball> getBalls() {
        return this.balls;
    }

    public static Boundary getBounds() {
        return bounds;
    }

    public void updateState(final double deltaTime) {
        this.updatePositions(deltaTime);
        for (final Ball ball : this.balls) {
            this.checkCollision(this.player, ball);
            this.checkCollision(this.opponent, ball);
        }
        for (int i = 0; i < this.balls.size() - 1; i++) {
            for (int j = i + 1; j < this.balls.size(); j++) {
                this.checkCollision(this.balls.get(i), this.balls.get(j));
            }
        }
    }

    private void updatePositions(final double deltaTime) {
        this.player.updateState(deltaTime);
        this.opponent.updateState(deltaTime);
        this.balls.forEach(b -> b.updateState(deltaTime));
    }

    private void checkCollision(final Ball a, final Ball b) {
        double dx = b.getPosition().getX() - a.getPosition().getX();
        double dy = b.getPosition().getY() - a.getPosition().getY();
        double dist = Math.hypot(dx, dy);
        double minD = a.getRadius() + b.getRadius();

        if (dist < minD && dist > 1e-6) {
            double nx = dx / dist;
            double ny = dy / dist;

            double dvx = b.getVelocity().getX() - a.getVelocity().getX();
            double dvy = b.getVelocity().getY() - a.getVelocity().getY();
            double dvn = dvx * nx + dvy * ny;

            // if not already separating, update velocities
            if (dvn <= 0) {
                double imp = -(1 + Globals.RESTITUTION_COEFFICIENT) * dvn / (1.0 / a.getMass() + 1.0 / b.getMass());
                a.setVelocity(a.getVelocity().getX() - (imp / a.getMass()) * nx, a.getVelocity().getY() - (imp / a.getMass()) * ny);
                b.setVelocity(b.getVelocity().getX() + (imp / b.getMass()) * nx, b.getVelocity().getY() + (imp / b.getMass()) * ny);
            }
        }
    }

}
