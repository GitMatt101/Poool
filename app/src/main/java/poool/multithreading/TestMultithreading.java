package poool.multithreading;

import java.util.Random;

import poool.multithreading.model.board.Board;
import poool.multithreading.model.board.LargeBoardConfiguration;
import poool.multithreading.model.board.MassiveBoardConfiguration;
import poool.multithreading.view.View;
import poool.multithreading.view.ViewModel;
import poool.utils.Vector2D;

public class TestMultithreading {

    public static void main(String[] args) {
        final Random rand = new Random(42);

        Board board = new Board(new MassiveBoardConfiguration());

        ViewModel viewModel = new ViewModel();
        View view = new View(viewModel, 1200, 800);

        viewModel.update(board, 0);
        view.render();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        int nFrames = 0;
        long t0 = System.currentTimeMillis();
        long lastUpdateTime = System.currentTimeMillis();

        var pb = board.getPlayerBall();
        var lastKickTime = t0;

        while (true) {
            if (pb.getVelocity().getAbsolute() < 0.05 && System.currentTimeMillis() - lastKickTime > 2000) {
                var angle = rand.nextDouble() * Math.PI * 0.25;
                var v = new Vector2D(Math.cos(angle), Math.sin(angle));
                v.mul(5.0);
                pb.setVelocity(v.getX(), v.getY());
                lastKickTime = System.currentTimeMillis();
            }

            long elapsed = System.currentTimeMillis() - lastUpdateTime;
            lastUpdateTime = System.currentTimeMillis();
            long start = System.currentTimeMillis();
            board.updateState(elapsed);
            long end = System.currentTimeMillis();
            System.out.println("Update time (" + board.getBalls().size() + " balls): " + (end - start) + "ms");

            nFrames++;
            int framePerSec = 0;
            long dt = (System.currentTimeMillis() - t0);
            if (dt > 0) {
                framePerSec = (int) (nFrames * 1000 / dt);
            }

            viewModel.update(board, framePerSec);
            view.render();
        }
    }

}
