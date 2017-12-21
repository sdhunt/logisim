/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

/**
 * Represents an input component.
 */
public interface JInputComponent {
    /**
     * Returns the value from the component.
     *
     * @return the value
     */
    Object getValue();

    /**
     * Sets the value on the component.
     *
     * @param value the value to set
     */
    void setValue(Object value);
}
