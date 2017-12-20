/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.awt.*;

/**
 * Graphics utilities.
 */
public class GraphicsUtil {
    /**
     * Designates horizontal alignment LEFT.
     */
    public static final int H_LEFT = -1;

    /**
     * Designates horizontal alignment CENTER.
     */
    public static final int H_CENTER = 0;

    /**
     * Designates horizontal alignment RIGHT.
     */
    public static final int H_RIGHT = 1;

    /**
     * Designates vertical alignment TOP.
     */
    public static final int V_TOP = -1;

    /**
     * Designates vertical alignment CENTER.
     */
    public static final int V_CENTER = 0;

    /**
     * Designates vertical alignment BASELINE.
     */
    public static final int V_BASELINE = 1;

    /**
     * Designates vertical alignment BOTTOM.
     */
    public static final int V_BOTTOM = 2;

    /**
     * Designates vertical alignment CENTER OVERALL.
     */
    public static final int V_CENTER_OVERALL = 3;

    // TODO: add TextAlign enum instead of the above integer constants.

    /**
     * Sets the stroke width of the given graphics context to the specified
     * width.
     *
     * @param g     the graphics context
     * @param width the stroke width
     */
    public static void switchToWidth(Graphics g, int width) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(width));
        }
    }

    /**
     * Draws an arc on the given graphics context, centered at the point
     * (x,y), with radius r, starting at the specified angle (in degrees),
     * for the specified distance (in degrees).
     * <p>
     * For example, the following will draw an arc starting at the 12 o'clock
     * position, drawing counter-clockwise to the 6 o'clock position, centered
     * at [100,100] with a radius of 50.
     * <pre>
     *     GraphicsUtil.drawCenteredArc(g2, 100, 100, 50, 90, 180);
     * </pre>
     *
     * @param g     the graphics context
     * @param x     the center point x-coordinate
     * @param y     the center point y-coordinate
     * @param r     the arc radius
     * @param start the start angle (degrees)
     * @param dist  the distance around the arc (degrees)
     * @see Graphics#drawArc(int, int, int, int, int, int)
     */
    public static void drawCenteredArc(Graphics g, int x, int y,
                                       int r, int start, int dist) {
        g.drawArc(x - r, y - r, 2 * r, 2 * r, start, dist);
    }

    /**
     * Returns a rectangle defining the bounds of the given text, in the given
     * graphics context, using the given font. If the specified graphics
     * context is null, a rectangle at (x,y) with dimensions (0,0) will be
     * returned.
     *
     * @param g      the graphics context
     * @param font   the font (may be null)
     * @param text   the text to compute the bounds for
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param halign horizontal alignment
     * @param valign vertical alignment
     * @return the computed bounds of the text
     */
    public static Rectangle getTextBounds(Graphics g, Font font, String text,
                                          int x, int y, int halign, int valign) {
        if (g == null) {
            return new Rectangle(x, y, 0, 0);
        }

        Font oldfont = g.getFont();
        if (font != null) {
            g.setFont(font);
        }

        Rectangle ret = getTextBounds(g, text, x, y, halign, valign);
        if (font != null) {
            g.setFont(oldfont);
        }

        return ret;
    }

    /**
     * Returns a rectangle defining the bounds of the given text in the given
     * graphics context (using the context's current font). If the specified
     * graphics context is null, a rectangle at (x,y) with dimensions (0,0)
     * will be returned.
     *
     * @param g      the graphics context
     * @param text   the text to compute the bounds for
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param halign horizontal alignment
     * @param valign vertical alignment
     * @return the computed bounds of the text
     */
    public static Rectangle getTextBounds(Graphics g, String text, int x, int y,
                                          int halign, int valign) {
        if (g == null) {
            return new Rectangle(x, y, 0, 0);
        }

        FontMetrics mets = g.getFontMetrics();
        int width = mets.stringWidth(text);
        int ascent = mets.getAscent();
        int descent = mets.getDescent();
        int height = ascent + descent;

        Rectangle ret = new Rectangle(x, y, width, height);
        switch (halign) {
            case H_CENTER:
                ret.translate(-(width / 2), 0);
                break;
            case H_RIGHT:
                ret.translate(-width, 0);
                break;
            default:
                ;
        }
        switch (valign) {
            case V_TOP:
                break;
            case V_CENTER:
                ret.translate(0, -(ascent / 2));
                break;
            case V_CENTER_OVERALL:
                ret.translate(0, -(height / 2));
                break;
            case V_BASELINE:
                ret.translate(0, -ascent);
                break;
            case V_BOTTOM:
                ret.translate(0, -height);
                break;
            default:
                ;
        }
        return ret;
    }

    /**
     * Draws the specified text on the given graphics context,
     * at location (x,y) with the specified alignment.
     * <p>
     * If font is not null, it is set on the graphics context, but the
     * {@link #drawText(Graphics, String, int, int, int, int)} call
     * immediately overwrites that with sans-serif/plain/10.
     *
     * @param g      the graphics context
     * @param font   the font (may be null)
     * @param text   the text to draw
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param halign horizontal alignment
     * @param valign vertical alignment
     */
    public static void drawText(Graphics g, Font font, String text,
                                int x, int y, int halign, int valign) {
        Font oldfont = g.getFont();
        if (font != null) {
            g.setFont(font);
        }

        drawText(g, text, x, y, halign, valign);
        if (font != null) {
            g.setFont(oldfont);
        }

    }

    /**
     * Draws the specified text on the given graphics context,
     * at location (x,y) with the specified alignment.
     * <p>
     * NOTE: font has been hard-wired to sans-serif/plain/10.
     *
     * @param g      the graphics context
     * @param text   the text to draw
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param halign horizontal alignment
     * @param valign vertical alignment
     */
    public static void drawText(Graphics g, String text, int x, int y,
                                 int halign, int valign) {
        if (text.length() == 0) {
            return;
        }

        Rectangle bd = getTextBounds(g, text, x, y, halign, valign);
        // TODO: clean-up this hard-coded font!!
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        g.drawString(text, bd.x, bd.y + g.getFontMetrics().getAscent());
    }

    /**
     * Draws the specified text on the given graphic context,
     * centered at location (x,y).
     * <p>
     * NOTE: font has been hard-wired to sans-serif/plain/10.
     *
     * @param g    the graphics context
     * @param text the text to draw
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     */
    public static void drawCenteredText(Graphics g, String text, int x, int y) {
        drawText(g, text, x, y, H_CENTER, V_CENTER);
    }

    /**
     * Draws an arrow on the given graphics context with the main shaft of the
     * arrow defined by the line (x0,y0) to (x1,y1), and the arrow head at the
     * latter end with the specified length and angle (in degrees).
     *
     * @param g          the graphics context
     * @param x0         the x-coordinate of the arrow tail
     * @param y0         the y-coordinate of the arrow tail
     * @param x1         the x-coordinate of the arrow head
     * @param y1         the y-coordinate of the arrow head
     * @param headLength the arrow head length
     * @param headAngle  the arrow head angle between main shaft and segment
     */
    public static void drawArrow(Graphics g, int x0, int y0, int x1, int y1,
                                 int headLength, int headAngle) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y0 - y1, x0 - x1);
        int[] xs = {x1 + (int) (headLength * Math.cos(angle + offs)), x1,
                x1 + (int) (headLength * Math.cos(angle - offs))};
        int[] ys = {y1 + (int) (headLength * Math.sin(angle + offs)), y1,
                y1 + (int) (headLength * Math.sin(angle - offs))};
        g.drawLine(x0, y0, x1, y1);
        g.drawPolyline(xs, ys, 3);
    }
}
