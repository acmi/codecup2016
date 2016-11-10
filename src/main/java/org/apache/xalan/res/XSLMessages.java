/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.res;

import java.util.ListResourceBundle;
import org.apache.xpath.res.XPATHMessages;

public class XSLMessages
extends XPATHMessages {
    private static ListResourceBundle XSLTBundle = null;
    private static final String XSLT_ERROR_RESOURCES = "org.apache.xalan.res.XSLTErrorResources";

    public static final String createMessage(String string, Object[] arrobject) {
        if (XSLTBundle == null) {
            XSLTBundle = XSLMessages.loadResourceBundle("org.apache.xalan.res.XSLTErrorResources");
        }
        if (XSLTBundle != null) {
            return XSLMessages.createMsg(XSLTBundle, string, arrobject);
        }
        return "Could not load any resource bundles.";
    }

    public static final String createWarning(String string, Object[] arrobject) {
        if (XSLTBundle == null) {
            XSLTBundle = XSLMessages.loadResourceBundle("org.apache.xalan.res.XSLTErrorResources");
        }
        if (XSLTBundle != null) {
            return XSLMessages.createMsg(XSLTBundle, string, arrobject);
        }
        return "Could not load any resource bundles.";
    }
}

