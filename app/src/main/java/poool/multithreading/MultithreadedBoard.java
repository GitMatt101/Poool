package poool.multithreading;

import java.util.ArrayList;
import java.util.List;

import poool.model.Ball;
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
            } catch (InterruptedException e) {
            }
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
        final double xStep = (this.bounds.x1() - this.bounds.x0()) / Globals.GRID_COLS;
        final double yStep = (this.bounds.y1() - this.bounds.y0()) / Globals.GRID_ROWS;

        int totalCells = Globals.GRID_ROWS * Globals.GRID_COLS;
        List<List<Ball>> workersBalls = new ArrayList<>(totalCells);
        for (int i = 0; i < totalCells; i++)
            workersBalls.add(new ArrayList<>());

        for (final Ball b : this.balls) {
            int col = (int) ((b.getPosition().getX() - this.bounds.x0()) / xStep);
            int row = (int) ((b.getPosition().getY() - this.bounds.y0()) / yStep);
            col = Math.max(0, Math.min(col, Globals.GRID_COLS - 1));
            row = Math.max(0, Math.min(row, Globals.GRID_ROWS - 1));
            int cellIndex = row * Globals.GRID_COLS + col;
            workersBalls.get(cellIndex).add(b);
            if (b.getPosition().getX() + b.getRadius() > this.bounds.x0() + (col + 1) * xStep && col < Globals.GRID_COLS - 1)
                workersBalls.get(cellIndex + 1).add(b);
            if (b.getPosition().getY() + b.getRadius() > this.bounds.y0() + (row + 1) * yStep && row < Globals.GRID_ROWS - 1)
                workersBalls.get(cellIndex + Globals.GRID_COLS).add(b);
        }

        for (int i = 0; i < this.workers.size(); i++) {
            Worker worker = this.workers.get(i);
            worker.setBalls(workersBalls.get(i));
            worker.startWorking();
        }
    }

}
