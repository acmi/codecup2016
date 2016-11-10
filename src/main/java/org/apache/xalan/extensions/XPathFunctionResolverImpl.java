/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ExtensionHandlerJavaClass;
import org.apache.xalan.extensions.XPathFunctionImpl;
import org.apache.xalan.res.XSLMessages;

public class XPathFunctionResolverImpl {
    public XPathFunction resolveFunction(QName qName, int n2) {
        int n3;
        if (qName == null) {
            throw new NullPointerException(XSLMessages.createMessage("ER_XPATH_RESOLVER_NULL_QNAME", null));
        }
        if (n2 < 0) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_XPATH_RESOLVER_NEGATIVE_ARITY", null));
        }
        String string = qName.getNamespaceURI();
        if (string == null || string.length() == 0) {
            return null;
        }
        String string2 = null;
        String string3 = null;
        if (string.startsWith("http://exslt.org")) {
            string2 = this.getEXSLTClassName(string);
            string3 = qName.getLocalPart();
        } else if (!string.equals("http://xml.apache.org/xalan/java") && -1 != (n3 = string2.lastIndexOf(47))) {
            string2 = string2.substring(n3 + 1);
        }
        String string4 = qName.getLocalPart();
        int n4 = string4.lastIndexOf(46);
        if (n4 > 0) {
            string2 = string2 != null ? string2 + "." + string4.substring(0, n4) : string4.substring(0, n4);
            string3 = string4.substring(n4 + 1);
        } else {
            string3 = string4;
        }
        if (null == string2 || string2.trim().length() == 0 || null == string3 || string3.trim().length() == 0) {
            return null;
        }
        ExtensionHandlerJavaClass extensionHandlerJavaClass = null;
        try {
            ExtensionHandler.getClassForName(string2);
            extensionHandlerJavaClass = new ExtensionHandlerJavaClass(string, "javaclass", string2);
        }
        catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
        return new XPathFunctionImpl(extensionHandlerJavaClass, string3);
    }

    private String getEXSLTClassName(String string) {
        if (string.equals("http://exslt.org/math")) {
            return "org.apache.xalan.lib.ExsltMath";
        }
        if (string.equals("http://exslt.org/sets")) {
            return "org.apache.xalan.lib.ExsltSets";
        }
        if (string.equals("http://exslt.org/strings")) {
            return "org.apache.xalan.lib.ExsltStrings";
        }
        if (string.equals("http://exslt.org/dates-and-times")) {
            return "org.apache.xalan.lib.ExsltDatetime";
        }
        if (string.equals("http://exslt.org/dynamic")) {
            return "org.apache.xalan.lib.ExsltDynamic";
        }
        if (string.equals("http://exslt.org/common")) {
            return "org.apache.xalan.lib.ExsltCommon";
        }
        return null;
    }
}

