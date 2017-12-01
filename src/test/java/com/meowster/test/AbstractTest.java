/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.meowster.test;

import com.meowster.util.StringUtils;

import static com.meowster.util.StringUtils.EOL;

/**
 * Base class for unit tests.
 */
public abstract class AbstractTest {

    /**
     * Enumeration useful for collection testing.
     */
    public enum StarWars {LUKE, LEIA, HAN, C3PO, R2D2, VADER}

    protected void print(String s) {
        StringUtils.print(s);
    }

    protected void print(Object o) {
        StringUtils.print(o);
    }

    protected void print(String fmt, Object... params) {
        StringUtils.print(fmt, params);
    }

    /**
     * Prints a "title header".
     *
     * @param text the text of the title
     */
    protected void title(String text) {
        StringUtils.print("%s=== %s ===", EOL, text);
    }

}
