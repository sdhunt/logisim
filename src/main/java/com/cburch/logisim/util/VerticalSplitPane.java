/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class VerticalSplitPane extends JPanel {
    private class MyLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            if (fraction <= 0.0) {
                return comp1.getPreferredSize();
            }

            if (fraction >= 1.0) {
                return comp0.getPreferredSize();
            }

            Insets in = parent.getInsets();
            Dimension d0 = comp0.getPreferredSize();
            Dimension d1 = comp1.getPreferredSize();
            return new Dimension(in.left + d0.width + d1.width + in.right,
                                 in.top + Math.max(d0.height, d1.height) + in.bottom);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            if (fraction <= 0.0) {
                return comp1.getMinimumSize();
            }

            if (fraction >= 1.0) {
                return comp0.getMinimumSize();
            }

            Insets in = parent.getInsets();
            Dimension d0 = comp0.getMinimumSize();
            Dimension d1 = comp1.getMinimumSize();
            return new Dimension(in.left + d0.width + d1.width + in.right,
                                 in.top + Math.max(d0.height, d1.height) + in.bottom);
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets in = parent.getInsets();
            int maxWidth = parent.getWidth() - (in.left + in.right);
            int maxHeight = parent.getHeight() - (in.top + in.bottom);
            int split;
            if (fraction <= 0.0) {
                split = 0;
            } else if (fraction >= 1.0) {
                split = maxWidth;
            } else {
                split = (int) Math.round(maxWidth * fraction);
                split = Math.min(split, maxWidth - comp1.getMinimumSize().width);
                split = Math.max(split, comp0.getMinimumSize().width);
            }

            comp0.setBounds(in.left, in.top,
                            split, maxHeight);
            comp1.setBounds(in.left + split, in.top,
                            maxWidth - split, maxHeight);
            dragbar.setBounds(in.left + split - HorizontalSplitPane.DRAG_TOLERANCE, in.top,
                              2 * HorizontalSplitPane.DRAG_TOLERANCE, maxHeight);
        }
    }

    private class MyDragbar extends HorizontalSplitPane.Dragbar {
        MyDragbar() {
            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        }

        @Override
        int getDragValue(MouseEvent e) {
            return getX() + e.getX() - VerticalSplitPane.this.getInsets().left;
        }

        @Override
        void setDragValue(int value) {
            Insets in = VerticalSplitPane.this.getInsets();
            setFraction((double) value / (VerticalSplitPane.this.getWidth() - in.left - in.right));
            revalidate();
        }
    }

    private JComponent comp0;
    private JComponent comp1;
    private MyDragbar dragbar;
    private double fraction;

    /**
     * Creates a vertical split pane containing the two specified
     * components, with an initial split ratio of 0.5.
     *
     * @param comp0 the left component
     * @param comp1 the right component
     */
    public VerticalSplitPane(JComponent comp0, JComponent comp1) {
        this(comp0, comp1, 0.5);
    }

    /**
     * Creates a vertical split pane containing the two specified
     * components, with an initial split specified by the given fraction.
     * For example, a fraction of 0.25 will give a quarter of the space to
     * the left component, and three quarters of the space to the right
     * component.
     *
     * @param comp0    the left component
     * @param comp1    the right component
     * @param fraction the initial split fraction
     */
    public VerticalSplitPane(JComponent comp0, JComponent comp1,
                             double fraction) {
        this.comp0 = comp0;
        this.comp1 = comp1;
        this.dragbar = new MyDragbar();
        this.fraction = HorizontalSplitPane.clipFraction(fraction);

        setLayout(new MyLayout());
        // above the other components
        add(dragbar);
        add(comp0);
        add(comp1);
    }

    /**
     * Returns the current split fraction.
     *
     * @return the split fraction
     */
    public double getFraction() {
        return fraction;
    }


    /**
     * Sets the split fraction to the given value. Note that this value is
     * clipped to the range 0.0 to 1.0.
     *
     * @param value the new split value to set.
     */
    public void setFraction(double value) {
        double clipped = HorizontalSplitPane.clipFraction(value);
        if (fraction != clipped) {
            fraction = clipped;
            revalidate();
        }
    }
}
