/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.Test;

import static com.cburch.logisim.util.MacCompatibility.*;

/**
 * Unit tests for {@link MacCompatibility}.
 */
public class MacCompatabilityTest extends AbstractTest {

    @Test
    public void basic() {
        print("Automatically Present...");
        print("  about: %s", isAboutAutomaticallyPresent());
        print("  prefs: %s", isPreferencesAutomaticallyPresent());
        print("   quit: %s", isQuitAutomaticallyPresent());
        print("");
        print("Swing using screen menu bar: %s", isSwingUsingScreenMenuBar());
    }
}
