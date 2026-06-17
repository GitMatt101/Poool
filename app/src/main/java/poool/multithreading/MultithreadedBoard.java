package poool.multithreading;

import java.util.ArrayList;
import java.util.List;

import poool.model.Ball;
import poool.model.Boundary;
import poool.model.board.BasicBoard;
import poool.model.board.BoardConfiguration;
import poool.utils.Globals;

public class MultithreadedBoard extends BasicBoard {

    private final List<Worker> workers = new ArrayList<>();
    private int finishedJobs;

    public MultithreadedBoard(final BoardConfiguration configuration) {
        super(configuration);
    }

    public void init() {
        for (int i = 0; i < Globals.MAX_THREADS; i++)
            this.workers.add(new Worker(this));
        this.workers.forEach(Worker::start);
        this.finishedJobs = this.workers.size();
    }

    @Override
    public synchronized List<Ball> getAllBalls() {
        while (this.finishedJobs < this.workers.size()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        return this.balls;
    }

    public synchronized void signalJobDone() {
        this.finishedJobs++;
        if (finishedJobs == this.workers.size())
            notifyAll();
    }

    @Override
    public synchronized void updateState(final double deltaTime) {
        this.updatePositions(deltaTime);
        this.checkCollisions();
        while (finishedJobs < this.workers.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        this.finishedJobs = 0;
    }

    private void updatePositions(final double deltaTime) {
        this.player.updateState(deltaTime, this.getBounds());
        this.opponent.updateState(deltaTime, this.getBounds());
        this.balls.forEach(b -> b.updateState(deltaTime, this.getBounds()));
    }

    private void checkCollisions() {
        final List<Boundary> cells = new ArrayList<>();
        final double xStep = (this.bounds.x1() - this.bounds.x0()) / Globals.GRID_COLS;
        final double yStep = (this.bounds.y1() - this.bounds.y0()) / Globals.GRID_ROWS;

        for (int i = 0; i < Globals.GRID_ROWS; i++) {
            for (int j = 0; j < Globals.GRID_COLS; j++) {
                double x0 = this.bounds.x0() + (j * xStep);
                double y0 = this.bounds.y0() + (i * yStep);
                double x1 = this.bounds.x0() + ((j + 1) * xStep);
                double y1 = this.bounds.y0() + ((i + 1) * yStep);
                cells.add(new Boundary(x0, y0, x1, y1));
            }
        }

        if (this.workers.size() != cells.size())
            throw new UnsupportedOperationException();

        for (int i = 0; i < cells.size(); i++) {
            final Boundary cell = cells.get(i);
            final Worker worker = this.workers.get(i);
            worker.setBalls(this.filter(cell));
            worker.startWorking();
        }
    }

    private List<Ball> filter(final Boundary boundary) {
        return this.balls.stream()
                .filter(b -> b.getPosition().getX() >= boundary.x0() && b.getPosition().getX() <= boundary.x1())
                .filter(b -> b.getPosition().getY() >= boundary.y0() && b.getPosition().getY() <= boundary.y1())
                .toList();
    }

}
