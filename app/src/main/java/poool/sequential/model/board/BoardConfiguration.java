package poool.sequential.model.board;

import java.util.List;

import poool.sequential.model.Ball;
import poool.utils.Boundary;

public interface BoardConfiguration {

    Ball getPlayerBall();

    Ball getOpponentBall();

    List<Ball> getBalls();

    Boundary getBounds();

}
