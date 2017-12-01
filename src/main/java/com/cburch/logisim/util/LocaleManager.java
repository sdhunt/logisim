/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import org.apache.commons.collections15.EnumerationUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import static java.util.ResourceBundle.getBundle;

/**
 * Manager for locale-based functionality.
 */
public class LocaleManager {

    /*
     * NOTE: this class seems to be re-implementing functionality that java
     *       standard libraries already provide.
     *       cf. ResourceBundle and related classes
     */

    private static final String SETTINGS_NAME = "settings";

    private static List<LocaleManager> managers = new ArrayList<>();
    private static List<LocaleListener> listeners = new ArrayList<>();

    private static boolean replaceAccents = false;

    private static Map<Character, String> repl = null;
    private static Locale curLocale = null;

    private final String dirName;
    private final String baseName;

    private ResourceBundle settings = null;
    private ResourceBundle locale = null;
    private ResourceBundle defaultLocale = null;


    /**
     * Creates a locale manager instance for the given directory and base name.
     *
     * @param dirName  the directory containing bundles
     * @param baseName the base name of the bundle
     */
    public LocaleManager(String dirName, String baseName) {
        this.dirName = dirName;
        this.baseName = baseName;
        loadDefault();
        managers.add(this);
    }

    private void loadDefault() {
        if (settings == null) {
            try {
                settings = getBundle(dirName + "/" + SETTINGS_NAME);
            } catch (MissingResourceException e) {
            }
        }

        try {
            loadLocale(Locale.getDefault());
            if (locale != null) {
                return;
            }

        } catch (MissingResourceException e) {
        }

        try {
            loadLocale(Locale.ENGLISH);
            if (locale != null) {
                return;
            }

        } catch (MissingResourceException e) {
        }

        Locale[] choices = getFromLocaleOptions();
        if (choices != null && choices.length > 0) {
            loadLocale(choices[0]);
        }

        if (locale == null) {
            throw new RuntimeException("No locale bundles are available");
        }
    }

    private void loadLocale(Locale loc) {
        String bundleName = dirName + "/" + loc.getLanguage() + "/" + baseName;
        locale = getBundle(bundleName, loc);
    }

    /**
     * Returns the keys available in the current bundle.
     *
     * @return the available keys
     */
    public Iterable<String> getKeys() {
        return EnumerationUtils.toList(locale.getKeys());
    }

    /**
     * Returns the localized string for the given localization key.
     *
     * @param key the key
     * @return the associated localized text
     */
    public String get(String key) {
        String ret;
        try {
            ret = locale.getString(key);
        } catch (MissingResourceException e) {
            ResourceBundle backup = defaultLocale;
            if (backup == null) {
                Locale backupLocale = Locale.US;
                backup = getBundle(dirName + "/en/" + baseName, backupLocale);
                defaultLocale = backup;
            }
            try {
                ret = backup.getString(key);
            } catch (MissingResourceException e2) {
                ret = key;
            }
        }
        Map<Character, String> repl = LocaleManager.repl;
        if (repl != null) {
            ret = replaceAccents(ret, repl);
        }

        return ret;
    }

    /**
     * Returns an array of available locales.
     *
     * @return array of available locales
     */
    Locale[] getFromLocaleOptions() {
        String locs = null;
        try {
            if (settings != null) {
                locs = settings.getString("locales");
            }

        } catch (MissingResourceException e) {
        }
        if (locs == null) {
            return new Locale[]{};
        }


        List<Locale> retl = new ArrayList<>();
        StringTokenizer toks = new StringTokenizer(locs);
        while (toks.hasMoreTokens()) {
            String f = toks.nextToken();
            String language;
            String country;
            if (f.length() >= 2) {
                language = f.substring(0, 2);
                country = f.length() >= 5 ? f.substring(3, 5) : null;
            } else {
                language = null;
                country = null;
            }
            if (language != null) {
                Locale loc = country == null ? new Locale(language) : new Locale(language, country);
                retl.add(loc);
            }
        }

        return retl.toArray(new Locale[retl.size()]);
    }

    /**
     * Returns a locale selector component, populated with the available
     * locales.
     *
     * @return a locale selector component
     */
    JComponent createLocaleSelector() {
        Locale[] locales = getFromLocaleOptions();
        if (locales == null || locales.length == 0) {
            Locale cur = getFromLocale();
            if (cur == null) {
                cur = new Locale("en");
            }

            locales = new Locale[]{cur};
        }
        return new JScrollPane(new LocaleSelector(locales));
    }


    // === Static methods...

    /**
     * Returns the locale configured on the user's system.
     * If no locale was initially configured, the system default locale is used.
     *
     * @return the locale
     */
    public static Locale getFromLocale() {
        if (curLocale == null) {
            curLocale = Locale.getDefault();
        }
        return curLocale;
    }

    /**
     * Sets the specified locale. If this is different to to current locale,
     * a {@link LocaleListener#localeChanged()} event is fired.
     *
     * @param loc the locale to set
     */
    public static void setLocale(Locale loc) {
        Locale cur = getFromLocale();
        if (!loc.equals(cur)) {
            Locale[] opts = LocaleString.getUtilLocaleManager().getFromLocaleOptions();
            Locale select = null;
            Locale backup = null;
            String locLang = loc.getLanguage();

            for (Locale opt : opts) {
                if (select == null && opt.equals(loc)) {
                    select = opt;
                }
                if (backup == null && opt.getLanguage().equals(locLang)) {
                    backup = opt;
                }
            }
            if (select == null) {
                select = backup == null ? new Locale("en") : backup;
            }

            curLocale = select;
            Locale.setDefault(select);
            for (LocaleManager man : managers) {
                man.loadDefault();
            }
            repl = replaceAccents ? fetchReplaceAccents() : null;
            fireLocaleChanged();
        }
    }

    /**
     * Returns true if accented characters can be replaced.
     *
     * @return true, if accents can be replaced; false otherwise
     */
    public static boolean canReplaceAccents() {
        return fetchReplaceAccents() != null;
    }

    /**
     * Sets accent replacement on or off.
     *
     * @param value true, to switch replacement on; false, to switch off
     */
    public static void setReplaceAccents(boolean value) {
        HashMap<Character, String> newRepl = value ? fetchReplaceAccents() : null;
        replaceAccents = value;
        repl = newRepl;
        fireLocaleChanged();
    }

    private static HashMap<Character, String> fetchReplaceAccents() {
        HashMap<Character, String> ret = null;
        String val;
        try {
            val = LocaleString.getUtilLocaleManager().locale.getString("accentReplacements");
        } catch (MissingResourceException e) {
            return null;
        }
        StringTokenizer toks = new StringTokenizer(val, "/");
        while (toks.hasMoreTokens()) {
            String tok = toks.nextToken().trim();
            char c = '\0';
            String s = null;
            if (tok.length() == 1) {
                c = tok.charAt(0);
                s = "";
            } else if (tok.length() >= 2 && tok.charAt(1) == ' ') {
                c = tok.charAt(0);
                s = tok.substring(2).trim();
            }
            if (s != null) {
                if (ret == null) {
                    ret = new HashMap<>();
                }

                ret.put(c, s);
            }
        }
        return ret;
    }

    /**
     * Adds the specified locale listener.
     *
     * @param l the listener to add
     */
    public static void addLocaleListener(LocaleListener l) {
        listeners.add(l);
    }

    /**
     * Removes the specified locale listener.
     *
     * @param l the listener to remove
     */
    public static void removeLocaleListener(LocaleListener l) {
        listeners.remove(l);
    }

    private static void fireLocaleChanged() {
        for (LocaleListener l : listeners) {
            l.localeChanged();
        }
    }

    private static String replaceAccents(String src, Map<Character, String> repl) {
        // find first non-standard character - so we can avoid the
        // replacement process if possible
        int i = 0;
        int n = src.length();
        for (; i < n; i++) {
            char ci = src.charAt(i);
            if (ci < 32 || ci >= 127) {
                break;
            }

        }
        if (i == n) {
            return src;
        }

        // ok, we'll have to consider replacing accents
        char[] cs = src.toCharArray();
        StringBuilder ret = new StringBuilder(src.substring(0, i));
        for (int j = i; j < cs.length; j++) {
            char cj = cs[j];
            if (cj < 32 || cj >= 127) {
                String out = repl.get(cj);
                if (out != null) {
                    ret.append(out);
                } else {
                    ret.append(cj);
                }
            } else {
                ret.append(cj);
            }
        }
        return ret.toString();
    }
}
