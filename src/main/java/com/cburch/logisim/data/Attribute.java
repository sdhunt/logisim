/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an attribute of the given value type.
 *
 * @param <V> the value type
 */
public abstract class Attribute<V> {
    private final String name;
    private final String disp;

    /**
     * Constructs an attribute with the given internal name and display name.
     *
     * @param name the internal name
     * @param disp the display name
     */
    public Attribute(String name, String disp) {
        this.name = name;
        this.disp = disp;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns the attribute's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the attribute's display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return disp;
    }

    /**
     * Returns a cell editor setting the initial contents
     * to the given value.
     * <p>
     * This default implementation returns a JTextField element.
     *
     * @param source the source window
     * @param value the initial value to place in the editor
     * @return a cell editor instance
     */
    public java.awt.Component getCellEditor(Window source, V value) {
        return getCellEditor(value);
    }

    /**
     * Returns a cell editor setting the initial contents
     * to the given value.
     * <p>
     * This default implementation returns a JTextField element.
     *
     * @param value the initial value to place in the editor
     * @return a cell editor instance
     */
    protected java.awt.Component getCellEditor(V value) {
        return new JTextField(toDisplayString(value));
    }

    /**
     * Return a displayable string for the given attribute value.
     * <p>
     * This default implementation returns the value's toString() result.
     *
     * @param value the value to display
     * @return a string representation of the value
     */
    public String toDisplayString(V value) {
        return value == null ? "" : value.toString();
    }

    /**
     * Return the standard string for the given attribute value.
     * <p>
     * This default implementation returns the value's toString() result.
     *
     * @param value the value to display
     * @return a string representation of the value
     */
    public String toStandardString(V value) {
        return value.toString();
    }

    /**
     * Returns an attribute value of the appropriate type, parsed from the
     * given string representation.
     *
     * @param value the string representation
     * @return the value instance
     */
    public abstract V parse(String value);
}
