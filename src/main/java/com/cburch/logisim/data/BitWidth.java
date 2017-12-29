/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;


import javax.swing.*;

/**
 * Represents the number of bits in a wire or connection.
 */
public class BitWidth implements Comparable<BitWidth> {
    /**
     * Designates an unknown bit width.
     */
    public static final BitWidth UNKNOWN = new BitWidth(0);

    /**
     * Designates a bit width of 1.
     */
    public static final BitWidth ONE = new BitWidth(1);

    private static BitWidth[] prefab = null;

    /**
     * Bit width attribute implementation.
     */
    // TODO: rename this class; it isn't exposed outside Attributes
    static class Attribute extends com.cburch.logisim.data.Attribute<BitWidth> {
        private BitWidth[] choices;

        public Attribute(String name, String disp) {
            super(name, disp);
            ensurePrefab();
            choices = prefab;
        }

        public Attribute(String name, String disp, int min, int max) {
            super(name, disp);
            choices = new BitWidth[max - min + 1];
            for (int i = 0; i < choices.length; i++) {
                choices[i] = BitWidth.create(min + i);
            }
        }

        @Override
        public BitWidth parse(String value) {
            return BitWidth.parse(value);
        }

        @Override
        public java.awt.Component getCellEditor(BitWidth value) {
            JComboBox<BitWidth> combo = new JComboBox<>(choices);
            if (value != null) {
                int wid = value.getWidth();
                if (wid <= 0 || wid > prefab.length) {
                    combo.addItem(value);
                }
                combo.setSelectedItem(value);
            }
            return combo;
        }
    }

    private final int width;

    private BitWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the number of bits in this bit width instance.
     *
     * @return the number of bits
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns a bitmask corresponding to the number of bits in this
     * instance.
     * <p>
     * For example, a bit width of 6 will return the mask {@code 0x3f}.
     *
     * @return the bitmask
     */
    public int getMask() {
        if (width == 0) {
            return 0;
        }
        if (width == 32) {
            return -1;
        }
        return (1 << width) - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BitWidth bitWidth = (BitWidth) o;
        return width == bitWidth.width;
    }

    @Override
    public int hashCode() {
        return width;
    }

    @Override
    public int compareTo(BitWidth other) {
        return this.width - other.width;
    }

    @Override
    public String toString() {
        return "" + width;
    }

    /**
     * Returns a bit width instance of the specified width.
     *
     * @param width the required width
     * @return a corresponding bit width instance
     */
    public static BitWidth create(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("width " + width +
                                                       " must be positive");
        }
        if (width == 0) {
            return UNKNOWN;
        }

        ensurePrefab();
        int idx = width - 1;
        return idx < prefab.length ? prefab[idx] : new BitWidth(width);
    }

    /**
     * Parses the given string to produce an associated bit width instance.
     * Note that the string may start with a leading slash.
     * <p>
     * For example:
     * <pre>
     *     BitWidth bw4 = Bitwidth.parse("4");
     *     BitWidth bw4 = Bitwidth.parse("/4");
     * </pre>
     * @param str the string to parse
     * @return the corresponding bitwidth
     * @throws NumberFormatException if the given string is unacceptable
     */
    public static BitWidth parse(String str) {
        // TODO: should throw NPE and IArE here
        if (str == null || str.length() == 0) {
            throw new NumberFormatException("Width string cannot be null");
        }
        if (str.charAt(0) == '/') {
            str = str.substring(1);
        }

        return create(Integer.parseInt(str));
    }

    // TODO: Just do this up front, and don't call it from create()
    private static void ensurePrefab() {
        if (prefab == null) {
            prefab = new BitWidth[Math.min(32, Value.MAX_WIDTH)];
            prefab[0] = ONE;
            for (int i = 1; i < prefab.length; i++) {
                prefab[i] = new BitWidth(i + 1);
            }
        }
    }
}
