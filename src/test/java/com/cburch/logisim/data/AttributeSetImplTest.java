/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static com.cburch.logisim.data.AttributeSetImplTest.EvType.LIST_CHG;
import static com.cburch.logisim.data.AttributeSetImplTest.EvType.VALUE_CHG;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AttributeSetImpl}.
 */
public class AttributeSetImplTest extends AbstractTest {
    private static final String E_NOEX = "no exception thrown";
    private static final String FMT_CORRECT = "correct> %s";

    enum EvType {LIST_CHG, VALUE_CHG}

    private static class AttrListener implements AttributeListener {
        AttributeEvent lastEvent = null;
        EvType lastEvType;

        @Override
        public void attributeListChanged(AttributeEvent e) {
            lastEvent = e;
            lastEvType = LIST_CHG;
        }

        @Override
        public void attributeValueChanged(AttributeEvent e) {
            lastEvent = e;
            lastEvType = VALUE_CHG;
        }

        void reset() {
            lastEvent = null;
            lastEvType = null;
        }

        boolean heard(AttributeSet expSource, EvType expEvType) {
            assertThat(lastEvent.getSource(), is(equalTo(expSource)));
            assertThat(lastEvType, is(equalTo(expEvType)));
            return true;
        }

        boolean heard(AttributeSet expSource, EvType expEvType,
                      Attribute<?> expAttr, String expVal) {
            heard(expSource, expEvType);
            assertThat(lastEvent.getAttribute(), is(equalTo(expAttr)));
            assertThat(lastEvent.getValue(), is(equalTo(expVal)));
            return true;
        }
    }

    private static class TestAttribute extends Attribute<String> {
        TestAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public String parse(String value) {
            return value;
        }
    }

    private static final TestAttribute THING_ONE =
            new TestAttribute("t1", "Thing One");

    private static final TestAttribute THING_TWO =
            new TestAttribute("t2", "Thing Two");


    private AttrListener listener;
    private AttributeSetImpl aset;
    private List<Attribute<?>> attrs;
    private Iterator<Attribute<?>> iter;

    @Before
    public void setUp() {
        listener = new AttrListener();
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

        assertThat(aset.getAttribute("t1"), is(equalTo(THING_ONE)));
        assertThat(aset.getAttribute("t2"), is(equalTo(null)));

        assertThat(aset.isReadOnly(THING_ONE), is(false));
        assertThat(aset.isToSave(THING_ONE), is(true));

        assertThat(aset.getValue(THING_ONE), is(equalTo("foo")));

        aset.setValue(THING_ONE, "bar");
        assertThat(listener.heard(aset, VALUE_CHG, THING_ONE, "bar"), is(true));

        aset.removeAttribute(THING_ONE);
        assertThat(listener.heard(aset, LIST_CHG), is(true));
    }

}
