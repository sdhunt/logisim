/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.draw.tools;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.slf4j.Logger;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A Scalable Vector Graphics (SVG) based icon.
 */
public class SVGIcon implements Icon {
    private static final Logger logger = getLogger(SVGIcon.class);

    private static final String PATH_PREFIX = "/logisim/icons/";

    private static final double ZERO = 0.0;
    private static final double ONE = 1.0;
    private static final int HEIGHT = 16;
    private static final int WIDTH = 16;

    private GraphicsNode svgIcon = null;


    /**
     * Creates a new instance of an SVG icon, loading from the definition
     * file at the specified path.
     *
     * @param path the SVG definition file path
     */
    public SVGIcon(String path) {
        try {
            // Quick and dirty hack. We should probably use a path resolver.
            String svgPath = (path.startsWith(PATH_PREFIX))
                    ? path : PATH_PREFIX + path;
            URL url = SVGIcon.class.getResource(svgPath);
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(url.toString());
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            this.svgIcon = builder.build(ctx, doc);

        } catch (IOException e) {
            logger.warn("Failed to load SVG icon from path {}", path);
        }
    }

    @Override
    public int getIconHeight() {
        return HEIGHT;
    }

    @Override
    public int getIconWidth() {
        return WIDTH;
    }

    private void paintSvgIcon(Graphics2D g2, int x, int y,
                              double scaleX, double scaleY) {
        AffineTransform transform =
                new AffineTransform(scaleX, ZERO, ZERO, scaleY, x, y);
        svgIcon.setTransform(transform);
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        svgIcon.paint(g2);
    }

    @Override
    public void paintIcon(Component comp, Graphics g, int x, int y) {
        paintSvgIcon((Graphics2D) g, x, y, ONE, ONE);
    }
}
