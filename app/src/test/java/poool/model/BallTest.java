package poool.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BallTest {

    private static final Point2D INITIAL_POSITION = new Point2D(1, 1);
    private static final Vector2D INITIAL_VELOCITY = new Vector2D(1, 0);
    private static final double RADIUS = 1;
    private static final double MASS = 1;
    private Ball ball;

    @BeforeEach
    void init() {
        this.ball = new Ball(INITIAL_POSITION, INITIAL_VELOCITY, RADIUS, MASS);
    }

    @Test
    void testGetters() {
        assertEquals(INITIAL_POSITION, this.ball.getPosition());
        assertEquals(INITIAL_VELOCITY, this.ball.getVelocity());
        assertEquals(RADIUS, this.ball.getRadius());
        assertEquals(MASS, this.ball.getRadius());
    }

    @Test
    void testUpdateState() {
        this.ball.updateState(1000, new Boundary(-1000, -1000, 1000, 1000));
        assertEquals(new Vector2D(0.75, 0), this.ball.getVelocity());
        assertEquals(new Point2D(1.75, 1), this.ball.getPosition());
    }
    
}
