/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link PropertyChangeWeakSupport}.
 */
public class PropertyChangeWeakSupportTest extends AbstractTest {

    private static class TestListener implements PropertyChangeListener {
        List<PropertyChangeEvent> events = new ArrayList<>();

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            events.add(evt);
        }

        private int eventCount() {
            return events.size();
        }

        private boolean heardNothing() {
            return events.isEmpty();
        }

        private boolean heard(String prop, Object newVal) {
            for (PropertyChangeEvent e : events) {
                if (e.getPropertyName().equals(prop) &&
                        e.getNewValue().equals(newVal)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final String FOO = "foo";
    private static final String BAR = "bar";
    private static final String ZOO = "zoo";

    private static final String XYZZY = "xyzzy";
    private static final String FROBOZZ = "frobozz";

    private PropertyChangeWeakSupport support;
    private TestListener alpha;
    private TestListener beta;
    private TestListener gamma;

    @Before
    public void setUp() {
        support = new PropertyChangeWeakSupport(new Object());
        alpha = new TestListener();
        beta = new TestListener();
        gamma = new TestListener();
        support.addPropertyChangeListener(FOO, alpha);
        support.addPropertyChangeListener(BAR, beta);
        support.addPropertyChangeListener(gamma);
    }

    private void checkEventCounts(int expAlpha, int expBeta, int expGamma) {
        assertThat(alpha.eventCount(), is(equalTo(expAlpha)));
        assertThat(beta.eventCount(), is(equalTo(expBeta)));
        assertThat(gamma.eventCount(), is(equalTo(expGamma)));
    }

    @Test
    public void stringProp() {
        title("string prop");
        support.firePropertyChange(FOO, XYZZY, FROBOZZ);
        assertThat(alpha.heard(FOO, FROBOZZ), is(true));
        assertThat(beta.heardNothing(), is(true));
        assertThat(gamma.heard(FOO, FROBOZZ), is(true));
        checkEventCounts(1, 0, 1);
    }

    @Test
    public void integerProp() {
        title("integer prop");
        support.firePropertyChange(BAR, 0, 42);
        assertThat(alpha.heardNothing(), is(true));
        assertThat(beta.heard(BAR, 42), is(true));
        assertThat(gamma.heard(BAR, 42), is(true));
        checkEventCounts(0, 1, 1);
    }


    @Test
    public void booleanProp() {
        title("boolean prop");
        support.firePropertyChange(ZOO, false, true);
        assertThat(alpha.heardNothing(), is(true));
        assertThat(beta.heardNothing(), is(true));
        assertThat(gamma.heard(ZOO, true), is(true));
        checkEventCounts(0, 0, 1);
    }

    @Test
    public void enumProp() {
        title("enum prop");
        support.firePropertyChange(FOO, StarWars.LUKE, StarWars.VADER);
        assertThat(alpha.heard(FOO, StarWars.VADER), is(true));
        assertThat(beta.heardNothing(), is(true));
        assertThat(gamma.heard(FOO, StarWars.VADER), is(true));
        checkEventCounts(1, 0, 1);

        // but if alpha stops listening for foo events...
        support.removePropertyChangeListener(FOO, alpha);
        support.firePropertyChange(FOO, StarWars.HAN, StarWars.LEIA);
        assertThat(alpha.heard(FOO, StarWars.LEIA), is(false));
        assertThat(beta.heardNothing(), is(true));
        assertThat(gamma.heard(FOO, StarWars.LEIA), is(true));
        checkEventCounts(1, 0, 2);
    }

}
