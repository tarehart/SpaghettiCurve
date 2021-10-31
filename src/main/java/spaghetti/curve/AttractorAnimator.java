package spaghetti.curve;

public abstract class AttractorAnimator {

	protected CurveAttractor attractor;
	
	public Temperament temperament;
	
	public AttractorAnimator() {
		attractor = new CurveAttractor();
	}
	
	public AttractorAnimator(CurveAttractor c) {
		attractor = c;
	}
	
	public abstract void update();
	
	public CurveAttractor getAttractor() {
		return attractor;
	}
	
	public void setAttractorEnabled(boolean enabled) {
		attractor.enabled = enabled;
	}
	
	public enum Temperament {
		Orderly, Chaotic
	}
	
}
