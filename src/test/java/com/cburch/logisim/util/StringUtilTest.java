/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link StringUtil}.
 */
public class StringUtilTest extends AbstractTest {

    @Test
    public void hexStringBasic() {
        title("toHexString() - basic");
        String hex = StringUtil.toHexString(16, 20);
        print(hex);
        assertThat(hex, is(equalTo("0014")));
    }

    @Test
    public void hexStringTrunc() {
        title("toHexString() - truncated");
        String hex = StringUtil.toHexString(4, 20);
        print(hex);
        assertThat(hex, is(equalTo("4")));
    }

    private static final int[] VALUES = {
            0, 1, 5, 20, 33, 60, 126, 240, 398, 1234, 35987,
    };
    private static final String[] EXP_M4 = {
            "0", "1", "5", "4", "1", "c", "e", "0", "e", "2", "3",
    };
    private static final String[] EXP_M6 = {
            "00", "01", "05", "14", "21", "3c", "3e", "30", "0e", "12", "13",
    };
    private static final String[] EXP_M8 = {
            "00", "01", "05", "14", "21", "3c", "7e", "f0", "8e", "d2", "93",
    };
    private static final String[] EXP_M16 = {
            "0000", "0001", "0005", "0014", "0021", "003c", "007e", "00f0",
            "018e", "04d2", "8c93",
    };

    private void checkHexResults(int bits, String[] expResults) {
        print("...");
        for (int i = 0; i < VALUES.length; i++) {
            int value = VALUES[i];
            String exp = expResults[i];
            String act = StringUtil.toHexString(bits, value);
            print("bits:%d value:%d result:%s", bits, value, act);
            assertThat(act, is(equalTo(exp)));
        }
    }

    @Test
    public void hexStringSeveral() {
        title("toHexString() - several");
        checkHexResults(4, EXP_M4);
        checkHexResults(6, EXP_M6);
        checkHexResults(8, EXP_M8);
        checkHexResults(16, EXP_M16);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroBits() {
        StringUtil.toHexString(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeBits() {
        StringUtil.toHexString(-3, 5);
    }
}
