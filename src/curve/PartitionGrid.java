package curve;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import math.Vector;
import curve.Curve.Point;

public class PartitionGrid {

	private static final int gridWidth = 10;
	private static final int gridHeight = 10;
	
	private GridSquare[][] grid;
	private Rectangle interiorBounds;
	private int xlen;
	private int ylen;
	private GridSquare exteriorSquare;
	
	private Curve curve;
	
	public PartitionGrid(Curve c, Rectangle bounds) {
		this.curve = c;
		interiorBounds = bounds;
		grid = new GridSquare[gridHeight][gridWidth];
		
		xlen = interiorBounds.width / gridWidth;
		ylen = interiorBounds.height / gridHeight;
		
		// revise the interior bounds so it lines up perfectly with the squares
		interiorBounds.width = xlen * gridWidth;
		interiorBounds.height = ylen * gridHeight;
		
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				Rectangle r = new Rectangle(xlen * j, ylen * i, xlen, ylen);
				grid[i][j] = new GridSquare(r);
			}
		}
		
		exteriorSquare = new GridSquare(null);
		
		addNewPoints(c);
		
	}
	
	public List<Point> getAffectedPoints(int x, int y, int effectRadius) {
		LinkedList<Point> affected = new LinkedList<Point>();
		Rectangle effectRectangle = new Rectangle(
				x - effectRadius,
				y - effectRadius,
				effectRadius * 2,
				effectRadius * 2);
		
		//int squareCount = 0;
		
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				GridSquare s = grid[i][j];
				if (s.intersects(effectRectangle)) {
//					for (Point p: s.getPoints()) {
//						assert !affected.contains(p);
//					}
					
					affected.addAll(s.getPoints());
					//squareCount++;
				}
			}
		}
		
//		for (Point p: exteriorSquare.getPoints()) {
//			assert !affected.contains(p);
//		}
		
		affected.addAll(exteriorSquare.getPoints());
		
//		System.out.println(affected.size() + " points in " + squareCount + " squares.");
//		System.out.println(affected.toString());
		
//		for (Point p: curve) {
//			assert getContainingSquare(p.getX(), p.getY()).points.contains(p): "Improper grid square containment.";
//			
//		}
		
		return affected;
	}
	
	public void processPointMove(Point p, Vector previousLoc) {
		GridSquare previousSquare = getContainingSquare(previousLoc.x, previousLoc.y);
		GridSquare newSquare = getContainingSquare(p.getX(), p.getY());
		
		if (previousSquare != newSquare) {
			previousSquare.getPoints().remove(p);
			newSquare.points.add(p);
		}
	}
	
	private GridSquare getContainingSquare(float x, float y) {
		if (interiorBounds.contains(x, y)) {
			int xcoord = ((int)x - interiorBounds.x) / xlen;
			int ycoord = ((int)y - interiorBounds.y) / ylen;
			
			return grid[ycoord][xcoord];
			
		}
		return exteriorSquare;
	}
	
	public enum InfiniteDirection {
		NORTH, SOUTH, EAST, WEST
	}
	
	public class GridSquare {
		
		private HashSet<Point> points = new HashSet<Point>();
		protected Rectangle square;
		
		public GridSquare(Rectangle r) {
			square = r;
		}
		
		public boolean intersects(Rectangle r) {
			if (r == null) return false;
			return square.intersects(r);
		}
		
		public Collection<Point> getPoints() {
			return points;
		}
	}

	public void addNewPoints(Iterable<Point> newPoints) {
		for (Point p: newPoints) {
			GridSquare s = getContainingSquare(p.getX(), p.getY());
			s.points.add(p);
		}
	}

	public void removePoint(Point p) {
		getContainingSquare(p.getX(), p.getY()).points.remove(p);
		
	}
	
}
