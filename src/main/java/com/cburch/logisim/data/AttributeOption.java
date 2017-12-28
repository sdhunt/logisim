/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

/**
 * Designates an attribute option.
 */
public class AttributeOption implements AttributeOptionInterface {
    private final Object value;
    private final String name;
    private final String desc;

    /**
     * Constructs an attribute option for the given value and description.
     * Note that the option's name is set to the string representation of
     * the value.
     *
     * @param value the option value
     * @param desc  the option description
     */
    public AttributeOption(Object value, String desc) {
        this.value = value;
        this.name = value.toString();
        this.desc = desc;
    }

    /**
     * Constructs an attribute option for the given value, with the given name
     * and description.
     *
     * @param value the option value
     * @param name  the option name
     * @param desc  the option description
     */
    public AttributeOption(Object value, String name, String desc) {
        this.value = value;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toDisplayString() {
        return desc;
    }
}
