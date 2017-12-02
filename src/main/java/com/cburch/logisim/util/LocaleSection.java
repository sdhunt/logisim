/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

/**
 * Designates the localization sections.
 */
enum LocaleSection {
    ANALYZE,
    CIRCUIT,
    DATA,
    DRAW,
    FILE,
    GUI,
    HEX,
    LOG,
    MENU,
    OPTS,
    PREFS,
    PROJ,
    START,
    STD,
    TOOLS,
    UTIL;


    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
