package svg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import math.Vector;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import curve.Curve;

public class SVGExporter {

	//private static final int RENDER_DPI = 200;
	
	private static final int STROKE_LENGTH = 10;
	private static final float LINE_THICKNESS = 1;

	public static void CurveToSVG(Curve c, File f) {
		// Get a DOMImplementation.
        DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = domImpl.createDocument(svgNS, "svg", null);

        
        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);

        // Ask the test to render into the SVG Graphics2D implementation.
        //TestSVGGen test = new TestSVGGen();
        //test.paint(svgGenerator);
        paintCurve(c, svgGenerator);

        // Finally, stream out SVG to the standard output using
        // UTF-8 encoding.
        boolean useCSS = false; // we want to use CSS style attributes
        Writer out = null;
		try {
			out = new FileWriter(f);
			//out = new OutputStreamWriter(System.out, "UTF-8");
			svgGenerator.stream(out, useCSS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
	}
	
//	public static void CurveToPNG(Curve c, File f, Rectangle areaOfInterest) {
//		File svg = new File(f.getAbsolutePath() + ".svg");
//		CurveToSVG(c, svg);
//		
//		SVGtoPNG(svg, f, 200 * 48, 200 * 24, areaOfInterest);
//		
//		
//	}
	
	private static void paintCurve(Curve c, SVGGraphics2D g) {
		
		Curve.Point prev = null;
		int pointNo = 0;
		int curvePoints = c.getNumPoints();
		
		GeneralPath path = new GeneralPath();
		g.setStroke(new BasicStroke(LINE_THICKNESS));
		
		int strokecounter = 0;
		
		for (Curve.Point p : c) {
			if (prev == null) { // first point only
				moveWithOffset(path, p, pointNo);
				//path.moveTo(p.getX(), p.getY());
				g.setColor(Color.getHSBColor(0, .5f, .7f ));
				//pointNo++;
				
			} else if (strokecounter >= STROKE_LENGTH) { // time for a new stroke
				lineWithOffset(path, p, pointNo);
				//path.lineTo(p.getX(), p.getY());  // wrap up the last one
				g.draw(path); // spit it out
				
				path = new GeneralPath(); // start a new path
				//path.moveTo(p.getX(), p.getY());
				moveWithOffset(path, p, pointNo);
				float progress = pointNo * 1.0f / curvePoints;
				//float brightness = (float) (Math.random() * .7);
				float brightness = (float) Math.sin(progress * 1000) * .4f + .4f;
				g.setColor(Color.getHSBColor(progress, .5f, brightness ));
				strokecounter = 0;
				
			} else {  // general case
				//path.lineTo(p.getX(), p.getY());
				lineWithOffset(path, p, pointNo);
				strokecounter++;
			}
			
			prev = p;
			pointNo++;
		}
		
		g.draw(path);
		
	}
	
	private static void moveWithOffset(GeneralPath path, Curve.Point p, int pointNum) {
		Vector offsetLoc = getOffsetLocation(p, pointNum);
		
		path.moveTo(offsetLoc.x, offsetLoc.y);
	}
	
	private static void lineWithOffset(GeneralPath path, Curve.Point p, int pointNum) {
		Vector offsetLoc = getOffsetLocation(p, pointNum);
		
		path.lineTo(offsetLoc.x, offsetLoc.y);
	}
	
	private static Vector getOffsetLocation(Curve.Point p, int pointNum) {
		float x = p.getX();
		float y = p.getY();
		
		return new Vector(x + (float)Math.cos(pointNum * .001) * 5, y + (float)Math.sin(pointNum * .001) * 5);
	}

	/**
	 * 
	 * @param svgIn existing svg file
	 * @param pngOut intended png file
	 * @param width in pixels
	 * @param height in pixels
	 * @param originalDimensions in pixels
	 */
	public static void SVGtoPNG(File svgIn, File pngOut, int width, int height) {

		PNGTranscoder trans = new PNGTranscoder();
		trans.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float)width);
		trans.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float)height);
		//trans.addTranscodingHint(PNGTranscoder.KEY_AOI, originalDimensions);
		
		

		try {
			
			TranscoderInput input = new TranscoderInput(new FileInputStream(svgIn));
			OutputStream out = new FileOutputStream(pngOut);
			TranscoderOutput output = new TranscoderOutput(out);
			trans.transcode(input, output);
			
			out.flush();
			out.close();
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TranscoderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
