/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 *
 */

package com.cburch.logisim.util;

import com.cburch.logisim.gui.generic.ZoomControl;
import com.cburch.logisim.gui.generic.ZoomModel;
import com.cburch.logisim.gui.menu.LogisimMenuBar;
import com.cburch.logisim.gui.menu.MenuSimulate;
import com.meowster.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;

import java.beans.PropertyChangeListener;

import static com.cburch.logisim.util.CustomAction.KEY_SIM_TICK;
import static com.cburch.logisim.util.CustomAction.KEY_ZOOM_IN;
import static com.cburch.logisim.util.CustomAction.KEY_ZOOM_OUT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CustomAction}.
 */
public class CustomActionTest extends AbstractTest {

    private class ZoomModelAdapter implements ZoomModel {

        @Override
        public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        }

        @Override
        public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        }

        @Override
        public boolean getShowGrid() {
            return false;
        }

        @Override
        public double getZoomFactor() {
            return 0;
        }

        @Override
        public double[] getZoomOptions() {
            return new double[0];
        }

        @Override
        public void setShowGrid(boolean value) {
        }

        @Override
        public void setZoomFactor(double value) {
        }
    }

    private class MockZoomControl extends ZoomControl {
        private boolean weZoomedIn = false;
        private boolean weZoomedOut = false;

        private MockZoomControl() {
            super(new ZoomModelAdapter());
        }

        @Override
        public void zoomIn() {
            weZoomedIn = true;
        }

        @Override
        public void zoomOut() {
            weZoomedOut = true;
        }
    }

    private class MockMenuBar extends LogisimMenuBar {
        MockMenuBar() {
            super(null, null);
        }
    }

    private class MockMenuSimulate extends MenuSimulate {
        private boolean weTicked = false;

        private MockMenuSimulate() {
            super(new MockMenuBar());
        }

        @Override
        public void tick() {
            weTicked = true;
        }
    }


    private class TestableCustomAction extends CustomAction {
        TestableCustomAction(String cmd, Object obj) {
            super(cmd, obj);
        }

        @Override
        boolean keyboardFocusedOnCanvas() {
            // fake the system into thinking a canvas has keyboard focus
            return true;
        }
    }

    private CustomAction action;

    /*
     * TODO: Investigate why these unit tests take so long to initialize...
     *       Presumably it is the setup of the ZoomControl / MenuSimulate
     */

    @Ignore("Slow to run ~ 1.476 secs")
    @Test
    public void zoomInAction() {
        title("zoom-in action");
        MockZoomControl mockZoomer = new MockZoomControl();
        action = new TestableCustomAction(KEY_ZOOM_IN, mockZoomer);

        assertThat(mockZoomer.weZoomedIn, is(false));
        action.actionPerformed(null);
        assertThat(mockZoomer.weZoomedIn, is(true));
    }

    @Ignore("Slow to run ~ 1.460 secs")
    @Test
    public void zoomOutAction() {
        title("zoom-out action");
        MockZoomControl mockZoomer = new MockZoomControl();
        action = new TestableCustomAction(KEY_ZOOM_OUT, mockZoomer);

        assertThat(mockZoomer.weZoomedOut, is(false));
        action.actionPerformed(null);
        assertThat(mockZoomer.weZoomedOut, is(true));
    }

    @Test
    public void simTickAction() {
        title("sim-tick action");
        MockMenuSimulate mockMenuSim = new MockMenuSimulate();
        action = new TestableCustomAction(KEY_SIM_TICK, mockMenuSim);

        assertThat(mockMenuSim.weTicked, is(false));
        action.actionPerformed(null);
        assertThat(mockMenuSim.weTicked, is(true));
    }

    @Test(expected = NullPointerException.class)
    public void nullCommand() {
        new CustomAction(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void nullObject() {
        new CustomAction(KEY_SIM_TICK, null);
    }
}
