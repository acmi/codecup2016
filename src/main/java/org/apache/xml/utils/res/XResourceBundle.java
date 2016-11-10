/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.xml.utils.res.CharArrayWrapper;

public class XResourceBundle
extends ListResourceBundle {
    public static final XResourceBundle loadResourceBundle(String string, Locale locale) throws MissingResourceException {
        String string2 = XResourceBundle.getResourceSuffix(locale);
        try {
            String string3 = string + string2;
            return (XResourceBundle)ResourceBundle.getBundle(string3, locale);
        }
        catch (MissingResourceException missingResourceException) {
            try {
                return (XResourceBundle)ResourceBundle.getBundle("org.apache.xml.utils.res.XResourceBundle", new Locale("en", "US"));
            }
            catch (MissingResourceException missingResourceException2) {
                throw new MissingResourceException("Could not load any resource bundles.", string, "");
            }
        }
    }

    private static final String getResourceSuffix(Locale locale) {
        String string = locale.getLanguage();
        String string2 = locale.getCountry();
        String string3 = locale.getVariant();
        String string4 = "_" + locale.getLanguage();
        if (string.equals("zh")) {
            string4 = string4 + "_" + string2;
        }
        if (string2.equals("JP")) {
            string4 = string4 + "_" + string2 + "_" + string3;
        }
        return string4;
    }

    public Object[][] getContents() {
        return new Object[][]{{"ui_language", "en"}, {"help_language", "en"}, {"language", "en"}, {"alphabet", new CharArrayWrapper(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'})}, {"tradAlphabet", new CharArrayWrapper(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'})}, {"orientation", "LeftToRight"}, {"numbering", "additive"}};
    }
}

