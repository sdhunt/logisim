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

    private void assertBoundsStats(Bounds b, int ex, int ey, int ew, int eh) {
        assertThat(b.getX(), is(ex));
        assertThat(b.getY(), is(ey));
        assertThat(b.getWidth(), is(ew));
        assertThat(b.getHeight(), is(eh));
    }

    @Test
    public void addToEmptyBounds() {
        title("add: to empty bounds");
        a = Bounds.EMPTY_BOUNDS;
        print(a);
        b = a.add(5, 7);
        print(b);
        assertBoundsStats(b, 5, 7, 1, 1);
    }

    @Test
    public void addToInsideBounds() {
        title("add: to inside bounds");
        a = Bounds.create(5, 7, 9, 11);
        print(a);
        b = a.add(6, 8);
        print(b);
        assertThat(a, is(equalTo(b)));
    }

    private void verifyAddedPoint(int px, int py, String where,
                                  int x, int y, int w, int h) {
        b = a.add(px, py);
        print("  add: point %s (%d,%d) ... %s", where, px, py, b);
        assertBoundsStats(b, x, y, w, h);
    }

    @Test
    public void addPoints() {
        title("addPoints:");
        a = Bounds.create(10, 20, 30, 40);

        // NOTE: N and W borders are inclusive; E and S borders are exclusive.
        //   so to make sure a point E or S of the bounds is included, the
        //   width/height is pushed out by +1 more than you would expect
        verifyAddedPoint(5, 5, "NW", 5, 5, 35, 55);
        verifyAddedPoint(15, 5, "N", 10, 5, 30, 55);
        verifyAddedPoint(55, 5, "NE", 10, 5, 46, 55);
        verifyAddedPoint(5, 30, "W", 5, 20, 35, 40);
        verifyAddedPoint(45, 30, "E", 10, 20, 36, 40);
        verifyAddedPoint(5, 65, "SW", 5, 20, 35, 46);
        verifyAddedPoint(20, 65, "S", 10, 20, 30, 46);
        verifyAddedPoint(55, 65, "SE", 10, 20, 46, 46);
    }

    @Test
    public void addLocation() {
        title("add location");
        a = Bounds.create(5, 5, 10, 10);
        b = a.add(loc(20, 20));
        print(b);
        assertBoundsStats(b, 5, 5, 16, 16);
    }

    private void verifyAddedBounds(int nx, int ny, int nw, int nh, String where,
                                   int ex, int ey, int ew, int eh) {
        b = a.add(nx, ny, nw, nh);
        print("  %s ... %s", where, b);
        assertBoundsStats(b, ex, ey, ew, eh);
    }

    @Test
    public void addBoundsByInts() {
        title("addBoundsByInts:");
        a = Bounds.create(100, 100, 300, 200);
        verifyAddedBounds(250, 50, 50, 300, "N-S", 100, 50, 300, 300);
        verifyAddedBounds(50, 150, 500, 50, "E-W", 50, 100, 500, 200);
        verifyAddedBounds(50, 50, 100, 100, "NW", 50, 50, 350, 250);
        verifyAddedBounds(350, 50, 100, 100, "NE", 100, 50, 350, 250);
        verifyAddedBounds(50, 250, 100, 100, "SW", 50, 100, 350, 250);
        verifyAddedBounds(350, 250, 100, 100, "SE", 100, 100, 350, 250);
    }

    @Test
    public void addBoundsByIntsToEmptyBounds() {
        title("add: bounds (by ints) to empty bounds");
        a = Bounds.EMPTY_BOUNDS;
        verifyAddedBounds(1, 2, 3, 4, "empty", 1, 2, 3, 4);
    }

    private void verifyAddedBoundsInstance(int nx, int ny, int nw, int nh,
                                           String where,
                                           int ex, int ey, int ew, int eh) {
        b = a.add(Bounds.create(nx, ny, nw, nh));
        print("  %s ... %s", where, b);
        assertBoundsStats(b, ex, ey, ew, eh);
    }

    @Test
    public void addBoundsByBounds() {
        title("addBoundsByBounds:");
        a = Bounds.create(100, 100, 300, 200);
        verifyAddedBoundsInstance(250, 50, 50, 300, "N-S", 100, 50, 300, 300);
        verifyAddedBoundsInstance(50, 150, 500, 50, "E-W", 50, 100, 500, 200);
        verifyAddedBoundsInstance(50, 50, 100, 100, "NW", 50, 50, 350, 250);
        verifyAddedBoundsInstance(350, 50, 100, 100, "NE", 100, 50, 350, 250);
        verifyAddedBoundsInstance(50, 250, 100, 100, "SW", 50, 100, 350, 250);
        verifyAddedBoundsInstance(350, 250, 100, 100, "SE", 100, 100, 350, 250);
    }

    @Test
    public void addBoundsToEmptyBounds() {
        title("add: bounds to empty bounds");
        a = Bounds.EMPTY_BOUNDS.add(Bounds.create(1, 2, 3, 4));
        print(a);
        assertBoundsStats(a, 1, 2, 3, 4);
    }

    @Test
    public void addEmptyBoundsToBounds() {
        title("add: empty bounds to bounds");
        a = Bounds.create(1, 2, 3, 4).add(Bounds.EMPTY_BOUNDS);
        print(a);
        assertBoundsStats(a, 1, 2, 3, 4);
    }

    @Test
    public void expandEmptyBounds() {
        title("expand: empty bounds");
        // NOTE: can't expand empty bounds
        a = Bounds.EMPTY_BOUNDS.expand(3);
        print(a);
        assertThat(a, is(equalTo(Bounds.EMPTY_BOUNDS)));
    }

    @Test
    public void expandBoundsBigger() {
        title("expand: bounds bigger");
        a = Bounds.create(10, 10, 100, 100);
        b = a.expand(3);
        print(b);
        assertBoundsStats(b, 7, 7, 106, 106);
    }

    @Test
    public void expandBoundsZero() {
        title("expand: bounds ZERO");
        a = Bounds.create(10, 10, 100, 100);
        b = a.expand(0);
        print(b);
        assertThat(b, is(equalTo(a)));
    }

    @Test
    public void expandBoundsSmaller() {
        title("expand: bounds smaller");
        a = Bounds.create(10, 10, 100, 100);
        b = a.expand(-3);
        print(b);
        assertBoundsStats(b, 13, 13, 94, 94);
    }

    @Test
    public void expandJavadocExamples() {
        title("expand: javadoc examples");
        a = Bounds.create(10, 20, 100, 150);
        assertThat(a.expand(2), is(equalTo(Bounds.create(8, 18, 104, 154))));
        assertThat(a.expand(-3), is(equalTo(Bounds.create(13, 23, 94, 144))));
    }

    @Test
    public void translateEmptyBounds() {
        title("translate: empty bounds");
        // NOTE: can't translate empty bounds
        assertThat(Bounds.EMPTY_BOUNDS.translate(5, 6),
                   is(equalTo(Bounds.EMPTY_BOUNDS)));
    }

    @Test
    public void translateByZero() {
        title("translate: by zero");
        a = Bounds.create(1, 2, 4, 5);
        b = a.translate(0, 0);
        print(b);
        assertThat(b, is(equalTo(a)));
    }

    @Test
    public void translateSome() {
        title("translate: some");
        a = Bounds.create(10, 20, 40, 50);
        b = a.translate(-5, 14);
        print(b);
        assertBoundsStats(b, 5, 34, 40, 50);
    }

    @Test
    public void rotateEastToNorth() {
        title("rotate: east-to-north");
        a = Bounds.create(100, 100, 200, 100);
        print(a);
        int x = a.getCenterX();
        int y = a.getCenterY();
        b = a.rotate(Direction.EAST, Direction.NORTH, x, y);
        print(b);
        assertBoundsStats(b, 150, 50, 100, 200);
    }

    private void verifyRotateBounds(Direction toDir, int expX, int expY) {
        b = a.rotate(Direction.EAST, toDir, a.getX(), a.getY());
        print("  rotate %s ... %s", toDir, b);
        assertBoundsStats(b, expX, expY, 100, 100);
    }

    @Test
    public void rotateSquare() {
        title("rotate: square...");
        a = Bounds.create(300, 300, 100, 100);
        verifyRotateBounds(Direction.NORTH, 300, 200);
        verifyRotateBounds(Direction.WEST, 200, 200);
        verifyRotateBounds(Direction.SOUTH, 200, 300);
        verifyRotateBounds(Direction.EAST, 300, 300);
    }

    @Test
    public void intersectMiddleLower() {
        title("intersect: middle lower");
        a = Bounds.create(10, 10, 40, 20);
        b = a.intersect(Bounds.create(20, 20, 20, 30));
        print(b);
        assertBoundsStats(b, 20, 20, 20, 10);
    }

    @Test
    public void intersectLeftSide() {
        title("intersect: left side");
        a = Bounds.create(10, 10, 40, 20);
        b = a.intersect(Bounds.create(0, 0, 30, 40));
        print(b);
        assertBoundsStats(b, 10, 10, 20, 20);
    }

    @Test
    public void intersectUpperRight() {
        title("intersect: upper right");
        a = Bounds.create(10, 10, 40, 20);
        b = a.intersect(Bounds.create(30, -10, 30, 30));
        print(b);
        assertBoundsStats(b, 30, 10, 20, 10);
    }

    @Test
    public void intersectNothing() {
        title("intersect: nothing");
        a = Bounds.create(10, 10, 40, 20);
        b = a.intersect(Bounds.create(0, 0, 8, 50));
        print(b);
        assertThat(b, is(equalTo(Bounds.EMPTY_BOUNDS)));
    }

}
