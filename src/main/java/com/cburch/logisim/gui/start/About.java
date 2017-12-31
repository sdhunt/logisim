/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.start;

import com.cburch.logisim.Logisim;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;

import javax.swing.*;
import java.awt.*;

public class About {
    static final int IMAGE_WIDTH = 200;
    static final int IMAGE_HEIGHT = 200;
    static final Dimension DIMENSION = new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT);
    private static JSVGCanvas svgCanvas = new JSVGCanvas();

    public static JComponent createComponents() {
        final JPanel panel = new JPanel();
        panel.add(svgCanvas);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setBackground(Color.WHITE);
        svgCanvas.setURI(About.class.getResource("/logisim/drawing.svg").toString());
        svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {});
        svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {});
        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {});
        return panel;
    }

    public static void showAboutDialog(JFrame owner) {
        JOptionPane.showMessageDialog(owner, createComponents(),
                                      "Logisim " + Logisim.VERSION_NAME,
                                      JOptionPane.PLAIN_MESSAGE);
    }
}