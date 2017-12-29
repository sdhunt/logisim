/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link BitWidth}.
 */
public class BitWidthTest extends AbstractTest {

    private static final int[] UNSORTED = {5, 32, 3, 7, 4, 0, 8, 1,};
    private static final int[] SORTED = {0, 1, 3, 4, 5, 7, 8, 32,};

    private BitWidth bw;

    @Test
    public void unknownBitWidth() {
        title("unknown bit width");
        bw = BitWidth.UNKNOWN;
        print(bw);
        assertThat(bw.getWidth(), is(0));
        assertThat(bw.getMask(), is(0));
    }

    @Test
    public void bitWidthOfOne() {
        title("bit width of 1");
        bw = BitWidth.ONE;
        print(bw);
        assertThat(bw.getWidth(), is(1));
        assertThat(bw.getMask(), is(0x1));
    }

    @Test
    public void createNegativeBitWidth() {
        title("create: negative");
        try {
            bw = BitWidth.create(-1);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void createBwUnknown() {
        title("create: unknown");
        bw = BitWidth.create(0);
        assertThat(bw, is(equalTo(BitWidth.UNKNOWN)));
    }

    private int expMaskFrom(int width) {
        // Naive way of creating a bit mask:
        //  start with 0; for each bit in width: shift left and set LSB to 1
        int mask = 0x0;
        for (int i = 0; i < width; i++) {
            mask <<= 1;
            mask |= 1;
        }
        print("mask for width %d is 0x%x", width, mask);
        return mask;
    }

    @Test
    public void createBitWidthsTo32() {
        title("create: 1 .. 32");
        for (int width = 1; width <= 32; width++) {
            bw = BitWidth.create(width);
            print(bw);
            assertThat(bw.getWidth(), is(width));
            assertThat(bw.getMask(), is(equalTo(expMaskFrom(width))));
        }
    }

    // TODO: Review -- Does it make sense to support bit widths > 32 ????
    @Test
    public void createBiggerBitWidth() {
        title("create bigger bit width (65)");
        bw = BitWidth.create(65);
        assertThat(bw.getWidth(), is(65));
        // TODO: mask == 1 doesn't make sense
        assertThat(bw.getMask(), is(1));
    }

    @Test
    public void parseNull() {
        title("parse: null");
        try {
            bw = BitWidth.parse(null);
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: code convention is to throw NPE
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void parseEmptyString() {
        title("parse: empty string");
        try {
            bw = BitWidth.parse("");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: code convention is to throw IArE
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void parseThree() {
        title("parse: 3");
        bw = BitWidth.parse("3");
        assertThat(bw.getWidth(), is(3));
    }

    @Test
    public void parseSlashThree() {
        title("parse: /3");
        bw = BitWidth.parse("/3");
        assertThat(bw.getWidth(), is(3));
    }

    @Test
    public void parseJunkThree() {
        title("parse: foo3");
        try {
            bw = BitWidth.parse("foo3");
            fail(E_NOEX);
        } catch (NumberFormatException e) {
            // TODO: code convention is to throw IArE
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void parseSixtyFive() {
        // TODO: Review -- should we limit width to 32 max?
        title("parse: 65");
        bw = BitWidth.parse("65");
        assertThat(bw.getWidth(), is(65));
    }

    private List<BitWidth> createList(int[] widths) {
        List<BitWidth> result = new ArrayList<>(widths.length);
        for (int w : widths) {
            result.add(BitWidth.create(w));
        }
        return result;
    }

    @Test
    public void comparisons() {
        title("comparisons");
        List<BitWidth> widths = createList(UNSORTED);
        List<BitWidth> sorted = createList(SORTED);
        assertThat(widths, is(not(equalTo(sorted))));

        Collections.sort(widths);
        assertThat(widths, is(equalTo(sorted)));
        print(widths);
    }

    @Test
    public void equalsAndHashCode() {
        title("equals and hashCode");
        bw = BitWidth.create(17);
        BitWidth other = BitWidth.create(17);
        assertThat(bw.equals(other), is(true));
        assertThat(other.equals(bw), is(true));
        assertThat(bw.equals(null), is(false));
        assertThat(bw.hashCode(), is(equalTo(other.hashCode())));
    }
}
