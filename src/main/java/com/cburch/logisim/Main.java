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
import org.slf4j.LoggerFactory;

/**
 * Main entry point into Logisim.
 *
 * @author Carl Burch
 * @version 2.7.2
 */
// TODO: consider renaming to Logisim
public class Main {
    /**
     * Current version.
     */
    public static final LogisimVersion VERSION = LogisimVersion.get(2, 7, 2);

    /**
     * Current version as a string.
     */
    public static final String VERSION_NAME = VERSION.toString();

    /**
     * Copyright year.
     */
    // TODO: not used. consider removing.
    public static final int COPYRIGHT_YEAR = 2012;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

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

        logger.info("Starting Logisim");
        startup.run();
    }
}
