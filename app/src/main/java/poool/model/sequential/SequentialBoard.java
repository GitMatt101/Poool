package poool.model.sequential;

import poool.model.board.BasicBoard;
import poool.model.board.BoardConfiguration;
import poool.model.Ball;

public class SequentialBoard extends BasicBoard {

    public SequentialBoard(final BoardConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void updateState(final double deltaTime) {
        this.updatePositions(deltaTime);
        for (final Ball ball : this.balls) {
            Ball.checkCollision(this.player, ball);
            Ball.checkCollision(this.opponent, ball);
        }
        for (int i = 0; i < this.balls.size() - 1; i++) {
            for (int j = i + 1; j < this.balls.size(); j++) {
                Ball.checkCollision(this.balls.get(i), this.balls.get(j));
            }
        }
    }

    private void updatePositions(final double deltaTime) {
        this.player.updateState(deltaTime, this.getBounds());
        this.opponent.updateState(deltaTime, this.getBounds());
        this.balls.forEach(b -> b.updateState(deltaTime, this.getBounds()));
    }
 
}
