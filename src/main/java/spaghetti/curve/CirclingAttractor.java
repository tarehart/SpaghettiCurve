package spaghetti.curve;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

public class CirclingAttractor extends AttractorAnimator {

	private int radius;
	protected final int maxRadius;
	protected final int minRadius;
	private float omega; // radians per second
	private float theta = 0;
	protected float vx;
	protected float vy;
	protected float centerx;
	protected float centery;
	
	protected Component component;
	
	public CirclingAttractor(CurveAttractor a, Component c, float vx, float vy, int maxRadius, int minRadius, float omega) {
		this(c, vx, vy, maxRadius, minRadius, omega);
		this.attractor = a;
	}
	
	public CirclingAttractor(Component c, float vx, float vy, int maxRadius, int minRadius, float omega) {
		super();
		component = c;
		this.maxRadius = maxRadius;
		this.minRadius = minRadius;
		this.omega = omega;
		this.vx = vx;
		this.vy = vy;
		
		this.temperament = Temperament.Chaotic;
	}

	@Override
	public void update() {
		
		updateLinear();
		updateCircular();

	}
	
	protected void updateLinear() {
		centerx += vx;
		centery += vy;
		
		
		//Rectangle r = component.getBounds();
		Dimension dim = component.getSize();
		Rectangle x = new Rectangle(minRadius, minRadius, dim.width - 2 * minRadius, dim.height - 2 * minRadius);
		
		if (centerx > x.getMaxX()) {
			centerx = (float)x.getMaxX();
			vx *= -1;
		} else if(centerx < x.getMinX()) {
			centerx = (float)x.getMinX();
			vx *= -1;
		}
		
		if (centery > x.getMaxY()) {
			centery = (float)x.getMaxY();
			vy *= -1;
		} else if(centery < x.getMinY()) {
			centery = (float)x.getMinY();
			vy *= -1;
		}
		
	}
	
	protected void updateCircular() {
		theta += omega;
		radius = (int) ((Math.sin(theta * 0.3) + 1) / 2 * (maxRadius - minRadius) + minRadius);
		
		attractor.x = (float) (centerx + Math.cos(theta) * radius);
		attractor.y = (float) (centery + Math.sin(theta) * radius);
	}

}
