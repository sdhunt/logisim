/*
 *  Copyright (c) 2012, Joey Lawrance et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */
package com.cburch.logisim.util;

import org.slf4j.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Given a string, return the locale-specific translation.
 * This class is analogous to GNU gettext.
 *
 * @author Joey Lawrance
 */
public class LocaleString {

    private static final Logger logger = getLogger(LocaleString.class);

    private static LocaleString self = null;

    // TODO: Are sections referenced anywhere else? Should be enum perhaps?
    private String[] sections = ("analyze circuit data draw file gui hex " +
            "log menu opts prefs proj start std tools util").split(" ");

    private final Map<String, LocaleManager> sourceMap = new HashMap<>();

    private LocaleManager util;

    private LocaleString() {
        for (String section : sections) {
            LocaleManager manager = new LocaleManager("logisim", section);
            for (String key : manager.getKeys()) {
                sourceMap.put(key, manager);
            }
            if (section.equals("util")) {
                util = manager;
            }
        }
    }

    /**
     * Returns a reference to the locale manager for the 'util' bundle.
     *
     * @return the 'util' locale manager
     */
    static LocaleManager getUtilLocaleManager() {
        return getInstance().util;
    }

    private static LocaleString getInstance() {
        if (self == null) {
            self = new LocaleString();
        }
        return self;
    }

    /**
     * Returns the available locales.
     *
     * @return the available locales
     */
    // TODO: This shouldn't belong here
    public static Locale[] getFromLocaleOptions() {
        return getUtilLocaleManager().getFromLocaleOptions();
    }

    /**
     * Returns a locale selector component, populated with the available
     * locales.
     *
     * @return a locale selector component
     */
    // TODO: This shouldn't belong here
    public static JComponent createLocaleSelector() {
        return getUtilLocaleManager().createLocaleSelector();
    }

    /**
     * Returns the localized string for the given key.
     * If no such key exists in the registered localization bundles, the
     * key is returned verbatim.
     *
     * @param key the localization key
     * @return the associated localized text
     */
    public static String getFromLocale(String key) {
        LocaleManager localeManager = getInstance().sourceMap.get(key);
        if (localeManager == null) {
            logger.error("Could not get string \"" + key + "\".");
            return key;
        }
        return localeManager.get(key);
    }

    /**
     * Returns a formatted string using the given arguments. Note that the
     * string format is pulled from the appropriate localization bundle.
     *
     * @param key  the localization key for the format string
     * @param args the arguments to be substituted into the format string
     * @return the formatted string
     * @see String#format(String, Object...)
     */
    public static String getFromLocale(String key, String... args) {
        return String.format(getFromLocale(key), (Object[]) args);
    }
}
