/*
 * Copyright (c) 2012, Carl Burch.
 *
 * This file is part of the Logisim source code. The latest
 * version is available at http://www.cburch.com/logisim/.
 *
 * Logisim is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Logisim is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Logisim.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cburch.logisim;

import com.cburch.logisim.gui.start.Startup;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Main entry point into Logisim.
 *
 * @author Carl Burch
 * @version 2.7.2
 */
public class Logisim {
    private static final int MAJOR = 2;
    private static final int MINOR = 7;
    private static final int RELEASE = 3;

    // while we are cleaning up the code...
    private static final int REVISION = 101;

    /**
     * Current version.
     */
    public static final LogisimVersion VERSION =
            LogisimVersion.get(MAJOR, MINOR, RELEASE, REVISION);

    /**
     * Current version as a string.
     */
    public static final String VERSION_NAME = VERSION.toString();

    private static final Logger logger = getLogger(Logisim.class);

    /**
     * Main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Startup startup = Startup.parseArgs(args);
        if (startup == null) {
            System.exit(0);
        }

        logger.info("Starting Logisim ({})", VERSION);
        startup.run();
    }
}
