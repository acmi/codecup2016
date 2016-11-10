/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer.utils;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {
    private final Locale m_locale = Locale.getDefault();
    private ListResourceBundle m_resourceBundle;
    private String m_resourceBundleName;

    Messages(String string) {
        this.m_resourceBundleName = string;
    }

    private Locale getLocale() {
        return this.m_locale;
    }

    public final String createMessage(String string, Object[] arrobject) {
        if (this.m_resourceBundle == null) {
            this.m_resourceBundle = this.loadResourceBundle(this.m_resourceBundleName);
        }
        if (this.m_resourceBundle != null) {
            return this.createMsg(this.m_resourceBundle, string, arrobject);
        }
        return "Could not load the resource bundles: " + this.m_resourceBundleName;
    }

    private final String createMsg(ListResourceBundle listResourceBundle, String string, Object[] arrobject) {
        String string2;
        boolean bl;
        string2 = null;
        bl = false;
        String string3 = null;
        if (string != null) {
            string3 = listResourceBundle.getString(string);
        } else {
            string = "";
        }
        if (string3 == null) {
            bl = true;
            try {
                string3 = MessageFormat.format("BAD_MSGKEY", string, this.m_resourceBundleName);
            }
            catch (Exception exception) {
                string3 = "The message key '" + string + "' is not in the message class '" + this.m_resourceBundleName + "'";
            }
        } else if (arrobject != null) {
            try {
                int n2 = arrobject.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    if (null != arrobject[i2]) continue;
                    arrobject[i2] = "";
                }
                string2 = MessageFormat.format(string3, arrobject);
            }
            catch (Exception exception) {
                bl = true;
                try {
                    string2 = MessageFormat.format("BAD_MSGFORMAT", string, this.m_resourceBundleName);
                    string2 = string2 + " " + string3;
                }
                catch (Exception exception2) {
                    string2 = "The format of message '" + string + "' in message class '" + this.m_resourceBundleName + "' failed.";
                }
            }
        } else {
            string2 = string3;
        }
        if (bl) {
            throw new RuntimeException(string2);
        }
        return string2;
    }

    private ListResourceBundle loadResourceBundle(String string) throws MissingResourceException {
        ListResourceBundle listResourceBundle;
        this.m_resourceBundleName = string;
        Locale locale = this.getLocale();
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(this.m_resourceBundleName, locale);
            listResourceBundle = (ListResourceBundle)resourceBundle;
        }
        catch (MissingResourceException missingResourceException) {
            try {
                listResourceBundle = (ListResourceBundle)ResourceBundle.getBundle(this.m_resourceBundleName, new Locale("en", "US"));
            }
            catch (MissingResourceException missingResourceException2) {
                throw new MissingResourceException("Could not load any resource bundles." + this.m_resourceBundleName, this.m_resourceBundleName, "");
            }
        }
        this.m_resourceBundle = listResourceBundle;
        return listResourceBundle;
    }
}

