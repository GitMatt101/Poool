package poool.jpf;

import java.util.ArrayList;
import java.util.List;

import poool.model.Ball;
import poool.model.Boundary;
import poool.model.Point2D;
import poool.model.Vector2D;
import poool.model.board.BoardConfiguration;
import poool.model.multithreading.MultithreadedBoard;

public class TestMultithreadJpf {

    private static final BoardConfiguration configuration = new BoardConfiguration() {

        @Override
        public Ball getPlayerBall() {
            return new Ball(new Point2D(0, -0.07), new Vector2D(0, 1), 0.05, 1.5);
        }

        @Override
        public Ball getOpponentBall() {
            return new Ball(new Point2D(-0.5, -0.5), 0.05, 1.5);
        }

        @Override
        public List<Ball> getBalls() {
            final List<Ball> balls = new ArrayList<>();
            balls.add(new Ball(new Point2D(-0.02, 0), 0.01, 0.25));
            balls.add(new Ball(new Point2D(0.02, 0), 0.01, 0.25));
            return balls;
        }

        @Override
        public Boundary getBounds() {
            return new Boundary(-1, -1, 1, 1);
        }

    };

    public static void main(String[] args) throws InterruptedException {
        final MultithreadedBoard board = new MultithreadedBoard(configuration);
        for (int i = 0; i < 2; i++) {
            board.updateState(10);
        }
        board.stop();
    }

}
