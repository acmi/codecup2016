/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.apache.xalan.res.XSLMessages;

public class ExtensionNamespaceContext
implements NamespaceContext {
    public static final String EXSLT_PREFIX = "exslt";
    public static final String EXSLT_URI = "http://exslt.org/common";
    public static final String EXSLT_MATH_PREFIX = "math";
    public static final String EXSLT_MATH_URI = "http://exslt.org/math";
    public static final String EXSLT_SET_PREFIX = "set";
    public static final String EXSLT_SET_URI = "http://exslt.org/sets";
    public static final String EXSLT_STRING_PREFIX = "str";
    public static final String EXSLT_STRING_URI = "http://exslt.org/strings";
    public static final String EXSLT_DATETIME_PREFIX = "datetime";
    public static final String EXSLT_DATETIME_URI = "http://exslt.org/dates-and-times";
    public static final String EXSLT_DYNAMIC_PREFIX = "dyn";
    public static final String EXSLT_DYNAMIC_URI = "http://exslt.org/dynamic";
    public static final String JAVA_EXT_PREFIX = "java";
    public static final String JAVA_EXT_URI = "http://xml.apache.org/xalan/java";

    public String getNamespaceURI(String string) {
        if (string == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NAMESPACE_CONTEXT_NULL_PREFIX", null));
        }
        if (string.equals("")) {
            return "";
        }
        if (string.equals("xml")) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if (string.equals("xmlns")) {
            return "http://www.w3.org/2000/xmlns/";
        }
        if (string.equals("exslt")) {
            return "http://exslt.org/common";
        }
        if (string.equals("math")) {
            return "http://exslt.org/math";
        }
        if (string.equals("set")) {
            return "http://exslt.org/sets";
        }
        if (string.equals("str")) {
            return "http://exslt.org/strings";
        }
        if (string.equals("datetime")) {
            return "http://exslt.org/dates-and-times";
        }
        if (string.equals("dyn")) {
            return "http://exslt.org/dynamic";
        }
        if (string.equals("java")) {
            return "http://xml.apache.org/xalan/java";
        }
        return "";
    }

    public String getPrefix(String string) {
        if (string == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NAMESPACE_CONTEXT_NULL_NAMESPACE", null));
        }
        if (string.equals("http://www.w3.org/XML/1998/namespace")) {
            return "xml";
        }
        if (string.equals("http://www.w3.org/2000/xmlns/")) {
            return "xmlns";
        }
        if (string.equals("http://exslt.org/common")) {
            return "exslt";
        }
        if (string.equals("http://exslt.org/math")) {
            return "math";
        }
        if (string.equals("http://exslt.org/sets")) {
            return "set";
        }
        if (string.equals("http://exslt.org/strings")) {
            return "str";
        }
        if (string.equals("http://exslt.org/dates-and-times")) {
            return "datetime";
        }
        if (string.equals("http://exslt.org/dynamic")) {
            return "dyn";
        }
        if (string.equals("http://xml.apache.org/xalan/java")) {
            return "java";
        }
        return null;
    }

    public Iterator getPrefixes(String string) {
        String string2 = this.getPrefix(string);
        return new Iterator(this, string2){
            private boolean isFirstIteration;
            private final String val$result;
            private final ExtensionNamespaceContext this$0;

            public boolean hasNext() {
                return this.isFirstIteration;
            }

            public Object next() {
                if (this.isFirstIteration) {
                    this.isFirstIteration = false;
                    return this.val$result;
                }
                return null;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}

