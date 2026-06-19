package poool.model.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import poool.model.Ball;
import poool.model.board.BasicBoard;
import poool.model.board.BoardConfiguration;
import poool.utils.Globals;

public class TaskBasedBoard extends BasicBoard {

    private final ExecutorService executor = Executors.newFixedThreadPool(Globals.MAX_THREADS);

    public TaskBasedBoard(final BoardConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void updateState(final double deltaTime) {
        this.updatePositions(deltaTime);
        this.checkCollisions();
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

        final Collection<Callable<Void>> tasks = new ArrayList<>();
        for (final List<Ball> cellBalls : workersBalls) {
            tasks.add(new TaskWorker(cellBalls));
        }

        try {
            this.executor.invokeAll(tasks);
        } catch (InterruptedException e) {}
    }
    
}
