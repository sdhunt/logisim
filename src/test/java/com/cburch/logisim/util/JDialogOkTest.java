/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractGraphicsTest;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Unit tests for {@link JDialogOk}.
 */
public class JDialogOkTest extends AbstractGraphicsTest {

    private class MyDialog extends JDialogOk {

        boolean responseOk = false;
        boolean responseCancel = false;

        MyDialog(Frame frame) {
            super(frame, "MyDialog");
        }

        private void configure() {
            Box b = mkBox(200, 150, Color.RED);
            b.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            getContentPane().add(b);
            pack();
        }

        @Override
        public void okClicked() {
            responseOk = true;
        }

        @Override
        public void cancelClicked() {
            responseCancel = true;
        }
    }

    ClosableTestFrame frame;
    MyDialog dialog;

    @Ignore(DONT_RUN)
    @Test
    public void basic() {
        title("basic");
        frame = new ClosableTestFrame(mkBox(500, 400, Color.black));
        frame.displayMe();

        dialog = new MyDialog(frame);
        dialog.configure();
        dialog.setVisible(true);

        if (dialog.responseOk) {
            print("OK clicked");
        } else if (dialog.responseCancel) {
            print("Cancel clicked");
        }
    }
}
