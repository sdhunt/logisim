/*
 * Copyright (c) 2010, Carl Burch.
 * Copyright (c) 2014, Spanti Nicola (RyDroid)
 * License information is located in the com.cburch.logisim.
 * Main source code and at https://github.com/lawrancej/logisim/
 */

package com.cburch.logisim.util;

/**
 * Useful functions for manipulating strings.
 */
public class StringUtil {

    // TODO: WTF?? Exactly what is the point of this method?
    public static String constantGetter(final String value) {
        return value;
    }

    /**
     * Returns a string representation of the given value using hex digits,
     * where the value is first truncated to the lowest n bits, using a
     * bit mask.
     *
     * @param bits  number of bits in the mask
     * @param value the value to convert
     * @return a hex string representation
     */
    public static String toHexString(int bits, int value) {
        if (bits < 1) {
            throw new IllegalArgumentException("bits must be 1..32");
        }

        int maskedValue = bits >= 32 ? value : value & ((1 << bits) - 1);

        StringBuilder sb = new StringBuilder(Integer.toHexString(maskedValue));
        int len = (bits + 3) / 4;
        while (sb.length() < len) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }
}
