/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static com.cburch.logisim.util.LocaleString.getFromLocale;
import static com.cburch.logisim.util.MacCompatibility.isQuitAutomaticallyPresent;
import static com.cburch.logisim.util.WindowMenuManager.getCurrentManager;
import static javax.swing.KeyStroke.getKeyStroke;

/**
 * Implements the window menu.
 */
public class WindowMenu extends JMenu {

    private class MyListener implements LocaleListener, ActionListener {
        @Override
        public void localeChanged() {
            WindowMenu.this.setText(getFromLocale("windowMenu"));
            minimize.setText(getFromLocale("windowMinimizeItem"));
            close.setText(getFromLocale("windowCloseItem"));
            zoom.setText(isQuitAutomaticallyPresent()
                                 ? getFromLocale("windowZoomItemMac")
                                 : getFromLocale("windowZoomItem"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == minimize) {
                doMinimize();

            } else if (src == zoom) {
                doZoom();

            } else if (src == close) {
                doClose();

            } else if (src instanceof WindowMenuItem) {
                WindowMenuItem choice = (WindowMenuItem) src;
                if (choice.isSelected()) {
                    WindowMenuItem item = findOwnerItem();
                    if (item != null) {
                        item.setSelected(true);
                    }
                    choice.actionPerformed(e);
                }
            }
        }

        private WindowMenuItem findOwnerItem() {
            for (WindowMenuItem item : persistentItems) {
                if (item.getJFrame() == owner) {
                    return item;
                }
            }
            for (WindowMenuItem item : transientItems) {
                if (item.getJFrame() == owner) {
                    return item;
                }
            }
            return null;
        }
    }

    private JFrame owner;
    private MyListener myListener = new MyListener();
    private JMenuItem minimize = new JMenuItem();
    private JMenuItem zoom = new JMenuItem();
    private JMenuItem close = new JMenuItem();
    private JRadioButtonMenuItem nullItem = new JRadioButtonMenuItem();
    private ArrayList<WindowMenuItem> persistentItems = new ArrayList<>();
    private ArrayList<WindowMenuItem> transientItems = new ArrayList<>();

    /**
     * Constructs the window menu component.
     *
     * @param owner the owning frame
     */
    public WindowMenu(JFrame owner) {
        this.owner = owner;
        WindowMenuManager.addMenu(this);

        int menuMask = getToolkit().getMenuShortcutKeyMask();
        minimize.setAccelerator(getKeyStroke(KeyEvent.VK_M, menuMask));
        close.setAccelerator(getKeyStroke(KeyEvent.VK_W, menuMask));

        if (owner == null) {
            minimize.setEnabled(false);
            zoom.setEnabled(false);
            close.setEnabled(false);
        } else {
            minimize.addActionListener(myListener);
            zoom.addActionListener(myListener);
            close.addActionListener(myListener);
        }

        computeEnabled();
        computeContents();

        LocaleManager.addLocaleListener(myListener);
        myListener.localeChanged();
    }

    void addMenuItem(Object source, WindowMenuItem item, boolean persistent) {
        if (persistent) {
            persistentItems.add(item);
        } else {
            transientItems.add(item);
        }

        item.addActionListener(myListener);
        computeContents();
    }

    void removeMenuItem(Object source, JRadioButtonMenuItem item) {
        if (transientItems.remove(item)) {
            item.removeActionListener(myListener);
        }
        computeContents();
    }

    void computeEnabled() {
        WindowMenuItemManager currentManager = getCurrentManager();
        minimize.setEnabled(currentManager != null);
        zoom.setEnabled(currentManager != null);
        close.setEnabled(currentManager != null);
    }

    void setNullItemSelected(boolean value) {
        nullItem.setSelected(value);
    }

    private void computeContents() {
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(nullItem);

        removeAll();
        add(minimize);
        add(zoom);
        add(close);

        addItemsToButtonGroup(bgroup, persistentItems);
        addItemsToButtonGroup(bgroup, transientItems);

        WindowMenuItemManager currentManager = getCurrentManager();
        if (currentManager != null) {
            JRadioButtonMenuItem item = currentManager.getMenuItem(this);
            if (item != null) {
                item.setSelected(true);
            }
        }
    }

    private void addItemsToButtonGroup(ButtonGroup bgroup,
                                       ArrayList<WindowMenuItem> items) {
        if (!items.isEmpty()) {
            addSeparator();
            for (JRadioButtonMenuItem item : items) {
                bgroup.add(item);
                add(item);
            }
        }
    }

    private void doMinimize() {
        if (owner != null) {
            owner.setExtendedState(Frame.ICONIFIED);
        }
    }

    private void doClose() {
        if (owner instanceof WindowClosable) {
            ((WindowClosable) owner).requestClose();
        } else if (owner != null) {
            int action = owner.getDefaultCloseOperation();
            if (action == JFrame.EXIT_ON_CLOSE) {
                System.exit(0);
            } else if (action == WindowConstants.HIDE_ON_CLOSE) {
                owner.setVisible(false);
            } else if (action == WindowConstants.DISPOSE_ON_CLOSE) {
                owner.dispose();
            }
        }
    }

    private void doZoom() {
        if (owner == null) {
            return;
        }

        owner.pack();
        Dimension screenSize = owner.getToolkit().getScreenSize();
        Dimension windowSize = owner.getPreferredSize();
        Point windowLoc = owner.getLocation();

        boolean locChanged = false;
        boolean sizeChanged = false;
        if (windowLoc.x + windowSize.width > screenSize.width) {
            windowLoc.x = Math.max(0, screenSize.width - windowSize.width);
            locChanged = true;
            if (windowLoc.x + windowSize.width > screenSize.width) {
                windowSize.width = screenSize.width - windowLoc.x;
                sizeChanged = true;
            }
        }
        if (windowLoc.y + windowSize.height > screenSize.height) {
            windowLoc.y = Math.max(0, screenSize.height - windowSize.height);
            locChanged = true;
            if (windowLoc.y + windowSize.height > screenSize.height) {
                windowSize.height = screenSize.height - windowLoc.y;
                sizeChanged = true;
            }
        }

        if (locChanged) {
            owner.setLocation(windowLoc);
        }

        if (sizeChanged) {
            owner.setSize(windowSize);
        }
    }
}
