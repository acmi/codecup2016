/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.res;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLMessages {
    protected Locale fLocale = Locale.getDefault();
    private static ListResourceBundle XMLBundle = null;
    private static final String XML_ERROR_RESOURCES = "org.apache.xml.res.XMLErrorResources";
    protected static final String BAD_CODE = "BAD_CODE";
    protected static final String FORMAT_FAILED = "FORMAT_FAILED";

    public void setLocale(Locale locale) {
        this.fLocale = locale;
    }

    public Locale getLocale() {
        return this.fLocale;
    }

    public static final String createXMLMessage(String string, Object[] arrobject) {
        if (XMLBundle == null) {
            XMLBundle = XMLMessages.loadResourceBundle("org.apache.xml.res.XMLErrorResources");
        }
        if (XMLBundle != null) {
            return XMLMessages.createMsg(XMLBundle, string, arrobject);
        }
        return "Could not load any resource bundles.";
    }

    public static final String createMsg(ListResourceBundle listResourceBundle, String string, Object[] arrobject) {
        String string2 = null;
        boolean bl = false;
        String string3 = null;
        if (string != null) {
            string3 = listResourceBundle.getString(string);
        }
        if (string3 == null) {
            string3 = listResourceBundle.getString("BAD_CODE");
            bl = true;
        }
        if (arrobject != null) {
            try {
                int n2 = arrobject.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    if (null != arrobject[i2]) continue;
                    arrobject[i2] = "";
                }
                string2 = MessageFormat.format(string3, arrobject);
            }
            catch (Exception exception) {
                string2 = listResourceBundle.getString("FORMAT_FAILED");
                string2 = string2 + " " + string3;
            }
        } else {
            string2 = string3;
        }
        if (bl) {
            throw new RuntimeException(string2);
        }
        return string2;
    }

    public static ListResourceBundle loadResourceBundle(String string) throws MissingResourceException {
        Locale locale = Locale.getDefault();
        try {
            return (ListResourceBundle)ResourceBundle.getBundle(string, locale);
        }
        catch (MissingResourceException missingResourceException) {
            try {
                return (ListResourceBundle)ResourceBundle.getBundle(string, new Locale("en", "US"));
            }
            catch (MissingResourceException missingResourceException2) {
                throw new MissingResourceException("Could not load any resource bundles." + string, string, "");
            }
        }
    }

    protected static String getResourceSuffix(Locale locale) {
        String string = "_" + locale.getLanguage();
        String string2 = locale.getCountry();
        if (string2.equals("TW")) {
            string = string + "_" + string2;
        }
        return string;
    }
}

