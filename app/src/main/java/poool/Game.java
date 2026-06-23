package poool;

import poool.controller.ActiveController;
import poool.model.board.GameBoard;
import poool.model.board.GameBoardConfiguration;
import poool.model.game_logics.GameLoop;
import poool.model.task.TaskBasedBoard;
import poool.view.View;
import poool.view.ViewModel;

public class Game {
    
    public static void main(String[] args) {
        GameBoard board = new GameBoard(new TaskBasedBoard(new GameBoardConfiguration()));
        ViewModel viewModel = new ViewModel();
        ActiveController controller = new ActiveController(board.getBoard().getPlayer());
        View view = new View(viewModel, controller, 1200, 800);

        viewModel.update(board);

        GameLoop loop = new GameLoop(board, viewModel, view);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        loop.start();
        controller.start();
    }

}
