/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import javax.swing.*;

/**
 * Implements a horizontal split pane.
 */
public class HorizontalSplitPane extends AbstractSplitPane {

    /**
     * Creates a horizontal split pane containing the two specified
     * components, with an initial split ratio of the given fraction.
     * For example, a fraction of 0.25 will give a quarter of the space to
     * the top component, and three quarters of the space to the bottom
     * component.
     *
     * @param topComp    the top component
     * @param bottomComp the bottom component
     * @param fraction   the initial split fraction
     */
    public HorizontalSplitPane(JComponent topComp, JComponent bottomComp,
                               double fraction) {
        super(topComp, bottomComp, fraction);
    }

    @Override
    protected Orient myOrient() {
        return Orient.HORIZ;
    }
}
