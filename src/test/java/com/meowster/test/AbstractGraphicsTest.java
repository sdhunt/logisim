/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.meowster.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Superclass of unit tests dealing with swing components.
 */
public class AbstractGraphicsTest extends AbstractTest {
    protected static final String DONT_RUN =
            "don't run as part of the regression suite";

    protected static final Point FRAME_LOCATION = new Point(600, 100);
    protected static final int SHOW_FOR_MS = 4000;
    protected static final int CANVAS_DIM = 400;

    protected static final int HALF = CANVAS_DIM / 2;
    protected static final int QTR = HALF / 2;
    protected static final int TQTR = HALF + QTR;
    protected static final int T0 = 0;
    protected static final int T1 = CANVAS_DIM / 10;
    protected static final int T2 = CANVAS_DIM / 5;
    protected static final int T3 = T1 * 3;
    protected static final int T4 = T2 * 2;
    protected static final int T5 = CANVAS_DIM / 2;
    protected static final int T6 = T3 * 2;
    protected static final int T7 = T1 * 7;
    protected static final int T8 = T2 * 4;
    protected static final int T9 = T3 * 3;
    protected static final int TT = CANVAS_DIM;

    /**
     * A frame we can instantiate and populate with something we are "testing".
     */
    public class ClosableTestFrame extends JFrame implements ActionListener {

        volatile boolean quitting = false;

        JPanel content = new JPanel(new BorderLayout());
        JPanel control = new JPanel();
        JButton quit = new JButton("Quit");

        public ClosableTestFrame(JComponent comp) {
            super("Test Frame");

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            quit.addActionListener(this);
            control.add(quit);
            content.add(comp, BorderLayout.CENTER);
            content.add(control, BorderLayout.SOUTH);
            getContentPane().add(content);
            pack();
        }

        public void displayMe() {
            setLocation(FRAME_LOCATION);
            setVisible(true);
        }

        public void waitBeforeQuitting() {
            while (!quitting) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void displayMeAndWait() {
            displayMe();
            waitBeforeQuitting();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            quitting = true;
        }
    }

    /**
     * A canvas on which we can render stuff...
     */
    public class TestCanvas extends JPanel {
        BufferedImage img;
        Dimension dim;
        Graphics2D g2;

        public TestCanvas() {
            img = new BufferedImage(CANVAS_DIM, CANVAS_DIM, TYPE_INT_ARGB);
            dim = new Dimension(CANVAS_DIM, CANVAS_DIM);
            g2 = img.createGraphics();
            init(g2);
        }

        private void init(Graphics2D g2) {
            g2.setBackground(Color.WHITE);
            g2.setColor(Color.BLACK);
        }

        /**
         * Returns a reference to the graphics context for the canvas.
         *
         * @return the graphics context
         */
        public Graphics2D g2() {
            return g2;
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

    /**
     * Returns a colored box with a preferred size.
     */
    public class Box extends JPanel {
        private final Dimension dim;
        private final Dimension minDim;
        private final Dimension maxDim;

        private Box(int width, int height, Color color) {
            dim = new Dimension(width, height);
            minDim = new Dimension(width / 2, height / 2);
            maxDim = new Dimension(width * 2, height * 2);
            setBackground(color);
        }

        @Override
        public Dimension getPreferredSize() {
            return dim;
        }

        @Override
        public Dimension getMinimumSize() {
            return minDim;
        }

        @Override
        public Dimension getMaximumSize() {
            return maxDim;
        }
    }

    protected ClosableTestFrame frame;
    protected TestCanvas canvas;
    protected Graphics2D g2;

    /**
     * Creates a closable frame and populates it with the test canvas.
     */
    protected void showCanvasInFrame() {
        frame = new ClosableTestFrame(canvas);
        frame.displayMeAndWait();
    }

    /**
     * Returns i tenths of the canvas dimension.
     *
     * @param i the number of tenths required
     * @return the requested size in pixels
     */
    protected int tenth(int i) {
        return CANVAS_DIM / 10 * i;
    }

    /**
     * Returns a box component for the specified width, height and color.
     *
     * @param width  the box width
     * @param height the box height
     * @param color  the box color
     * @return the box component
     */
    protected Box mkBox(int width, int height, Color color) {
        return new Box(width, height, color);
    }
}
