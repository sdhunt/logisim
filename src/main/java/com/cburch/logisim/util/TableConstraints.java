/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.awt.*;

/**
 * Designates a specific row and column of a table.
 * Can be used in conjunction with
 * {@link TableLayout#addLayoutComponent(Component, Object)} to force the
 * component to be positioned in the specified row and column of the table.
 */
public class TableConstraints {

    private final int col;
    private final int row;

    private TableConstraints(int row, int col) {
        this.col = col;
        this.row = row;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    /**
     * Creates a table constraints for the given row and column.
     *
     * @param row the row
     * @param col the column
     * @return the table constraints instance
     */
    public static TableConstraints at(int row, int col) {
        return new TableConstraints(row, col);
    }
}
