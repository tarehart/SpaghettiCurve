package curve;

import java.util.ArrayList;
import java.util.List;

import math.Vector;
import curve.Curve.Point;

public class CurveAttractor {
	
	public static final float DEFAULT_STRENGTH = .03f;
	public static final float DEFAULT_RADIUS = 60;
	private static final float FREEZE_SPEED = .75f;
	private static final float DRAG_DETECTION_THRESHOLD = 8;
	private static final float EXPLOSION_THRESHOLD = 30;
	private static final int FREEZE_COUNT = 25; // number of frames to freeze a co-dragged point
	private static final int FREEZE_RANGE = 50;
	private float strength;
	private float radius;
	private float innerSanctum;
	
	public float x;
	public float y;
	
	public boolean enabled;

	public CurveAttractor(float strength, float radius) {
		this(strength, radius, 0);
	}
	
	public CurveAttractor(float strength, float radius, float innerSanctum) {
		this.strength = strength;
		this.radius = radius;
		this.innerSanctum = innerSanctum;
	}
	
	
	public CurveAttractor() {
		this(DEFAULT_STRENGTH, DEFAULT_RADIUS);
	}

	public void attract(List<Curve> curves) {
		for (Curve c: curves) {
			attract(c);
		}
	}
	
	
	private void attract(Curve c) {
		
		if (!enabled) return;
		
		List<Point> pointsMoved = new ArrayList<Point>();
		List<Point> pointsDragged = new ArrayList<Point>();
		List<Point> pointsExploded = new ArrayList<Curve.Point>();
		Point closest = null;
		float closestD = Float.MAX_VALUE;
		
		//int loopcount = 0;
		
		for(Point p : c.getLocalPoints(x, y, radius)) {
		//for(Point p: c) {
			
			//loopcount++;
			float d = attract(p, c);
			if (d >= 0) {
				
				pointsMoved.add(p);
				
				
				if (d < DRAG_DETECTION_THRESHOLD) {
					if (closest == null) {
						closest = p;
						closestD = d;
					}
					else if (d < closestD) {
						closest = p;
						closestD = d;
					}
					
					pointsDragged.add(p);
				}
				else if (d < EXPLOSION_THRESHOLD) {
					pointsExploded.add(p);
				}
			}
		}
		
		explode(pointsExploded, c);
		
		//System.out.println("Loop count: " + loopcount + " total points: " + c.getNumPoints());
		
//		for (Point p:c) {
//			if (!(Curve.distance(p, x, y) > radius || pointsMoved.contains(p))) {
//				c.getLocalPoints(x, y, radius);
//			}
//
//			assert p == c.first || (p.getPrev() != null && p.getPrev().getNext() == p) : "Linked list broke";
//			
//			assert p == c.last || (p.getNext() != null && p.getNext().getPrev() == p) : "Linked list broke";
//			
//		}
//		
//		assert c.first.getNext().getPrev() == c.first : "Linked list broke";
//		assert c.last.getPrev().getNext() == c.last : "Linked list broke";
		
		for (Point p : pointsDragged) {
			if (p != closest) setSectionFreeze(p, FREEZE_RANGE, FREEZE_COUNT);
		}
		
		if (closest != null) {
			setSectionFreeze(closest, FREEZE_RANGE, 0); // unfreeze the section that's allowed to be dragged
		}
		
		c.subdivideAsNecessary(pointsMoved);
		
		
		
		//c.mergeClosePoints(); //gonna do this elsewhere
		
	}


	private void explode(List<Point> pointsMoved, Curve c) {
		for (Point p: pointsMoved) {
			for (Point other: pointsMoved) {
				if (p == other || p.getNext() == other || p.getPrev() == other) {
					continue;
				}
				Vector otherToP = p.createVector().subtract(other.createVector());
				Vector norm = otherToP.norm();
				c.movePoint(p, norm.scale(.006f * (EXPLOSION_THRESHOLD - otherToP.mag()) / pointsMoved.size()));
				
			}
			
		}
		
	}

	private void setSectionFreeze(Point p, int range, int freezeCount) {
		
		p.freezeCount = freezeCount;
		
		Point curr = p.getNext();
		for (int i = 0; i < range; i++) {
			if (curr == null) break;
			curr.freezeCount = freezeCount;
			curr = curr.getNext();
		}
		
		curr = p.getPrev();
		for (int i = 0; i < range; i++) {
			if (curr == null) break;
			curr.freezeCount = freezeCount;
			curr = curr.getPrev();
		}
	}

	

	/**
	 * Returns the initial distance before attraction, or -1
	 * if the point was too far away to be attracted.
	 * @param p
	 * @return
	 */
	private float attract(Point p, Curve c) {
		
		if (earlyAbort(p)) return -1;
		
//		if (p.freezeCount > 0) {
//			p.freezeCount--;
//			return -1;
//		}
		
		if (p == c.first || p == c.last) return -1; // leave the start and end where they are.
		
		float d = Curve.distance(p, x, y);
		
		if (d > radius || d < innerSanctum) {
			return -1;
		}
		
		
		Vector meVec = new Vector(x, y);
		Vector pVec = p.createVector();
		
		Vector toMe = meVec.subtract(pVec).norm();
		
		float scale = (radius - d) * strength;
		
		scale -= scale * Math.random() * .1; // randomly lose up to 10% strength (for diversity)
		
		
		if (p.freezeCount > 0) {
			scale *= FREEZE_SPEED;
			p.freezeCount--;
		}
		
		Vector move = toMe.scale(scale);
		
		c.movePoint(p, move);
		
		return d;
		
	}

	private boolean earlyAbort(Point p) {
		float px = p.getX();
		float py = p.getY();
		
		if (x - radius > px) return true;
		if (x + radius < px) return true;
		if (y - radius > py) return true;
		if (y + radius < py) return true;
		
		return false;
		
	}


	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	
	
}
