/*
 * Copyright (c) 2017, Simon Hunt et al.
 * License information is located in the com.cburch.logisim.Main source code.
 */
package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link Cache}.
 */
public class CacheTest extends AbstractTest {

    private class Thing {
        int val;

        Thing(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "<< " + val + " >>";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Thing thing = (Thing) o;
            return val == thing.val;
        }

        @Override
        public int hashCode() {
            return val;
        }
    }

    private Cache cache;
    private Object value;
    private Object other;

    @Test
    public void basic() {
        title("basic");
        cache = new Cache();
        value = cache.get(0);
        assertEquals(null, value);

        value = new Thing(1);
        print(value);
        other = cache.get(value);
        assertSame(value, other);
    }

    // TODO: write a more complete set of tests.

}
