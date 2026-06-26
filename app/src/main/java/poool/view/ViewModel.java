package poool.view;

import java.util.ArrayList;
import java.util.Optional;

import poool.model.Ball;
import poool.model.Point2D;
import poool.model.board.GameBoard;

record BallViewInfo(Point2D pos, double radius) {}

public class ViewModel {

	private ArrayList<BallViewInfo> balls;
	private BallViewInfo player;
	private BallViewInfo bot;
	private int playerScore;
	private int botScore;
	private Optional<Boolean> over;

	public ViewModel() {
		balls = new ArrayList<BallViewInfo>();
	}

	public synchronized void update(final GameBoard board, final int playerScore, final int botScore, Optional<Boolean> over) {
		balls.clear();
		for (var b : board.getBoard().getSmallBalls()) {
			balls.add(new BallViewInfo(b.getPosition(), b.getRadius()));
		}
		final Ball player = board.getBoard().getPlayer();
		final Ball bot = board.getBoard().getOpponent();
		this.player = new BallViewInfo(player.getPosition(), player.getRadius());
		this.bot = new BallViewInfo(bot.getPosition(), bot.getRadius());
		this.playerScore = playerScore;
		this.botScore = botScore;
		this.over = over;
	}

	public synchronized ArrayList<BallViewInfo> getBalls() {
		var copy = new ArrayList<BallViewInfo>();
		copy.addAll(balls);
		return copy;

	}

	public synchronized BallViewInfo getPlayerBall() {
		return this.player;
	}

	public synchronized BallViewInfo getBotBall() {
		return this.bot;
	}

	public synchronized int getPlayerScore() {
		return this.playerScore;
	}

	public synchronized int getBotScore() {
		return this.botScore;
	}

	public synchronized Optional<Boolean> isOver() {
		return this.over;
	}

}
