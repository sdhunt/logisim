/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link InputEventUtil}.
 */
public class InputEventUtilTest extends AbstractTest {

    private int mkBitMask(int[] bits) {
        int mask = 0;
        for (int b : bits) {
            mask |= b;
        }
        return mask;
    }

    private void checkBit(String str, int expBit) {
        int bits = InputEventUtil.fromTokenString(str);
        print("'%s' --> 0x%x", str, bits);
        assertThat(bits, is(equalTo(expBit)));
    }

    @Test
    public void fromTokenStringBasics() {
        title("fromTokenString() basics");
        checkBit("Shift", InputEvent.SHIFT_DOWN_MASK);
        checkBit("Ctrl", InputEvent.CTRL_DOWN_MASK);
        checkBit("Alt", InputEvent.ALT_DOWN_MASK);
        checkBit("Button1", InputEvent.BUTTON1_DOWN_MASK);
        checkBit("Button2", InputEvent.BUTTON2_DOWN_MASK);
        checkBit("Button3", InputEvent.BUTTON3_DOWN_MASK);
    }

    @Test
    public void fromTokenStringNoBits() {
        title("fromTokenString() no bits");
        int bits = InputEventUtil.fromTokenString("");
        print("0x%x", bits);
        assertThat(bits, is(equalTo(0)));
    }

    @Test
    public void fromTokenStringBadToken() {
        title("fromTokenString() bad token");
        try {
            InputEventUtil.fromTokenString("foobar");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
            print("correct> %s", e);
            assertThat(e.getMessage().endsWith("foobar"), is(true));
        }
    }

    private void checkMultiBits(String str, int... expBits) {
        int bits = InputEventUtil.fromTokenString(str);
        print("'%s' --> 0x%x", str, bits);
        assertThat(bits, is(equalTo(mkBitMask(expBits))));
    }

    @Test
    public void fromTokenStringMultiTokens() {
        title("fromTokenString() multi tokens");
        checkMultiBits("Alt Button2 Ctrl",
                       InputEvent.CTRL_DOWN_MASK,
                       InputEvent.ALT_DOWN_MASK,
                       InputEvent.BUTTON2_DOWN_MASK);

        checkMultiBits("Shift Button1 Button3",
                       InputEvent.BUTTON1_DOWN_MASK,
                       InputEvent.BUTTON3_DOWN_MASK,
                       InputEvent.SHIFT_DOWN_MASK);

        checkMultiBits("Ctrl Shift Alt Button1 Button3 Button2",
                       InputEvent.BUTTON1_DOWN_MASK,
                       InputEvent.BUTTON2_DOWN_MASK,
                       InputEvent.BUTTON3_DOWN_MASK,
                       InputEvent.CTRL_DOWN_MASK,
                       InputEvent.SHIFT_DOWN_MASK,
                       InputEvent.ALT_DOWN_MASK);
    }

    private void checkToken(int bit, String expToken) {
        String token = InputEventUtil.toTokenString(bit);
        print("0x%x --> '%s'", bit, token);
        assertThat(token, is(equalTo(expToken)));

    }

    @Test
    public void toTokenStringBasics() {
        title("toTokenString() basics");
        checkToken(InputEvent.SHIFT_DOWN_MASK, "Shift");
        checkToken(InputEvent.CTRL_DOWN_MASK, "Ctrl");
        checkToken(InputEvent.ALT_DOWN_MASK, "Alt");
        checkToken(InputEvent.BUTTON1_DOWN_MASK, "Button1");
        checkToken(InputEvent.BUTTON2_DOWN_MASK, "Button2");
        checkToken(InputEvent.BUTTON3_DOWN_MASK, "Button3");
    }

    private void checkMultiTokens(String expTokens, int... bits) {
        int bitmask = mkBitMask(bits);
        String tokens = InputEventUtil.toTokenString(bitmask);
        print("0x%x --> '%s'", bitmask, tokens);
        assertThat(tokens, is(equalTo(expTokens)));
    }

    @Test
    public void toTokenStringMultiBits() {
        title("toTokenString() multi bits");
        // NOTE: this test is brittle, in the sense that we need to know the
        //       order in which the implementation adds the tokens to the string

        checkMultiTokens("Ctrl Button1",
                         InputEvent.CTRL_DOWN_MASK,
                         InputEvent.BUTTON1_DOWN_MASK);

        checkMultiTokens("Alt Button2",
                         InputEvent.ALT_DOWN_MASK,
                         InputEvent.BUTTON2_DOWN_MASK);

        checkMultiTokens("Shift Button3",
                         InputEvent.SHIFT_DOWN_MASK,
                         InputEvent.BUTTON3_DOWN_MASK);

        checkMultiTokens("Shift Ctrl Alt Button1 Button2 Button3",
                         InputEvent.CTRL_DOWN_MASK,
                         InputEvent.ALT_DOWN_MASK,
                         InputEvent.SHIFT_DOWN_MASK,
                         InputEvent.BUTTON2_DOWN_MASK,
                         InputEvent.BUTTON1_DOWN_MASK,
                         InputEvent.BUTTON3_DOWN_MASK);
    }

    @Test
    public void toTokenStringNoBits() {
        title("toTokenString() no bits");
        String tokens = InputEventUtil.toTokenString(0);
        assertThat(tokens, is(equalTo("")));
    }

    @Test
    public void toTokenStringDontCareBits() {
        title("toTokenString() don't care bits");
        String tokens =
                InputEventUtil.toTokenString(InputEvent.ALT_GRAPH_DOWN_MASK);
        assertThat(tokens, is(equalTo("")));
    }

    private void checkDisplayString(int bit, String expString) {
        String disp = InputEventUtil.toDisplayString(bit);
        print("0x%x --> %s", bit, disp);
        assertThat(disp, is(equalTo(expString)));
    }

    @Test
    public void toDisplayStringBasics() {
        title("toDisplayString() basics");
        setUpLocaleMap(Locale.US);

        checkDisplayString(InputEvent.SHIFT_DOWN_MASK, "Shift");
        checkDisplayString(InputEvent.CTRL_DOWN_MASK, "Ctrl");
        checkDisplayString(InputEvent.ALT_DOWN_MASK, "Alt");
        checkDisplayString(InputEvent.BUTTON1_DOWN_MASK, "Button1");
        checkDisplayString(InputEvent.BUTTON2_DOWN_MASK, "Button2");
        checkDisplayString(InputEvent.BUTTON3_DOWN_MASK, "Button3");
    }

    @Test
    public void toDisplayStringBasicsAufDeutsch() {
        title("toDisplayString() basics - Auf Deutsch");
        setUpLocaleMap(Locale.GERMANY);

        checkDisplayString(InputEvent.SHIFT_DOWN_MASK, "Umschalt");
        checkDisplayString(InputEvent.CTRL_DOWN_MASK, "Strg");
        checkDisplayString(InputEvent.ALT_DOWN_MASK, "Alt");
        checkDisplayString(InputEvent.BUTTON1_DOWN_MASK, "Taste1");
        checkDisplayString(InputEvent.BUTTON2_DOWN_MASK, "Taste2");
        checkDisplayString(InputEvent.BUTTON3_DOWN_MASK, "Taste3");
    }

    private void checkMultiDisplay(String expDisp, int... bits) {
        int bitmask = mkBitMask(bits);
        String disp = InputEventUtil.toDisplayString(bitmask);
        print("0x%x --> %s", bitmask, disp);
        assertThat(disp, is(equalTo(expDisp)));
    }

    @Test
    public void toDisplayStringMulti() {
        title("toDisplayString() multi");
        setUpLocaleMap(Locale.US);

        checkMultiDisplay("Shift Alt Button2",
                          InputEvent.SHIFT_DOWN_MASK,
                          InputEvent.ALT_DOWN_MASK,
                          InputEvent.BUTTON2_DOWN_MASK);

        checkMultiDisplay("Ctrl Button1 Button3",
                          InputEvent.CTRL_DOWN_MASK,
                          InputEvent.BUTTON1_DOWN_MASK,
                          InputEvent.BUTTON3_DOWN_MASK);

        checkMultiDisplay("Shift Ctrl Alt Button1 Button2 Button3",
                          InputEvent.SHIFT_DOWN_MASK,
                          InputEvent.CTRL_DOWN_MASK,
                          InputEvent.ALT_DOWN_MASK,
                          InputEvent.BUTTON1_DOWN_MASK,
                          InputEvent.BUTTON2_DOWN_MASK,
                          InputEvent.BUTTON3_DOWN_MASK);
    }

    @Test
    public void toDisplayStringMultiAufDeutsch() {
        title("toDisplayString() multi - Auf Deutsch");
        setUpLocaleMap(Locale.GERMANY);

        checkMultiDisplay("Umschalt Alt Taste2",
                          InputEvent.SHIFT_DOWN_MASK,
                          InputEvent.ALT_DOWN_MASK,
                          InputEvent.BUTTON2_DOWN_MASK);

        checkMultiDisplay("Strg Taste1 Taste3",
                          InputEvent.CTRL_DOWN_MASK,
                          InputEvent.BUTTON1_DOWN_MASK,
                          InputEvent.BUTTON3_DOWN_MASK);

        checkMultiDisplay("Umschalt Strg Alt Taste1 Taste2 Taste3",
                          InputEvent.SHIFT_DOWN_MASK,
                          InputEvent.CTRL_DOWN_MASK,
                          InputEvent.ALT_DOWN_MASK,
                          InputEvent.BUTTON1_DOWN_MASK,
                          InputEvent.BUTTON2_DOWN_MASK,
                          InputEvent.BUTTON3_DOWN_MASK);
    }

    private void checkKeyDisplayString(int bit, String expString) {
        String disp = InputEventUtil.toKeyDisplayString(bit);
        print("0x%x --> %s", bit, disp);
        assertThat(disp, is(equalTo(expString)));

    }

    @Test
    public void toKeyDisplayStringBasics() {
        title("toKeyDisplayString() basics");
        setUpLocaleMap(Locale.US);

        checkKeyDisplayString(Event.SHIFT_MASK, "Shift");
        checkKeyDisplayString(Event.CTRL_MASK, "Ctrl");
        checkKeyDisplayString(Event.META_MASK, "Meta");
        checkKeyDisplayString(Event.ALT_MASK, "Alt");
    }

    @Test
    public void toKeyDisplayStringBasicsAufDeutsch() {
        title("toKeyDisplayString() basics -- auf Deutsch");
        setUpLocaleMap(Locale.GERMANY);

        checkKeyDisplayString(Event.SHIFT_MASK, "Umschalt");
        checkKeyDisplayString(Event.CTRL_MASK, "Strg");
        checkKeyDisplayString(Event.META_MASK, "Meta");
        checkKeyDisplayString(Event.ALT_MASK, "Alt");
    }

    private void checkMultiKeyDisplay(String expDisp, int... bits) {
        int bitmask = mkBitMask(bits);
        String disp = InputEventUtil.toKeyDisplayString(bitmask);
        print("0x%x --> %s", bitmask, disp);
        assertThat(disp, is(equalTo(expDisp)));
    }

    @Test
    public void toKeyDisplayStringMulti() {
        title("toKeyDisplayString() multi");
        setUpLocaleMap(Locale.US);

        checkMultiKeyDisplay("Shift Meta Alt",
                             Event.SHIFT_MASK,
                             Event.ALT_MASK,
                             Event.META_MASK);

        checkMultiKeyDisplay("Ctrl Meta",
                             Event.CTRL_MASK,
                             Event.META_MASK);

        checkMultiKeyDisplay("Shift Ctrl Meta Alt",
                             Event.SHIFT_MASK,
                             Event.CTRL_MASK,
                             Event.ALT_MASK,
                             Event.META_MASK);
    }


    @Test
    public void toKeyDisplayStringMultiAufDeutsch() {
        title("toKeyDisplayString() multi -- Auf Deutsch");
        setUpLocaleMap(Locale.GERMANY);

        checkMultiKeyDisplay("Umschalt Meta Alt",
                             Event.SHIFT_MASK,
                             Event.ALT_MASK,
                             Event.META_MASK);

        checkMultiKeyDisplay("Strg Meta",
                             Event.CTRL_MASK,
                             Event.META_MASK);

        checkMultiKeyDisplay("Umschalt Strg Meta Alt",
                             Event.SHIFT_MASK,
                             Event.CTRL_MASK,
                             Event.ALT_MASK,
                             Event.META_MASK);
    }
}
