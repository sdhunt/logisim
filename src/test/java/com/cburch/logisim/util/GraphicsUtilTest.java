/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractGraphicsTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;

/**
 * Unit tests for {@link GraphicsUtil}.
 * <p>
 * NOTE: These are not unit tests in the traditional sense (they don't assert
 * anything), and have been annotated with @Ignore to remove them from the
 * regression suite. However, when a test method is run individually (e.g. from
 * the IDE), a JFrame will open and display the results of the test method, so
 * that a visual inspection may be made of the method under test.
 */
public class GraphicsUtilTest extends AbstractGraphicsTest {

    @Before
    public void setUp() {
        canvas = new TestCanvas();
        g2 = canvas.g2();
    }

    @Ignore(DONT_RUN)
    @Test
    public void basic() {
        title("basic");
        canvas.setBackground(Color.RED);
        showCanvasInFrame();
    }

    @Ignore(DONT_RUN)
    @Test
    public void switchingStrokeWidth() {
        title("switch stroke width");
        canvas.setBackground(Color.ORANGE);

        g2.drawLine(T1, QTR, T9, QTR);
        GraphicsUtil.switchToWidth(g2, 3);
        g2.drawLine(T1, HALF, T9, HALF);
        GraphicsUtil.switchToWidth(g2, 5);
        g2.drawLine(T1, TQTR, T9, TQTR);

        g2.setColor(Color.BLUE);
        for (int i = 1; i <= 9; i++) {
            GraphicsUtil.switchToWidth(g2, i);
            g2.drawLine(tenth(i), QTR, tenth(i), TQTR);
        }
        showCanvasInFrame();
    }

    @Ignore(DONT_RUN)
    @Test
    public void drawCenteredArc() {
        title("draw centered arc");
        GraphicsUtil.switchToWidth(g2, 5);
        GraphicsUtil.drawCenteredArc(g2, HALF, HALF, QTR, 90, 270);
        showCanvasInFrame();
    }

    @Ignore(DONT_RUN)
    @Test
    public void textStuff() {
        title("draw text and get bounds");
        String text = "FooBar";

        Font font = g2.getFont().deriveFont(40.0f);

        GraphicsUtil.drawText(g2, font, text, HALF, HALF,
                              GraphicsUtil.H_LEFT,
                              GraphicsUtil.V_BASELINE);

        Rectangle r = GraphicsUtil.getTextBounds(g2, font, text, HALF, HALF,
                                                 GraphicsUtil.H_LEFT,
                                                 GraphicsUtil.V_BASELINE);
        g2.setColor(Color.RED);
        g2.draw(r);
        showCanvasInFrame();

        /*
         * UGH!! This demonstrates that the font has been hard-wired to
         *       sans-serif/plain/10, and totally ignores the font set
         *       on the graphics context!!
         */
    }

    @Ignore(DONT_RUN)
    @Test
    public void drawArrow() {
        title("draw an arrow");
        GraphicsUtil.switchToWidth(g2, 3);
        GraphicsUtil.drawArrow(g2, QTR, QTR, TQTR, TQTR, 20, 30);
        showCanvasInFrame();
    }
}
