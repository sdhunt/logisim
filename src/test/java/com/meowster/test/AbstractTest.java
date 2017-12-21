/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.meowster.test;

import com.cburch.logisim.util.LocaleString;
import com.meowster.util.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Locale;

import static com.meowster.util.StringUtils.EOL;

/**
 * Base class for unit tests.
 */
public abstract class AbstractTest {

    /**
     * Tolerance for double comparisons.
     */
    protected static final double TOLERANCE = 1e-9;

    /**
     * Enumeration useful for collection testing.
     */
    public enum StarWars {
        LUKE, LEIA, HAN, C3PO, R2D2, VADER
    }

    private static Locale systemLocale;


    /**
     * Print the given string to stdout.
     *
     * @param s the string to print
     */
    protected void print(String s) {
        StringUtils.print(s);
    }

    /**
     * Print the given object's string representation to stdout.
     *
     * @param o the object to print
     */
    protected void print(Object o) {
        StringUtils.print(o);
    }

    /**
     * Print the given string, formatted with the specified parameters.
     *
     * @param fmt    the string format
     * @param params the parameters
     * @see String#format(String, Object...)
     */
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

    @BeforeClass
    public static void classSetUp() {
        // ensure we always start in a known locale
        systemLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterClass
    public static void classTearDown() {
        // restore the default locale
        Locale.setDefault(systemLocale);
    }

    /**
     * Clears and re-initializes the locale cache for the given locale.
     *
     * @param locale the locale to set
     */
    protected void setUpLocaleMap(Locale locale) {
        LocaleString.clearSourceMap();
        Locale.setDefault(locale);
        LocaleString.reInitSourceMap();
    }
}
