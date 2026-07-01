package poool.model.task;

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
                        Ball.checkCollision(this.balls.get(i), this.balls.get(j));
                    }
                }
            }
        }
        return null;
    }
    
}
