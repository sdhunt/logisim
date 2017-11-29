/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

/**
 * Allows immutable objects to be cached in memory in order to reduce
 * the creation of duplicate objects.
 */
public class Cache {
    private int mask;
    private Object[] data;

    /**
     * Constructs an empty cache of log-size 8. That is, space
     * for 2^8 elements.
     */
    public Cache() {
        this(8);
    }

    /**
     * Constructs an empty cache of the specified log-size. That is,
     * space for 2^logSize elements. If the given size is greater than
     * 12 it is clipped to 12.
     *
     * @param logSize log size exponent
     */
    public Cache(int logSize) {
        if (logSize > 12) {
            logSize = 12;
        }

        data = new Object[1 << logSize];
        mask = data.length - 1;
    }

    /**
     * Returns the object in the cache for the given hash code.
     *
     * @param hashCode the hash code
     * @return the associated object
     */
    public Object get(int hashCode) {
        return data[hashCode & mask];
    }

    /**
     * Puts an object into the cache for the given hash code.
     *
     * @param hashCode the hash code
     * @param value the object to cache
     */
    public void put(int hashCode, Object value) {
        if (value != null) {
            data[hashCode & mask] = value;
        }
    }

    /**
     * Looks to see if there is a cached copy of the given object; if there
     * is, returns the reference to the cached copy, otherwise caches the
     * given object and returns that reference.
     *
     * @param value the value to be cached / returned
     * @return the given value or a cached copy of that value
     */
    public Object get(Object value) {
        if (value == null) {
            return null;
        }

        int code = value.hashCode() & mask;
        Object ret = data[code];
        if (ret != null && ret.equals(value)) {
            return ret;
        }
        data[code] = value;
        return value;
    }
}
