package poool.multithreading.model.board;

import java.util.ArrayList;
import java.util.List;

import poool.multithreading.model.Ball;
import poool.utils.Boundary;
import poool.utils.Point2D;
import poool.utils.Vector2D;

public class LargeBoardConfiguration implements BoardConfiguration {

    @Override
	public Ball getPlayerBall() {
		return new Ball(new Point2D(0, -0.75), new Vector2D(0, 1), 0.05, 1.5);
	}

    @Override
	public Ball getOpponentBall() {
		return new Ball(new Point2D(0, 0.75), 0.05, 1.5);
	}

	@Override
	public List<Ball> getBalls() {
		var ballRadius = 0.01;
		var balls = new ArrayList<Ball>();
		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 20; col++) {
				var px = -0.25 + col * 0.025;
				var py = row * 0.025;
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
