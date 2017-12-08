/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Unit tests for {@link GraphicsUtil}.
 * <p>
 * NOTE: These are not unit tests in the traditional sense (they don't assert
 * anything), and have been annotated with @Ignore to remove them from the
 * regression suite. However, when a test method is run individually (e.g. from
 * the IDE), a JFrame will open and display the results of the test method, so
 * that a visual inspection may be made of the method under test.
 */
public class GraphicsUtilTest extends AbstractTest {

    private static final String DONT_RUN =
            "don't run as part of the regression suite";

    private static final Point FRAME_LOCATION = new Point(600, 100);
    private static final int SHOW_FOR_MS = 4000;
    private static final int CANVAS_DIM = 400;

    private static final int HALF = CANVAS_DIM / 2;
    private static final int QTR = HALF / 2;
    private static final int TQTR = HALF + QTR;
    private static final int T0 = 0;
    private static final int T1 = CANVAS_DIM / 10;
    private static final int T2 = CANVAS_DIM / 5;
    private static final int T3 = T1 * 3;
    private static final int T4 = T2 * 2;
    private static final int T5 = CANVAS_DIM / 2;
    private static final int T6 = T3 * 2;
    private static final int T7 = T1 * 7;
    private static final int T8 = T2 * 4;
    private static final int T9 = T3 * 3;
    private static final int TT = CANVAS_DIM;

    /**
     * A canvas on which we can render stuff...
     */
    private class TestCanvas extends JPanel {

        BufferedImage img;
        Dimension dim;
        Graphics2D g2;

        TestCanvas() {
            img = new BufferedImage(CANVAS_DIM, CANVAS_DIM, TYPE_INT_ARGB);
            dim = new Dimension(CANVAS_DIM, CANVAS_DIM);
            g2 = img.createGraphics();
            init(g2);
        }

        private void init(Graphics2D g2) {
            g2.setBackground(Color.WHITE);
            g2.setColor(Color.BLACK);
        }

        @Override
        public Dimension getPreferredSize() {
            return dim;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Creating a copy of the Graphics
            // so any reconfiguration we do on
            // it doesn't interfere with what
            // Swing is doing.
            Graphics2D g2 = (Graphics2D) g.create();
            // Drawing the image.
            int w = img.getWidth();
            int h = img.getHeight();
            g2.drawImage(img, 0, 0, w, h, null);
            // At the end, we dispose the
            // Graphics copy we've created
            g2.dispose();
        }
    }

    private TestCanvas canvas;

    private int tenth(int i) {
        return CANVAS_DIM / 10 * i;
    }

    private void showCanvasBriefly() {
        JFrame frame = new JFrame("Test frame");
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setLocation(FRAME_LOCATION);
        frame.setVisible(true);
        try {
            Thread.sleep(SHOW_FOR_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        canvas = new TestCanvas();
    }


    @Ignore(DONT_RUN)
    @Test
    public void basic() {
        title("basic");
        canvas.setBackground(Color.RED);
        showCanvasBriefly();
    }

    @Ignore(DONT_RUN)
    @Test
    public void switchingStrokeWidth() {
        title("switch stroke width");
        canvas.g2.drawLine(T1, QTR, T9, QTR);
        GraphicsUtil.switchToWidth(canvas.g2, 3);
        canvas.g2.drawLine(T1, HALF, T9, HALF);
        GraphicsUtil.switchToWidth(canvas.g2, 5);
        canvas.g2.drawLine(T1, TQTR, T9, TQTR);

        canvas.g2.setColor(Color.BLUE);
        for (int i = 1; i <= 9; i++) {
            GraphicsUtil.switchToWidth(canvas.g2, i);
            canvas.g2.drawLine(tenth(i), QTR, tenth(i), TQTR);
        }
        showCanvasBriefly();
    }
}
