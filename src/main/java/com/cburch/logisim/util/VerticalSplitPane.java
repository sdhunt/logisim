/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import javax.swing.*;

public class VerticalSplitPane extends AbstractSplitPane {

    /**
     * Creates a vertical split pane containing the two specified
     * components, with an initial split specified by the given fraction.
     * For example, a fraction of 0.25 will give a quarter of the space to
     * the left component, and three quarters of the space to the right
     * component.
     *
     * @param leftComp  the left component
     * @param rightComp the right component
     * @param fraction  the initial split fraction
     */
    public VerticalSplitPane(JComponent leftComp, JComponent rightComp,
                             double fraction) {
        super(leftComp, rightComp, fraction);
    }

    @Override
    protected Orient myOrient() {
        return Orient.VERT;
    }
}
