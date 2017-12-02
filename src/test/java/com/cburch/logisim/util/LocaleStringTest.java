/*
 *  Copyright (c) 2017, Simon Hunt et al.
 *  License information is located in the com.cburch.logisim.Main source code.
 */

package com.cburch.logisim.util;

import com.meowster.test.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link LocaleString}.
 */
public class LocaleStringTest extends AbstractTest {

    private static Locale systemLocale;

    @BeforeClass
    public static void classSetUp() {
        systemLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterClass
    public static void classTearDown() {
        Locale.setDefault(systemLocale);
    }

    private void setUpLocaleMap(Locale locale) {
        LocaleString.clearSourceMap();
        Locale.setDefault(locale);
        LocaleString.reInitSourceMap();
    }

    @Test
    public void undefinedKey() {
        title("undefined key");
        setUpLocaleMap(Locale.US);

        String result = LocaleString.getFromLocale("fooNoKey");
        print(result);
        assertThat(result, is(equalTo("fooNoKey")));
    }

    @Test
    public void splitterBitWidth() {
        title("splitter bit width");
        setUpLocaleMap(Locale.US);

        // from en/circuit.properties:
        //      splitterBitWidthAttr = Bit Width In
        String result = LocaleString.getFromLocale("splitterBitWidthAttr");
        print(result);
        assertThat(result, is(equalTo("Bit Width In")));
    }

    @Test
    public void splitterBitWidthGerman() {
        title("splitter bit width (auf Deutsche)");
        setUpLocaleMap(Locale.GERMANY);

        // from de/circuit.properties:
        //      splitterBitWidthAttr= Bitbreite
        String result = LocaleString.getFromLocale("splitterBitWidthAttr");
        print(result);
        assertThat(result, is(equalTo("Bitbreite")));
    }

    @Test
    public void splitterSplitManyTip() {
        title("splitter bit many tip");
        setUpLocaleMap(Locale.US);

        // from en/circuit.properties:
        //      splitterSplitManyTip = Bits %s from combined end
        String result = LocaleString.getFromLocale("splitterSplitManyTip", "123");
        print(result);
        assertThat(result, is(equalTo("Bits 123 from combined end")));
    }

    @Test
    public void splitterSplitManyTipGerman() {
        title("splitter bit many tip (auf Deutsche)");
        setUpLocaleMap(Locale.GERMANY);

        // from de/circuit.properties:
        //      splitterSplitManyTip= Bits %s der gemeinsamen Seite
        String result = LocaleString.getFromLocale("splitterSplitManyTip", "123");
        print(result);
        assertThat(result, is(equalTo("Bits 123 der gemeinsamen Seite")));
    }
}
