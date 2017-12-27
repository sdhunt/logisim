/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import java.util.List;

/**
 * Represents a set of attributes.
 */
public interface AttributeSet {

    /**
     * Clones this attribute set.
     *
     * @return a clone of this set
     */
    Object clone();

    /**
     * Adds an attribute listener to this set.
     *
     * @param l the listener to add
     */
    void addAttributeListener(AttributeListener l);

    /**
     * Removes an attribute listener from this set.
     *
     * @param l the listener to remove
     */
    void removeAttributeListener(AttributeListener l);

    /**
     * Returns the list of attributes contained in this set.
     *
     * @return the list of attributes
     */
    List<Attribute<?>> getAttributes();

    /**
     * Returns true if the specified attribute is contained within this set;
     * false otherwise.
     *
     * @param attr the attribute to test
     * @return true if the attribute is a member of this set; false otherwise
     */
    boolean containsAttribute(Attribute<?> attr);

    /**
     * Returns the attribute with the given name from this set. If no such
     * attribute exists, null is returned.
     *
     * @param name the name of the attribute
     * @return the corresponding attribute; or null
     */
    Attribute<?> getAttribute(String name);

    /**
     * Returns true if the specified attribute is read-only.
     *
     * @param attr the attribute to test
     * @return true if the attribute is read-only; false otherwise
     */
    boolean isReadOnly(Attribute<?> attr);

    /**
     * Sets (or clears) the specified attribute's read-only status.
     *
     * @param attr the attribute
     * @param value true to set read-only; false to set read-write
     */
    // optional (?)
    void setReadOnly(Attribute<?> attr, boolean value);

    /**
     * Returns true if the specified attribute should be saved.
     *
     * @param attr the attribute
     * @return true if the attribute needs saving; false otherwise
     */
    boolean isToSave(Attribute<?> attr);

    /**
     * Attempts to cast the specified attribute as the given type, and return
     * it from this set.
     *
     * @param attr the attribute
     * @param <V> its type
     * @return the attribute value
     */
    <V> V getValue(Attribute<V> attr);

    /**
     * Attempts to set the specified attribute as the given type.
     *
     * @param attr the attribute to set
     * @param value its value
     * @param <V> the value type
     */
    <V> void setValue(Attribute<V> attr, V value);
}
