/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.file;

import com.cburch.logisim.std.Builtin;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.util.JFileChoosers;
import com.cburch.logisim.util.MacCompatibility;
import com.cburch.logisim.util.ZipClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.cburch.logisim.util.LocaleString.getFromLocale;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * A class to encapsulate loading functionality
 * of files or library objects.
 */
public class Loader implements LibraryLoader {
    private static final Logger logger = LoggerFactory.getLogger(Loader.class);

    private static final String W_MAC_COMPATIBLE = "Could not set Mac " +
            "compatible file associative flags (file creator and type)";
    private static final String E_FILE_LOAD =
            "An error occurred attempting to load the file {}: {}";
    private static final String E_FILE_OPEN =
            "An error occurred attempting to open the file {}: {}";

    // localization keys
    private static final String LK_FILE_CIRCULAR_E = "fileCircularError";
    private static final String LK_JAR_FILTER = "jarFileFilter";
    private static final String LK_FILE_FILTER = "logisimFileFilter";
    private static final String LK_LOG_CIRC_E = "logisimCircularError";
    private static final String LK_LOAD_E = "logisimLoadError";
    private static final String LK_FILE_SAVE_E_TTL = "fileSaveErrorTitle";
    private static final String LK_FILE_SAVE_CLOSE_E = "fileSaveCloseError";
    private static final String LK_FILE_SAVE_ZERO_E = "fileSaveZeroError";
    private static final String LK_FILE_SAVE_E = "fileSaveError";
    private static final String LK_JAR_CLS_NOT_FOUND_E = "jarClassNotFoundError";
    private static final String LK_JAR_CLS_NOT_LIB_E = "jarClassNotLibraryError";
    private static final String LK_JAR_LIB_NOT_CREATED_E = "jarLibraryNotCreatedError";
    private static final String LK_FILE_ERR_TTL = "fileErrorTitle";
    private static final String LK_FILE_MSG_TTL = "fileMessageTitle";
    private static final String LK_FILE_LIB_MISS_E = "fileLibraryMissingError";
    private static final String LK_FILE_LIB_MISS_TTL = "fileLibraryMissingTitle";
    private static final String LK_FILE_LIB_MISS_BTN = "fileLibraryMissingButton";
    private static final String LK_FILE_LOAD_CANCELED_E = "fileLoadCanceledError";

    private static final String _BAK = ".bak";
    private static final String _JAR = ".jar";
    private static final String FILE_CREATOR = "LGSM";
    private static final String FILE_TYPE = "circ";


    /**
     * Designates the file extension of Logisim project files.
     */
    public static final String LOGISIM_EXTENSION = ".circ";

    /**
     * A file filter for Logisim project files.
     */
    public static final FileFilter LOGISIM_FILTER = new LogisimFileFilter();

    /**
     * A file filter for Jar files.
     */
    public static final FileFilter JAR_FILTER = new JarFileFilter();

    private static class LogisimFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(LOGISIM_EXTENSION);
        }

        @Override
        public String getDescription() {
            return getFromLocale(LK_FILE_FILTER);
        }
    }

    private static class JarFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(_JAR);
        }

        @Override
        public String getDescription() {
            return getFromLocale(LK_JAR_FILTER);
        }
    }

    // fixed
    private final Builtin builtin = new Builtin();
    private Component parent;

    // to be cleared with each new file
    private File mainFile = null;
    private Stack<File> filesOpening = new Stack<>();
    private Map<File, File> substitutions = new HashMap<>();

    /**
     * Creates a loader associated with the given component.
     *
     * @param parent the parent component
     */
    public Loader(Component parent) {
        this.parent = parent;
        clear();
    }

    /**
     * Returns the built-in functionality.
     *
     * @return the built-in functionality
     */
    public Builtin getBuiltin() {
        return builtin;
    }

    /**
     * Sets the parent component of this loader.
     *
     * @param parent the loader's new parent
     */
    public void setParent(Component parent) {
        this.parent = parent;
    }

    /**
     * Returns a substitution file for the given source file, if one has been
     * configured; otherwise just returns the given file.
     *
     * @param source the source file
     * @return the (possibly substituted) file
     */
    private File getSubstitution(File source) {
        File substitute = substitutions.get(source);
        return substitute == null ? source : substitute;
    }

    /**
     * Returns the main file.
     *
     * @return the main file
     */
    public File getMainFile() {
        return mainFile;
    }

    /**
     * Creates a file chooser opened at the current directory.
     *
     * @return a file chooser instance
     */
    public JFileChooser createChooser() {
        return JFileChoosers.createAt(getCurrentDirectory());
    }

    // used here and in LibraryManager only
    File getCurrentDirectory() {
        File ref;
        if (!filesOpening.empty()) {
            ref = filesOpening.peek();
        } else {
            ref = mainFile;
        }
        return ref == null ? null : ref.getParentFile();
    }

    private void setMainFile(File value) {
        mainFile = value;
    }

    /*
     * More substantive methods accessed from outside this package.
     */

    /**
     * Clears the loader state; files-opening stack is cleared and main-file
     * is set to null.
     */
    public void clear() {
        filesOpening.clear();
        mainFile = null;
    }

    /**
     * Opens a Logisim file. The supplied map of file substitutions is cached
     * in the loader, to be used for substitution lookups.
     *
     * @param file          the main file
     * @param substitutions the substitution files
     * @return a functioning LogisimFile
     * @throws LoadFailedException if there was an error loading the file
     */
    public LogisimFile openLogisimFile(File file, Map<File, File> substitutions)
            throws LoadFailedException {
        this.substitutions = substitutions;

        // FIXME allevaton: What's up with this? Try finally with no catch?
        try {
            return openLogisimFile(file);

        } finally {
            this.substitutions = Collections.emptyMap();
        }
    }

    /**
     * Opens a Logisim file from the hard drive.
     *
     * @param file the file to load
     * @return the loaded logisim file
     * @throws LoadFailedException if there was a problem loading the file
     */
    public LogisimFile openLogisimFile(File file) throws LoadFailedException {
        try {
            LogisimFile loadedFile = loadLogisimFile(file);
            if (loadedFile != null) {
                setMainFile(file);
            }

            showMessages(loadedFile);
            return loadedFile;

        } catch (LoaderException e) {
            logger.error(E_FILE_LOAD, file, e.getMessage());
            throw new LoadFailedException(e.getMessage(), e.isShown());
        }
    }

    /**
     * Opens a Logisim file from the given input stream.
     *
     * @param reader the input stream
     * @return the loaded logisim file
     * @throws LoadFailedException if it failed to load
     * @throws IOException         if another error occurred
     */
    public LogisimFile openLogisimFile(InputStream reader)
            throws LoadFailedException, IOException {
        LogisimFile loadedFile;
        try {
            loadedFile = LogisimFile.load(reader, this);

        } catch (LoaderException e) {
            logger.error(E_FILE_LOAD, "{from input stream}", e.getMessage());
            // TODO: should we be throwing a LoadFailedException here??
            return null;
        }
        showMessages(loadedFile);
        return loadedFile;
    }

    /**
     * Loads the specified Logisim library.
     *
     * @param file the library file
     * @return the library object
     */
    public Library loadLogisimLibrary(File file) {
        File actual = getSubstitution(file);
        LoadedLibrary loadedLib =
                LibraryManager.instance.loadLogisimLibrary(this, actual);

        if (loadedLib != null) {
            LogisimFile libBase = (LogisimFile) loadedLib.getBase();
            showMessages(libBase);
        }
        return loadedLib;
    }

    /**
     * Loads a library from a jar file.
     *
     * @param jarFile   the jar file
     * @param className the class in the jar
     * @return the library object
     */
    public Library loadJarLibrary(File jarFile, String className) {
        File actual = getSubstitution(jarFile);
        return LibraryManager.instance.loadJarLibrary(this, actual, className);
    }

    /**
     * Reloads the specified library.
     *
     * @param lib the library to reload
     */
    public void reload(LoadedLibrary lib) {
        LibraryManager.instance.reload(this, lib);
    }


    /**
     * Saves the current file to the give destination file.
     *
     * @param file the source Logisim file
     * @param dest the destination file
     * @return true if the save was successful; false otherwise
     */
    // TODO: refactor this method to break up into smaller pieces.
    public boolean save(LogisimFile file, File dest) {
        Library libRef = LibraryManager.instance.findReference(file, dest);
        if (libRef != null) {
            showMessageDialog(parent,
                              getFromLocale(LK_FILE_CIRCULAR_E, libRef.getDisplayName()),
                              getFromLocale(LK_FILE_SAVE_E_TTL),
                              JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File backup = determineBackupName(dest);
        boolean backupCreated = backup != null && dest.renameTo(backup);

        FileOutputStream fwrite;
        try {
            try {
                MacCompatibility.setFileCreatorAndType(dest, FILE_CREATOR, FILE_TYPE);
            } catch (IOException e) {
                logger.warn(W_MAC_COMPATIBLE);
            }
            fwrite = new FileOutputStream(dest);
            file.write(fwrite, this);
            file.setName(toProjectName(dest));

            File oldFile = getMainFile();
            setMainFile(dest);
            LibraryManager.instance.fileSaved(this, dest, oldFile, file);

        } catch (IOException e) {
            if (backupCreated) {
                logger.warn("Backup found, recovering from it.");
                recoverBackup(backup, dest);
            }

            if (dest.exists() && dest.length() == 0) {
                dest.delete();
            }

            showMessageDialog(parent,
                              getFromLocale(LK_FILE_SAVE_E, e.toString()),
                              getFromLocale(LK_FILE_SAVE_E_TTL),
                              JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // FIXME: fwrite != null is always true (??)
        if (fwrite != null) {
            try {
                fwrite.close();

            } catch (IOException e) {
                if (backupCreated) {
                    logger.info("Backup found, recovering from it.");
                    recoverBackup(backup, dest);
                }

                if (dest.exists() && dest.length() == 0) {
                    dest.delete();
                }

                showMessageDialog(parent,
                                  getFromLocale(LK_FILE_SAVE_CLOSE_E, e.toString()),
                                  getFromLocale(LK_FILE_SAVE_E_TTL),
                                  JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (!dest.exists() || dest.length() == 0) {
            if (backupCreated && backup != null && backup.exists()) {
                recoverBackup(backup, dest);

            } else {
                dest.delete();
            }

            showMessageDialog(parent,
                              getFromLocale(LK_FILE_SAVE_ZERO_E),
                              getFromLocale(LK_FILE_SAVE_E_TTL),
                              JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (backupCreated && backup.exists()) {
            backup.delete();
        }

        return true;
    }


    /**
     * Returns a name for a backup file, using the given base.
     *
     * @param base the base file
     * @return the backup file
     */
    private static File determineBackupName(File base) {
        File dir = base.getParentFile();
        String name = base.getName();

        if (name.endsWith(LOGISIM_EXTENSION)) {
            name = name.substring(0, name.length() - LOGISIM_EXTENSION.length());
        }
        // TODO: this seems brittle (not to mention magic number 20)... review
        for (int i = 1; i <= 20; i++) {
            String ext = i == 1 ? _BAK : (_BAK + i);
            File candidate = new File(dir, name + ext);
            if (!candidate.exists()) {
                return candidate;
            }
        }
        return null;
    }


    /**
     * Attempts to recover from a backup file.
     *
     * @param backup the backup file to recover from
     * @param dest   where does the backup file go
     */
    private static void recoverBackup(File backup, File dest) {
        // TODO: should check return values of delete() and renameTo() ops!
        if (backup != null && backup.exists()) {
            if (dest.exists()) {
                dest.delete();
            }
            backup.renameTo(dest);
        }
    }

    /**
     * Loads a Logisim file.
     *
     * @param request the requested file
     * @return the Logisim file
     */
    LogisimFile loadLogisimFile(File request) throws LoadFailedException {
        File actual = getSubstitution(request);
        String projectName = toProjectName(actual);

        for (File fileOpening : filesOpening) {
            if (fileOpening.equals(actual)) {
                logger.error(E_FILE_OPEN, getFromLocale(LK_LOG_CIRC_E), projectName);
                throw new LoadFailedException(getFromLocale(LK_LOG_CIRC_E, projectName));
            }
        }

        LogisimFile loadedFile;
        filesOpening.push(actual);
        try {
            loadedFile = LogisimFile.load(actual, this);

        } catch (IOException e) {
            logger.error(E_FILE_OPEN, getFromLocale(LK_LOG_CIRC_E), projectName);
            throw new LoadFailedException(getFromLocale(LK_LOAD_E, projectName, e.toString()));

        } finally {
            filesOpening.pop();
        }
        if (loadedFile != null) {
            loadedFile.setName(projectName);
        }
        return loadedFile;
    }

    /**
     * Loads a jar file as a library.
     *
     * @param request the requested jar file
     * @param className  the class name specified in the file
     * @return the loaded library
     * @throws LoadFailedException if there was an issue loading the library
     */
    Library loadJarFile(File request, String className)
            throws LoadFailedException {

        File actual = getSubstitution(request);
        // Up until 2.1.8, this was written to use a URLClassLoader, which
        // worked pretty well, except that the class never releases its file
        // handles. For this reason, with 2.2.0, it's been switched to use
        // a custom-written class ZipClassLoader instead. The ZipClassLoader
        // is based on something downloaded off a forum, and I'm not as sure
        // that it works as well. It certainly does more file accesses.

        // Anyway, here's the line for this new version:
        ZipClassLoader loader = new ZipClassLoader(actual);

        // And here's the code that was present up until 2.1.8, and which I
        // know to work well except for the closing-files bit. If necessary, we
        // can revert by deleting the above declaration and reinstating the below.
        /*
        URL url;
        try {
            url = new URL("file", "localhost", file.getCanonicalPath());
        } catch (MalformedURLException e1) {
            throw new LoadFailedException("Internal error: Malformed URL");
        } catch (IOException e1) {
            throw new LoadFailedException(Strings.get("jarNotOpenedError"));
        }
        URLClassLoader loader = new URLClassLoader(new URL[] { url });
        */

        // load library class from loader
        Class<?> libClass;
        try {
            libClass = loader.loadClass(className);

        } catch (ClassNotFoundException e) {
            logger.error("Class not found: {}", className);
            throw new LoadFailedException(getFromLocale(LK_JAR_CLS_NOT_FOUND_E, className));
        }
        if (!(Library.class.isAssignableFrom(libClass))) {
            logger.error("Class not library: {}", libClass);
            throw new LoadFailedException(getFromLocale(LK_JAR_CLS_NOT_LIB_E, className));
        }

        // instantiate library
        Library lib;
        try {
            lib = (Library) libClass.newInstance();

        } catch (Exception e) {
            logger.error("Class not library: {}", libClass);
            throw new LoadFailedException(getFromLocale(LK_JAR_LIB_NOT_CREATED_E, className));
        }
        return lib;
    }


    //
    // Library methods
    //

    /**
     * Loads a Logisim library designated by the specified descriptor.
     *
     * @param desc the library file descriptor
     * @return the library
     */
    @Override
    public Library loadLibrary(String desc) {
        return LibraryManager.instance.loadLibrary(this, desc);
    }

    /**
     * Returns the descriptor from the specified library.
     *
     * @param lib the library
     * @return the descriptor
     */
    @Override
    public String getDescriptor(Library lib) {
        return LibraryManager.instance.getDescriptor(this, lib);
    }

    /**
     * Displays an error description to the user, in addition to logging it.
     *
     * @param description the error text
     */
    @Override
    public void showError(String description) {
        logger.error(description);

        // TODO: refactor with helper methods, and remove magic numbers.

        if (!filesOpening.empty()) {
            File top = filesOpening.peek();
            String init = toProjectName(top) + ":";
            if (description.contains("\n")) {
                description = init + "\n" + description;
            } else {
                description = init + " " + description;
            }
        }

        if (description.contains("\n") || description.length() > 60) {
            int lines = 1;
            for (int pos = description.indexOf('\n'); pos >= 0;
                 pos = description.indexOf('\n', pos + 1)) {
                lines++;
            }
            lines = Math.max(4, Math.min(lines, 7));

            JTextArea textArea = new JTextArea(lines, 60);
            textArea.setEditable(false);
            textArea.setText(description);
            textArea.setCaretPosition(0);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 150));
            showMessageDialog(parent, scrollPane,
                              getFromLocale(LK_FILE_ERR_TTL),
                              JOptionPane.ERROR_MESSAGE);

        } else {
            showMessageDialog(parent, description,
                              getFromLocale(LK_FILE_ERR_TTL),
                              JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the messages from a logisim file to the user in a dialog pane.
     *
     * @param source the file to load from
     */
    private void showMessages(LogisimFile source) {
        if (source == null) {
            return;
        }

        String message = source.getMessage();
        while (message != null) {
            showMessageDialog(parent, message,
                              getFromLocale(LK_FILE_MSG_TTL),
                              JOptionPane.INFORMATION_MESSAGE);
            message = source.getMessage();
        }
    }

    /**
     * Returns the file for the given name.
     *
     * @param name   file name
     * @param filter the file name filter
     * @return the file object
     */
    File getFileFor(String name, FileFilter filter) {
        // Determine the actual file name.
        File file = new File(name);
        if (!file.isAbsolute()) {
            File currentDirectory = getCurrentDirectory();
            if (currentDirectory != null) {
                file = new File(currentDirectory, name);
            }
        }

        while (!file.canRead()) {
            // It doesn't exist. Ask the user to supply it.
            showMessageDialog(parent, getFromLocale(LK_FILE_LIB_MISS_E, file.getName()));
            JFileChooser chooser = createChooser();
            chooser.setFileFilter(filter);
            chooser.setDialogTitle(getFromLocale(LK_FILE_LIB_MISS_TTL, file.getName()));

            int action = chooser.showDialog(parent, getFromLocale(LK_FILE_LIB_MISS_BTN));
            if (action != JFileChooser.APPROVE_OPTION) {
                throw new LoaderException(getFromLocale(LK_FILE_LOAD_CANCELED_E));
            }
            file = chooser.getSelectedFile();
        }
        return file;
    }

    /**
     * Returns the project name form of the specified file.
     * In other words, strips of the ".circ" suffix if present.
     *
     * @param file the file
     * @return the project name
     */
    private String toProjectName(File file) {
        String result = file.getName();
        if (result.endsWith(LOGISIM_EXTENSION)) {
            return result.substring(0, result.length() - LOGISIM_EXTENSION.length());
        }
        return result;
    }
}
