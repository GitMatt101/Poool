package poool.multithreading.model.board;

import java.util.ArrayList;
import java.util.List;

import poool.multithreading.model.Ball;
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
        this.balls.add(this.player);
        //this.balls.add(this.opponent);
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
        this.checkAllCollisions();
    }

    @SuppressWarnings("unchecked")
    private void checkAllCollisions() {
        final double length = Board.getBounds().x1() - Board.getBounds().x0();
        final double xStep = length / Globals.GRID_COLS;
        final double height = Board.getBounds().y1() - Board.getBounds().y0();
        final double yStep = height / Globals.GRID_ROWS;

        List<Ball>[][] grid = new ArrayList[Globals.GRID_ROWS][Globals.GRID_COLS];
        for (int r = 0; r < Globals.GRID_ROWS; r++) {
            for (int c = 0; c < Globals.GRID_COLS; c++) {
                grid[r][c] = new ArrayList<>();
            }
        }

        for (final Ball b : this.balls) {
            int col = (int) ((b.getPosition().getX() - Board.getBounds().x0()) / xStep);
            int row = (int) ((b.getPosition().getY() - Board.getBounds().y0()) / yStep);

            col = Math.max(0, Math.min(col, Globals.GRID_COLS - 1));
            row = Math.max(0, Math.min(row, Globals.GRID_ROWS - 1));

            grid[row][col].add(b);
        }

        List<List<Ball>> activeCells = new ArrayList<>();
        for (int r = 0; r < Globals.GRID_ROWS; r++) {
            for (int c = 0; c < Globals.GRID_COLS; c++) {
                activeCells.add(grid[r][c]);
            }
        }

        activeCells.parallelStream().forEach(cellBalls -> {
            for (int i = 0; i < cellBalls.size() - 1; i++) {
                for (int j = i + 1; j < cellBalls.size(); j++) {
                    this.checkCollision(cellBalls.get(i), cellBalls.get(j));
                }
            }
        });
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
                a.setVelocity(a.getVelocity().getX() - (imp / a.getMass()) * nx,
                        a.getVelocity().getY() - (imp / a.getMass()) * ny);
                b.setVelocity(b.getVelocity().getX() + (imp / b.getMass()) * nx,
                        b.getVelocity().getY() + (imp / b.getMass()) * ny);
            }
        }
    }

}
