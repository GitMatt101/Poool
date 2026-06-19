package poool.task;

import java.util.List;
import java.util.concurrent.Callable;

import poool.model.Ball;
import poool.utils.Globals;

public class TaskWorker implements Callable<Void> {

    private final List<Ball> balls;

    public TaskWorker(final List<Ball> balls) {
        this.balls = balls;
    }

    @Override
    public Void call() throws Exception {
        if (this.balls.stream().anyMatch(b -> b.getVelocity().getAbsolute() > Globals.MIN_VELOCITY)) {
            for (int i = 0; i < this.balls.size() - 1; i++) {
                for (int j = i + 1; j < this.balls.size(); j++) {
                    final Ball a = this.balls.get(i);
                    final Ball b = this.balls.get(j);
                    if (a.getVelocity().getAbsolute() > Globals.MIN_VELOCITY || b.getVelocity().getAbsolute() > Globals.MIN_VELOCITY) {
                        this.checkCollision(this.balls.get(i), this.balls.get(j));
                    }
                }
            }
        }
        return null;
    }

    private void checkCollision(final Ball a, final Ball b) {
        final double dx = b.getPosition().getX() - a.getPosition().getX();
        final double dy = b.getPosition().getY() - a.getPosition().getY();
        final double dist = Math.hypot(dx, dy);
        final double minD = a.getRadius() + b.getRadius();

        if (dist < minD && dist > 1e-6) {
            final double nx = dx / dist;
            final double ny = dy / dist;

            final double dvx = b.getVelocity().getX() - a.getVelocity().getX();
            final double dvy = b.getVelocity().getY() - a.getVelocity().getY();
            final double dvn = dvx * nx + dvy * ny;

            // if not already separating, update velocities
            if (dvn <= 0) {
                final double imp = -(1 + Globals.RESTITUTION_COEFFICIENT) * dvn / (1.0 / a.getMass() + 1.0 / b.getMass());
                a.setVelocity(a.getVelocity().getX() - (imp / a.getMass()) * nx, a.getVelocity().getY() - (imp / a.getMass()) * ny);
                b.setVelocity(b.getVelocity().getX() + (imp / b.getMass()) * nx, b.getVelocity().getY() + (imp / b.getMass()) * ny);
            }
        }
    }
    
}
