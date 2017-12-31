/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link Bounds}.
 */
public class BoundsTest extends AbstractTest {

    private static final int TEST_X = 10;
    private static final int TEST_Y = 20;
    private static final int TEST_W = 100;
    private static final int TEST_H = 150;
    private static final int TEST_CX = 60;
    private static final int TEST_CY = 95;

    private static final Bounds TEST_BOUNDS = Bounds.create(TEST_X, TEST_Y,
                                                            TEST_W, TEST_H);

    /*
            0...........................(X)
            :      10            110
            :   20  +---- 100 ----+
            :       |             |
            :      150            |
            :       |             |
            :  170  +-------------+
            :         TEST_BOUNDS
           (Y)
     */

    private Bounds a;
    private Bounds b;

    private Location loc(int x, int y) {
        return Location.create(x, y);
    }


    @Test
    public void emptyBounds() {
        title("empty bounds");
        a = Bounds.EMPTY_BOUNDS;
        print(a);
        assertThat(a.getX(), is(0));
        assertThat(a.getY(), is(0));
        assertThat(a.getWidth(), is(0));
        assertThat(a.getHeight(), is(0));
        assertThat(a.getCenterX(), is(0));
        assertThat(a.getCenterY(), is(0));
    }

    @Test
    public void createFromInts() {
        title("create from ints");
        a = TEST_BOUNDS;
        print(a);
        assertThat(a.getX(), is(TEST_X));
        assertThat(a.getY(), is(TEST_Y));
        assertThat(a.getWidth(), is(TEST_W));
        assertThat(a.getHeight(), is(TEST_H));
        assertThat(a.getCenterX(), is(TEST_CX));
        assertThat(a.getCenterY(), is(TEST_CY));
    }

    @Test
    public void equalsHashCode() {
        title("equals, hashCode");
        a = TEST_BOUNDS;
        b = Bounds.create(TEST_X, TEST_Y, TEST_W, TEST_H);
        assertThat(a.equals(b), is(true));
        assertThat(b.equals(a), is(true));
        assertThat(a.hashCode(), is(b.hashCode()));
    }

    @Test
    public void notEqualsHashCode() {
        title("NOT equals, hashCode");
        a = TEST_BOUNDS;
        b = Bounds.EMPTY_BOUNDS;
        assertThat(a.equals(b), is(false));
        assertThat(b.equals(a), is(false));
        assertThat(a.hashCode(), is(not(b.hashCode())));
    }

    @Test
    public void rectangleToBounds() {
        title("rectangle to bounds");
        Rectangle rect = new Rectangle(TEST_X, TEST_Y, TEST_W, TEST_H);
        a = Bounds.create(rect);
        print(a);
        assertThat(a, is(equalTo(TEST_BOUNDS)));
    }

    @Test
    public void locationToBounds() {
        title("location to bounds");
        Location loc = loc(TEST_X, TEST_Y);
        a = Bounds.create(loc);
        print(a);
        assertThat(a.getX(), is(TEST_X));
        assertThat(a.getY(), is(TEST_Y));
        assertThat(a.getWidth(), is(1));
        assertThat(a.getHeight(), is(1));
    }

    @Test
    public void boundsToRectangle() {
        title("bounds to rectangle");
        Rectangle rect = TEST_BOUNDS.toRectangle();
        print(rect);
        assertThat(rect.x, is(TEST_X));
        assertThat(rect.y, is(TEST_Y));
        assertThat(rect.width, is(TEST_W));
        assertThat(rect.height, is(TEST_H));
    }

    @Test
    public void contains() {
        title("contains");
        a = TEST_BOUNDS;

        // outside the bounds
        assertThat(a.contains(5, 15), is(false));
        assertThat(a.contains(30, 19), is(false));
        assertThat(a.contains(111, 15), is(false));
        assertThat(a.contains(9, 100), is(false));
        assertThat(a.contains(111, 100), is(false));
        assertThat(a.contains(8, 171), is(false));
        assertThat(a.contains(80, 175), is(false));
        assertThat(a.contains(112, 175), is(false));

        // right on the bounds
        assertThat(a.contains(9, 19), is(false));
        assertThat(a.contains(10, 20), is(true));

        assertThat(a.contains(109, 20), is(true));
        assertThat(a.contains(110, 20), is(false));

        assertThat(a.contains(10, 169), is(true));
        assertThat(a.contains(10, 170), is(false));

        assertThat(a.contains(109, 169), is(true));
        assertThat(a.contains(110, 170), is(false));
    }

    @Test
    public void containsLocation() {
        title("contains location");
        a = TEST_BOUNDS;

        // outside the bounds
        assertThat(a.contains(loc(5, 15)), is(false));
        assertThat(a.contains(loc(30, 19)), is(false));
        assertThat(a.contains(loc(111, 15)), is(false));
        assertThat(a.contains(loc(9, 100)), is(false));
        assertThat(a.contains(loc(111, 100)), is(false));
        assertThat(a.contains(loc(8, 171)), is(false));
        assertThat(a.contains(loc(80, 175)), is(false));
        assertThat(a.contains(loc(112, 175)), is(false));

        // right on the bounds
        assertThat(a.contains(loc(9, 19)), is(false));
        assertThat(a.contains(loc(10, 20)), is(true));

        assertThat(a.contains(loc(109, 20)), is(true));
        assertThat(a.contains(loc(110, 20)), is(false));

        assertThat(a.contains(loc(10, 169)), is(true));
        assertThat(a.contains(loc(10, 170)), is(false));

        assertThat(a.contains(loc(109, 169)), is(true));
        assertThat(a.contains(loc(110, 170)), is(false));
    }

    @Test
    public void containsLocationWithError() {
        title("contains location with error");
        a = TEST_BOUNDS;

        assertThat(a.contains(loc(9, 19), 1), is(true));
        assertThat(a.contains(loc(9, 19), 0), is(false));
        assertThat(a.contains(loc(10, 20), 0), is(true));

        assertThat(a.contains(loc(109, 169), 0), is(true));
        assertThat(a.contains(loc(110, 170), 0), is(false));
        assertThat(a.contains(loc(110, 170), 1), is(true));
    }

    @Test
    public void containsOtherByInts() {
        title("contains other by ints");
        a = TEST_BOUNDS;

        assertThat(a.contains(18, 8, 50, 50), is(false));
        assertThat(a.contains(10, 20, 50, 50), is(true));

        assertThat(a.contains(30, 19, 20, 20), is(false));
        assertThat(a.contains(30, 20, 20, 20), is(true));

        assertThat(a.contains(100, 15, 10, 20), is(false));
        assertThat(a.contains(100, 20, 10, 10), is(true));

        assertThat(a.contains(TEST_X, TEST_Y, TEST_W, TEST_H), is(true));
    }

    @Test
    public void containsOtherByBounds() {
        title("contains other by bounds");
        a = TEST_BOUNDS;
        assertThat(a.contains(TEST_BOUNDS), is(true));

        b = Bounds.create(TEST_X, TEST_Y, TEST_W, TEST_H + 1);
        assertThat(a.contains(b), is(false));
    }

    @Test
    public void borderContainsFudge() {
        title("border contains fudge");
        a = TEST_BOUNDS;

        // WEST border
        assertThat(a.borderContains(6, 100, 3), is(false));
        assertThat(a.borderContains(8, 100, 3), is(true));
        assertThat(a.borderContains(10, 100, 3), is(true));
        assertThat(a.borderContains(12, 100, 3), is(true));
        assertThat(a.borderContains(14, 100, 3), is(false));

        // EAST border
        assertThat(a.borderContains(105, 100, 3), is(false));
        assertThat(a.borderContains(107, 100, 3), is(true));
        assertThat(a.borderContains(109, 100, 3), is(true));
        assertThat(a.borderContains(111, 100, 3), is(true));
        assertThat(a.borderContains(113, 100, 3), is(false));

        // NORTH border
        assertThat(a.borderContains(30, 16, 3), is(false));
        assertThat(a.borderContains(30, 18, 3), is(true));
        assertThat(a.borderContains(30, 20, 3), is(true));
        assertThat(a.borderContains(30, 22, 3), is(true));
        assertThat(a.borderContains(30, 24, 3), is(false));

        // SOUTH border
        assertThat(a.borderContains(30, 165, 3), is(false));
        assertThat(a.borderContains(30, 167, 3), is(true));
        assertThat(a.borderContains(30, 169, 3), is(true));
        assertThat(a.borderContains(30, 171, 3), is(true));
        assertThat(a.borderContains(30, 173, 3), is(false));
    }

    @Test
    public void borderContainsLocation() {
        title("border contains location");
        a = TEST_BOUNDS;

        assertThat(a.borderContains(loc(6, 100), 3), is(false));
        assertThat(a.borderContains(loc(8, 100), 3), is(true));
    }

    /*
            0...........................(X)
            :      10            110
            :   20  +---- 100 ----+
            :       |             |
            :      150            |
            :       |             |
            :  170  +-------------+
            :         TEST_BOUNDS
           (Y)
     */

    // TODO: add unit tests for add(), expand(), translate(), rotate()

}
