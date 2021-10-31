package main;

import curve.BouncingAttractor;
import curve.CirclingAttractor;
import curve.Curve;
import curve.CurveAttractor;
import curve.SteadyLoopAttractor;
import curve.WallCirclingAttractor;
import curve.WiggleLoopAttractor;
import gui.CurveCanvas;
import gui.CurveFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CurveFrame frame = new CurveFrame();
		CurveCanvas canvas = frame.getCanvas();

		
		// 1, 1.32
		// .3, 1.7
		// .3, 1.8
		
		//Curve.maxSpacing = 5;
		//Curve.minSpacing = 4;
		
		canvas.addCurve(new Curve());
		CurveAttractor attractor = new CurveAttractor(.035f, 60);
		CurveAttractor weak1 = new CurveAttractor(.03f, 60);
		CurveAttractor big = new CurveAttractor(.035f, 100);
		CurveAttractor weak2 = new CurveAttractor(.03f, 60);
		CurveAttractor attractor5 = new CurveAttractor(.03f, 60);
		CurveAttractor attractor6 = new CurveAttractor(.035f, 60);
		CurveAttractor attractor7 = new CurveAttractor(.03f, 60);
		CurveAttractor attractor8 = new CurveAttractor(.03f, 60);
		
		//BouncingAttractor ba = new BouncingAttractor(attractor8, canvas, 1f, 1.158f);
		CirclingAttractor ca = new CirclingAttractor(canvas, .14f, .23f, 80, 60, .025f);
		CirclingAttractor ca2 = new WallCirclingAttractor(canvas, .5f, 40, 30, .03f);
		//BouncingAttractor ba2 = new BouncingAttractor(attractor6, canvas, .2f, 1.6f);
		
		//SteadyLoopAttractor sa = new SteadyLoopAttractor(attractor5, canvas, 105, 290, 80, .03f);
		//WiggleLoopAttractor srev = new WiggleLoopAttractor(attractor6, canvas, 105, 290, 60, 40, -.035f);
		
		//SteadyLoopAttractor sa3 = new SteadyLoopAttractor(attractor7, canvas, 695, 110, 80, .03f);
		
		SteadyLoopAttractor sa2 = new SteadyLoopAttractor(weak1, canvas, 300, 150, 120, .019f);

		SteadyLoopAttractor sa4 = new SteadyLoopAttractor(weak2, canvas, 300, 150, 60, -.039f);
		
		canvas.addAttractor(ca);
		//canvas.addAttractor(sa);
		canvas.addAttractor(sa2);
		//canvas.addAttractor(sa3);
		canvas.addAttractor(sa4);
		//canvas.addAttractor(ba);
		//canvas.addAttractor(srev);
		canvas.addAttractor(ca2);
		//canvas.addAttractor(ba2);
		
		
		frame.setVisible(true);
		
		canvas.startLoop();

	}

}
