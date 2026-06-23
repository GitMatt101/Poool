package poool.model.game_logics;

import java.util.Optional;

import poool.model.Ball;
import poool.model.Vector2D;
import poool.model.board.Board;

public class BotUpdater extends Thread {

	private static final long MOVE_INTERVAL = 500;
	private final Ball bot;
	private final Board board;

	public BotUpdater(final Board board) {
		this.bot = board.getOpponent();
		this.board = board;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this) {
				while (bot.getVelocity().getAbsolute() > 0) {
					try {
						wait();
					} catch (InterruptedException e) {}
				}
			}
			try {
				Thread.sleep(MOVE_INTERVAL);
			} catch (InterruptedException e) {}
			if (this.bot.getVelocity().getAbsolute() == 0)
				this.chooseDirection();
		}
	}

	public synchronized void botUpdated() {
		notifyAll();
	}

	private void chooseDirection() {
		final Optional<Ball> closestBallOptional = board.getSmallBalls().stream()
				.min((p1, p2) -> Double.compare(this.bot.getPosition().sub(p1.getPosition()).getAbsolute(),
						this.bot.getPosition().sub(p2.getPosition()).getAbsolute()));
		if (closestBallOptional.isPresent()) {
			final Ball closestBall = closestBallOptional.get();
			final Vector2D velocity = closestBall.getPosition().sub(this.bot.getPosition());
			if (velocity.getAbsolute() < 1) {
				final double speed = velocity.getAbsolute();
				double y = (1 / speed) * Math.sqrt((Math.pow(velocity.getX(), 2) + Math.pow(velocity.getY(), 2)) / (Math.pow(velocity.getX() / velocity.getY(), 2) + 1));
				y = y * velocity.getY() < 0 ? -y : y;
				final double x = velocity.getX() / velocity.getY() * y;
				velocity.setX(x);
				velocity.setY(y);
			}
			this.bot.setVelocity(velocity.getX(), velocity.getY());
		}
	}

}
