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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link HorizontalSplitPane} and {@link VerticalSplitPane}.
 * <p>
 * NOTE: These are not unit tests in the traditional sense (they don't assert
 * anything), and have been annotated with @Ignore to remove them from the
 * regression suite. However, when a test method is run individually (e.g. from
 * the IDE), a JFrame will open and display the results of the test method, so
 * that a visual inspection may be made of the method under test.
 */
public class SplitPaneTest extends AbstractGraphicsTest {

    private static final int SIZE = 300;

    private Box one;
    private Box two;

    @Before
    public void setUp() {
        one = mkBox(SIZE, SIZE, Color.RED);
        two = mkBox(SIZE, SIZE, Color.YELLOW);
    }

    @Ignore(DONT_RUN)
    @Test
    public void horizontal() {
        HorizontalSplitPane hsp = new HorizontalSplitPane(one, two, 0.5);
        frame = new ClosableTestFrame(hsp);
        frame.displayMe();
    }

    @Ignore(DONT_RUN)
    @Test
    public void vertical() {
        VerticalSplitPane vsp = new VerticalSplitPane(one, two, 0.5);
        frame = new ClosableTestFrame(vsp);
        frame.displayMe();
    }

    @Test
    public void horizSetAtQuarter() {
        title("horizontal split pane 0.25");
        HorizontalSplitPane hsp = new HorizontalSplitPane(one, two, 0.25);
        assertThat(hsp.getFraction(), is(equalTo(0.25)));
    }

    @Test
    public void horizClipLow() {
        title("horizontal split pane < 0.0");
        HorizontalSplitPane hsp = new HorizontalSplitPane(one, two, -0.33);
        assertThat(hsp.getFraction(), is(equalTo(0.0)));
    }

    @Test
    public void horizClipHigh() {
        title("horizontal split pane > 1.0");
        HorizontalSplitPane hsp = new HorizontalSplitPane(one, two, 2.71);
        assertThat(hsp.getFraction(), is(equalTo(1.0)));
    }

    @Test
    public void vertSetAtQuarter() {
        title("vertical split pane 0.25");
        VerticalSplitPane vsp = new VerticalSplitPane(one, two, 0.25);
        assertThat(vsp.getFraction(), is(equalTo(0.25)));
    }

    @Test
    public void vertClipLow() {
        title("vertical split pane < 0.0");
        VerticalSplitPane vsp = new VerticalSplitPane(one, two, -0.33);
        assertThat(vsp.getFraction(), is(equalTo(0.0)));
    }

    @Test
    public void vertClipHigh() {
        title("vertical split pane > 1.0");
        VerticalSplitPane hsp = new VerticalSplitPane(one, two, 2.71);
        assertThat(hsp.getFraction(), is(equalTo(1.0)));
    }
}
