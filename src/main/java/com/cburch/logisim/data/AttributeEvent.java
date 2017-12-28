/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

/**
 * Encapsulates an attribute-related event.
 */
public class AttributeEvent {
    private final AttributeSet source;
    private final Attribute<?> attr;
    private final Object value;

    /**
     * Constructs an event for the given source attribute set, attribute, and
     * new value.
     *
     * @param source the source attribute set
     * @param attr   the attribute
     * @param value  the new value
     */
    public AttributeEvent(AttributeSet source, Attribute<?> attr, Object value) {
        this.source = source;
        this.attr = attr;
        this.value = value;
    }

    /**
     * Constructs an event for the given source attribute set.
     * The attribute and value fields are both set to null.
     *
     * @param source the source attribute set
     */
    public AttributeEvent(AttributeSet source) {
        this(source, null, null);
    }

    /**
     * Returns a reference to the source attribute set.
     *
     * @return the source attribute set
     */
    public AttributeSet getSource() {
        return source;
    }

    /**
     * Returns the attribute.
     *
     * @return the attribute
     */
    public Attribute<?> getAttribute() {
        return attr;
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }
}
