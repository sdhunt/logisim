/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import com.cburch.logisim.gui.generic.ZoomControl;
import com.cburch.logisim.gui.menu.MenuSimulate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager;

/**
 * A custom action.
 * Note that this implementation explicitly handles keyboard events for
 * the main canvas:
 * <ul>
 * <li>zoom-in</li>
 * <li>zoom-out</li>
 * <li>simulation-tick</li>
 * </ul>
 */
// TODO: Consider renaming this class to CanvasKeyboardAction
//       ... and placing it in the package containing Canvas
public class CustomAction extends AbstractAction {
    private static final Class<?> CANVAS_CLASS =
            com.cburch.logisim.gui.main.Canvas.class;

    private static final String E_NULL_DISALLOWED =
            "command / object must not be null";

    /**
     * Designates key for zoom-in action (ctrl/cmd-plus).
     */
    public static final String KEY_ZOOM_IN = "CTRL+";

    /**
     * Designates key for zoom-out action (ctrl/cmd-minus).
     */
    public static final String KEY_ZOOM_OUT = "CTRL-";

    /**
     * Designates key for simulation-tick action (space bar).
     */
    public static final String KEY_SIM_TICK = "Space";


    private final String cmd;
    private final Object obj;

    /**
     * Creates a custom action.
     *
     * @param cmd the command associated with this action
     * @param obj the object upon which the action is performed
     * @throws NullPointerException if either parameter is null
     */
    public CustomAction(String cmd, Object obj) {
        if (cmd == null || obj == null) {
            throw new NullPointerException(E_NULL_DISALLOWED);
        }
        this.cmd = cmd;
        this.obj = obj;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (keyboardFocusedOnCanvas()) {
            if (cmd.equalsIgnoreCase(KEY_ZOOM_IN)) {
                ((ZoomControl) obj).zoomIn();
            } else if (cmd.equalsIgnoreCase(KEY_ZOOM_OUT)) {
                ((ZoomControl) obj).zoomOut();
            } else if (cmd.equalsIgnoreCase(KEY_SIM_TICK)) {
                ((MenuSimulate) obj).tick();
            }
        }
    }

    boolean keyboardFocusedOnCanvas() {
        // NOTE: protect against focusOwner being null
        Component focusOwner = getCurrentKeyboardFocusManager().getFocusOwner();
        return focusOwner != null && focusOwner.getClass().equals(CANVAS_CLASS);
    }
}
