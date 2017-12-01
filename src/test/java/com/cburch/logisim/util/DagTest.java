/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import static com.meowster.test.AbstractTest.StarWars.LUKE;
import static com.meowster.test.AbstractTest.StarWars.R2D2;
import static com.meowster.test.AbstractTest.StarWars.VADER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link Dag}.
 */
public class DagTest extends AbstractTest {

    private Dag dag;

    @Before
    public void setUp() {
        dag = new Dag();
    }

    @Test
    public void basic() {
        title("basic");
        print(dag);
        assertThat(dag.hasSuccessors(LUKE), is(false));
        assertThat(dag.hasPredecessors(VADER), is(false));
        // neither luke, nor vader, are in the DAG...
        assertThat(dag.canFollow(LUKE, VADER), is(true));

        // add an edge from luke to vader
        assertThat(dag.addEdge(LUKE, VADER), is(true));
        print(dag);
        assertThat(dag.hasSuccessors(LUKE), is(true));
        assertThat(dag.hasPredecessors(VADER), is(true));

        // luke cannot follow vader, because vader now follows luke
        assertThat(dag.canFollow(LUKE, VADER), is(false));

        assertThat(dag.removeEdge(R2D2, LUKE), is(false));
        assertThat(dag.removeEdge(VADER, LUKE), is(false));

        assertThat(dag.removeEdge(LUKE, VADER), is(true));
        print(dag);
        assertThat(dag.hasSuccessors(LUKE), is(false));
        assertThat(dag.hasPredecessors(VADER), is(false));
    }
}
