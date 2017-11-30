/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */
package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.cburch.logisim.util.CollectionUtilTest.StarWars.C3PO;
import static com.cburch.logisim.util.CollectionUtilTest.StarWars.HAN;
import static com.cburch.logisim.util.CollectionUtilTest.StarWars.LEIA;
import static com.cburch.logisim.util.CollectionUtilTest.StarWars.LUKE;
import static com.cburch.logisim.util.CollectionUtilTest.StarWars.R2D2;
import static com.cburch.logisim.util.CollectionUtilTest.StarWars.VADER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CollectionUtil}.
 */
public class CollectionUtilTest extends AbstractTest {

    enum StarWars {LUKE, LEIA, HAN, C3PO, R2D2, VADER}

    private static final Set<StarWars> HEROES = new HashSet<>(
            Arrays.asList(LUKE, LEIA, HAN)
    );

    private static final Set<StarWars> DROIDS = new HashSet<>(
            Arrays.asList(C3PO, R2D2)
    );

    private static final Set<StarWars> VILLAIN = new HashSet<>(
            Collections.singletonList(VADER)
    );

    private static final Set<StarWars> HOLOGRAM = new HashSet<>(
            Arrays.asList(LEIA, R2D2)
    );

    private static final Set<StarWars> EMPTY = Collections.emptySet();

    private Set<StarWars> union;


    private void mkUnion(Set<StarWars> a, Set<StarWars> b) {
        union = CollectionUtil.createUnmodifiableSetUnion(a, b);
        print(union);
    }

    @Test
    public void basic() {
        title("basic");
        mkUnion(DROIDS, HEROES);
        assertThat(union, is(not(equalTo(null))));
        assertThat(union, hasItems(LEIA, LUKE, C3PO, HAN, R2D2));
        assertThat(union.size(), is(5));
    }

    @Test(expected = NullPointerException.class)
    public void firstParamNull() {
        mkUnion(null, VILLAIN);
    }

    @Test(expected = NullPointerException.class)
    public void secondParamNull() {
        mkUnion(VILLAIN, null);
    }

    @Test
    public void emptySets() {
        title("empty sets");
        mkUnion(EMPTY, EMPTY);
        assertThat(union.iterator().hasNext(), is(false));
        assertThat(union.size(), is(0));
    }

    @Test
    public void duplicateSets() {
        title("duplicate sets");
        mkUnion(VILLAIN, VILLAIN);
        assertThat(union, hasItems(VADER));
        assertThat(union.size(), is(1));
    }

    @Test
    public void overlappingSets() {
        title("overlapping sets");
        mkUnion(DROIDS, HOLOGRAM);
        print("... should be union of %s and %s", DROIDS, HOLOGRAM);
        assertThat(union, hasItems(LEIA, C3PO, R2D2));
        assertThat(union.size(), is(3));
    }

}
