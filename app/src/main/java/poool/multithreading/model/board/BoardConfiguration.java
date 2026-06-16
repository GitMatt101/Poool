package poool.multithreading.model.board;

import java.util.List;

import poool.multithreading.model.Ball;
import poool.utils.Boundary;

public interface BoardConfiguration {

    Ball getPlayerBall();

    Ball getOpponentBall();

    List<Ball> getBalls();

    Boundary getBounds();

}
