package poool.model.game_logics;

import java.util.Optional;

import poool.model.board.GameBoard;
import poool.view.ViewModel;
import poool.view.View;

public class GameLoop extends Thread {

    private final GameBoard board;
    private final BotUpdater botUpdater;
    private final ViewModel viewModel;
    private final View view;
    private int playerScore = 0;
    private int botScore = 0;
    private boolean playerWin = false;
    private boolean over = false;

    public GameLoop(final GameBoard board, final ViewModel viewModel, final View view) {
        this.board = board;
        this.botUpdater = new BotUpdater(this.board.getBoard());
        this.viewModel = viewModel;
        this.view = view;
    }

    @Override
    public void run() {
        long lastUpdateTime = System.currentTimeMillis();
        this.botUpdater.start();
        this.board.setLoop(this);
        while (!over) {
            final long elapsed = System.currentTimeMillis() - lastUpdateTime;
            lastUpdateTime = System.currentTimeMillis();
            this.board.updateState(elapsed);
            this.viewModel.update(this.board, this.playerScore, this.botScore, over ? Optional.of(this.playerWin) : Optional.empty());
            this.view.render();
            this.botUpdater.botUpdated();
        }
    }
    
    public void gameOver() {
        this.over = true;
        this.playerWin = this.playerScore > this.botScore;
    }

    public void gameOver(final boolean playerWins) {
        this.over = true;
        this.playerWin = playerWins;
    }

    public boolean didPlayerWin() {
        return this.playerWin;
    }

    public void playerScores() {
        this.playerScore++;
    }

    public void botScores() {
        this.botScore++;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public int getBotScore() {
        return this.botScore;
    }
    
}
