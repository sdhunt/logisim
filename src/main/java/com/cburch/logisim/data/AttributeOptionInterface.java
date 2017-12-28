/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

/**
 * Option attribute values implement this interface to provide consistent
 * access to the option values and display strings.
 */
public interface AttributeOptionInterface {
    /**
     * Returns the value of the attribute.
     *
     * @return the value
     */
    Object getValue();

    /**
     * Returns a string representation of the attribute suitable for
     * displaying to the user.
     *
     * @return a display string
     */
    String toDisplayString();
}
