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
    private static final int LOG_MIN = 2;
    private static final int LOG_MAX = 12;
    private static final int LOG_DEFAULT = 8;

    private static final String E_LOGSIZE =
            "Invalid log size (" + LOG_MIN + ".." + LOG_MAX + "): ";

    private final int mask;
    private final Object[] data;

    /**
     * Constructs an empty cache of log-size {@value #LOG_DEFAULT}.
     * That is, space for 2^{@value #LOG_DEFAULT} elements.
     */
    public Cache() {
        this(LOG_DEFAULT);
    }

    /**
     * Constructs an empty cache of the specified log-size. That is,
     * space for 2^logSize elements. The valid ranges are from
     * {@value #LOG_MIN} to {@value #LOG_MAX}.
     *
     * @param logSize log size exponent
     */
    public Cache(int logSize) {
        if (logSize < LOG_MIN || logSize > LOG_MAX) {
            throw new IllegalArgumentException(E_LOGSIZE + logSize);
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
     * @param value    the object to cache
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cache{");
        sb.append("size=").append(mask + 1)
                .append(", entries=").append(nonEmptyCount())
                .append("}");
        return sb.toString();
    }

    // allow unit tests to assert size and occupancy of cache
    int size() {
        return mask + 1;
    }

    int nonEmptyCount() {
        int nonEmpty = 0;
        for (int i = 0; i <= mask; i++) {
            if (data[i] != null) {
                nonEmpty++;
            }
        }
        return nonEmpty;
    }
}
