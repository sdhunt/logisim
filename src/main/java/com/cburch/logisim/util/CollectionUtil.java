/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom utilities for collections.
 */
public class CollectionUtil {

    // no instantiation from outside this class
    private CollectionUtil() {
    }

    /**
     * Creates a set union of the two specified sets.
     *
     * @param a   the first set
     * @param b   the second set
     * @param <E> the set element type
     * @return a set union of the two sets
     */
    public static <E> Set<E> createUnmodifiableSetUnion(Set<? extends E> a,
                                                        Set<? extends E> b) {
        if (a == null || b == null) {
            throw new NullPointerException("null is not permitted");
        }
        Set<E> combo = new HashSet<>();
        combo.addAll(a);
        combo.addAll(b);
        return Collections.unmodifiableSet(combo);
    }
}
