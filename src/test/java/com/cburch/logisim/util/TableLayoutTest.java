/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractGraphicsTest;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TableLayout}.
 */
public class TableLayoutTest extends AbstractGraphicsTest {

    private class Box extends Container {
        private final Dimension dim = new Dimension(100, 100);

        @Override
        public Dimension getSize() {
            return dim;
        }
    }

    private class Thing extends Component {
        private final String id;
        private final int width;
        private final int height;
        private final Dimension prefSize;
        private int bx;
        private int by;
        private int bw;
        private int bh;

        Thing(String id, int w, int h) {
            this.id = id;
            width = w;
            height = h;
            prefSize = new Dimension(w, h);
        }

        @Override
        public String toString() {
            return "Thing{" + id + ":" + width + "x" + height + "}";
        }

        @Override
        public Dimension getPreferredSize() {
            return prefSize;
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            bx = x;
            by = y;
            bw = width;
            bh = height;
            TableLayoutTest.this.print("%s: %d, %d %dx%d", id, bx, by, bw, bh);
        }

        boolean boundsMatch(int x, int y, int w, int h) {
            return x == bx &&
                    y == by &&
                    w == bw &&
                    h == bh;
        }
    }


    private Box container;
    private TableLayout layout;

    @Before
    public void setUp() {
        layout = new TableLayout(2);
        container = new Box();
    }

    @Test
    public void basicLayout() {
        title("basic layout");

        Thing ta = new Thing("A", 20, 20);
        Thing tb = new Thing("B", 30, 30);
        Thing tc = new Thing("C", 40, 40);
        Thing td = new Thing("D", 50, 50);

        layout.addLayoutComponent("A", ta);
        layout.addLayoutComponent("B", tb);
        layout.addLayoutComponent("C", tc);
        layout.addLayoutComponent("D", td);

        Dimension prefSize = layout.preferredLayoutSize(null);
        print(prefSize);
        assertThat(prefSize.width, is(equalTo(90)));
        assertThat(prefSize.height, is(equalTo(80)));

        layout.layoutContainer(container);
        assertThat(ta.boundsMatch(5, 10, 40, 30), is(true));
        assertThat(tb.boundsMatch(45, 10, 50, 30), is(true));
        assertThat(tc.boundsMatch(5, 40, 40, 50), is(true));
        assertThat(td.boundsMatch(45, 40, 50, 50), is(true));
    }

    @Test
    public void maxLayoutSize() {
        title("max layout size");
        Dimension d = layout.maximumLayoutSize(null);
        assertThat(d.width, is(equalTo(Integer.MAX_VALUE)));
        assertThat(d.height, is(equalTo(Integer.MAX_VALUE)));
    }

    @Test
    public void layoutAlignments() {
        title("layout alignments");
        assertThat(layout.getLayoutAlignmentX(null), is(equalTo(0.5f)));
        assertThat(layout.getLayoutAlignmentY(null), is(equalTo(0.5f)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badWeight() {
        layout.setRowWeight(0, -0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badRowIndex() {
        layout.setRowWeight(-2, 0.5);
    }

    @Test
    public void rowWeight() {
        title("row weight");
        Thing ta = new Thing("A", 20, 20);
        Thing tb = new Thing("B", 20, 20);
        Thing tc = new Thing("C", 20, 20);
        Thing td = new Thing("D", 20, 20);

        layout.addLayoutComponent("A", ta);
        layout.addLayoutComponent("B", tb);
        layout.addLayoutComponent("C", tc);
        layout.addLayoutComponent("D", td);

        layout.setRowWeight(1, 0.7);

        layout.layoutContainer(container);
        assertThat(ta.boundsMatch(30, 0, 20, 20), is(true));
        assertThat(tb.boundsMatch(50, 0, 20, 20), is(true));
        assertThat(tc.boundsMatch(30, 20, 20, 20), is(true));
        assertThat(td.boundsMatch(50, 20, 20, 20), is(true));
    }
}
