/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SAXMessageFormatter {
    public static String formatMessage(Locale locale, String string, Object[] arrobject) throws MissingResourceException {
        String string2;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.SAXMessages", locale);
        try {
            string2 = resourceBundle.getString(string);
            if (arrobject != null) {
                try {
                    string2 = MessageFormat.format(string2, arrobject);
                }
                catch (Exception exception) {
                    string2 = resourceBundle.getString("FormatFailed");
                    string2 = string2 + " " + resourceBundle.getString(string);
                }
            }
        }
        catch (MissingResourceException missingResourceException) {
            String string3 = resourceBundle.getString("BadMessageKey");
            throw new MissingResourceException(string, string3, string);
        }
        if (string2 == null) {
            string2 = string;
            if (arrobject.length > 0) {
                StringBuffer stringBuffer = new StringBuffer(string2);
                stringBuffer.append('?');
                int n2 = 0;
                while (n2 < arrobject.length) {
                    if (n2 > 0) {
                        stringBuffer.append('&');
                    }
                    stringBuffer.append(String.valueOf(arrobject[n2]));
                    ++n2;
                }
            }
        }
        return string2;
    }
}

