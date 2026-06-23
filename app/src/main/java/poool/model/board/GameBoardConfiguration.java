package poool.model.board;

import java.util.ArrayList;
import java.util.List;

import poool.model.Ball;
import poool.model.Boundary;
import poool.model.Point2D;

public class GameBoardConfiguration implements BoardConfiguration {

    private static final int ROWS = 20;
    private static final int COLS = 50;
    private static final double RADIUS = 0.01;

    @Override
    public Ball getPlayerBall() {
        return new Ball(new Point2D(0.75, -0.5), 0.05, 1.5);
    }

    @Override
    public Ball getOpponentBall() {
        return new Ball(new Point2D(-0.75, -0.5), 0.05, 1.5);
    }

    @Override
    public List<Ball> getBalls() {
        final double totalLength = COLS * RADIUS * 2;
        final double totalHeight = ROWS * RADIUS * 2;
        final List<Ball> balls = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                double px = -totalLength / 2 + j * RADIUS * 2;
                double py = -totalHeight / 2 + i * RADIUS * 2;
                balls.add(new Ball(new Point2D(px, py), RADIUS, 0.25));
            }
        }
        return balls;
    }

    @Override
    public Boundary getBounds() {
        return new Boundary(-1.5, -1, 1.5, 1);
    }

}
