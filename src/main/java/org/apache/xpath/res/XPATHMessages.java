/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.res;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import org.apache.xml.res.XMLMessages;

public class XPATHMessages
extends XMLMessages {
    private static ListResourceBundle XPATHBundle = null;
    private static final String XPATH_ERROR_RESOURCES = "org.apache.xpath.res.XPATHErrorResources";

    public static final String createXPATHMessage(String string, Object[] arrobject) {
        if (XPATHBundle == null) {
            XPATHBundle = XPATHMessages.loadResourceBundle("org.apache.xpath.res.XPATHErrorResources");
        }
        if (XPATHBundle != null) {
            return XPATHMessages.createXPATHMsg(XPATHBundle, string, arrobject);
        }
        return "Could not load any resource bundles.";
    }

    public static final String createXPATHWarning(String string, Object[] arrobject) {
        if (XPATHBundle == null) {
            XPATHBundle = XPATHMessages.loadResourceBundle("org.apache.xpath.res.XPATHErrorResources");
        }
        if (XPATHBundle != null) {
            return XPATHMessages.createXPATHMsg(XPATHBundle, string, arrobject);
        }
        return "Could not load any resource bundles.";
    }

    public static final String createXPATHMsg(ListResourceBundle listResourceBundle, String string, Object[] arrobject) {
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
}

