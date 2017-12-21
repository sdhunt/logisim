/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Provides support for managing a collection of property change listeners
 * using weak references, for property changes on a given source object.
 */
public class PropertyChangeWeakSupport {
    private static final String ALL_PROPERTIES = "ALL PROPERTIES";

    /**
     * Encapsulates a property and its listener (as a weak reference).
     */
    private static class ListenerData {
        private final String property;
        private final WeakReference<PropertyChangeListener> listener;

        ListenerData(String property, PropertyChangeListener listener) {
            this.property = property;
            this.listener = new WeakReference<>(listener);
        }
    }

    private Object source;
    private ConcurrentLinkedQueue<ListenerData> listeners;

    /**
     * Creates an (initially empty) collection of weak listeners for property
     * changes occuring on the specified source object.
     *
     * @param source the source object
     */
    public PropertyChangeWeakSupport(Object source) {
        this.source = source;
        this.listeners = new ConcurrentLinkedQueue<>();
    }

    /**
     * Adds the specified property change listener to this collection, to be
     * notified of changes to all properties on the source object.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(ALL_PROPERTIES, listener);
    }

    /**
     * Adds the specified property change listener to this collection, to be
     * notified of changes to the specified property on the source object.
     *
     * @param property the property to listen for
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(String property,
                                          PropertyChangeListener listener) {
        listeners.add(new ListenerData(property, listener));
    }

    /**
     * Removes the specified property change listener from this collection.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        removePropertyChangeListener(ALL_PROPERTIES, listener);
    }

    /**
     * Removes the specified property change listener (listening for changes
     * to the given property) from this collection.
     *
     * @param property the property listened for
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(String property,
                                             PropertyChangeListener listener) {
        for (Iterator<ListenerData> it = listeners.iterator(); it.hasNext(); ) {
            ListenerData data = it.next();
            PropertyChangeListener pcl = data.listener.get();
            if (pcl == null) {
                it.remove();
            } else if (data.property.equals(property) && pcl == listener) {
                it.remove();
            }
        }
    }

    private boolean caresAbout(ListenerData data, String property) {
        return data.property.equals(ALL_PROPERTIES) ||
                data.property.equals(property);
    }

    private PropertyChangeEvent mkEvent(String prop, Object oVal, Object nVal) {
        return new PropertyChangeEvent(source, prop, oVal, nVal);
    }

    /**
     * Fires a property change, notifying all registered listeners of the
     * change.
     *
     * @param property the property that changed
     * @param oldValue the property's old value
     * @param newValue the property's new value
     */
    public void firePropertyChange(String property, Object oldValue,
                                   Object newValue) {
        PropertyChangeEvent e = null;

        for (Iterator<ListenerData> it = listeners.iterator(); it.hasNext(); ) {
            ListenerData data = it.next();
            PropertyChangeListener pcl = data.listener.get();
            if (pcl == null) {
                it.remove();
            } else if (caresAbout(data, property)) {
                if (e == null) {
                    e = mkEvent(property, oldValue, newValue);
                }
                pcl.propertyChange(e);
            }
        }
    }

    /**
     * Fires an integer property change, notifying all registered listeners
     * of the change.
     *
     * @param property the property that changed
     * @param oldValue the property's old integer value
     * @param newValue the property's new integer value
     */
    public void firePropertyChange(String property, int oldValue, int newValue) {
        firePropertyChange(property, (Object) oldValue, (Object) newValue);
    }

    /**
     * Fires a boolean property change, notifying all registered listeners
     * of the change.
     *
     * @param property the property that changed
     * @param oldValue the property's old boolean value
     * @param newValue the property's new boolean value
     */
    public void firePropertyChange(String property, boolean oldValue,
                                   boolean newValue) {
        firePropertyChange(property, (Object) oldValue, (Object) newValue);
    }
}
