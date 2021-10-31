package curve;

import java.awt.Component;

public class SteadyLoopAttractor extends AttractorAnimator {
	private final int radius;
	private float omega; // radians per second
	private float theta = 0;
	private float centerx;
	private float centery;
	
	
	public SteadyLoopAttractor(CurveAttractor a, Component c, float x, float y, int radius, float omega) {
		super(a);
		this.radius = radius;
		this.omega = omega;
		this.centerx = x;
		this.centery = y;
		
		this.temperament = Temperament.Orderly;
	}

	@Override
	public void update() {
		
		
		
		theta += omega;
		
		
		
		attractor.x = (float) (centerx + Math.cos(theta) * radius);
		attractor.y = (float) (centery + Math.sin(theta) * radius);
		
	}
}
