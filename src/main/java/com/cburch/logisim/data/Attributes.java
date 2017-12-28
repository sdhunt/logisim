/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import com.bric.swing.ColorPicker;
import com.cburch.logisim.util.FontUtil;
import com.cburch.logisim.util.JInputComponent;
import com.connectina.swing.fontchooser.JFontChooser;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

import static com.cburch.logisim.util.LocaleString.getFromLocale;

/**
 * Utility class for dealing with attributes.
 */
public final class Attributes {

    private static final String HASH = "#";
    private static final String ZERO = "0";
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String UNKNOWN = "???";

    private static final int MAX_SINGLE_INT_OPTS = 32;
    private static final String OX = "0x";
    private static final String OB = "0b";


    // non-instantiable
    private Attributes() {
    }

    // TODO: WTF? This doesn't add any value!@
    private static String getter(String s) {
        return s;
    }

    //
    // methods with display name == standard name
    // TODO: none of these are used anywhere (except forBoolean, see below)

    //
    public static Attribute<String> forString(String name) {
        return forString(name, getter(name));
    }

    public static Attribute<?> forOption(String name, Object[] vals) {
        return forOption(name, getter(name), vals);
    }

    public static Attribute<Integer> forInteger(String name) {
        return forInteger(name, getter(name));
    }

    public static Attribute<Integer> forHexInteger(String name) {
        return forHexInteger(name, getter(name));
    }

    public static Attribute<Integer> forIntegerRange(String name,
                                                     int start, int end) {
        return forIntegerRange(name, getter(name), start, end);
    }

    public static Attribute<Double> forDouble(String name) {
        return forDouble(name, getter(name));
    }

    // TODO: only used in NegateAttribute -- could be replaced and deleted...
    //   ...if NegateAttribute used the localization support version below
    public static Attribute<Boolean> forBoolean(String name) {
        return forBoolean(name, getter(name));
    }

    public static Attribute<Direction> forDirection(String name) {
        return forDirection(name, getter(name));
    }

    public static Attribute<BitWidth> forBitWidth(String name) {
        return forBitWidth(name, getter(name));
    }

    public static Attribute<BitWidth> forBitWidth(String name, int min, int max) {
        return forBitWidth(name, getter(name), min, max);
    }

    public static Attribute<Font> forFont(String name) {
        return forFont(name, getter(name));
    }

    public static Attribute<Location> forLocation(String name) {
        return forLocation(name, getter(name));
    }

    public static Attribute<Color> forColor(String name) {
        return forColor(name, getter(name));
    }

    //
    // methods with internationalization support
    //

    /**
     * Returns a string attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a string attribute instance
     */
    public static Attribute<String> forString(String name, String disp) {
        return new StringAttribute(name, disp);
    }

    /**
     * Returns an option attribute instance for the given option values.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @param vals the attribute option values
     * @return an option attribute instance
     */
    public static <V> Attribute<V> forOption(String name, String disp, V[] vals) {
        return new OptionAttribute<>(name, disp, vals);
    }

    /**
     * Returns an integer attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return an integer attribute instance
     */
    public static Attribute<Integer> forInteger(String name, String disp) {
        return new IntegerAttribute(name, disp);
    }

    /**
     * Returns a hex-integer attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a hex-integer attribute instance
     */
    public static Attribute<Integer> forHexInteger(String name, String disp) {
        return new HexIntegerAttribute(name, disp);
    }

    /**
     * Returns an integer-range attribute instance for the given start/end
     * range of integers.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @param start the first integer in the range
     * @param end the last integer in the range
     * @return an integer-range attribute instance
     */
    public static Attribute<Integer> forIntegerRange(String name, String disp,
                                                     int start, int end) {
        return new IntegerRangeAttribute(name, disp, start, end);
    }

    /**
     * Returns a double attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a double attribute instance
     */
    public static Attribute<Double> forDouble(String name, String disp) {
        return new DoubleAttribute(name, disp);
    }

    /**
     * Returns a boolean attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a boolean attribute instance
     */
    public static Attribute<Boolean> forBoolean(String name, String disp) {
        return new BooleanAttribute(name, disp);
    }

    /**
     * Returns a direction attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a direction attribute instance
     */
    public static Attribute<Direction> forDirection(String name, String disp) {
        return new DirectionAttribute(name, disp);
    }

    /**
     * Returns a bit-width attribute instance with min/max range of 1..32.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a bit-width attribute instance
     */
    public static Attribute<BitWidth> forBitWidth(String name, String disp) {
        return new BitWidth.Attribute(name, disp);
    }

    /**
     * Returns a bit-width attribute instance for the given min/max range.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @param min the minimum number of bits allowed
     * @param max the maximum number of bits allowed
     * @return a bit-width attribute instance
     */
    public static Attribute<BitWidth> forBitWidth(String name, String disp,
                                                  int min, int max) {
        return new BitWidth.Attribute(name, disp, min, max);
    }

    /**
     * Returns a font attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a font attribute instance
     */
    public static Attribute<Font> forFont(String name, String disp) {
        return new FontAttribute(name, disp);
    }

    /**
     * Returns a location attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a location attribute instance
     */
    public static Attribute<Location> forLocation(String name, String disp) {
        return new LocationAttribute(name, disp);
    }

    /**
     * Returns a color attribute instance.
     *
     * @param name the attribute name
     * @param disp the attribute display name
     * @return a color attribute instance
     */
    public static Attribute<Color> forColor(String name, String disp) {
        return new ColorAttribute(name, disp);
    }

    /**
     * Implements a string attribute.
     */
    private static class StringAttribute extends Attribute<String> {
        private StringAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public String parse(String value) {
            return value;
        }
    }

    // option combo renderer - used for cell editor of OptionAttribute
    private static class OptionComboRenderer<V> extends BasicComboBoxRenderer {
        Attribute<V> attr;

        OptionComboRenderer(Attribute<V> attr) {
            this.attr = attr;
        }

        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            Component ret = super.getListCellRendererComponent(list,
                                                               value,
                                                               index,
                                                               isSelected,
                                                               cellHasFocus);
            if (ret instanceof JLabel) {
                @SuppressWarnings("unchecked")
                V val = (V) value;
                String text = value == null ? EMPTY : attr.toDisplayString(val);
                ((JLabel) ret).setText(text);
            }
            return ret;
        }
    }

    /**
     * Option attribute implementation.
     *
     * @param <V> the type of attributes
     */
    private static class OptionAttribute<V> extends Attribute<V> {
        private final V[] vals;

        private OptionAttribute(String name, String disp, V[] vals) {
            super(name, disp);
            this.vals = vals;
        }

        @Override
        public String toDisplayString(V value) {
            if (value instanceof AttributeOptionInterface) {
                return ((AttributeOptionInterface) value).toDisplayString();
            }
            return value.toString();
        }

        @Override
        public V parse(String value) {
            for (V val : vals) {
                if (value.equals(val.toString())) {
                    return val;
                }
            }
            throw new NumberFormatException("value not among choices: " + value);
            // TODO: IllegalArgumentException would be better
        }

        @Override
        public java.awt.Component getCellEditor(Object value) {
            JComboBox<V> combo = new JComboBox<>(vals);
            combo.setRenderer(new OptionComboRenderer<V>(this));
            if (value == null) {
                combo.setSelectedIndex(-1);
            } else {
                combo.setSelectedItem(value);
            }
            return combo;
        }
    }

    /**
     * Integer attribute implementation.
     */
    private static class IntegerAttribute extends Attribute<Integer> {
        private IntegerAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public Integer parse(String value) {
            return Integer.valueOf(value);
        }
    }

    /**
     * Hex integer attribute implementation.
     */
    private static class HexIntegerAttribute extends Attribute<Integer> {
        private HexIntegerAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public String toDisplayString(Integer value) {
            return OX + Integer.toHexString(value);
        }

        @Override
        public String toStandardString(Integer value) {
            return toDisplayString(value);
        }

        @Override
        public Integer parse(String value) {
            value = value.toLowerCase();

            if (value.startsWith(OX)) {
                return (int) Long.parseLong(value.substring(2), 16);
            }

            if (value.startsWith(OB)) {
                return (int) Long.parseLong(value.substring(2), 2);
            }

            if (value.startsWith(ZERO)) {
                return (int) Long.parseLong(value.substring(1), 8);
            }

            return (int) Long.parseLong(value, 10);
        }
    }


    /**
     * Double attribute implementation.
     */
    private static class DoubleAttribute extends Attribute<Double> {
        private DoubleAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public Double parse(String value) {
            return Double.valueOf(value);
        }
    }

    /**
     * Boolean attribute implementation.
     */
    private static class BooleanAttribute extends OptionAttribute<Boolean> {
        private static Boolean[] vals = {Boolean.TRUE, Boolean.FALSE};

        private BooleanAttribute(String name, String disp) {
            super(name, disp, vals);
        }

        @Override
        public String toDisplayString(Boolean value) {
            String lkey = value ? "booleanTrueOption" : "booleanFalseOption";
            return getFromLocale(lkey);
        }

        @Override
        public Boolean parse(String value) {
            return Boolean.valueOf(value);
        }
    }

    /**
     * Integer Range attribute implementation.
     */
    private static class IntegerRangeAttribute extends Attribute<Integer> {
        private Integer[] options = null;
        private final int start;
        private final int end;
        private final int optCount;

        private IntegerRangeAttribute(String name, String disp,
                                      int start, int end) {
            super(name, disp);
            this.start = start;
            this.end = end;
            optCount = end - start + 1;
        }

        @Override
        public Integer parse(String value) {
            int v = (int) Long.parseLong(value);

            if (v < start) {
                throw new NumberFormatException("Integer too small: " + value);
            }
            if (v > end) {
                throw new NumberFormatException("Integer too large: " + value);
            }
            return v;
        }

        @Override
        public java.awt.Component getCellEditor(Integer value) {
            if (optCount > MAX_SINGLE_INT_OPTS) {
                return super.getCellEditor(value);
            }

            if (options == null) {
                options = new Integer[optCount];
                for (int i = start; i <= end; i++) {
                    options[i - start] = i;
                }
            }

            JComboBox<Integer> combo = new JComboBox<>(options);
            if (value == null) {
                combo.setSelectedIndex(-1);
            } else {
                combo.setSelectedItem(value);
            }

            return combo;
        }
    }


    /**
     * Direction attribute implementation.
     */
    private static class DirectionAttribute extends OptionAttribute<Direction> {
        private static final Direction[] vals = {
                Direction.NORTH,
                Direction.SOUTH,
                Direction.EAST,
                Direction.WEST,
        };

        private DirectionAttribute(String name, String disp) {
            super(name, disp, vals);
        }

        @Override
        public String toDisplayString(Direction value) {
            return value == null ? UNKNOWN : value.toDisplayString();
        }

        @Override
        public Direction parse(String value) {
            return Direction.parse(value);
        }
    }

    /**
     * Font attribute implementation.
     */
    private static class FontAttribute extends Attribute<Font> {
        private FontAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public String toDisplayString(Font f) {
            if (f == null) {
                return UNKNOWN;
            }

            return f.getFamily()
                    + SPACE + FontUtil.toStyleDisplayString(f.getStyle())
                    + SPACE + f.getSize();
        }

        @Override
        public String toStandardString(Font f) {
            return f.getFamily()
                    + SPACE + FontUtil.toStyleStandardString(f.getStyle())
                    + SPACE + f.getSize();
        }

        @Override
        public Font parse(String value) {
            return Font.decode(value);
        }

        @Override
        public java.awt.Component getCellEditor(Font value) {
            return new FontChooser(value);
        }
    }

    // implements a font chooser component; used as cell editor for font attr.
    private static class FontChooser extends JFontChooser
            implements JInputComponent {
        private FontChooser(Font initial) {
            super(initial);
        }

        @Override
        public Object getValue() {
            return getSelectedFont();
        }

        @Override
        public void setValue(Object value) {
            setSelectedFont((Font) value);
        }
    }

    /**
     * Location attribute implementation.
     */
    private static class LocationAttribute extends Attribute<Location> {
        private LocationAttribute(String name, String desc) {
            super(name, desc);
        }

        @Override
        public Location parse(String value) {
            return Location.parse(value);
        }
    }

    /**
     * Color attribute implementation.
     */
    private static class ColorAttribute extends Attribute<Color> {
        private ColorAttribute(String name, String desc) {
            super(name, desc);
        }

        @Override
        public String toDisplayString(Color value) {
            return toStandardString(value);
        }

        @Override
        public String toStandardString(Color c) {
            String ret = HASH +
                    hex(c.getRed()) + hex(c.getGreen()) + hex(c.getBlue());
            return c.getAlpha() == 255 ? ret : ret + hex(c.getAlpha());
        }

        private String hex(int value) {
            return (value < 16 ? ZERO : EMPTY) + Integer.toHexString(value);
        }

        @Override
        public Color parse(String value) {
            // TODO: make this more robust
            if (value.length() == 9) {
                int r = Integer.parseInt(value.substring(1, 3), 16);
                int g = Integer.parseInt(value.substring(3, 5), 16);
                int b = Integer.parseInt(value.substring(5, 7), 16);
                int a = Integer.parseInt(value.substring(7, 9), 16);
                return new Color(r, g, b, a);
            }
            return Color.decode(value);
        }

        @Override
        public java.awt.Component getCellEditor(Color value) {
            Color init = value == null ? Color.BLACK : value;
            return new ColorChooser(init);
        }
    }

    // Implements a color chooser; used as a cell editor for a color attribute
    private static class ColorChooser extends ColorPicker
            implements JInputComponent {

        ColorChooser(Color initial) {
            if (initial != null) {
                setColor(initial);
            }

            setOpacityVisible(true);
        }

        @Override
        public Object getValue() {
            return getColor();
        }

        @Override
        public void setValue(Object value) {
            setColor((Color) value);
        }
    }
}
