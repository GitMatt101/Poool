package poool.model.board;

import java.util.List;

import poool.model.Ball;
import poool.model.Boundary;

public interface Board {

    Ball getPlayer();

    Ball getOpponent();

    List<Ball> getAllBalls();

    Boundary getBounds();

    void updateState(double deltaTime);
    
}
