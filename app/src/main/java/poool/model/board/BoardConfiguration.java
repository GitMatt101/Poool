package poool.model.board;

import java.util.List;

import poool.model.Boundary;
import poool.model.Ball;

public interface BoardConfiguration {

    Ball getPlayerBall();

    Ball getOpponentBall();

    List<Ball> getBalls();

    Boundary getBounds();

}
