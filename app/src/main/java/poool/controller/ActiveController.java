package poool.controller;

import poool.model.Ball;

public class ActiveController extends Thread {

	private static final int BUFFER_SIZE = 100;
	private BoundedBuffer<Move> cmdBuffer;
	private final Ball player;
	
	public ActiveController(final Ball player) {
		this.cmdBuffer = new BoundedBufferImpl<Move>(BUFFER_SIZE);
		this.player = player;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				cmdBuffer.get().execute(this.player);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void notifyNewMove(Move cmd) {
		try {
			cmdBuffer.put(cmd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
