/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EventSourceWeakSupport}.
 */
public class EventSourceWeakSupportTest extends AbstractTest {

    private class Foo {
        private final char id;

        Foo(char id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Foo{" + id + "}";
        }
    }

    private EventSourceWeakSupport<Foo> listeners;
    private Iterator<Foo> iter;

    @Before
    public void setUp() {
        listeners = new EventSourceWeakSupport<>();
    }


    @Test
    public void basic() {
        title("basic");
        assertThat(listeners.isEmpty(), is(true));
        iter = listeners.iterator();
        assertThat(iter.hasNext(), is(false));
    }

    @Test
    public void singleListener() {
        title("single listener");
        Foo a = new Foo('a');

        listeners.add(a);
        assertThat(listeners.isEmpty(), is(false));
        iter = listeners.iterator();
        assertThat(iter.hasNext(), is(true));
        Foo x = iter.next();
        assertThat(iter.hasNext(), is(false));
        assertThat(x, is(equalTo(a)));

        listeners.remove(a);
        assertThat(listeners.isEmpty(), is(true));
    }

    private void checkContainsExactly(EventSourceWeakSupport<Foo> listeners,
                                      Foo... foos) {
        print("checking: %s", Arrays.toString(foos));
        List<Foo> collect = new ArrayList<>();

        iter = listeners.iterator();
        while (iter.hasNext()) {
            collect.add(iter.next());
        }
        print(collect);

        assertThat(collect.size(), is(equalTo(foos.length)));

        for (Foo f : foos) {
            assertThat(collect.contains(f), is(true));
        }
    }

    @Test
    public void oneRemoved() {
        title("one removed");
        Foo a = new Foo('a');
        Foo b = new Foo('b');
        Foo c = new Foo('c');

        listeners.add(a);
        listeners.add(b);
        listeners.add(c);
        assertThat(listeners.isEmpty(), is(false));

        checkContainsExactly(listeners, a, b, c);

        // now remove the reference to b
        listeners.remove(b);

        checkContainsExactly(listeners, a, c);
    }

    /*
     * NOTE: it is pretty much impossible to reliably test the automatic
     *       removal of weak references, as it requires the GC to do its
     *       thing, over which the programmer has no direct control.
     *
     *        -- System.gc() guarantees "best effort" only --
     */
}
