/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import com.cburch.draw.tools.SVGIcon;
import com.cburch.logisim.data.Direction;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for manipulating SVG icons.
 */
public final class Icons {

    private static final double TWO = 2.0;

    // non-instantiable
    private Icons() {
    }

    /**
     * Returns the SVG icon for the given name;
     *
     * @param name the icon name
     * @return the SVG icon instance
     */
    public static SVGIcon getIcon(String name) {
        // TODO: implement lazy loading
        // TODO: or, cache them all up front
        return new SVGIcon(name);
    }

    /**
     * Paints the given icon into the specified graphics context at the given
     * location, facing in the specified direction.
     *
     * @param g the graphics context
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param dir the direction (for rotation)
     * @param icon the icon to paint
     * @param dest the destination component
     */
    public static void paintRotated(Graphics g, int x, int y, Direction dir,
                                    Icon icon, Component dest) {
        if (!(g instanceof Graphics2D) || dir == Direction.EAST) {
            icon.paintIcon(dest, g, x, y);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        double cx = x + icon.getIconWidth() / TWO;
        double cy = y + icon.getIconHeight() / TWO;

        if (dir == Direction.WEST) {
            g2.rotate(Math.PI, cx, cy);
        } else if (dir == Direction.NORTH) {
            g2.rotate(-Math.PI / TWO, cx, cy);
        } else if (dir == Direction.SOUTH) {
            g2.rotate(Math.PI / TWO, cx, cy);
        } else {
            g2.translate(-x, -y);
        }
        icon.paintIcon(dest, g2, x, y);
        g2.dispose();
    }
}
