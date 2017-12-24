/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Handles everything involving Logisim's version number.
 *
 * @author Carl Burch, Ryan Steinmetz
 */
public class LogisimVersion implements Comparable<LogisimVersion> {
    private static final int FINAL_REVISION = Integer.MAX_VALUE / 4;
    private static final String DOT = ".";
    private static final String EMPTY = "";

    private static final Logger logger = getLogger(LogisimVersion.class);

    /**
     * Returns a Logisim version omitting a revision number.
     *
     * @param major   the major number
     * @param minor   the minor number
     * @param release the release number
     * @return a Logisim version instance
     * @throws IllegalArgumentException if any value is negative
     */
    public static LogisimVersion get(int major, int minor, int release) {
        return get(major, minor, release, FINAL_REVISION);
    }

    /**
     * Returns a Logisim version with the specified revision number.
     *
     * @param major    the major number
     * @param minor    the minor number
     * @param release  the release number
     * @param revision the revision number
     * @return a Logisim version instance
     * @throws IllegalArgumentException if any value is negative
     */
    public static LogisimVersion get(int major, int minor, int release,
                                     int revision) {
        if (major < 0 || minor < 0 || release < 0 || revision < 0) {
            throw new IllegalArgumentException("Negatives prohibited");
        }
        return new LogisimVersion(major, minor, release, revision);
    }

    /**
     * Returns a Logisim version corresponding to the given string
     * representation.
     * <p>
     * Note that the expected form is one of:
     * <pre>
     *         "major"
     *         "major.minor"
     *         "major.minor.release"
     *         "major.minor.release.revision"
     *     </pre>
     *
     * @param versionString the string representation
     * @return a Logisim version instance
     */
    public static LogisimVersion parse(String versionString) {
        String[] parts = versionString.split("\\.");
        int major = 0;
        int minor = 0;
        int release = 0;
        int revision = FINAL_REVISION;
        try {
            if (parts.length >= 1) {
                major = Integer.parseInt(parts[0]);
            }

            if (parts.length >= 2) {
                minor = Integer.parseInt(parts[1]);
            }

            if (parts.length >= 3) {
                release = Integer.parseInt(parts[2]);
            }

            if (parts.length >= 4) {
                revision = Integer.parseInt(parts[3]);
            }

        } catch (NumberFormatException e) {
            logger.warn("Bad Version String '{}'", versionString);
            throw new IllegalArgumentException("Bad version string: " +
                                                       versionString);
        }

        return get(major, minor, release, revision);
    }

    private final int major;
    private final int minor;
    private final int release;
    private final int revision;
    private final String asString;

    /**
     * Creates a Logisim version.
     * Versions have the form: major.minor.release.revision
     *
     * @param major    the major number
     * @param minor    the minor number
     * @param release  the release number
     * @param revision the revision number
     */
    private LogisimVersion(int major, int minor, int release, int revision) {
        this.major = major;
        this.minor = minor;
        this.release = release;
        this.revision = revision;
        this.asString = createStringRep();
    }

    private String createStringRep() {
        String suffix = revision == FINAL_REVISION ? EMPTY : DOT + revision;
        return EMPTY + major + DOT + minor + DOT + release + suffix;
    }


    @Override
    public String toString() {
        return asString;
    }

    @Override
    public int compareTo(LogisimVersion other) {
        int result = this.major - other.major;
        if (result != 0) {
            return result;
        }

        result = this.minor - other.minor;
        if (result != 0) {
            return result;
        }

        result = this.release - other.release;
        if (result != 0) {
            return result;
        }

        return this.revision - other.revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogisimVersion that = (LogisimVersion) o;
        return major == that.major &&
                minor == that.minor &&
                release == that.release &&
                revision == that.revision;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + release;
        result = 31 * result + revision;
        return result;
    }
}
