/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import net.roydesign.mac.MRJAdapter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Helper class to determine if we are dealing with Mac OS idiosyncrasies.
 */
public final class MacCompatibility {

    // non-instantiable
    private MacCompatibility() {
    }

    public static final double mrjVersion;

    static {
        double versionValue;
        try {
            versionValue = MRJAdapter.mrjVersion;
        } catch (Exception t) {
            versionValue = 0.0;
        }
        mrjVersion = versionValue;
    }

    /**
     * Returns true if the 'about' menu item is provided for us by
     * virtue of being run on a Mac OS platform.
     *
     * @return true if about is present automatically; false otherwise
     */
    public static boolean isAboutAutomaticallyPresent() {
        try {
            return MRJAdapter.isAboutAutomaticallyPresent();
        } catch (Exception t) {
            return false;
        }
    }

    /**
     * Returns true if the 'preferences' menu item is provided for us by
     * virtue of being run on a Mac OS platform.
     *
     * @return true if preferences is present automatically; false otherwise
     */
    public static boolean isPreferencesAutomaticallyPresent() {
        try {
            return MRJAdapter.isPreferencesAutomaticallyPresent();
        } catch (Exception t) {
            return false;
        }
    }

    /**
     * Returns true if the 'quit' menu item is provided for us by
     * virtue of being run on a Mac OS platform.
     *
     * @return true if quit is present automatically; false otherwise
     */
    public static boolean isQuitAutomaticallyPresent() {
        try {
            return MRJAdapter.isQuitAutomaticallyPresent();
        } catch (Exception t) {
            return false;
        }
    }

    /**
     * Returns true if Swing is using the Mac menu bar by
     * virtue of being run on a Mac OS platform.
     *
     * @return true if swing is using the Mac menu bar; false otherwise
     */
    public static boolean isSwingUsingScreenMenuBar() {
        try {
            return MRJAdapter.isSwingUsingScreenMenuBar();
        } catch (Exception t) {
            return false;
        }
    }

    /**
     * Sets a frameless menu bar.
     *
     * @param menubar the menu bar to set
     */
    public static void setFramelessJMenuBar(JMenuBar menubar) {
        try {
            MRJAdapter.setFramelessJMenuBar(menubar);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets the file creator and file type.
     *
     * @param dest the destination file
     * @param app the application
     * @param type the file type
     * @throws IOException if there was an I/O issue
     */
    public static void setFileCreatorAndType(File dest, String app, String type)
            throws IOException {
        IOException ioExcept = null;
        try {
            try {
                MRJAdapter.setFileCreatorAndType(dest, app, type);
            } catch (IOException e) {
                ioExcept = e;
            }
        } catch (Exception ignored) {
        }
        if (ioExcept != null) {
            throw ioExcept;
        }
    }
}
