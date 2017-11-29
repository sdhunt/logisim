/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.meowster.util;

/**
 * Useful utilities involving strings.
 */
public final class StringUtils {

    /**
     * Platform dependent end-of-line.
     */
    public static final String EOL = String.format("%n");

    /**
     * Prints the given string to stdout.
     *
     * @param s the string to print
     */
    public static void print(String s) {
        System.out.println(s);
    }

    /**
     * Prints the given object string representation to stdout.
     *
     * @param o the object to print
     */
    public static void print(Object o) {
        print(o.toString());
    }

    /**
     * Prints the given formatted string with the given parameters.
     *
     * @param fmt    the format string
     * @param params the parameters
     * @see String#format(String, Object...)
     */
    public static void print(String fmt, Object... params) {
        print(String.format(fmt, params));
    }
}
