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

    /**
     * Returns a bounds object for the given location and size.
     *
     * @param x   the x-coordinate
     * @param y   the y-coordinate
     * @param wid the width
     * @param ht  the height
     * @return a corresponding bounds instance
     */
    public static Bounds create(int x, int y, int wid, int ht) {
        // TODO: why are we not using a hashCode() implementation???
        int hashCode = 13 * (31 * (31 * x + y) + wid) + ht;
        Object cached = cache.get(hashCode);
        if (cached != null) {
            Bounds bds = (Bounds) cached;
            if (bds.x == x && bds.y == y && bds.wid == wid && bds.ht == ht) {
                return bds;
            }

        }
        Bounds ret = new Bounds(x, y, wid, ht);
        cache.put(hashCode, ret);
        return ret;
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
     * @param pt the location
     * @return a corresponding bounds instance of size 1x1
     */
    public static Bounds create(Location pt) {
        return create(pt.getX(), pt.getY(), 1, 1);
    }

    private final int x;
    private final int y;
    private final int wid;
    private final int ht;

    private Bounds(int x, int y, int wid, int ht) {
        this.x = x;
        this.y = y;
        this.wid = wid;
        this.ht = ht;

        // TODO: following does not make sense: values are not used
        if (wid < 0) {
            {
                x += wid / 2;
            }
            wid = 0;
        }
        if (ht < 0) {
            {
                y += ht / 2;
            }
            ht = 0;
        }
    }

    // TODO: re-write equals and hashCode to use standard template
    @Override
    public boolean equals(Object other_obj) {
        if (!(other_obj instanceof Bounds)) {
            return false;
        }

        Bounds other = (Bounds) other_obj;
        return x == other.x && y == other.y
                && wid == other.wid && ht == other.ht;
    }

    @Override
    public int hashCode() {
        int ret = 31 * x + y;
        ret = 31 * ret + wid;
        ret = 31 * ret + ht;
        return ret;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "): " + wid + "x" + ht;
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
        return wid;
    }

    /**
     * Returns the bounds height.
     *
     * @return the height
     */
    public int getHeight() {
        return ht;
    }

    /**
     * Returns the x-coordinate of the center of the bounds.
     *
     * @return x-coordinate of center
     */
    public int getCenterX() {
        return x + wid / 2;
    }

    /**
     * Returns the y-coordinate of the center of the bounds.
     *
     * @return y-coordinate of center
     */
    public int getCenterY() {
        return y + ht / 2;
    }

    /**
     * Returns an equivalent rectangle instance.
     *
     * @return an equivalent rectangle
     */
    public Rectangle toRectangle() {
        return new Rectangle(x, y, wid, ht);
    }


    /**
     * Returns true if the specified location lies within the bounds.
     *
     * @param p the location to test
     * @return true, if p is within bounds; false otherwise
     */
    public boolean contains(Location p) {
        return contains(p.getX(), p.getY(), 0);
    }

    /**
     * Returns true if the specified location lies within the bounds, or
     * just outside the bounds (within the specified error).
     *
     * @param p            the location to test
     * @param allowedError the amount by which the location my lie outside the
     *                     bounds and still return a value of true
     * @return true, if p is within bounds (with allowed error); false otherwise
     */
    public boolean contains(Location p, int allowedError) {
        return contains(p.getX(), p.getY(), allowedError);
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
        return px >= x - allowedError && px < x + wid + allowedError
                && py >= y - allowedError && py < y + ht + allowedError;
    }

    /**
     * Returns true if the specified bounds are wholly contained within this
     * bounds instance.
     *
     * @param x   x-coordinate
     * @param y   y-coordinate
     * @param wid width
     * @param ht  height
     * @return true, if the specified bounds lie within these bounds; false
     * otherwise
     */
    public boolean contains(int x, int y, int wid, int ht) {
        int oth_x = (wid <= 0 ? x : x + wid - 1);
        int oth_y = (ht <= 0 ? y : y + ht - 1);
        return contains(x, y) && contains(oth_x, oth_y);
    }

    /**
     * Returns true if the specified bounds are wholly contained within this
     * bounds instance.
     *
     * @param bd the bounds instance to test
     * @return true, if the given bounds lie within these bounds; false
     * otherwise
     */
    public boolean contains(Bounds bd) {
        return contains(bd.x, bd.y, bd.wid, bd.ht);
    }

    /**
     * Returns true if the given location lies within this bound's border
     * (indicated by the fudge factor).
     *
     * @param p     the point to test
     * @param fudge the width of the border
     * @return true, if p lies within the border; false otherwise
     */
    public boolean borderContains(Location p, int fudge) {
        return borderContains(p.getX(), p.getY(), fudge);
    }

    /**
     * Returns true if the given location lies within this bound's border
     * (indicated by the fudge factor).
     *
     * @param px    x-coordinate of the point to test
     * @param py    y-coordinate of the point to test
     * @param fudge the width of the border
     * @return true, if the point lies within the border; false otherwise
     */
    public boolean borderContains(int px, int py, int fudge) {
        int x1 = x + wid - 1;
        int y1 = y + ht - 1;
        if (Math.abs(px - x) <= fudge || Math.abs(px - x1) <= fudge) {
            // maybe on east or west border?
            return y - fudge <= py && py <= y1 + fudge;
        }
        if (Math.abs(py - y) <= fudge || Math.abs(py - y1) <= fudge) {
            // maybe on north or south border?
            return x - fudge <= px && px <= x1 + fudge;
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


        int new_x = this.x;
        int new_wid = this.wid;
        int new_y = this.y;
        int new_ht = this.ht;
        if (x < this.x) {
            new_x = x;
            new_wid = (this.x + this.wid) - x;
        } else if (x >= this.x + this.wid) {
            new_x = this.x;
            new_wid = x - this.x + 1;
        }
        if (y < this.y) {
            new_y = y;
            new_ht = (this.y + this.ht) - y;
        } else if (y >= this.y + this.ht) {
            new_y = this.y;
            new_ht = y - this.y + 1;
        }
        return create(new_x, new_y, new_wid, new_ht);
    }

    /**
     * Returns bounds representing this instance with the addition of the
     * given bounds.
     *
     * @param x   x-coordinate of the bounds to add
     * @param y   y-coordinate of the bounds to add
     * @param wid width of the bounds to add
     * @param ht  height of the bounds to add
     * @return the adjusted bounds
     */
    public Bounds add(int x, int y, int wid, int ht) {
        if (this == EMPTY_BOUNDS) {
            return Bounds.create(x, y, wid, ht);
        }

        int retX = Math.min(x, this.x);
        int retY = Math.min(y, this.y);
        int retWidth = Math.max(x + wid, this.x + this.wid) - retX;
        int retHeight = Math.max(y + ht, this.y + this.ht) - retY;

        // TODO: review- why bother, when Bounds.create() uses a cache??
        if (retX == this.x && retY == this.y &&
                retWidth == this.wid && retHeight == this.ht) {
            return this;

        } else {
            return Bounds.create(retX, retY, retWidth, retHeight);
        }
    }

    /**
     * Returns bounds representing this instance with the addition of the
     * given bounds.
     *
     * @param bd the bounds to add
     * @return the adjusted bounds
     */
    // TODO: just call add(int,int,int,int)
    public Bounds add(Bounds bd) {
        if (this == EMPTY_BOUNDS) {
            return bd;
        }

        if (bd == EMPTY_BOUNDS) {
            return this;
        }

        int retX = Math.min(bd.x, this.x);
        int retY = Math.min(bd.y, this.y);
        int retWidth = Math.max(bd.x + bd.wid, this.x + this.wid) - retX;
        int retHeight = Math.max(bd.y + bd.ht, this.y + this.ht) - retY;

        // TODO: review- why bother, when Bounds.create() uses a cache??
        if (retX == this.x && retY == this.y &&
                retWidth == this.wid && retHeight == this.ht) {
            return this;
        } else if (retX == bd.x && retY == bd.y &&
                retWidth == bd.wid && retHeight == bd.ht) {
            return bd;

        } else {
            return Bounds.create(retX, retY, retWidth, retHeight);
        }
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
        if (this == EMPTY_BOUNDS) {
            return this;
        }

        if (d == 0) {
            return this;
        }

        return create(x - d, y - d, wid + 2 * d, ht + 2 * d);
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
        if (this == EMPTY_BOUNDS) {
            return this;
        }

        if (dx == 0 && dy == 0) {
            return this;
        }

        return create(x + dx, y + dy, wid, ht);
    }

    /**
     * Returns bounds corresponding to a rotation of these bounds around
     * {@code (xc,yc)}, for the given from and to directions. TODO: better wording
     *
     * @param from direction this instance is facing
     * @param to   direction the returned bounds should be facing
     * @param xc   x-coordinate of center of rotation
     * @param yc   y-coordinate of center of rotation
     * @return the rotated bounds
     */
    // rotates this around (xc,yc) assuming that this is facing in the
    // from direction and the returned bounds should face in the to direction.
    public Bounds rotate(Direction from, Direction to, int xc, int yc) {
        int degrees = to.toDegrees() - from.toDegrees();
        while (degrees >= 360) degrees -= 360;
        while (degrees < 0) degrees += 360;

        int dx = x - xc;
        int dy = y - yc;
        if (degrees == 90) {
            return create(xc + dy, yc - dx - wid, ht, wid);
        } else if (degrees == 180) {
            return create(xc - dx - wid, yc - dy - ht, wid, ht);
        } else if (degrees == 270) {
            return create(xc - dy - ht, yc + dx, ht, wid);
        } else {
            return this;
        }
    }

    /**
     * Returns bounds corresponding to the intersection of this bounds instance
     * and the given bounds instance.
     *
     * @param other the other bounds instance to intersect with
     * @return the intersection of the two bounds
     */
    public Bounds intersect(Bounds other) {
        int x0 = this.x;
        int y0 = this.y;
        int x1 = x0 + this.wid;
        int y1 = y0 + this.ht;
        int x2 = other.x;
        int y2 = other.y;
        int x3 = x2 + other.wid;
        int y3 = y2 + other.ht;
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
        } else {
            return create(x0, y0, x1 - x0, y1 - y0);
        }
    }
}
