/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.cburch.logisim.util.LocaleString.getFromLocale;

/**
 * An abstract base class for a dialog with OK and Cancel buttons.
 */
public abstract class JDialogOk extends JDialog {

    private class MyListener extends WindowAdapter
            implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == ok) {
                okClicked();
                dispose();
            } else if (src == cancel) {
                cancelClicked();
                dispose();
            }
        }

        @Override
        public void windowClosing(WindowEvent e) {
            JDialogOk.this.removeWindowListener(this);
            cancelClicked();
            dispose();
        }
    }

    private JPanel contents = new JPanel(new BorderLayout());
    protected JButton ok = new JButton(getFromLocale("dlogOkButton"));
    protected JButton cancel = new JButton(getFromLocale("dlogCancelButton"));

    /**
     * Constructs a modal dialog for the specified parent dialog.
     *
     * @param parent the parent dialog
     * @param title  the dialog title
     */
    public JDialogOk(Dialog parent, String title) {
        super(parent, title, true);
        configure();
    }

    /**
     * Constructs a modal dialog for the specified parent frame.
     *
     * @param parent the parent frame
     * @param title  the dialog title
     */
    public JDialogOk(Frame parent, String title) {
        super(parent, title, true);
        configure();
    }

    private void configure() {
        MyListener listener = new MyListener();
        this.addWindowListener(listener);
        ok.addActionListener(listener);
        cancel.addActionListener(listener);

        Box buttons = Box.createHorizontalBox();
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(cancel);
        buttons.add(Box.createHorizontalGlue());

        Container pane = super.getContentPane();
        pane.add(contents, BorderLayout.CENTER);
        pane.add(buttons, BorderLayout.SOUTH);
    }

    @Override
    public Container getContentPane() {
        return contents;
    }

    /**
     * Invoked when the OK button is clicked.
     */
    public abstract void okClicked();

    /**
     * Invoked when the Cancel button is clicked, or when the window is closed.
     */
    public void cancelClicked() {
    }

}
