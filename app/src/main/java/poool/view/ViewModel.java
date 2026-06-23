package poool.view;

import java.util.ArrayList;

import poool.model.Ball;
import poool.model.Point2D;
import poool.model.board.GameBoard;

record BallViewInfo(Point2D pos, double radius) {}

public class ViewModel {

	private ArrayList<BallViewInfo> balls;
	private BallViewInfo player;
	private BallViewInfo bot;

	public ViewModel() {
		balls = new ArrayList<BallViewInfo>();
	}

	public synchronized void update(GameBoard board) {
		balls.clear();
		for (var b : board.getBoard().getSmallBalls()) {
			balls.add(new BallViewInfo(b.getPosition(), b.getRadius()));
		}
		final Ball player = board.getBoard().getPlayer();
		final Ball bot = board.getBoard().getOpponent();
		this.player = new BallViewInfo(player.getPosition(), player.getRadius());
		this.bot = new BallViewInfo(bot.getPosition(), bot.getRadius());
	}

	public synchronized ArrayList<BallViewInfo> getBalls() {
		var copy = new ArrayList<BallViewInfo>();
		copy.addAll(balls);
		return copy;

	}

	public synchronized BallViewInfo getPlayerBall() {
		return player;
	}

	public synchronized BallViewInfo getBotBall() {
		return bot;
	}

}
