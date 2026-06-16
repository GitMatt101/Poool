package poool.multithreading.model.board;

import java.util.ArrayList;
import java.util.List;

import poool.multithreading.model.Ball;
import poool.utils.Boundary;
import poool.utils.Point2D;

public class MassiveBoardConfiguration implements BoardConfiguration {

    @Override
	public Ball getPlayerBall() {
		return new Ball(new Point2D(0, -0.75), 0.05, 1.5);
	}

    @Override
	public Ball getOpponentBall() {
		return new Ball(new Point2D(0, 0.75), 0.05, 1.5);
	}

	@Override
	public List<Ball> getBalls() {
		var ballRadius = 0.01;
		var balls = new ArrayList<Ball>();
		for (int row = 0; row < 30; row++) {
			for (int col = 0; col < 150; col++) {
				var px = -1.0 + col * 0.015;
				var py = row * 0.015;
				var b = new Ball(new Point2D(px, py), ballRadius, 0.25);
				balls.add(b);
			}
		}
		return balls;
	}

    @Override
	public Boundary getBounds() {
		return new Boundary(-1.5, -1.0, 1.5, 1.0);
	}
}
