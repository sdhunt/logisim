/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import com.cburch.logisim.prefs.AppPreferences;
import com.cburch.logisim.util.LocaleSelector.LocaleOption;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Locale;

/**
 * Component that allows selection of an available locale.
 */
class LocaleSelector extends JList<LocaleOption>
        implements LocaleListener, ListSelectionListener {

    /**
     * A single locale option.
     */
    protected static class LocaleOption implements Runnable {
        private Locale locale;
        private String text;

        LocaleOption(Locale locale) {
            this.locale = locale;
            update(locale);
        }

        @Override
        public String toString() {
            return text;
        }

        void update(Locale current) {
            text = locale.getDisplayName(locale);
            if (current == null || !current.equals(locale)) {
                text += " / " + locale.getDisplayName(current);
            }
        }

        @Override
        public void run() {
            if (!LocaleManager.getFromLocale().equals(locale)) {
                LocaleManager.setLocale(locale);
                AppPreferences.LOCALE.set(locale.getLanguage());
            }
        }
    }

    private LocaleOption[] items;

    /**
     * Creates a locale selector for the given locales.
     *
     * @param locales the locales with which to populate the list
     */
    LocaleSelector(Locale[] locales) {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel<LocaleOption> model = new DefaultListModel<>();
        items = new LocaleOption[locales.length];
        for (int i = 0; i < locales.length; ++i) {
            items[i] = new LocaleOption(locales[i]);
            model.addElement(items[i]);
        }
        setModel(model);
        setVisibleRowCount(Math.min(items.length, 8));
        LocaleManager.addLocaleListener(this);
        localeChanged();
        addListSelectionListener(this);
    }

    @Override
    public void localeChanged() {
        Locale current = LocaleManager.getFromLocale();
        LocaleOption sel = null;
        for (LocaleOption item : items) {
            item.update(current);
            if (current.equals(item.locale)) {
                sel = item;
            }
        }
        if (sel != null) {
            setSelectedValue(sel, true);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        LocaleOption opt = getSelectedValue();
        if (opt != null) {
            SwingUtilities.invokeLater(opt);
        }
    }
}
