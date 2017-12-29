/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import com.meowster.test.AbstractTest;

import static com.cburch.logisim.data.AbstractAttributeTest.EvType.LIST_CHG;
import static com.cburch.logisim.data.AbstractAttributeTest.EvType.VALUE_CHG;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Superclass for unit tests involving attributes.
 */
abstract class AbstractAttributeTest extends AbstractTest {

    protected enum EvType {LIST_CHG, VALUE_CHG}

    protected static class TestAttrListener implements AttributeListener {
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

        boolean heardNothing() {
            assertThat(lastEvent == null && lastEvType == null, is(true));
            return true;
        }
    }

    protected static class TestAttribute extends Attribute<String> {
        TestAttribute(String name, String disp) {
            super(name, disp);
        }

        @Override
        public String parse(String value) {
            return value;
        }
    }

    static final String T1 = "t1";
    static final String T2 = "t2";
    static final TestAttribute THING_ONE = new TestAttribute(T1, "Thing One");
    static final TestAttribute THING_TWO = new TestAttribute(T2, "Thing Two");

}
