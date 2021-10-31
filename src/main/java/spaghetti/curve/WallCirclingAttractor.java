package spaghetti.curve;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

public class WallCirclingAttractor extends CirclingAttractor {
	
	protected float speed;
	
	public WallCirclingAttractor(Component c, float speed, int maxRadius, int minRadius, float omega) {
		super(c, speed, speed, maxRadius, minRadius, omega);
		
		this.speed = speed;
	}

	protected void updateLinear() {
		centerx += vx;
		centery += vy;
		
		
		//Rectangle r = component.getBounds();
		Dimension dim = component.getSize();
		Rectangle x = new Rectangle(maxRadius, maxRadius, dim.width - 2 * maxRadius, dim.height - 2 * maxRadius);
		
		if (centerx > x.getMaxX()) {
			centerx = (float)x.getMaxX();
			vx = 0;
			vy = speed;
		} else if(centerx < x.getMinX()) {
			centerx = (float)x.getMinX();
			vx = 0;
			vy = -speed;
		}
		
		if (centery > x.getMaxY()) {
			centery = (float)x.getMaxY();
			vy = 0;
			vx = -speed;
		} else if(centery < x.getMinY()) {
			centery = (float)x.getMinY();
			vy = 0;
			vx = speed;
		}
		
	}


}
