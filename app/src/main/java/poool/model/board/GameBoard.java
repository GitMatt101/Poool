package poool.model.board;

import poool.model.Ball;
import poool.model.Point2D;
import poool.model.game_logics.GameLoop;
import poool.utils.Globals;

public class GameBoard {

    private Board board;
    private GameLoop loop;
    private final Point2D leftHole;
    private final Point2D rightHole;

    public GameBoard(final Board board) {
        this.board = board;
        this.leftHole = new Point2D(board.getBounds().x0(), board.getBounds().y1());
        this.rightHole = new Point2D(board.getBounds().x1(), board.getBounds().y1());
    }

    public void setLoop(final GameLoop loop) {
        this.loop = loop;
    }

    public void updateState(final long deltaTime) {
        this.board.updateState(deltaTime);
        final Ball player = this.board.getPlayer();
        final Ball bot = this.board.getOpponent();

        if (this.isPositionInHole(player.getPosition()))
            this.loop.gameOver(false);
        else if (this.isPositionInHole(bot.getPosition()))
            this.loop.gameOver(true);

        for (final Ball ball : this.board.getSmallBalls()) {
            final double distanceFromLeftHole = ball.getPosition().sub(leftHole).getAbsolute();
            final double distanceFromRightHole = ball.getPosition().sub(rightHole).getAbsolute();
            if (distanceFromLeftHole <= Globals.HOLE_RADIUS || distanceFromRightHole <= Globals.HOLE_RADIUS) {
                this.board.removeBall(ball);
                if (ball.getLastHit().isPresent()) {
                    if (ball.getLastHit().get().equals(player))
                        this.loop.playerScores();
                    else if (ball.getLastHit().get().equals(bot))
                        this.loop.botScores();
                }
            }
        }
        if (this.board.getSmallBalls().isEmpty())
            this.loop.gameOver();
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isPositionInHole(final Point2D position) {
        return position.sub(this.leftHole).getAbsolute() <= Globals.HOLE_RADIUS || position.sub(this.rightHole).getAbsolute() <= Globals.HOLE_RADIUS;
    }

}