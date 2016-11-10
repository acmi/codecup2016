/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DOMMessageFormatter {
    public static final String DOM_DOMAIN = "http://www.w3.org/dom/DOMTR";
    public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
    public static final String SERIALIZER_DOMAIN = "http://apache.org/xml/serializer";
    private static ResourceBundle domResourceBundle = null;
    private static ResourceBundle xmlResourceBundle = null;
    private static ResourceBundle serResourceBundle = null;
    private static Locale locale = null;

    DOMMessageFormatter() {
        locale = Locale.getDefault();
    }

    public static String formatMessage(String string, String string2, Object[] arrobject) throws MissingResourceException {
        String string3;
        ResourceBundle resourceBundle = DOMMessageFormatter.getResourceBundle(string);
        if (resourceBundle == null) {
            DOMMessageFormatter.init();
            resourceBundle = DOMMessageFormatter.getResourceBundle(string);
            if (resourceBundle == null) {
                throw new MissingResourceException("Unknown domain" + string, null, string2);
            }
        }
        try {
            string3 = string2 + ": " + resourceBundle.getString(string2);
            if (arrobject != null) {
                try {
                    string3 = MessageFormat.format(string3, arrobject);
                }
                catch (Exception exception) {
                    string3 = resourceBundle.getString("FormatFailed");
                    string3 = string3 + " " + resourceBundle.getString(string2);
                }
            }
        }
        catch (MissingResourceException missingResourceException) {
            String string4 = resourceBundle.getString("BadMessageKey");
            throw new MissingResourceException(string2, string4, string2);
        }
        if (string3 == null) {
            string3 = string2;
            if (arrobject.length > 0) {
                StringBuffer stringBuffer = new StringBuffer(string3);
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
        return string3;
    }

    static ResourceBundle getResourceBundle(String string) {
        if (string == "http://www.w3.org/dom/DOMTR" || string.equals("http://www.w3.org/dom/DOMTR")) {
            return domResourceBundle;
        }
        if (string == "http://www.w3.org/TR/1998/REC-xml-19980210" || string.equals("http://www.w3.org/TR/1998/REC-xml-19980210")) {
            return xmlResourceBundle;
        }
        if (string == "http://apache.org/xml/serializer" || string.equals("http://apache.org/xml/serializer")) {
            return serResourceBundle;
        }
        return null;
    }

    public static void init() {
        Locale locale = DOMMessageFormatter.locale;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        domResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.DOMMessages", locale);
        serResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLSerializerMessages", locale);
        xmlResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLMessages", locale);
    }

    public static void setLocale(Locale locale) {
        DOMMessageFormatter.locale = locale;
    }
}

