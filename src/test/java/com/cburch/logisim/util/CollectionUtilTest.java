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


    private Set<StarWars> mkUnion(Set<StarWars> a, Set<StarWars> b) {
        return CollectionUtil.createUnmodifiableSetUnion(a, b);
    }

    @Test
    public void basic() {
        title("basic");
        union = mkUnion(DROIDS, HEROES);
        print(union);
        assertThat(union, is(not(equalTo(null))));
        assertThat(union.size(), is(5));
        assertThat(union, hasItems(LEIA, LUKE, C3PO, HAN, R2D2));
    }

    @Test(expected = NullPointerException.class)
    public void firstParamNull() {
        union = mkUnion(null, VILLAIN);
    }

    @Test(expected = NullPointerException.class)
    public void secondParamNull() {
        union = mkUnion(VILLAIN, null);
    }

    @Test
    public void emptySets() {
        title("empty sets");
        union = mkUnion(EMPTY, EMPTY);
        print(union);
        assertThat(union.size(), is(0));
        assertThat(union.iterator().hasNext(), is(false));
    }

    @Test
    public void duplicateSets() {
        title("duplicate sets");
        union = mkUnion(VILLAIN, VILLAIN);
        print(union);
        assertThat(union.size(), is(0));
        assertThat(union, hasItems(VADER));
    }

    @Test
    public void overlappingSets() {
        title("overlapping sets");
        union = mkUnion(DROIDS, HOLOGRAM);
        print("union of %s and %s ...", DROIDS, HOLOGRAM);
        print(union);
        assertThat(union, hasItems(LEIA, C3PO, R2D2));
        assertThat(union.size(), is(3));
    }

}
