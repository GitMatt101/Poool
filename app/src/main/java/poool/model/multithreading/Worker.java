package poool.model.multithreading;

import java.util.ArrayList;
import java.util.List;

import poool.model.Ball;
import poool.utils.Globals;

public class Worker extends Thread {

    private final MultithreadedBoard monitor;
    private final List<Ball> balls = new ArrayList<>();
    private boolean work = false;

    public Worker(final MultithreadedBoard board) {
        this.monitor = board;
    }

    public void setBalls(final List<Ball> balls) {
        this.balls.clear();
        this.balls.addAll(balls);
    }

    public synchronized void startWorking() {
        this.work = true;
        notify();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (!this.work) {
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
            }
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
            this.work = false;
            this.monitor.signalJobDone();
        }
    }

}