/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import org.apache.commons.collections15.iterators.IteratorChain;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Custom utilities for collections.
 */
public class CollectionUtil {

    // WARNING: This implementation actually violates Set semantics...
    //          since there can be duplicate items in the "set"
    //          (as evidenced by unit tests).

    private static class UnionSet<E> extends AbstractSet<E> {
        private Set<? extends E> a;
        private Set<? extends E> b;

        UnionSet(Set<? extends E> a, Set<? extends E> b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int size() {
            return a.size() + b.size();
        }

        @Override
        public Iterator<E> iterator() {
            return new IteratorChain<>(a.iterator(), b.iterator());
        }
    }

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
    // TODO: review - despite the method name, the returned "set" is mutable!
    //  since references to the given sets are retained by the caller.

    // To ensure immutability, the UnionSet constructor should make copies
    // of sets a and b.

    // Also, technically, the returned class does not represent set-union;
    // it represents a pair of sets (which may have repeated elements in them).
    public static <E> Set<E> createUnmodifiableSetUnion(Set<? extends E> a,
                                                        Set<? extends E> b) {
        if (a == null || b == null) {
            throw new NullPointerException("null is not permitted");
        }
        return new UnionSet<>(a, b);
    }
}
