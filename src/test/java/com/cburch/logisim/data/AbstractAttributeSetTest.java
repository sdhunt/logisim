/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AbstractAttributeSet}.
 */
public class AbstractAttributeSetTest extends AbstractTest {

    private class TestAttrSet extends AbstractAttributeSet {

        @Override
        protected void copyInto(AbstractAttributeSet dest) {

        }

        @Override
        public List<Attribute<?>> getAttributes() {
            return null;
        }

        @Override
        public <V> V getValue(Attribute<V> attr) {
            return null;
        }

        @Override
        public <V> void setValue(Attribute<V> attr, V value) {

        }
    }


    private TestAttrSet attrSet;

    @Before
    public void setUp() {
        attrSet = new TestAttrSet();
    }

    @Test
    public void basic() {
        title("basic");

        assertThat(attrSet.isReadOnly(null), is(false));

        try {
            attrSet.setReadOnly(null, true);
            fail("no exception thrown");
        } catch (UnsupportedOperationException e) {
            print("correct> %s", e);
        }

        assertThat(attrSet.isToSave(null), is(true));
    }
}
