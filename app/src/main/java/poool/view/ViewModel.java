package poool.view;

import java.util.ArrayList;

import poool.model.Point2D;
import poool.model.board.Board;

record BallViewInfo(Point2D pos, double radius) {
}

public class ViewModel {

	private ArrayList<BallViewInfo> balls;
	private BallViewInfo player;
	private int framePerSec;

	public ViewModel() {
		balls = new ArrayList<BallViewInfo>();
		framePerSec = 0;
	}

	public synchronized void update(Board board, int framePerSec) {
		balls.clear();
		for (var b : board.getAllBalls()) {
			balls.add(new BallViewInfo(b.getPosition(), b.getRadius()));
		}
		this.framePerSec = framePerSec;
	}

	public synchronized ArrayList<BallViewInfo> getBalls() {
		var copy = new ArrayList<BallViewInfo>();
		copy.addAll(balls);
		return copy;

	}

	public synchronized int getFramePerSec() {
		return framePerSec;
	}

	public synchronized BallViewInfo getPlayerBall() {
		return player;
	}

}
