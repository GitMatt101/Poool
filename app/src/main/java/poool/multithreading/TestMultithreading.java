package poool.multithreading;

import java.util.Random;

import poool.model.Vector2D;
import poool.view.View;
import poool.view.ViewModel;
import poool.model.board.MassiveBoardConfiguration;

public class TestMultithreading {

    public static void main(String[] args) {
        final Random rand = new Random(42);

        MultithreadedBoard board = new MultithreadedBoard(new MassiveBoardConfiguration());
        board.init();

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

        var pb = board.getPlayer();
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
            board.updateState(elapsed);

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
