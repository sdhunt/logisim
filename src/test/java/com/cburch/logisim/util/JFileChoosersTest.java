/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractGraphicsTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Unit tests for {@link JFileChoosers}.
 */
public class JFileChoosersTest extends AbstractGraphicsTest {

    private static final String TEST_DIR_PATH = "src/test/resources/files/util";
    private static final String B_TXT = "b.txt";

    private ClosableTestFrame frame;
    private JFileChooser chooser;

    @Before
    public void setUp() {
        frame = new ClosableTestFrame(mkBox(400, 300, Color.CYAN));
        frame.displayMe();
    }

    @Ignore(DONT_RUN)
    @Test
    public void create() {
        title("create");

        chooser = JFileChoosers.create();
        int result = chooser.showOpenDialog(frame);
        print("open result is %d", result);

        result = chooser.showSaveDialog(frame);
        print("save result is %d", result);
    }

    @Ignore(DONT_RUN)
    @Test
    public void createWithDir() {
        title("create with dir");

        File dir = new File(TEST_DIR_PATH);
        print(dir);

        chooser = JFileChoosers.createAt(dir);
        int result = chooser.showOpenDialog(frame);
        print("open result is %d", result);
    }

    @Ignore(DONT_RUN)
    @Test
    public void createWithSelectedFile() {
        title("create with selected file");

        File dir = new File(TEST_DIR_PATH);
        File sel = new File(dir, B_TXT);
        print(sel);

        chooser = JFileChoosers.createSelected(sel);
        int result = chooser.showSaveDialog(frame);
        print("open result is %d", result);
    }

}
