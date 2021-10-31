package svg;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DocumentFactory;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

public class SVGInfo {

	public static Rectangle getDimensions(File svgFile) {
		SVGDocument svgDoc = null;
        UserAgent userAgent;
        DocumentLoader loader;
        BridgeContext ctx;
        GVTBuilder builder;
        GraphicsNode rootGN;
        
        
        DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        
        DocumentFactory f = new SAXDocumentFactory(domImpl, null);
        
        try {
			FileInputStream input = new FileInputStream(svgFile);
		
        
	        svgDoc = (SVGDocument) f.createDocument(svgNS, "svg", svgFile.toURI().toString(), input);
	
	        
	//        svgDoc = new SVGDocum
	        userAgent = new UserAgentAdapter();
	        loader = new DocumentLoader(userAgent);
	        ctx = new BridgeContext(userAgent, loader);
	        ctx.setDynamicState(BridgeContext.DYNAMIC);
	        builder = new GVTBuilder();
	        rootGN = builder.build(ctx, svgDoc);
	        
	        return rootGN.getBounds().getBounds();
        
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
