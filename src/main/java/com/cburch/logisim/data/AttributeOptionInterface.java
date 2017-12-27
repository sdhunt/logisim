/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

/**
 * Attributes have options.
 */
public interface AttributeOptionInterface {
    /**
     * Returns the value of the attribute.
     *
     * @return the value
     */
    Object getValue();

    // ?? this doesn't make sense - all objects implement toString()
//    @Override
//    String toString();

    /**
     * Returns a string representation of the attribute suitable for
     * displaying to the user.
     *
     * @return a display string
     */
    String toDisplayString();
}
