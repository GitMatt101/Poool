package poool.model;

import java.util.Optional;

import poool.utils.Globals;

public class Ball {

    private final Point2D position;
    private final Vector2D velocity;
    private final double radius;
    private final double mass;
    private Ball lastHit = null;

    public Ball(final Point2D position, final double radius, final double mass) {
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.radius = radius;
        this.mass = mass;
    }

    public Ball(final Point2D position, final Vector2D velocity, final double radius, final double mass) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.mass = mass;
    }

    public Point2D getPosition() {
        return this.position;
    }
    
    public void setPosition(final double x, final double y) {
        this.position.sum(new Vector2D(-this.position.getX(), -this.position.getY()));
        this.position.sum(new Vector2D(x, y));
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }
    
    public void setVelocity(final double x, final double y) {
        this.velocity.setX(x);
        this.velocity.setY(y);
    }

    public double getRadius() {
        return this.radius;
    }

    public double getMass() {
        return this.mass;
    }

    public void updateState(final double deltaTime, final Boundary bounds) {
        final double speed = this.velocity.getAbsolute();
        if (speed > Globals.MIN_VELOCITY) {
            final double deltaTimeScaled = deltaTime * Globals.MIN_VELOCITY;
            final double decrease = Globals.FRICTION_COEFFICIENT * deltaTimeScaled;
            final double coefficient = Math.max(0, speed - decrease) / speed;
            this.velocity.mul(coefficient);
            final Vector2D velocityScaled = new Vector2D(this.velocity.getX(), this.velocity.getY());
            velocityScaled.mul(deltaTimeScaled);
            this.position.sum(velocityScaled);
            this.applyBoundaryConstraints(bounds);
        } else {
            // Reset velocity to (0,0)
            this.velocity.mul(0);
        }
    }

    private void applyBoundaryConstraints(final Boundary bounds) {
        if (this.position.getX() + radius >= bounds.x1() && this.velocity.getX() > 0)
            this.velocity.invertX();
        else if (this.position.getX() - radius <= bounds.x0() && this.velocity.getX() < 0)
            this.velocity.invertX();
        else if (this.position.getY() + radius >= bounds.y1() && this.velocity.getY() > 0)
            this.velocity.invertY();
        else if (this.position.getY() - radius <= bounds.y0() && this.velocity.getY() < 0)
            this.velocity.invertY();
    }

    public void hit(final Ball ball) {
        this.lastHit = ball;
    }

    public Optional<Ball> getLastHit() {
        return Optional.ofNullable(this.lastHit);
    }

    public static void checkCollision(final Ball a, final Ball b) {
        double dx   = b.getPosition().getX() - a.getPosition().getX();
        double dy   = b.getPosition().getY() - a.getPosition().getY();
        double dist = Math.hypot(dx, dy);
        double minD = a.getRadius() + b.getRadius();
        
        /* 
         * There is a collision if the distance between the two balls is less than the sum of the radii 
         * 
         */
        if (dist < minD && dist > 1e-6)  {

	        /* 
	         * Collision case - what to do:
	         * 
	         * 1) solve overlaps, moving balls 
	         * 2) update velocities
	         * 
	         */
	        
        	/* dvn = V2d(nx,ny) = dv unit vector */
    
        	double nx = dx / dist;
	        double ny = dy / dist;
	
	        /* 
	         * 
	         * Update positions to solve overlaps, moving balls along dvn
	         * - the displacements is proportional to the mass
	         * 
	         */
	        double overlap = minD - dist;
	        double totalM  = a.getMass() + b.getMass();
	
	        double a_factor = overlap * (b.getMass() / totalM);
	        double a_deltax = nx * a_factor; 
	        double a_deltay = ny * a_factor; 
	        
	        a.setPosition(a.getPosition().getX() - a_deltax, a.getPosition().getY() - a_deltay);
	        
	        double b_factor = overlap * (a.getMass() / totalM);
	        double b_deltax = nx * b_factor; 
	        double b_deltay = ny * b_factor; 
	
	        b.setPosition(b.getPosition().getX() + b_deltax, b.getPosition().getY() + b_deltay);
	
	        /* Update velocities  */
	        
	        /* relative speed along the normal vector*/
	
	        double dvx = b.getVelocity().getX() - a.getVelocity().getX();
	        double dvy = b.getVelocity().getY() - a.getVelocity().getY(); 
	        double dvn = dvx * nx + dvy * ny;
	
	        if (dvn <= 0) { /* if not already separating, update velocities */
                a.hit(b);
                b.hit(a);
	        	double imp = -(1 + Globals.RESTITUTION_COEFFICIENT) * dvn / (1.0/a.getMass() + 1.0/b.getMass());        
	        	a.setVelocity(a.getVelocity().getX() - (imp / a.getMass()) * nx, a.getVelocity().getY() - (imp / a.getMass()) * ny);                
	        	b.setVelocity(b.getVelocity().getX() + (imp / b.getMass()) * nx, b.getVelocity().getY() + (imp / b.getMass()) * ny);
	        }
        }
    }
    
}
