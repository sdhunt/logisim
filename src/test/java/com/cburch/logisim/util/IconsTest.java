/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.cburch.logisim.data.Direction;
import com.meowster.test.AbstractGraphicsTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link Icons}.
 */
public class IconsTest extends AbstractGraphicsTest {

    @Before
    public void setUp() {
        canvas = new TestCanvas();
        g2 = canvas.g2();
    }

    private void paint4Icons(int yoff, Icon icon) {
        Icons.paintRotated(g2, T1, yoff, Direction.EAST, icon, canvas);
        Icons.paintRotated(g2, T2, yoff, Direction.NORTH, icon, canvas);
        Icons.paintRotated(g2, T3, yoff, Direction.WEST, icon, canvas);
        Icons.paintRotated(g2, T4, yoff, Direction.SOUTH, icon, canvas);
    }

    @Ignore(DONT_RUN)
    @Test
    public void basic() {
        title("basic");
        paint4Icons(T1, Icons.getIcon("select.svg"));
        paint4Icons(T2, Icons.getIcon("pinInput.svg"));
        paint4Icons(T3, Icons.getIcon("pinOutput.svg"));

        showCanvasInFrame();
    }

    @Test
    public void loadAnIcon() {
        title("load an icon");
        Icon icon = Icons.getIcon("select.svg");
        assertThat(icon, is(notNullValue()));
        assertThat(icon.getIconWidth(), is(equalTo(16)));
        assertThat(icon.getIconHeight(), is(equalTo(16)));
    }
}
