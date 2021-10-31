package curve;

import java.awt.Component;

public class WiggleLoopAttractor extends AttractorAnimator {

	private int radius;
	private final int maxRadius;
	private final int minRadius;
	private float omega; // radians per second
	private float theta = 0;
	private float centerx;
	private float centery;
	
	
	public WiggleLoopAttractor(CurveAttractor a, Component c, float x, float y, int maxRadius, int minRadius, float omega) {
		super(a);
		this.maxRadius = maxRadius;
		this.minRadius = minRadius;
		this.omega = omega;
		centerx = x;
		centery = y;
		
		this.temperament = Temperament.Orderly;
	}

	@Override
	public void update() {
		
		
		theta += omega;
		radius = (int) ((Math.sin(theta * 8.2) + 1) / 2 * (maxRadius - minRadius) + minRadius);
		
		attractor.x = (float) (centerx + Math.cos(theta) * radius);
		attractor.y = (float) (centery + Math.sin(theta) * radius);


	}
}
