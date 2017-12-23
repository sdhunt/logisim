/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

/**
 * Abstract base class for managing window menu items.
 */
public abstract class WindowMenuItemManager {

    private class MyListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent event) {
        }

        @Override
        public void windowClosing(WindowEvent event) {
            JFrame frame = getJFrame(false);
            if (frame.getDefaultCloseOperation() == HIDE_ON_CLOSE) {
                removeFromManager();
            }
        }

        @Override
        public void windowClosed(WindowEvent event) {
            removeFromManager();
        }

        @Override
        public void windowDeiconified(WindowEvent event) {
        }

        @Override
        public void windowIconified(WindowEvent event) {
            addToManager();
            WindowMenuManager.setCurrentManager(WindowMenuItemManager.this);
        }

        @Override
        public void windowActivated(WindowEvent event) {
            addToManager();
            WindowMenuManager.setCurrentManager(WindowMenuItemManager.this);
        }

        @Override
        public void windowDeactivated(WindowEvent event) {
            WindowMenuManager.unsetCurrentManager(WindowMenuItemManager.this);
        }
    }

    private MyListener myListener = new MyListener();
    private String text;
    private boolean persistent;
    private boolean listenerAdded = false;
    private boolean inManager = false;
    private Map<WindowMenu, JRadioButtonMenuItem> menuItems = new HashMap<>();

    /**
     * Constructs a new window menu item manager with the given text (name) and
     * an indication of whether the item is persistent or transient.
     *
     * @param text       the text
     * @param persistent true for persistent; false for transient
     */
    public WindowMenuItemManager(String text, boolean persistent) {
        this.text = text;
        this.persistent = persistent;
        if (persistent) {
            WindowMenuManager.addManager(this);
        }
    }

    /**
     * Returns the associated JFrame, creating it if necessary (if the create
     * boolean is true).
     *
     * @param create if true, create the frame if necessary
     * @return the frame
     */
    public abstract JFrame getJFrame(boolean create);

    /**
     * Callback invoked when the specified frame opens.
     *
     * @param frame the frame that opened
     */
    public void frameOpened(JFrame frame) {
        if (!listenerAdded) {
            frame.addWindowListener(myListener);
            listenerAdded = true;
        }
        addToManager();
        WindowMenuManager.setCurrentManager(this);
    }

    /**
     * Callback invoked when the specified frame closes.
     *
     * @param frame the frame that closed
     */
    public void frameClosed(JFrame frame) {
        if (!persistent) {
            if (listenerAdded) {
                frame.removeWindowListener(myListener);
                listenerAdded = false;
            }
            removeFromManager();
        }
    }

    private void addToManager() {
        if (!persistent && !inManager) {
            WindowMenuManager.addManager(this);
            inManager = true;
        }
    }

    private void removeFromManager() {
        if (!persistent && inManager) {
            inManager = false;
            for (WindowMenu menu : WindowMenuManager.getMenus()) {
                JRadioButtonMenuItem menuItem = menuItems.get(menu);
                menu.removeMenuItem(this, menuItem);
            }
            WindowMenuManager.removeManager(this);
        }
    }

    /**
     * Returns the text associated with this menu item.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text on this menu item.
     *
     * @param value
     */
    public void setText(String value) {
        text = value;
        for (JRadioButtonMenuItem menuItem : menuItems.values()) {
            menuItem.setText(text);
        }
    }

    JRadioButtonMenuItem getMenuItem(WindowMenu key) {
        return menuItems.get(key);
    }

    void createMenuItem(WindowMenu menu) {
        WindowMenuItem ret = new WindowMenuItem(this);
        menuItems.put(menu, ret);
        menu.addMenuItem(this, ret, persistent);
    }

    void removeMenuItem(WindowMenu menu) {
        JRadioButtonMenuItem item = menuItems.remove(menu);
        if (item != null) {
            menu.removeMenuItem(this, item);
        }

    }

    void setSelected(boolean selected) {
        for (JRadioButtonMenuItem item : menuItems.values()) {
            item.setSelected(selected);
        }
    }
}
