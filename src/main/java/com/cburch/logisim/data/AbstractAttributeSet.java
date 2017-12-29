/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a base class for attribute set implementations.
 */
public abstract class AbstractAttributeSet implements Cloneable, AttributeSet {
    private List<AttributeListener> listeners = null;

    @Override
    public Object clone() {
        AbstractAttributeSet ret;
        try {
            ret = (AbstractAttributeSet) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException();
        }
        ret.listeners = new ArrayList<>();
        copyInto(ret);
        return ret;
    }

    @Override
    public void addAttributeListener(AttributeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        listeners.add(listener);
    }

    @Override
    public void removeAttributeListener(AttributeListener listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            listeners = null;
        }
    }

    /**
     * Fires an attribute value changed event to all registered listeners.
     *
     * @param attr  the attribute that changed
     * @param value the attribute's new value
     * @param <V>   the attribute's type
     */
    protected <V> void fireAttributeValueChanged(Attribute<? super V> attr,
                                                 V value) {
        if (listeners != null) {
            AttributeEvent event = new AttributeEvent(this, attr, value);
            List<AttributeListener> ls = new ArrayList<>(listeners);
            for (AttributeListener l : ls) {
                l.attributeValueChanged(event);
            }
        }
    }

    /**
     * Fires an attribute list changed event to all registered listeners.
     */
    protected void fireAttributeListChanged() {
        if (listeners != null) {
            AttributeEvent event = new AttributeEvent(this);
            List<AttributeListener> ls = new ArrayList<>(listeners);
            for (AttributeListener l : ls) {
                l.attributeListChanged(event);
            }
        }
    }

    @Override
    public boolean containsAttribute(Attribute<?> attr) {
        return getAttributes().contains(attr);
    }

    @Override
    public Attribute<?> getAttribute(String name) {
        for (Attribute<?> attr : getAttributes()) {
            if (attr.getName().equals(name)) {
                return attr;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This default implementation returns false.
     */
    @Override
    public boolean isReadOnly(Attribute<?> attr) {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This default implementation throws UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public void setReadOnly(Attribute<?> attr, boolean value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This default implementation returns true.
     */
    @Override
    public boolean isToSave(Attribute<?> attr) {
        return true;
    }

    /**
     * Subclasses should implement this method to support the clone operation,
     * copying their unique state into the destination instance.
     *
     * @param dest the destination instance
     */
    protected abstract void copyInto(AbstractAttributeSet dest);

}
