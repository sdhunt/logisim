/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import static com.meowster.test.AbstractTest.StarWars.C3PO;
import static com.meowster.test.AbstractTest.StarWars.HAN;
import static com.meowster.test.AbstractTest.StarWars.LEIA;
import static com.meowster.test.AbstractTest.StarWars.LUKE;
import static com.meowster.test.AbstractTest.StarWars.R2D2;
import static com.meowster.test.AbstractTest.StarWars.VADER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link Dag}.
 */
public class DagTest extends AbstractTest {

    private Dag<StarWars> dag;

    @Before
    public void setUp() {
        dag = new Dag<>();
    }

    /*
     *   LUKE ----> LEIA --> HAN
     *    |           |
     *    +--> C3PO   +-- R2D2
     */
    private Dag<StarWars> createDagOne() {
        Dag<StarWars> d = new Dag<>();
        d.addEdge(LUKE, LEIA);
        d.addEdge(LEIA, HAN);
        d.addEdge(LUKE, C3PO);
        d.addEdge(LEIA, R2D2);
        return d;
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

    @Test
    public void hasPredecessors() {
        title("has predecessors");
        dag = createDagOne();
        print(dag);
        assertThat(dag.hasPredecessors(LUKE), is(false));
        assertThat(dag.hasPredecessors(LEIA), is(true));
        assertThat(dag.hasPredecessors(HAN), is(true));
        assertThat(dag.hasPredecessors(C3PO), is(true));
        assertThat(dag.hasPredecessors(R2D2), is(true));

        assertThat(dag.hasPredecessors(null), is(false));
    }

    @Test
    public void hasSuccessors() {
        title("has successors");
        dag = createDagOne();
        assertThat(dag.hasSuccessors(LUKE), is(true));
        assertThat(dag.hasSuccessors(LEIA), is(true));
        assertThat(dag.hasSuccessors(HAN), is(false));
        assertThat(dag.hasSuccessors(C3PO), is(false));
        assertThat(dag.hasSuccessors(R2D2), is(false));

        assertThat(dag.hasSuccessors(null), is(false));
    }

    @Test
    public void canFollow() {
        title("can follow");
        dag = createDagOne();
        assertThat(dag.canFollow(LUKE, LEIA), is(false));
        assertThat(dag.canFollow(LEIA, LUKE), is(true));

        assertThat(dag.canFollow(LUKE, R2D2), is(false));
        assertThat(dag.canFollow(R2D2, LUKE), is(true));

        assertThat(dag.canFollow(C3PO, HAN), is(true));
        assertThat(dag.canFollow(HAN, C3PO), is(true));
    }

    /*
     *   LUKE ----> LEIA --> HAN
     *    |           |
     *    +--> C3PO   +-- R2D2
     */

    // TODO: more comprehensive testing
}
