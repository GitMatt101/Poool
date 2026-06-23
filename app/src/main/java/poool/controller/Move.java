package poool.controller;

import poool.model.Ball;

@FunctionalInterface
public interface Move {

	void execute(Ball player);

}
