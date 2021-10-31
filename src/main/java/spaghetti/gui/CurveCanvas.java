package spaghetti.gui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import spaghetti.curve.AttractorAnimator;
import spaghetti.curve.Curve;
import spaghetti.curve.CurveAttractor;

public class CurveCanvas extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8757036231780454215L;
//	private static final int POINT_RADIUS = 3;
	private int framesDropped = 0;
	
	private List<Curve> curves = new LinkedList<Curve>();
	private List<AttractorAnimator> attractors = new LinkedList<AttractorAnimator>();
	
	private boolean isRunning = false;
	private boolean buttonDown = false;
	
	private boolean playing;
	
	private long framecount = 0;
	private int mergeSchedule = 3;
	
	
	
	private CurveAttractor manualAttractor = new CurveAttractor(.03f, 60, 3);
	
	
	
	
	public CurveCanvas() {
		super();
		
		
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				buttonDown = false;
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				buttonDown = true;
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				buttonDown = false;
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				attractor.setX(e.getX());
				attractor.setY(e.getY());
				attractor.attract(curves);
				repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		
		
		
	}
	
	
	public void setTemperament(EnumSet<AttractorAnimator.Temperament> temperament) {
		for (AttractorAnimator a : attractors) {
			a.setAttractorEnabled(temperament.contains(a.temperament));
		}
	}
	
	
	public void startLoop() {
		isRunning = true;
		
		
		while (isRunning) {
			
			if (playing) {
			
				for (AttractorAnimator a : attractors) {
					a.update();
					a.getAttractor().attract(curves);
				}
				
				if (buttonDown || framesDropped == 0 || framecount % framesDropped == 0) {
				//if (buttonDown) {
					paint(getGraphics());
				}
				
				if (framecount % mergeSchedule == 0) {
					for (Curve c : curves) {
						c.mergeClosePoints();
					}
				}
				
				framecount++;
			}
				
			Thread.yield();
			
			
//				try {
//					Thread.sleep(16);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			
			
			
		}
		
	}



//	private void manualLoop() {
//		if (buttonDown) {
//			manualAttractor.setX(getMousePosition().x);
//			manualAttractor.setY(getMousePosition().y);
//			manualAttractor.attract(curves);
//			paint(getGraphics());
//			Thread.yield();
//		} else {
//			try {
//				Thread.sleep(16);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	
	public void paintCurves(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int totalPoints = 0;
		
		for (Curve c : curves) {
			
			int curvePoints = c.getNumPoints();
			totalPoints += curvePoints;
			
			int pointNo = 0;
			
			Curve.Point prev = null;
			
			for (Curve.Point p : c) {
				if (prev != null) {
					
					float progress = pointNo++ * 1.0f / curvePoints;
					float brightness = (float) (Math.sin(pointNo * .05) * .3 + .5);
					g2.setColor(Color.getHSBColor(progress, .5f, brightness ));
					
					drawLine(prev, p, g2);
				}
				prev = p;
			}
			
//			for (Point p : c) {
//				drawPoint(p, g2);
//				
//			}
		}
		
		g.setColor(Color.black);
		g.drawString("Points: " + totalPoints, 20, 20);
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		 Dimension dim = getSize(); 
		  
         Image offscreen = createImage(dim.width, dim.height); 
        
         Graphics bufferGraphics = offscreen.getGraphics(); 
		
        this.setBackground(Color.white);
         
        bufferGraphics.clearRect(0, 0, dim.width, dim.width); 
       
        paintCurves(bufferGraphics);
        
        paintAttractors(bufferGraphics);
        
        g.drawImage(offscreen,0,0,this); 
	}
	
	
	private void paintAttractors(Graphics g) {
		
		
		g.setColor(Color.black);
		
		for (AttractorAnimator a : attractors) {
			g.fillOval((int)a.getAttractor().x - 2, (int)a.getAttractor().y - 2, 8, 8);
		}
		g.fillOval((int)manualAttractor.x - 2, (int)manualAttractor.y - 2, 8, 8);
		
	}



	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	private void drawLine(Curve.Point prev, Curve.Point p, Graphics2D g) {
		//g.setColor(Color.blue);
		g.setStroke(new BasicStroke(4));
		g.drawLine((int)prev.getX(), (int)prev.getY(), (int)p.getX(), (int)p.getY());
		
	}


//	private void drawPoint(Curve.Point p, Graphics2D g) {
//		g.setColor(Color.BLACK);
//		g.fillOval(p.getX() - POINT_RADIUS, p.getY() - POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);
//	}


	public void addCurve(Curve curve) {
		curves.add(curve);		
	}
	
	public void addAttractor(AttractorAnimator a) {
		attractors.add(a);
	}



	public void playPause() {
		playing = !playing;
	}
	


	public void setFramesDropped(int frames) {
		framesDropped = frames;
	}



	public Curve getCurve() {
		return curves.get(0);
	}

}
