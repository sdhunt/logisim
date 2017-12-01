/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.awt.*;

import static com.cburch.logisim.util.LocaleString.getFromLocale;

/**
 * Font utilities.
 */
public class FontUtil {
    /**
     * Returns the standard string for the given font style. The parameter
     * should be one of:
     * <ul>
     *     <li>{@code Font.PLAIN}</li>
     *     <li>{@code Font.BOLD}</li>
     *     <li>{@code Font.ITALIC}</li>
     *     <li>{@code Font.BOLD | Font.ITALIC}</li>
     * </ul>
     *
     * @param style the required style constant
     * @return the standard string representation
     */
    public static String toStyleStandardString(int style) {
        switch (style) {
            case Font.PLAIN:
                return "plain";
            case Font.ITALIC:
                return "italic";
            case Font.BOLD:
                return "bold";
            case Font.BOLD | Font.ITALIC:
                return "bolditalic";
            default:
                return "??";
        }
    }

    /**
     * Returns the display string (localized) for the given font style.
     * The parameter should be one of:
     * <ul>
     *     <li>{@code Font.PLAIN}</li>
     *     <li>{@code Font.BOLD}</li>
     *     <li>{@code Font.ITALIC}</li>
     *     <li>{@code Font.BOLD | Font.ITALIC}</li>
     * </ul>
     *
     * @param style the required style constant
     * @return the display string representation
     */
    public static String toStyleDisplayString(int style) {
        switch (style) {
            case Font.PLAIN:
                return getFromLocale("fontPlainStyle");
            case Font.ITALIC:
                return getFromLocale("fontItalicStyle");
            case Font.BOLD:
                return getFromLocale("fontBoldStyle");
            case Font.BOLD | Font.ITALIC:
                return getFromLocale("fontBoldItalicStyle");
            default:
                return "??";
        }
    }

}
