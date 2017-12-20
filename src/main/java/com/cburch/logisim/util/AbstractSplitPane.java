/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Abstract superclass of the horizontal and vertical split pane components.
 */
abstract class AbstractSplitPane extends JPanel {

    enum Orient {HORIZ, VERT}

    private static final Color DRAG_COLOR = new Color(0, 0, 0, 128);
    private static final int DRAG_TOLERANCE = 3;

    private JComponent comp0;
    private JComponent comp1;
    private DragBar dragBar;
    private double fraction;


    AbstractSplitPane(JComponent comp0, JComponent comp1, double fraction) {
        this.comp0 = comp0;
        this.comp1 = comp1;
        this.fraction = clipFraction(fraction);

        this.dragBar = new DragBar();
        setLayout(new SplitLayoutManager());

        add(this.dragBar);
        add(comp0);
        add(comp1);
    }

    private double clipFraction(double value) {
        return value < 0.0 ? 0.0 : value > 1.0 ? 1.0 : value;
    }

    private void setFraction(double value) {
        double clipped = clipFraction(value);

        if (fraction != clipped) {
            fraction = clipped;
            revalidate();
        }
    }

    /**
     * Returns the current split fraction.
     *
     * @return the split fraction
     */
    public double getFraction() {
        return fraction;
    }

    protected abstract Orient myOrient();

    /**
     * Our custom layout manager.
     */
    private class SplitLayoutManager implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return computeLayoutSize(parent,
                                     comp0.getPreferredSize(),
                                     comp1.getPreferredSize());
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return computeLayoutSize(parent,
                                     comp0.getMinimumSize(),
                                     comp1.getMinimumSize());
        }

        private Dimension computeLayoutSize(Container parent,
                                            Dimension d0, Dimension d1) {
            if (fraction <= 0.0) {
                return d1;
            }
            if (fraction >= 1.0) {
                return d0;
            }

            Insets in = parent.getInsets();

            int w = myOrient() == Orient.HORIZ ?
                    Math.max(d0.width, d1.width) : d0.width + d1.width;

            int h = myOrient() == Orient.HORIZ ?
                    d0.height + d1.height : Math.max(d0.height, d1.height);

            return new Dimension(in.left + w + in.right, in.top + h + in.bottom);
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets in = parent.getInsets();
            int maxWidth = parent.getWidth() - (in.left + in.right);
            int maxHeight = parent.getHeight() - (in.top + in.bottom);
            Dimension d0Min = comp0.getMinimumSize();
            Dimension d1Min = comp1.getMinimumSize();

            int split;
            if (myOrient() == Orient.HORIZ) {
                split = (int) Math.round(maxHeight * fraction);
                split = Math.min(split, maxHeight - d1Min.height);
                split = Math.max(split, d0Min.height);
                comp0.setBounds(in.left, in.top, maxWidth, split);
                comp1.setBounds(in.left, in.top + split,
                                maxWidth, maxHeight - split);
                dragBar.setBounds(in.left, in.top + split - DRAG_TOLERANCE,
                                  maxWidth, 2 * DRAG_TOLERANCE);
            } else {
                split = (int) Math.round(maxWidth * fraction);
                split = Math.min(split, maxWidth - d1Min.width);
                split = Math.max(split, d0Min.width);
                comp0.setBounds(in.left, in.top, split, maxHeight);
                comp1.setBounds(in.left + split, in.top,
                                maxWidth - split, maxHeight);
                dragBar.setBounds(in.left + split - DRAG_TOLERANCE, in.top,
                                  2 * DRAG_TOLERANCE, maxHeight);
            }
        }
    }


    /**
     * Our custom drag bar component.
     */
    private class DragBar extends JComponent {
        private final MouseAdapter myMouseAdapter = new MyMouseAdapter();

        private boolean dragging = false;
        private int curValue;

        private DragBar() {
            addMouseListener(myMouseAdapter);
            addMouseMotionListener(myMouseAdapter);
            int cursor = myOrient() == Orient.HORIZ ?
                    Cursor.S_RESIZE_CURSOR : Cursor.E_RESIZE_CURSOR;
            setCursor(Cursor.getPredefinedCursor(cursor));
        }

        private int getDragValue(MouseEvent e) {
            Insets in = AbstractSplitPane.this.getInsets();
            return myOrient() == Orient.HORIZ
                    ? getY() + e.getY() - in.top
                    : getX() + e.getX() - in.left;
        }

        private void setDragValue(int value) {
            AbstractSplitPane sp = AbstractSplitPane.this;
            Insets in = sp.getInsets();
            int divisor = myOrient() == Orient.HORIZ
                    ? sp.getHeight() - in.bottom - in.top
                    : sp.getWidth() - in.left - in.right;
            setFraction((double) value / divisor);
            revalidate();
        }

        @Override
        public void paintComponent(Graphics g) {
            if (dragging) {
                g.setColor(DRAG_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }


        private class MyMouseAdapter extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!dragging) {
                    curValue = getDragValue(e);
                    dragging = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragging) {
                    dragging = false;
                    int newValue = getDragValue(e);
                    if (newValue != curValue) {
                        setDragValue(newValue);
                    }
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int newValue = getDragValue(e);
                    if (newValue != curValue) {
                        setDragValue(newValue);
                    }
                }
            }
        }
    }
}
