/*
 * Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

/**
 * An entity that represents a window that can be closed.
 */
public interface WindowClosable {

    /**
     * Invoked by an external event to request the window closes.
     */
    void requestClose();
}
