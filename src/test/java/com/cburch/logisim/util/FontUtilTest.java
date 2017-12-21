/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FontUtil}.
 */
public class FontUtilTest extends AbstractTest {

    @Test
    public void standardPlain() {
        title("standard: plain");
        assertThat(FontUtil.toStyleStandardString(Font.PLAIN),
                   is(equalTo("plain")));
    }

    @Test
    public void standardItalic() {
        title("standard: italic");
        assertThat(FontUtil.toStyleStandardString(Font.ITALIC),
                   is(equalTo("italic")));
    }

    @Test
    public void standardBold() {
        title("standard: bold");
        assertThat(FontUtil.toStyleStandardString(Font.BOLD),
                   is(equalTo("bold")));
    }

    @Test
    public void standardBoldItalic() {
        title("standard: bold-italic");
        assertThat(FontUtil.toStyleStandardString(Font.BOLD | Font.ITALIC),
                   is(equalTo("bolditalic")));
    }

    @Test
    public void standardunknown() {
        title("standard: unknown");
        assertThat(FontUtil.toStyleStandardString(456),
                   is(equalTo("??")));
    }

    @Test
    public void displayPlain() {
        title("display: plain");
        assertThat(FontUtil.toStyleDisplayString(Font.PLAIN),
                   is(equalTo("Plain")));
    }

    @Test
    public void displayItalic() {
        title("display: italic");
        assertThat(FontUtil.toStyleDisplayString(Font.ITALIC),
                   is(equalTo("Italic")));
    }

    @Test
    public void displayBold() {
        title("display: bold");
        assertThat(FontUtil.toStyleDisplayString(Font.BOLD),
                   is(equalTo("Bold")));
    }

    @Test
    public void displayBoldItalic() {
        title("display: bold-italic");
        assertThat(FontUtil.toStyleDisplayString(Font.BOLD | Font.ITALIC),
                   is(equalTo("Bold Italic")));
    }

    @Test
    public void displayunknown() {
        title("display: unknown");
        assertThat(FontUtil.toStyleDisplayString(456),
                   is(equalTo("??")));
    }

    // TODO: figure out how to conditionally run tests for other locales
    //       that are available on the developer's system
}
