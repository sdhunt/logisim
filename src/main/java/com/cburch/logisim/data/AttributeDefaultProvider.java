/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import com.cburch.logisim.LogisimVersion;

/**
 * Provides default values for attributes.
 */
public interface AttributeDefaultProvider {

    /**
     * Returns true if the given attribute set contains all default values.
     *
     * @param attrs the attribute set
     * @param ver the Logisim version
     * @return true if all values are default; false otherwise
     */
    boolean isAllDefaultValues(AttributeSet attrs, LogisimVersion ver);

    /**
     * Returns the default value for the given attribute.
     *
     * @param attr the attribute
     * @param ver the Logisim version
     * @return the default value for the attribute
     */
    Object getDefaultAttributeValue(Attribute<?> attr, LogisimVersion ver);
}
