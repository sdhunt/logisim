/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

/**
 * Listens for attribute events.
 */
public interface AttributeListener {
    /**
     * Callback invoked when an attribute list changes.
     *
     * @param e the event
     */
    void attributeListChanged(AttributeEvent e);

    /**
     * Callbeck invoked when an attribute value changes.
     *
     * @param e the event
     */
    void attributeValueChanged(AttributeEvent e);
}
