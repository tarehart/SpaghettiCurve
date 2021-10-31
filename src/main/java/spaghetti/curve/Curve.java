package spaghetti.curve;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import spaghetti.math.Vector;

public class Curve implements Iterable<Curve.Point> {


	public static float minSpacing = 5;
	Point first;
	Point last;
	public static float maxSpacing = 10;
	private int numPoints;
	public PartitionGrid grid;

	//private LinkedList<Point> dirtyPoints = new LinkedList<Point>();



	public Curve() {
		first = new Point(0, 0);
		last = new Point(600, 300);
		first.next = last;
		last.prev = first;
		numPoints = 2;

		grid = new PartitionGrid(this, Curve.rectFromPoints(first, last));
		subdivideAsNecessary();



	}

	public Curve(Point a, Point b) {
		first = a;
		last = b;
		numPoints = 2;

		grid = new PartitionGrid(this, Curve.rectFromPoints(a, b));
		subdivideAsNecessary();


	}


	public void movePoint(Point p, Vector translation) {
		if (translation == null || (translation.x == 0 && translation.y == 0))  return;

		Vector previousPoint = p.createVector();

		p.x += translation.x;
		p.y += translation.y;

		grid.processPointMove(p, previousPoint);

		//dirtyPoints.add(p);

	}


	public void mergeClosePoints() {
		Point current = first;
		while (current != null && current.next != last) {
			if (Curve.distance(current, current.next) < minSpacing) {
				grid.removePoint(current.next);
				current.next = current.next.next;
				current.next.prev = current;
				numPoints--;
				countme();
			} else {
				current = current.next;
			}


		}

	}

	private void countme() {

	}

	public List<Point> subdivideAsNecessary(Iterable<Point> pointsMoved) {

		List<Point> newPoints = new LinkedList<Point>();

		Point prev = null;

		for (Point p : pointsMoved) {
			if (prev == null || prev != p.prev)
				newPoints.addAll(subdivideAsNecessary(p.prev, p));
			newPoints.addAll(subdivideAsNecessary(p, p.next));
			prev = p;
		}

		grid.addNewPoints(newPoints);

		return newPoints;
	}

	public List<Point> subdivideAsNecessary() {
		return subdivideAsNecessary(this);
	}

	private List<Point> subdivideAsNecessary(Point a, Point b) {

		List<Point> newPoints = new LinkedList<Point>();

		if (a == null || b == null) return newPoints;

		assert a.next == b || a.prev == b : "Tried to subdivide non-adjacent point";



		float distance = Curve.distance(a, b);

		if (distance < maxSpacing) return newPoints;

		if (distance < maxSpacing * 2) {
			Point n = new Point((a.x + b.x) / 2, (a.y + b.y)/2);
			insertBefore(n, b);
			newPoints.add(n);
		} else {

			int numSegs = (int) Math.ceil((distance / maxSpacing));

			float xStep = (b.x - a.x) / numSegs;
			float yStep = (b.y - a.y) / numSegs;

			for (int i = 1; i < numSegs; i++) {
				float x = a.x + xStep * i;
				float y = a.y + yStep * i;
				Point n = new Point(x, y);
				insertBefore(n, b);
				newPoints.add(n);
			}
		}




		return newPoints;

	}

//	private Point interpolate(Point a, Point b) {
//
//
//		float x = (a.x + b.x) / 2;
//		float y = (a.y + b.y) / 2;
//
//		return new Point(x, y);
//	}



	private void insertBefore(Point newPoint, Point existingPoint) {

		Point a = existingPoint.prev;
		if (a != null) {
			a.next = newPoint;
			newPoint.prev = a;
		}

		numPoints++;

		newPoint.next = existingPoint;
		existingPoint.prev = newPoint;
	}



	@Override
	public Iterator<Point> iterator() {
		return new Iterator<Point>() {

			private Point current = first;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public Point next() {
				Point ret = current;
				current = current.next;
				return ret;
			}

			@Override
			public void remove() {
				// Do nothing
			}
		};
	}

	public int getNumPoints() {
		return numPoints;
	}

	public List<Point> getLocalPoints(float x, float y, float dragDetectionThreshold) {
		return grid.getAffectedPoints((int)x, (int)y, (int)dragDetectionThreshold);
	}

	public class Point {

		private float x = 0;
		private float y = 0;
		private Point next = null;
		private Point prev = null;

		public int freezeCount = 0;


		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public Point getNext() {
			return next;
		}

		public Point getPrev() {
			return prev;
		}

		public Vector createVector() {
			return new Vector(x, y);
		}

		public String toString() {
			return "(" + x + ", " + y + ")";

		}

	}

	public static float distance(Point a, Point b) {
		return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}


	public static float distance(Point p, float x, float y) {
		return (float) Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
	}

	private static Rectangle rectFromPoints(Point a, Point b) {

		return new Rectangle(
				(int)Math.min(a.x, b.x),
				(int)Math.min(a.y, b.y),
				(int)Math.abs(a.x - b.x),
				(int)Math.abs(a.y - b.y));

	}


}
