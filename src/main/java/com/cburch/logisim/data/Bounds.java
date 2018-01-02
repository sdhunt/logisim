/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import com.cburch.logisim.util.Cache;

import java.awt.*;

/**
 * Represents an immutable rectangular bounding box. This is analogous to
 * java.awt's <code>Rectangle</code> class, except that objects of this type
 * are immutable.
 */
public class Bounds {

    /**
     * Designates bounds of zero size, located at the origin.
     */
    public static Bounds EMPTY_BOUNDS = new Bounds(0, 0, 0, 0);

    private static final Cache cache = new Cache();

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private Bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bounds bounds = (Bounds) o;
        return x == bounds.x && y == bounds.y &&
                width == bounds.width && height == bounds.height;
    }

    @Override
    public int hashCode() {
        return computeHashCode(x, y, width, height);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "): " + width + "x" + height;
    }

    /**
     * Returns the bounds x-coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the bounds y-coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the bounds width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the bounds height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the x-coordinate of the center of the bounds.
     *
     * @return x-coordinate of center
     */
    public int getCenterX() {
        return x + width / 2;
    }

    /**
     * Returns the y-coordinate of the center of the bounds.
     *
     * @return y-coordinate of center
     */
    public int getCenterY() {
        return y + height / 2;
    }

    /**
     * Returns an equivalent rectangle instance.
     *
     * @return an equivalent rectangle
     */
    public Rectangle toRectangle() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns true if the specified location lies within the bounds.
     *
     * @param loc the location to test
     * @return true, if p is within bounds; false otherwise
     */
    public boolean contains(Location loc) {
        return contains(loc.getX(), loc.getY(), 0);
    }

    /**
     * Returns true if the specified location lies within the bounds, or
     * just outside the bounds (within the specified error).
     *
     * @param loc          the location to test
     * @param allowedError the amount by which the location my lie outside the
     *                     bounds and still return a value of true
     * @return true, if p is within bounds (with allowed error); false otherwise
     */
    public boolean contains(Location loc, int allowedError) {
        return contains(loc.getX(), loc.getY(), allowedError);
    }

    /**
     * Returns true if the specified point lies within the bounds.
     *
     * @param px point x-coordinate
     * @param py point y-coordinate
     * @return true, if the specified point is within bounds; false otherwise
     */
    public boolean contains(int px, int py) {
        return contains(px, py, 0);
    }

    /**
     * Returns true if the specified point lies within the bounds, or just
     * outside the bounds (within the specified error).
     *
     * @param px           point x-coordinate
     * @param py           point y-coordinate
     * @param allowedError the amount by which the point may lie outside the
     *                     bounds and still return a value of true
     * @return true, if the specified point is within bounds
     * (with allowed error); false otherwise
     */
    public boolean contains(int px, int py, int allowedError) {
        return px >= x - allowedError &&
                px < x + width + allowedError &&
                py >= y - allowedError &&
                py < y + height + allowedError;
    }

    /**
     * Returns true if the specified bounds are wholly contained within this
     * bounds instance.
     *
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @param width  width
     * @param height height
     * @return true, if the specified bounds lie within these bounds; false
     * otherwise
     */
    public boolean contains(int x, int y, int width, int height) {
        int otherX = (width <= 0 ? x : x + width - 1);
        int otherY = (height <= 0 ? y : y + height - 1);
        return contains(x, y) && contains(otherX, otherY);
    }

    /**
     * Returns true if the specified bounds are wholly contained within this
     * bounds instance.
     *
     * @param bounds the bounds instance to test
     * @return true, if the given bounds lie within these bounds; false
     * otherwise
     */
    public boolean contains(Bounds bounds) {
        return contains(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Returns true if the given location lies within this bound's border
     * (indicated by the fudge factor).
     *
     * @param loc   the point to test
     * @param delta the allowed distance from the border
     * @return true, if loc lies within the border; false otherwise
     */
    public boolean borderContains(Location loc, int delta) {
        return borderContains(loc.getX(), loc.getY(), delta);
    }

    /**
     * Returns true if the given location lies within this bound's border
     * (indicated by the fudge factor).
     *
     * @param px    x-coordinate of the point to test
     * @param py    y-coordinate of the point to test
     * @param delta the allowed distance from the border
     * @return true, if the point lies within the border; false otherwise
     */
    public boolean borderContains(int px, int py, int delta) {
        int otherX = x + width - 1;
        int otherY = y + height - 1;
        if (Math.abs(px - x) <= delta || Math.abs(px - otherX) <= delta) {
            // maybe on east or west border?
            return y - delta <= py && py <= otherY + delta;
        }
        if (Math.abs(py - y) <= delta || Math.abs(py - otherY) <= delta) {
            // maybe on north or south border?
            return x - delta <= px && px <= otherX + delta;
        }
        return false;
    }

    /**
     * Returns bounds representing this instance with the addition of the
     * given location.
     *
     * @param p the point to add to these bounds
     * @return the adjusted bounds
     */
    public Bounds add(Location p) {
        return add(p.getX(), p.getY());
    }

    /**
     * Returns bounds representing this instance with the addition of the
     * given point.
     *
     * @param x x-coordinate of the point to add
     * @param y y-coordinate of the point to add
     * @return the adjusted bounds
     */
    public Bounds add(int x, int y) {
        if (this == EMPTY_BOUNDS) {
            return Bounds.create(x, y, 1, 1);
        }

        if (contains(x, y)) {
            return this;
        }

        int newX = this.x;
        int newWidth = this.width;
        int newY = this.y;
        int newHeight = this.height;

        if (x < this.x) {
            newX = x;
            newWidth = (this.x + this.width) - x;
        } else if (x >= this.x + this.width) {
            newX = this.x;
            newWidth = x - this.x + 1;
        }

        if (y < this.y) {
            newY = y;
            newHeight = (this.y + this.height) - y;
        } else if (y >= this.y + this.height) {
            newY = this.y;
            newHeight = y - this.y + 1;
        }
        return create(newX, newY, newWidth, newHeight);
    }

    /**
     * Returns bounds representing this instance with the addition of the
     * given bounds.
     *
     * @param x      x-coordinate of the bounds to add
     * @param y      y-coordinate of the bounds to add
     * @param width  width of the bounds to add
     * @param height height of the bounds to add
     * @return the adjusted bounds
     */
    public Bounds add(int x, int y, int width, int height) {
        if (this == EMPTY_BOUNDS) {
            return Bounds.create(x, y, width, height);
        }

        int newX = Math.min(x, this.x);
        int newY = Math.min(y, this.y);
        int newWidth = Math.max(x + width, this.x + this.width) - newX;
        int retHeight = Math.max(y + height, this.y + this.height) - newY;

        return Bounds.create(newX, newY, newWidth, retHeight);
    }

    /**
     * Returns bounds representing this instance with the addition of the
     * given bounds.
     *
     * @param bd the bounds to add
     * @return the adjusted bounds
     */
    public Bounds add(Bounds bd) {
        return bd == EMPTY_BOUNDS ? this : add(bd.x, bd.y, bd.width, bd.height);
    }

    /**
     * Returns bounds corresponding to an expansion of these bounds by
     * the given amount in each direction.
     * <p>
     * For example:
     * <pre>
     *      Bounds b = Bounds.create(10, 20, 100, 150)   => (10,20): 100x150
     *      Bounds bigger = b.expand(2)                  =>  (8,18): 104x154
     *      Bounds smaller = b.expand(-3)                => (13,23): 94x144
     * </pre>
     * Note, however, that {@link #EMPTY_BOUNDS} cannot be expanded:
     * <pre>
     *     EMPTY_BOUNDS.expand(n).equals(EMPTY_BOUNDS)   => true, for all n
     * </pre>
     *
     * @param d the amount to expand
     * @return the expanded bounds
     */
    public Bounds expand(int d) {
        if (this == EMPTY_BOUNDS || d == 0) {
            return this;
        }
        return create(x - d, y - d, width + 2 * d, height + 2 * d);
    }

    /**
     * Returns bounds corresponding to a translation of these bounds by the
     * given amounts.
     * <p>
     * Note, however, that {@link #EMPTY_BOUNDS} cannot be translated.
     *
     * @param dx distance to translate in the x-dimension
     * @param dy distance to translate in the y-dimension
     * @return the translated bounds
     */
    public Bounds translate(int dx, int dy) {
        if (this == EMPTY_BOUNDS || (dx == 0 && dy == 0)) {
            return this;
        }
        return create(x + dx, y + dy, width, height);
    }

    private int normalizeDegrees(int degrees) {
        int result = degrees;
        while (result >= 360) {
            result -= 360;
        }
        while (result < 0) {
            result += 360;
        }
        return result;
    }

    /**
     * Returns bounds corresponding to a rotation of these bounds around
     * {@code (xc,yc)}, using the given from and to directions to compute
     * the angle of rotation.
     *
     * @param from initial direction of facing
     * @param to   final direction of facing
     * @param xc   x-coordinate of center of rotation
     * @param yc   y-coordinate of center of rotation
     * @return the rotated bounds
     */
    public Bounds rotate(Direction from, Direction to, int xc, int yc) {
        int degrees = normalizeDegrees(to.toDegrees() - from.toDegrees());

        int dx = x - xc;
        int dy = y - yc;
        if (degrees == 90) {
            return create(xc + dy, yc - dx - width, height, width);
        }
        if (degrees == 180) {
            return create(xc - dx - width, yc - dy - height, width, height);
        }
        if (degrees == 270) {
            return create(xc - dy - height, yc + dx, height, width);
        }
        return this;
    }

    /**
     * Returns bounds corresponding to the intersection of this bounds instance
     * and the given bounds instance.
     *
     * @param other the other bounds instance to intersect with
     * @return the intersection of the two bounds
     */
    public Bounds intersect(Bounds other) {
        int x0 = x;
        int y0 = y;
        int x1 = x0 + width;
        int y1 = y0 + height;
        int x2 = other.x;
        int y2 = other.y;
        int x3 = x2 + other.width;
        int y3 = y2 + other.height;

        if (x2 > x0) {
            x0 = x2;
        }
        if (y2 > y0) {
            y0 = y2;
        }
        if (x3 < x1) {
            x1 = x3;
        }
        if (y3 < y1) {
            y1 = y3;
        }

        if (x1 < x0 || y1 < y0) {
            return EMPTY_BOUNDS;
        }
        return create(x0, y0, x1 - x0, y1 - y0);
    }


    private static int computeHashCode(int x, int y, int w, int h) {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + w;
        result = 31 * result + h;
        return result;
    }

    /**
     * Returns a bounds object for the given location and size.
     *
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param width  the width
     * @param height the height
     * @return a corresponding bounds instance
     */
    public static Bounds create(int x, int y, int width, int height) {
        int hashCode = computeHashCode(x, y, width, height);

        // NOTE: Cache class should be implementing the following code, not
        //        the cache consumer.
        // TODO: re-write Cache to make the caching transparent from the call;
        //        perhaps make Cache generic so we can have Cache<Bounds> ?

        Object cached = cache.get(hashCode);
        if (cached != null) {
            Bounds bounds = (Bounds) cached;
            if (bounds.x == x && bounds.y == y &&
                    bounds.width == width && bounds.height == height) {
                return bounds;
            }
        }
        Bounds result = new Bounds(x, y, width, height);
        cache.put(hashCode, result);
        return result;
    }

    /**
     * Returns a bounds object corresponding to the given rectangle instance.
     *
     * @param rect the rectangle
     * @return corresponding bounds
     */
    public static Bounds create(Rectangle rect) {
        return create(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Returns a bounds object of width and height {@code 1x1}, located at
     * the specified location.
     *
     * @param loc the location
     * @return a corresponding bounds instance of size 1x1
     */
    public static Bounds create(Location loc) {
        return create(loc.getX(), loc.getY(), 1, 1);
    }
}
