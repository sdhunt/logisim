/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static com.meowster.test.AbstractTest.StarWars.HAN;
import static com.meowster.test.AbstractTest.StarWars.LEIA;
import static com.meowster.test.AbstractTest.StarWars.LUKE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link Attributes}.
 */
public class AttributesTest extends AbstractAttributeTest {

    private static final String FOO = "foo";
    private static final String FOO_DISPLAY = "+F+O+O+";

    private static final String HELLO = "hello!";
    private static final String GOODBYE = "goodbye!";
    private static final String XYZZY = "xyzzy";
    private static final String FROBOZZ = "Frobozz";
    private static final String ZORK_I = "Zork I";
    private static final String ENCHANTER = "Enchanter";

    private static final String[] STR_OPTS = {
            XYZZY, FROBOZZ, ZORK_I
    };

    private static final Integer[] INT_OPTS = {
            1, 2, 3
    };

    private static final StarWars[] SW_OPTS = {
            LUKE, LEIA, HAN
    };

    private AttributeSetImpl asetImpl = new AttributeSetImpl();
    private AttributeSet aset = asetImpl;

    @Test
    public void stringAttribute() {
        title("string attribute");
        Attribute<String> attr = Attributes.forString(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        asetImpl.addAttribute(attr, HELLO);
        assertThat(aset.getValue(attr), is(equalTo(HELLO)));
        aset.setValue(attr, GOODBYE);
        assertThat(aset.getValue(attr), is(equalTo(GOODBYE)));

        assertThat(attr.parse(HELLO), is(equalTo(HELLO)));
    }

    @Test
    public void stringOptionAttribute() {
        title("string OPTION attribute");
        Attribute<String> attrOpt =
                Attributes.forOption(FOO, FOO_DISPLAY, STR_OPTS);
        assertThat(attrOpt.getName(), is(equalTo(FOO)));
        assertThat(attrOpt.getDisplayName(), is(equalTo(FOO_DISPLAY)));
        assertThat(attrOpt.toDisplayString(FROBOZZ), is(equalTo(FROBOZZ)));

        assertThat(attrOpt.parse(XYZZY), is(equalTo(XYZZY)));

        try {
            attrOpt.parse(ENCHANTER);
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: IllegalArgumentException would be better
            print(FMT_CORRECT, e);
        }

        // TODO: creating swing object takes an appreciable amount of time :(
//        JComboBox<String> combo = (JComboBox<String>) attrOpt.getCellEditor(null);
//        assertThat(combo.getItemCount(), is(3));
    }

    @Test
    public void integerOptionAttribute() {
        title("integer OPTION attribute");
        Attribute<Integer> attrOpt =
                Attributes.forOption(FOO, FOO_DISPLAY, INT_OPTS);
        assertThat(attrOpt.getName(), is(equalTo(FOO)));
        assertThat(attrOpt.getDisplayName(), is(equalTo(FOO_DISPLAY)));
        assertThat(attrOpt.toDisplayString(1), is(equalTo("1")));

        assertThat(attrOpt.parse("2"), is(equalTo(2)));

        try {
            attrOpt.parse("9");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: IllegalArgumentException would be better
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void starWarsOptionAttribute() {
        title("StarWars OPTION attribute");
        Attribute<StarWars> attrOpt =
                Attributes.forOption(FOO, FOO_DISPLAY, SW_OPTS);
        assertThat(attrOpt.getName(), is(equalTo(FOO)));
        assertThat(attrOpt.getDisplayName(), is(equalTo(FOO_DISPLAY)));
        assertThat(attrOpt.toDisplayString(HAN), is(equalTo("HAN")));

        assertThat(attrOpt.parse("LEIA"), is(equalTo(LEIA)));

        try {
            attrOpt.parse("VADER");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: IllegalArgumentException would be better
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void integerAttribute() {
        title("integer attribute");
        Attribute<Integer> attr = Attributes.forInteger(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        asetImpl.addAttribute(attr, 42);
        assertThat(aset.getValue(attr), is(42));
        aset.setValue(attr, 69);
        assertThat(aset.getValue(attr), is(69));

        assertThat(attr.parse("100"), is(100));
    }

    @Test
    public void hexIntegerAttribute() {
        title("hex integer attribute");
        Attribute<Integer> attr = Attributes.forHexInteger(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        assertThat(attr.toDisplayString(20), is(equalTo("0x14")));
        assertThat(attr.toStandardString(20), is(equalTo("0x14")));

        assertThat(attr.parse("0x100"), is(256));
        assertThat(attr.parse("0XAB"), is(171));
        assertThat(attr.parse("0xab"), is(171));
        assertThat(attr.parse("0b1010"), is(10));
        assertThat(attr.parse("0B10101"), is(21));
        assertThat(attr.parse("055"), is(45));
        assertThat(attr.parse("321"), is(321));
    }

    @Test
    public void integerRangeAttribute() {
        title("integer range attribute");
        Attribute<Integer> attr = Attributes.forIntegerRange(FOO, FOO_DISPLAY,
                                                             5, 10);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        assertThat(attr.parse("7"), is(7));

        try {
            attr.parse("4");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            print(FMT_CORRECT, e);
            assertThat(e.getMessage().endsWith("too small: 4"), is(true));
        }

        try {
            attr.parse("11");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            print(FMT_CORRECT, e);
            assertThat(e.getMessage().endsWith("too large: 11"), is(true));
        }
    }

    @Test
    public void doubleAttribute() {
        title("double attribute");
        Attribute<Double> attr = Attributes.forDouble(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        assertThat(attr.parse("3.1415"), is(equalTo(3.1415)));
    }

    @Test
    public void booleanAttribute() {
        title("boolean attribute");
        Attribute<Boolean> attr = Attributes.forBoolean(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        assertThat(attr.parse("true"), is(equalTo(true)));
        assertThat(attr.toDisplayString(true), is(equalTo("Yes")));
        assertThat(attr.toDisplayString(false), is(equalTo("No")));

        setUpLocaleMap(Locale.GERMANY);
        assertThat(attr.toDisplayString(true), is(equalTo("Ja")));
        assertThat(attr.toDisplayString(false), is(equalTo("Nein")));
        setUpLocaleMap(Locale.US);
    }

    @Test
    public void directionAttribute() {
        title("direction attribute");
        Attribute<Direction> attr = Attributes.forDirection(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        assertThat(attr.toDisplayString(Direction.NORTH), is(equalTo("North")));
        assertThat(attr.toDisplayString(Direction.SOUTH), is(equalTo("South")));
        assertThat(attr.toDisplayString(Direction.EAST), is(equalTo("East")));
        assertThat(attr.toDisplayString(Direction.WEST), is(equalTo("West")));
        assertThat(attr.toDisplayString(null), is(equalTo("???")));

        assertThat(attr.parse("north"), is(equalTo(Direction.NORTH)));
        assertThat(attr.parse("south"), is(equalTo(Direction.SOUTH)));
        assertThat(attr.parse("east"), is(equalTo(Direction.EAST)));
        assertThat(attr.parse("west"), is(equalTo(Direction.WEST)));

        try {
            attr.parse("up");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: IllegalArgumentException would be better
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void bitWidthAttribute() {
        title("bit-width attribute");
        Attribute<BitWidth> attr = Attributes.forBitWidth(FOO, FOO_DISPLAY);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        BitWidth bw = attr.parse("2");
        assertThat(bw.getWidth(), is(2));
        assertThat(bw.getMask(), is(0x3));

        bw = attr.parse("6");
        assertThat(bw.getWidth(), is(6));
        assertThat(bw.getMask(), is(0x3f));

        bw = attr.parse("32");
        assertThat(bw.getWidth(), is(32));
        assertThat(bw.getMask(), is(0xffffffff));
    }

    @Test
    public void bitWidthConstrainedAttribute() {
        title("bit-width constrained attribute");
        Attribute<BitWidth> attr = Attributes.forBitWidth(FOO, FOO_DISPLAY, 4, 8);
        assertThat(attr.getName(), is(equalTo(FOO)));
        assertThat(attr.getDisplayName(), is(equalTo(FOO_DISPLAY)));

        JComboBox combo = (JComboBox) attr.getCellEditor(null);
        assertThat(combo.getItemCount(), is(5));
    }

    @Ignore("Slow to run ~ 1.735 secs")
    @Test
    public void fontAttribute() {
        title("font attribute");
        Attribute<Font> attr = Attributes.forFont(FOO, FOO_DISPLAY);

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        print(font);
        print(attr.toDisplayString(font));
        assertThat(attr.toDisplayString(font), is(equalTo("SansSerif Bold 14")));
        assertThat(attr.parse("SansSerif Bold 14"), is(equalTo(font)));
    }

    @Test
    public void locationAttribute() {
        title("location attribute");
        Attribute<Location> attr = Attributes.forLocation(FOO, FOO_DISPLAY);

        Location loc = attr.parse("(3, 5)");
        print(loc);
        assertThat(loc.getX(), is(3));
        assertThat(loc.getY(), is(5));
        assertThat(loc.toString(), is(equalTo("(3,5)")));
    }

    @Test
    public void colorAttribute() {
        title("color attribute");
        Attribute<Color> attr = Attributes.forColor(FOO, FOO_DISPLAY);

        assertThat(attr.toDisplayString(Color.RED), is(equalTo("#ff0000")));
        assertThat(attr.toDisplayString(Color.GREEN), is(equalTo("#00ff00")));
        assertThat(attr.toDisplayString(Color.BLUE), is(equalTo("#0000ff")));

        assertThat(attr.parse("#ffff00"), is(equalTo(Color.YELLOW)));

        Color exp = new Color(0x11, 0x22, 0x33, 0x44);
        // Hmmmm... really?
        assertThat(attr.parse("#11223344"), is(equalTo(exp)));
        assertThat(attr.parse("*11223344"), is(equalTo(exp)));
        assertThat(attr.parse("~11223344"), is(equalTo(exp)));

        assertThat(attr.toStandardString(exp), is(equalTo("#11223344")));
    }
}
