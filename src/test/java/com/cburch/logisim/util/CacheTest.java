/*
 * Copyright (c) 2017, Simon Hunt et al.
 * License information is located in the com.cburch.logisim.Main source code.
 */
package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

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
            return "Thing<< " + val + " >>";
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
    private Object thing;
    private Object other;

    @Before
    public void setUp() {
        cache = new Cache();
    }

    @Test
    public void basic() {
        title("basic");

        // empty cache, nothing to get
        thing = cache.get(1);
        assertThat("nothing here", cache.get(1), is(equalTo(null)));
        assertThat("not empty", cache.nonEmptyCount(), is(0));

        // first caching returns the same object
        thing = new Thing(1);
        print(thing);
        other = cache.get(thing);
        assertThat(other, is(sameInstance(thing)));
        assertThat("bad count", cache.nonEmptyCount(), is(1));

        other = new Thing(1);
        assertThat(other, is(not(sameInstance(thing))));
        other = cache.get(other);
        assertThat(other, is(sameInstance(thing)));
        print(cache);
        assertThat("bad count", cache.nonEmptyCount(), is(1));
    }

    @Test
    public void placedAtSpecificHashCode_retreivedByHashCode() {
        title("placed at specific hash code - retrieved by hash code");

        thing = new Thing(3);
        cache.put(3, thing);

        other = cache.get(3);
        assertThat(other, is(sameInstance(thing)));
        print(cache);
    }

    @Test
    public void placedAtSpecificHashCode_retreivedByObject() {
        title("placed at specific hash code - retrieved by object");

        thing = new Thing(3);
        cache.put(3, thing);

        other = new Thing(3);
        assertThat(other, allOf(is(not(sameInstance(thing))),
                                is(equalTo(thing))));

        other = cache.get(other);
        assertThat(other, is(sameInstance(thing)));
        print(cache);
    }

    @Test
    public void getNullReturnsNull() {
        title("get null returns null");
        assertThat(cache.get(null), is(equalTo(null)));
    }

    @Test
    public void defaultCacheSizeIs256() {
        title("default cache size is 256");
        print(cache);
        assertThat(cache.size(), is(equalTo(256)));
    }

    @Test
    public void biggerCacheIs1024() {
        title("bigger cache size is 1024");
        cache = new Cache(10);
        print(cache);
        assertThat(cache.size(), is(equalTo(1024)));
    }

    @Test
    public void biggestCacheIs4096() {
        title("biggest cache size is 4096");
        cache = new Cache(12);
        print(cache);
        assertThat(cache.size(), is(equalTo(4096)));
    }

    @Test
    public void smallestCacheSizeIs4() {
        title("smallest cache size is 4");
        cache = new Cache(2);
        print(cache);
        assertThat(cache.size(), is(equalTo(4)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cacheSizeTooBig() {
        cache = new Cache(13);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cacheSizeTooSmall() {
        cache = new Cache(1);
    }
}
