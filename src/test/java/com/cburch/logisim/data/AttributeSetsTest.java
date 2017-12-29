/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.data;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.cburch.logisim.data.AbstractAttributeTest.EvType.VALUE_CHG;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AttributeSets}.
 */
public class AttributeSetsTest extends AbstractAttributeTest {

    private static final String FOO = "foo";
    private static final String BAR = "bar";
    private static final String GOO = "goo";
    private static final String MOO = "moo";
    private static final String BOO = "boo";
    private static final String ZOO = "zoo";

    private static final Attribute<?>[] ZERO_ATTR = {};
    private static final String[] ZERO_VALUE = {};

    private static final Attribute<?>[] SINGLE_ATTR = {THING_ONE};
    private static final String[] SINGLE_VALUE = {FOO};

    private static final Attribute<String> ATTR3 = new TestAttribute("t3", "Attr-03");
    private static final Attribute<String> ATTR4 = new TestAttribute("t4", "Attr-04");
    private static final Attribute<String> ATTR5 = new TestAttribute("t5", "Attr-05");
    private static final Attribute<String> ATTR6 = new TestAttribute("t6", "Attr-06");
    private static final Attribute<String> ATTR7 = new TestAttribute("t7", "Attr-07");
    private static final Attribute<String> ATTR8 = new TestAttribute("t8", "Attr-08");

    private static final Attribute<?>[] MULTI_ATTR = {
            THING_ONE, THING_TWO, ATTR3, ATTR4, ATTR5, ATTR6, ATTR7, ATTR8
    };
    private static final String[] MULTI_VALUE = {
            FOO, BAR, GOO, MOO, BOO, ZOO, ZOO, ZOO
    };

    private static final Attribute<?>[] TOO_MANY_ATTRS = {
            THING_ONE, THING_TWO, ATTR3, ATTR4, ATTR5, ATTR6, ATTR7, ATTR8,
            THING_ONE, THING_TWO, ATTR3, ATTR4, ATTR5, ATTR6, ATTR7, ATTR8,
            THING_ONE, THING_TWO, ATTR3, ATTR4, ATTR5, ATTR6, ATTR7, ATTR8,
            THING_ONE, THING_TWO, ATTR3, ATTR4, ATTR5, ATTR6, ATTR7, ATTR8,
            THING_ONE
    };

    private TestAttrListener listener;
    private AttributeSet aset;
    private AttributeSet copy;
    private List<Attribute<?>> attrs;

    @Before
    public void setUp() {
        listener = new TestAttrListener();
    }

    @Test
    public void emptyAttributeSet() {
        title("empty attribute set");
        aset = AttributeSets.EMPTY;
        attrs = aset.getAttributes();
        assertThat(attrs.isEmpty(), is(true));
        assertThat(aset.containsAttribute(THING_ONE), is(false));
        assertThat(aset.getAttribute(T1), is(equalTo(null)));
        assertThat(aset.isReadOnly(null), is(true));
        assertThat(aset.isToSave(null), is(true));
        assertThat(aset.getValue(THING_ONE), is(equalTo(null)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptyAttrSetNoSetReadOnly() {
        title("empty attribute set -- no set read-only");
        aset = AttributeSets.EMPTY;
        aset.setReadOnly(THING_ONE, true);
    }

    @Test
    public void singleAttribute() {
        title("single attribute");
        aset = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        aset.addAttributeListener(listener);

        attrs = aset.getAttributes();
        assertThat(attrs.isEmpty(), is(false));
        assertThat(aset.containsAttribute(THING_ONE), is(true));
        assertThat(aset.getAttribute(T1), is(equalTo(THING_ONE)));
        assertThat(aset.isReadOnly(THING_ONE), is(false));
        assertThat(aset.isToSave(THING_ONE), is(true));
        assertThat(aset.getValue(THING_ONE), is(equalTo(FOO)));
        assertThat(listener.heardNothing(), is(true));

        aset.setValue(THING_ONE, GOO);
        assertThat(listener.heard(aset, VALUE_CHG, THING_ONE, GOO), is(true));
        assertThat(aset.getValue(THING_ONE), is(equalTo(GOO)));
        listener.reset();

        copy = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        assertThat(copy.getValue(THING_ONE), is(equalTo(FOO)));
        AttributeSets.copy(aset, copy);
        assertThat(listener.heardNothing(), is(true));
        assertThat(copy.getValue(THING_ONE), is(equalTo(GOO)));
    }

    @Test
    public void nullCopy() {
        title("null copy");
        copy = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        AttributeSets.copy(null, copy);
        assertThat(copy.getValue(THING_ONE), is(equalTo(FOO)));
    }

    @Test
    public void setValueNonExistent() {
        title("set value; non-existent");
        aset = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        try {
            aset.setValue(THING_TWO, GOO);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void setReadOnlyNonexistent() {
        title("set read-only; non-existent");
        aset = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        try {
            aset.setReadOnly(THING_TWO, true);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void setReadOnly() {
        title("set read-only");
        aset = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        assertThat(aset.getValue(THING_ONE), is(equalTo(FOO)));

        aset.setValue(THING_ONE, BAR);
        assertThat(aset.getValue(THING_ONE), is(equalTo(BAR)));

        aset.setReadOnly(THING_ONE, true);
        try {
            aset.setValue(THING_ONE, GOO);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void cloneMe() {
        title("cloneMe");
        aset = AttributeSets.fixedSet(SINGLE_ATTR, SINGLE_VALUE);
        aset.setValue(THING_ONE, BAR);
        assertThat(aset.getValue(THING_ONE), is(equalTo(BAR)));

        copy = (AttributeSet) aset.clone();
        assertThat(copy, is(not(sameInstance(aset))));
        assertThat(copy.getValue(THING_ONE), is(equalTo(BAR)));
    }

    @Test
    public void multiAttr() {
        title("multi-attr");
        aset = AttributeSets.fixedSet(MULTI_ATTR, MULTI_VALUE);
        aset.addAttributeListener(listener);
        attrs = aset.getAttributes();
        assertThat(attrs.isEmpty(), is(false));
        assertThat(attrs.size(), is(equalTo(MULTI_ATTR.length)));

        assertThat(aset.containsAttribute(ATTR7), is(true));
        assertThat(aset.getAttribute("t5"), is(equalTo(ATTR5)));
        assertThat(aset.isReadOnly(THING_ONE), is(false));
        assertThat(aset.isToSave(THING_ONE), is(true));
        assertThat(aset.getValue(ATTR5), is(equalTo(BOO)));
        assertThat(listener.heardNothing(), is(true));

        aset.setValue(ATTR8, BAR);
        assertThat(listener.heard(aset, VALUE_CHG, ATTR8, BAR), is(true));

        copy = (AttributeSet) aset.clone();
        assertThat(copy.getValue(ATTR5), is(equalTo(BOO)));
        assertThat(copy.getValue(ATTR8), is(equalTo(BAR)));

        aset.setValue(ATTR8, ZOO);
        assertThat(listener.heard(aset, VALUE_CHG, ATTR8, ZOO), is(true));
        assertThat(copy.getValue(ATTR8), is(equalTo(BAR)));
    }

    @Test
    public void lengthMismatchOneToMany() {
        title("length mismatch; 1-many");
        try {
            aset = AttributeSets.fixedSet(SINGLE_ATTR, MULTI_VALUE);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void lengthMismatchManyToOne() {
        title("length mismatch; many-1");
        try {
            aset = AttributeSets.fixedSet(MULTI_ATTR, SINGLE_VALUE);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void tooManyAttributes() {
        title("too many attributes");
        try {
            aset = AttributeSets.fixedSet(TOO_MANY_ATTRS, MULTI_VALUE);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }

    @Test
    public void noAttributes() {
        title("no attributes");
        aset = AttributeSets.fixedSet(ZERO_ATTR, ZERO_VALUE);
        assertThat(aset, is(sameInstance(AttributeSets.EMPTY)));
    }

    @Test
    public void multiReadOnly() {
        title("multi-readonly");
        aset = AttributeSets.fixedSet(MULTI_ATTR, MULTI_VALUE);
        aset.setReadOnly(ATTR6, true);
        aset.setReadOnly(ATTR7, true);
        aset.setReadOnly(ATTR8, true);


        assertThat(aset.getValue(ATTR4), is(equalTo(MOO)));
        aset.setValue(ATTR4, BAR);
        assertThat(aset.getValue(ATTR4), is(equalTo(BAR)));

        assertThat(aset.getValue(ATTR6), is(equalTo(ZOO)));
        assertThat(aset.getValue(ATTR7), is(equalTo(ZOO)));
        assertThat(aset.getValue(ATTR8), is(equalTo(ZOO)));
        checkReadOnly(ATTR6);
        checkReadOnly(ATTR7);
        checkReadOnly(ATTR8);

        aset.setReadOnly(ATTR8, false);
        aset.setValue(ATTR8, BAR);
        assertThat(aset.getValue(ATTR8), is(equalTo(BAR)));
    }

    private void checkReadOnly(Attribute<String> attr) {
        try {
            aset.setValue(attr, BAR);
            fail(E_NOEX);
        } catch (IllegalArgumentException e) {
            print(FMT_CORRECT, e);
        }
    }
}
