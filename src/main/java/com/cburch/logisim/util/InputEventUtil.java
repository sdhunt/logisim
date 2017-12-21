/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

import static com.cburch.logisim.util.LocaleString.getFromLocale;

/**
 * Utility methods for input events.
 */
public final class InputEventUtil {
    private static final String E_BAD_TOKEN = "Bad event bit token: ";
    private static final String SPACE = " ";

    private static final String SHIFT = "Shift";
    private static final String CTRL = "Ctrl";
    private static final String ALT = "Alt";
    private static final String BUTTON1 = "Button1";
    private static final String BUTTON2 = "Button2";
    private static final String BUTTON3 = "Button3";

    private static final String LKEY_META_MOD = "metaMod";
    private static final String LKEY_SHIFT_MOD = "shiftMod";
    private static final String LKEY_CTRL_MOD = "ctrlMod";
    private static final String LKEY_ALT_MOD = "altMod";
    private static final String LKEY_BUTTON1_MOD = "button1Mod";
    private static final String LKEY_BUTTON2_MOD = "button2Mod";
    private static final String LKEY_BUTTON3_MOD = "button3Mod";

    private static final int[] INPUT_EVENT_BITS = {
            InputEvent.SHIFT_DOWN_MASK,
            InputEvent.CTRL_DOWN_MASK,
            InputEvent.ALT_DOWN_MASK,
            InputEvent.BUTTON1_DOWN_MASK,
            InputEvent.BUTTON2_DOWN_MASK,
            InputEvent.BUTTON3_DOWN_MASK,
    };

    private static final String[] INPUT_EVENT_TOKENS = {
            SHIFT, CTRL, ALT, BUTTON1, BUTTON2, BUTTON3,
    };

    private static final String[] INPUT_EVENT_KEYS = {
            LKEY_SHIFT_MOD, LKEY_CTRL_MOD, LKEY_ALT_MOD,
            LKEY_BUTTON1_MOD, LKEY_BUTTON2_MOD, LKEY_BUTTON3_MOD,
    };

    private static final int[] EVENT_BITS = {
            Event.SHIFT_MASK,
            Event.CTRL_MASK,
            Event.META_MASK,
            Event.ALT_MASK,
    };

    private static final String[] EVENT_KEYS = {
            LKEY_SHIFT_MOD, LKEY_CTRL_MOD, LKEY_META_MOD, LKEY_ALT_MOD,
    };

    private static final Map<String, Integer> TOKEN_TO_BIT = new HashMap<>();

    static {
        TOKEN_TO_BIT.put(SHIFT, InputEvent.SHIFT_DOWN_MASK);
        TOKEN_TO_BIT.put(CTRL, InputEvent.CTRL_DOWN_MASK);
        TOKEN_TO_BIT.put(ALT, InputEvent.ALT_DOWN_MASK);
        TOKEN_TO_BIT.put(BUTTON1, InputEvent.BUTTON1_DOWN_MASK);
        TOKEN_TO_BIT.put(BUTTON2, InputEvent.BUTTON2_DOWN_MASK);
        TOKEN_TO_BIT.put(BUTTON3, InputEvent.BUTTON3_DOWN_MASK);
    }

    // non-instantiable
    private InputEventUtil() {
    }

    /**
     * Returns a bit mask of {@link InputEvent} constants corresponding to
     * bit tokens defined in the given string.
     *
     * @param tokenString the input string
     * @return corresponding bit mask
     * @throws IllegalArgumentException if any token in the string is not valid
     */
    public static int fromTokenString(String tokenString) {
        int bitmask = 0;
        StringTokenizer tokens = new StringTokenizer(tokenString);
        while (tokens.hasMoreTokens()) {
            String s = tokens.nextToken();
            Integer bit = TOKEN_TO_BIT.get(s);
            if (bit != null) {
                bitmask |= bit;
            } else {
                throw new IllegalArgumentException(E_BAD_TOKEN + s);
            }
        }
        return bitmask;
    }

    /**
     * Returns a string of tokens corresponding to the given event bit mask.
     *
     * @param mods the event bit modifiers
     * @return the corresponding tokenized string
     */
    public static String toTokenString(int mods) {
        return toSomeString(mods, INPUT_EVENT_BITS,
                            (i) -> INPUT_EVENT_TOKENS[i]);
    }

    /**
     * Returns a display string of event modifier tokens (in the default locale).
     *
     * @param mods the modifier bitmask
     * @return a space-delimited list of tokens
     */
    public static String toDisplayString(int mods) {
        return toSomeString(mods, INPUT_EVENT_BITS,
                            (i) -> getFromLocale(INPUT_EVENT_KEYS[i]));
    }

    /**
     * Returns a display string of key modifier tokens (in the default locale).
     *
     * @param mods the modifier bitmask
     * @return a space delimited list of tokens
     */
    public static String toKeyDisplayString(int mods) {
        return toSomeString(mods, EVENT_BITS,
                            (i) -> getFromLocale(EVENT_KEYS[i]));
    }

    private static String toSomeString(int mods, int[] bitset,
                                       Function<Integer, String> f) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitset.length; i++) {
            int bit = bitset[i];
            if ((mods & bit) != 0) {
                sb.append(f.apply(i)).append(SPACE);
            }
        }
        final int len = sb.length();
        if (len > 0) {
            sb.delete(len - 1, len);
        }
        return sb.toString();
    }
}
