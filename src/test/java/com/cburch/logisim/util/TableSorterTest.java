/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractGraphicsTest;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.meowster.test.AbstractTest.StarWars.C3PO;
import static com.meowster.test.AbstractTest.StarWars.HAN;
import static com.meowster.test.AbstractTest.StarWars.LEIA;
import static com.meowster.test.AbstractTest.StarWars.LUKE;
import static com.meowster.test.AbstractTest.StarWars.R2D2;
import static com.meowster.test.AbstractTest.StarWars.VADER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TableSorter}.
 */
public class TableSorterTest extends AbstractGraphicsTest {

    private static final Map<Color, Integer> COLOR_INT_MAP = new HashMap<>();

    static {
        COLOR_INT_MAP.put(Color.BLUE, 1);
        COLOR_INT_MAP.put(Color.GRAY, 2);
        COLOR_INT_MAP.put(Color.RED, 3);
    }

    private static final Map<Color, String> COLOR_NAME_MAP = new HashMap<>();

    static {
        COLOR_NAME_MAP.put(Color.BLUE, "blue");
        COLOR_NAME_MAP.put(Color.GRAY, "gray");
        COLOR_NAME_MAP.put(Color.RED, "red");
    }

    private final Comparator<Color> COLOR_COMP = (o1, o2) -> {
        int i1 = COLOR_INT_MAP.get(o1);
        int i2 = COLOR_INT_MAP.get(o2);
        return i1 - i2;
    };

    private final Comparator<StarWars> SW_COMP = Comparator.naturalOrder();

    //  LUKE, LEIA, HAN, C3PO, R2D2, VADER

    /*
     *      Who   Amount   Color
     *      ---------------------
     *      LEIA     3      BLUE
     *      VADER    9      RED
     *      LUKE     4      BLUE
     *      HAN      1      BLUE
     *      R2D2     7      GRAY
     *      C3PO     5      GRAY
     */

    private static final String[] COL_NAME = {
            "Who", "Amount", "Color"
    };
    private static final Class<?>[] COL_CLASS = {
            StarWars.class,
            Integer.class,
            Color.class
    };

    private static class Row {
        private final StarWars who;
        private final int amount;
        private final Color color;

        Row(StarWars who, int amount, Color color) {
            this.who = who;
            this.amount = amount;
            this.color = color;
        }

        public Object getCell(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return who;
                case 1:
                    return amount;
                case 2:
                    return color;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    private static final Row[] rows = {
            new Row(LEIA, 3, Color.BLUE),
            new Row(VADER, 9, Color.RED),
            new Row(LUKE, 4, Color.BLUE),
            new Row(HAN, 1, Color.BLUE),
            new Row(R2D2, 7, Color.GRAY),
            new Row(C3PO, 5, Color.GRAY),
    };

    private static class TestTableModel implements TableModel {
        @Override
        public int getRowCount() {
            return 6;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return COL_NAME[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return COL_CLASS[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return rows[rowIndex].getCell(columnIndex);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
        }
    }

    private TableModel tm;
    private TableSorter sorter;

    @Before
    public void setUp() {
        tm = new TestTableModel();
        sorter = new TableSorter(tm);
        sorter.setColumnComparator(StarWars.class, SW_COMP);
        sorter.setColumnComparator(Color.class, COLOR_COMP);
    }

    private void printAndAssertOrder(StarWars... characters) {
        assertThat(characters.length, is(equalTo(6)));
        print("");
        for (int rowIndex = 0; rowIndex < 6; rowIndex++) {
            printRow(rowIndex);
        }
        for (int rowIndex = 0; rowIndex < 6; rowIndex++) {
            assertThat(sorter.getValueAt(rowIndex, 0),
                       is(equalTo(characters[rowIndex])));
        }
    }

    private void printRow(int rowIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(rowIndex).append(": ");
        sb.append(sorter.getValueAt(rowIndex, 0)).append("-");
        sb.append(sorter.getValueAt(rowIndex, 1)).append("-");
        sb.append(COLOR_NAME_MAP.get(sorter.getValueAt(rowIndex, 2)));
        print(sb);
    }

    /*
     *      Who   Amount   Color
     *      ---------------------
     *      LEIA     3      BLUE
     *      VADER    9      RED
     *      LUKE     4      BLUE
     *      HAN      1      BLUE
     *      R2D2     7      GRAY
     *      C3PO     5      GRAY
     */

    @Test
    public void whoDescending() {
        title("who descending");
        sorter.setSortingStatus(0, TableSorter.DESCENDING);
        printAndAssertOrder(VADER, R2D2, C3PO, HAN, LEIA, LUKE);
    }

    @Test
    public void amountAscending() {
        title("amount ascending");
        sorter.setSortingStatus(1, TableSorter.ASCENDING);
        printAndAssertOrder(HAN, LEIA, LUKE, C3PO, R2D2, VADER);
    }

    @Test
    public void colorAscendingThenAmountDescending() {
        title("color asc then amount desc");
        sorter.setSortingStatus(2, TableSorter.ASCENDING);
        sorter.setSortingStatus(1, TableSorter.DESCENDING);
        printAndAssertOrder(LUKE, LEIA, HAN, R2D2, C3PO, VADER);
    }
}
