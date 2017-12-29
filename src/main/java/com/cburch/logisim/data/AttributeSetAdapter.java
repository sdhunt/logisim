/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import java.util.List;

/**
 * Adapter class for {@link AttributeSet}.
 */
public class AttributeSetAdapter implements AttributeSet {
    @Override
    public Object clone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAttributeListener(AttributeListener listener) {
    }

    @Override
    public void removeAttributeListener(AttributeListener listener) {
    }

    @Override
    public List<Attribute<?>> getAttributes() {
        return null;
    }

    @Override
    public boolean containsAttribute(Attribute<?> attr) {
        return false;
    }

    @Override
    public Attribute<?> getAttribute(String name) {
        return null;
    }

    @Override
    public boolean isReadOnly(Attribute<?> attr) {
        return false;
    }

    @Override
    public void setReadOnly(Attribute<?> attr, boolean value) {
    }

    @Override
    public boolean isToSave(Attribute<?> attr) {
        return false;
    }

    @Override
    public <V> V getValue(Attribute<V> attr) {
        return null;
    }

    @Override
    public <V> void setValue(Attribute<V> attr, V value) {
    }
}
