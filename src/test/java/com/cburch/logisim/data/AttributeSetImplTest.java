/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.cburch.logisim.data.AbstractAttributeTest.EvType.LIST_CHG;
import static com.cburch.logisim.data.AbstractAttributeTest.EvType.VALUE_CHG;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AttributeSetImpl}.
 */
public class AttributeSetImplTest extends AbstractAttributeTest {

    private TestAttrListener listener;
    private AttributeSetImpl aset;
    private List<Attribute<?>> attrs;

    @Before
    public void setUp() {
        listener = new TestAttrListener();
        aset = new AttributeSetImpl();
        aset.addAttributeListener(listener);
    }

    @Test
    public void anEmptyAttributeSet() {
        title("an empty attribute set");
        print(aset);
        attrs = aset.getAttributes();

        try {
            Attribute<?> a = attrs.get(0);
            fail(E_NOEX);
        } catch (IndexOutOfBoundsException e) {
            print(FMT_CORRECT, e);
        }

        assertThat(attrs.contains(THING_ONE), is(false));
        assertThat(attrs.indexOf(THING_ONE), is(-1));
        assertThat(attrs.size(), is(0));
        assertThat(attrs.isEmpty(), is(true));
        assertThat(attrs.iterator().hasNext(), is(false));

        try {
            aset.isReadOnly(THING_ONE);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }

        try {
            aset.setReadOnly(THING_ONE, true);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }

        try {
            aset.getValue(THING_ONE);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }

        try {
            aset.setValue(THING_ONE, "foo");
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void thingOne() {
        title("Thing ONE");
        aset.addAttribute(THING_ONE, "foo");
        assertThat(listener.heard(aset, LIST_CHG), is(true));

        attrs = aset.getAttributes();
        assertThat(attrs.contains(THING_ONE), is(true));
        assertThat(attrs.indexOf(THING_ONE), is(0));
        assertThat(attrs.size(), is(1));
        assertThat(attrs.isEmpty(), is(false));
        assertThat(attrs.iterator().hasNext(), is(true));

        assertThat(aset.containsAttribute(THING_ONE), is(true));
        assertThat(aset.containsAttribute(THING_TWO), is(false));

        assertThat(aset.getAttribute(T1), is(equalTo(THING_ONE)));
        assertThat(aset.getAttribute(T2), is(equalTo(null)));

        assertThat(aset.isReadOnly(THING_ONE), is(false));
        assertThat(aset.isToSave(THING_ONE), is(true));

        assertThat(aset.getValue(THING_ONE), is(equalTo("foo")));

        aset.setValue(THING_ONE, "bar");
        assertThat(listener.heard(aset, VALUE_CHG, THING_ONE, "bar"), is(true));

        aset.removeAttribute(THING_ONE);
        assertThat(listener.heard(aset, LIST_CHG), is(true));
    }

}
