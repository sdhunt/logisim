/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.gui.menu;

/**
 * Represents a menu item.
 */
public class LogisimMenuItem {
    private final String name;

    /**
     * Creates a menu item with the given name.
     */
    LogisimMenuItem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
