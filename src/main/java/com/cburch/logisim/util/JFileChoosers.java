/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import com.cburch.logisim.prefs.AppPreferences;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * A (hopefully) more robust implementation of a file chooser.
 */
/*
 * A user reported that JFileChooser's constructor sometimes resulted in
 * IOExceptions when Logisim is installed under a system administrator
 * account and then is attempted to run as a regular user. This class is
 * an attempt to be a bit more robust about which directory the JFileChooser
 * opens up under. (23 Feb 2010)
 */
public class JFileChoosers {

    private static final String EMPTY = "";

    private static final String[] PROP_NAMES = {
            null,
            "user.home",
            "user.dir",
            "java.home",
            "java.io.tmpdir",
    };

    /**
     * Custom file chooser implementation to cache current directory when a
     * file is selected.
     */
    private static class LogisimFileChooser extends JFileChooser {
        LogisimFileChooser() {
            super();
        }

        LogisimFileChooser(File initSelected) {
            super(initSelected);
        }

        @Override
        public File getSelectedFile() {
            File dir = getCurrentDirectory();
            if (dir != null) {
                JFileChoosers.currentDirectory = dir.toString();
            }
            return super.getSelectedFile();
        }
    }

    // cache the current directory
    private static String currentDirectory = EMPTY;

    // non-instantiable
    private JFileChoosers() {
    }

    /**
     * Returns the current directory, as cached by this class.
     *
     * @return the current directory
     */
    public static String getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Creates a file chooser, opening at the first accessible directory from
     * a small set of possibilities.
     *
     * @return a file chooser instance
     */
    public static JFileChooser create() {
        RuntimeException first = null;

        for (String prop : PROP_NAMES) {
            try {
                String dirname;

                if (prop == null) {
                    dirname = currentDirectory;
                    if (EMPTY.equals(dirname)) {
                        dirname = AppPreferences.DIALOG_DIRECTORY.get();
                    }
                } else {
                    dirname = System.getProperty(prop);
                }

                if (EMPTY.equals(dirname)) {
                    return new LogisimFileChooser();
                }

                File dir = new File(dirname);
                if (dir.canRead()) {
                    return new LogisimFileChooser(dir);
                }

            } catch (RuntimeException t) {
                if (first == null) {
                    first = t;
                }

                Throwable u = t.getCause();
                if (!(u instanceof IOException)) {
                    throw t;
                }
            }
        }
        throw first;
    }

    /**
     * Creates a file chooser, opening at the specified directory.
     *
     * @param openDirectory the directory to open at
     * @return a file chooser instance
     */
    public static JFileChooser createAt(File openDirectory) {
        if (openDirectory == null) {
            return create();
        }

        try {
            return new LogisimFileChooser(openDirectory);

        } catch (RuntimeException t) {
            if (t.getCause() instanceof IOException) {
                try {
                    return create();

                } catch (RuntimeException ignored) {
                }
            }
            throw t;
        }
    }

    /**
     * Creates a file chooser, opening at the directory containing the
     * specified file, with the specified file pre-selected.
     *
     * @param selected the selected file
     * @return a file chooser instance
     */
    public static JFileChooser createSelected(File selected) {
        if (selected == null) {
            return create();
        }

        JFileChooser chooser = createAt(selected.getParentFile());
        chooser.setSelectedFile(selected);
        return chooser;
    }
}
