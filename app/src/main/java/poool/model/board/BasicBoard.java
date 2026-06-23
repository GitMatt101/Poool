package poool.model.board;

import java.util.ArrayList;
import java.util.List;

import poool.model.Ball;
import poool.model.Boundary;

public abstract class BasicBoard implements Board {
    
    protected final Ball player;
    protected final Ball opponent;
    protected final List<Ball> balls;
    protected final Boundary bounds;

    public BasicBoard(final BoardConfiguration configuration) {
        this.player = configuration.getPlayerBall();
        this.opponent = configuration.getOpponentBall();
        this.balls = configuration.getBalls();
        this.balls.add(this.player);
        this.balls.add(this.opponent);
        this.bounds = configuration.getBounds();
    }

    @Override
    public Ball getPlayer() {
        return this.player;
    }

    @Override
    public Ball getOpponent() {
        return this.opponent;
    }

    @Override
    public List<Ball> getAllBalls() {
        return this.balls;
    }

    @Override
    public List<Ball> getSmallBalls() {
        final List<Ball> smallBalls = new ArrayList<>(this.balls);
        smallBalls.remove(this.player);
        smallBalls.remove(this.opponent);
        return smallBalls;
    }

    @Override
    public Boundary getBounds() {
        return this.bounds;
    }

    @Override
    public void removeBall(final Ball ball) {
        this.balls.remove(ball);
    }
    
}
