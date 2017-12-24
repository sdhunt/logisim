/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LogisimVersion}.
 */
public class LogisimVersionTest extends AbstractTest {

    private LogisimVersion ver;

    @Test
    public void versionOne() {
        title("version one");
        ver = LogisimVersion.get(1, 0, 0);
        print(ver);
        assertThat(ver.toString(), is(equalTo("1.0.0")));
    }

    @Test
    public void versionEuler() {
        title("version euler");
        ver = LogisimVersion.get(2, 7, 18);
        print(ver);
        assertThat(ver.toString(), is(equalTo("2.7.18")));
    }

    @Test
    public void versionWithRevision() {
        title("version with revision");
        ver = LogisimVersion.get(1, 2, 3, 500);
        print(ver);
        assertThat(ver.toString(), is(equalTo("1.2.3.500")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void versionNoNegativeMajor() {
        LogisimVersion.get(-1, 2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void versionNoNegativeMinor() {
        LogisimVersion.get(1, -2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void versionNoNegativeRelease() {
        LogisimVersion.get(1, 2, -3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void versionNoNegativeRevision() {
        LogisimVersion.get(1, 2, 3, -4);
    }

    @Test
    public void badStringParse() {
        title("bad string parse");
        try {
            LogisimVersion.parse("1.2.foo");
            fail("no exception thrown");
        } catch (IllegalArgumentException e) {
            print("correct> %s", e.getMessage());
            assertThat(e.getMessage().endsWith("1.2.foo"), is(true));
        }
    }

    @Test
    public void parseMajor() {
        title("parse major");
        ver = LogisimVersion.parse("4");
        print(ver);
        assertThat(ver.toString(), is(equalTo("4.0.0")));
    }

    @Test
    public void parseMajorMinor() {
        title("parse major.minor");
        ver = LogisimVersion.parse("4.3");
        print(ver);
        assertThat(ver.toString(), is(equalTo("4.3.0")));
    }

    @Test
    public void parseMajorMinorRelease() {
        title("parse major.minor.release");
        ver = LogisimVersion.parse("4.3.11");
        print(ver);
        assertThat(ver.toString(), is(equalTo("4.3.11")));
    }

    @Test
    public void parseMajorMinorReleaseRevision() {
        title("parse major.minor.release.revision");
        ver = LogisimVersion.parse("4.3.11.2001");
        print(ver);
        assertThat(ver.toString(), is(equalTo("4.3.11.2001")));
    }

    private static final String[] UNSORTED = {
            "1.0.5.92",
            "2.0.0",
            "1.0.5",
            "2.0.0.7",
            "3.2.1.4",
            "1.0.0",
            "2.3.0.99",
            "3.2.1",
            "2.3.0.1",
            "1.4.0",
    };

    /*
     * Note the idiosyncracy that versions with Revision numbers occur BEFORE
     * those without, in the naturally sorted order...
     *
     *    x.y.z.revision
     *    x.y.z
     *
     * This is because the (hidden) FINAL_REVISION placeholder is MAX_INT / 4.
     */
    private static final String[] SORTED = {
            "1.0.0",
            "1.0.5.92",
            "1.0.5",
            "1.4.0",
            "2.0.0.7",
            "2.0.0",
            "2.3.0.1",
            "2.3.0.99",
            "3.2.1.4",
            "3.2.1",
    };

    private List<LogisimVersion> createList(String[] source) {
        List<LogisimVersion> result = new ArrayList<>();
        for (String s : source) {
            result.add(LogisimVersion.parse(s));
        }
        return result;
    }

    @Test
    public void comparable() {
        title("comparable");
        List<LogisimVersion> versions = createList(UNSORTED);
        List<LogisimVersion> sorted = createList(SORTED);
        assertThat(versions, is(not(equalTo(sorted))));

        Collections.sort(versions);
        assertThat(versions, is(equalTo(sorted)));
    }

    @Test
    public void notEqual() {
        title("not equal");
        LogisimVersion v100 = LogisimVersion.get(1, 0, 0);
        LogisimVersion v103 = LogisimVersion.get(1, 0, 3);

        assertThat(v100.equals(v103), is(false));
        assertThat(v103.equals(v100), is(false));
        assertThat(v100.hashCode(), is(not(equalTo(v103.hashCode()))));
    }

    @Test
    public void equal() {
        title("equal");
        LogisimVersion vA = LogisimVersion.get(4, 0, 4);
        LogisimVersion vB = LogisimVersion.get(4, 0, 4);

        assertThat(vA, is(not(theInstance(vB))));
        assertThat(vA.equals(vB), is(true));
        assertThat(vB.equals(vA), is(true));
        assertThat(vA.hashCode(), is(equalTo(vB.hashCode())));
    }
}
