package curve;

import java.awt.Component;
import java.awt.Dimension;

public class BouncingAttractor extends AttractorAnimator {

	
	// 1, 1.32
	// .3, 1.7
	// .3, 1.8
	
	private float vx = 1.2f;
	private float vy = 1.1f;
	
	private Component component;
	
	public BouncingAttractor(CurveAttractor a, Component c, float vx, float vy) {
		super(a);
		component = c;
		this.vx = vx;
		this.vy = vy;
		
		this.temperament = Temperament.Chaotic;
	}

	@Override
	public void update() {
		
		
		
		attractor.x += vx;
		attractor.y += vy;
		
		Dimension dim = component.getSize();
		
		if (attractor.x > dim.width) {
			attractor.x = dim.width;
			vx *= -1;
		} else if(attractor.x < 0) {
			attractor.x = 0;
			vx *= -1;
		}
		
		if (attractor.y > dim.height) {
			attractor.y = dim.height;
			vy *= -1;
		} else if(attractor.y < 0) {
			attractor.y = 0;
			vy *= -1;
		}

	}

}
