package poool.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import poool.controller.ActiveController;
import poool.utils.Globals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;

public class ViewFrame extends JFrame implements KeyListener {

	private final VisualiserPanel panel;
	private final ViewModel model;
    private final ActiveController controller;

    public ViewFrame(final ViewModel model, final ActiveController controller, final int w, final int h) {
		this.model = model;
        this.controller = controller;
		setTitle("Poool");
		setSize(w, h);
		setResizable(false);
		panel = new VisualiserPanel(w, h);
		getContentPane().add(panel);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}

			public void windowClosed(WindowEvent ev) {
				System.exit(-1);
			}
		});
		this.addKeyListener(this);
	}

	public void render() {
		panel.repaint();
	}

    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getExtendedKeyCode()) {
            case KeyEvent.VK_W:
                this.controller.notifyNewMove(player -> player.getVelocity().setY(Globals.PLAYER_SPPED));
                break;
            case KeyEvent.VK_A:
                this.controller.notifyNewMove(player -> player.getVelocity().setX(-Globals.PLAYER_SPPED));
                break;
            case KeyEvent.VK_S:
                this.controller.notifyNewMove(player -> player.getVelocity().setY(-Globals.PLAYER_SPPED));
                break;
            case KeyEvent.VK_D:
                this.controller.notifyNewMove(player -> player.getVelocity().setX(Globals.PLAYER_SPPED));
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        switch (key.getExtendedKeyCode()) {
            case KeyEvent.VK_W: case KeyEvent.VK_S:
                this.controller.notifyNewMove(player -> player.getVelocity().setY(0));
                break;
            case KeyEvent.VK_A: case KeyEvent.VK_D:
                this.controller.notifyNewMove(player -> player.getVelocity().setX(0));
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent key) {}

    public class VisualiserPanel extends JPanel {
		private int ox;
		private int oy;
		private int delta;

		public VisualiserPanel(int w, int h) {
			setSize(w, h);
			ox = w / 2;
			oy = h / 2;
			delta = Math.min(ox, oy);
		}

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(Color.LIGHT_GRAY);
			g2.setStroke(new BasicStroke(1));
			g2.drawLine(ox, 0, ox, oy * 2);
			g2.drawLine(0, oy, ox * 2, oy);
			g2.setColor(Color.BLACK);

			g2.setStroke(new BasicStroke(1));
			for (var b : model.getBalls()) {
				var p = b.pos();
				int x0 = (int) (ox + p.getX() * delta);
				int y0 = (int) (oy - p.getY() * delta);
				int radiusX = (int) (b.radius() * delta);
				int radiusY = (int) (b.radius() * delta);
				g2.drawOval(x0 - radiusX, y0 - radiusY, radiusX * 2, radiusY * 2);
			}

			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLUE);
			var pb = model.getPlayerBall();
			if (pb != null) {
				var p1 = pb.pos();
				int x0 = (int) (ox + p1.getX() * delta);
				int y0 = (int) (oy - p1.getY() * delta);
				int radiusX = (int) (pb.radius() * delta);
				int radiusY = (int) (pb.radius() * delta);
				g2.drawOval(x0 - radiusX, y0 - radiusY, radiusX * 2, radiusY * 2);
			}

			g2.setStroke(new BasicStroke(3));
			var bot = model.getBotBall();
			g2.setColor(Color.RED);
			if (bot != null) {
				var p1 = bot.pos();
				int x0 = (int) (ox + p1.getX() * delta);
				int y0 = (int) (oy - p1.getY() * delta);
				int radiusX = (int) (bot.radius() * delta);
				int radiusY = (int) (bot.radius() * delta);
				g2.drawOval(x0 - radiusX, y0 - radiusY, radiusX * 2, radiusY * 2);
			}

			g2.setColor(Color.BLACK);
			int holeRadius = (int) (Globals.HOLE_RADIUS * delta);
			g2.fillOval(0 - holeRadius, 0 - holeRadius, holeRadius * 2, holeRadius * 2);
			g2.fillOval(ox * 2 - holeRadius, 0 - holeRadius, holeRadius * 2, holeRadius * 2);

			g2.setFont(new Font("Arial", Font.BOLD, 50));

			g2.setColor(Color.BLUE);
			g2.drawString(Integer.toString(model.getPlayerScore()), (float) (ox * 1.5), (float) (oy * 1.75));

			g2.setColor(Color.RED); 
			g2.drawString(Integer.toString(model.getBotScore()), (float) (ox / 2), (float) (oy * 1.75));

			if (model.isOver().isPresent()) {
				g2.setFont(new Font("Arial", Font.BOLD, 70));
				g2.setColor(Color.BLACK);
				g2.drawString((model.isOver().get() ? "Player" : "Bot") + " wins!", ox - 150, oy + 25);
			}
		}

	}
    
}
