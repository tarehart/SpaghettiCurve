package math;

public class Vector {
	
	
	public static final Vector ZERO = new Vector(0, 0);
	
	public Vector(float x, float y) {
		this.x = x; 
		this.y = y;
	}
	
	public float x;
	public float y;
	
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}
	
	public Vector scale(float s) {
		return new Vector(x * s, y * s);
	}
	
	public float mag() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vector subtract(Vector u) {
		return new Vector(x - u.x, y - u.y);
	}
	
	public Vector norm() {
		float mag = mag();
		return new Vector(x / mag, y / mag);
	}
	

}
