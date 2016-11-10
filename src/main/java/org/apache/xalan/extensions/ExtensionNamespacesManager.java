/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.util.Vector;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ExtensionNamespaceSupport;

public class ExtensionNamespacesManager {
    private Vector m_extensions = new Vector();
    private Vector m_predefExtensions = new Vector(7);
    private Vector m_unregisteredExtensions = new Vector();

    public ExtensionNamespacesManager() {
        this.setPredefinedNamespaces();
    }

    public void registerExtension(String string) {
        if (this.namespaceIndex(string, this.m_extensions) == -1) {
            int n2 = this.namespaceIndex(string, this.m_predefExtensions);
            if (n2 != -1) {
                this.m_extensions.add(this.m_predefExtensions.get(n2));
            } else if (!this.m_unregisteredExtensions.contains(string)) {
                this.m_unregisteredExtensions.add(string);
            }
        }
    }

    public void registerExtension(ExtensionNamespaceSupport extensionNamespaceSupport) {
        String string = extensionNamespaceSupport.getNamespace();
        if (this.namespaceIndex(string, this.m_extensions) == -1) {
            this.m_extensions.add(extensionNamespaceSupport);
            if (this.m_unregisteredExtensions.contains(string)) {
                this.m_unregisteredExtensions.remove(string);
            }
        }
    }

    public int namespaceIndex(String string, Vector vector) {
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            if (!((ExtensionNamespaceSupport)vector.get(i2)).getNamespace().equals(string)) continue;
            return i2;
        }
        return -1;
    }

    public Vector getExtensions() {
        return this.m_extensions;
    }

    public void registerUnregisteredNamespaces() {
        for (int i2 = 0; i2 < this.m_unregisteredExtensions.size(); ++i2) {
            String string = (String)this.m_unregisteredExtensions.get(i2);
            ExtensionNamespaceSupport extensionNamespaceSupport = this.defineJavaNamespace(string);
            if (extensionNamespaceSupport == null) continue;
            this.m_extensions.add(extensionNamespaceSupport);
        }
    }

    public ExtensionNamespaceSupport defineJavaNamespace(String string) {
        return this.defineJavaNamespace(string, string);
    }

    public ExtensionNamespaceSupport defineJavaNamespace(String string, String string2) {
        int n2;
        if (null == string || string.trim().length() == 0) {
            return null;
        }
        String string3 = string2;
        if (string3.startsWith("class:")) {
            string3 = string3.substring(6);
        }
        if (-1 != (n2 = string3.lastIndexOf(47))) {
            string3 = string3.substring(n2 + 1);
        }
        if (null == string3 || string3.trim().length() == 0) {
            return null;
        }
        try {
            ExtensionHandler.getClassForName(string3);
            return new ExtensionNamespaceSupport(string, "org.apache.xalan.extensions.ExtensionHandlerJavaClass", new Object[]{string, "javaclass", string3});
        }
        catch (ClassNotFoundException classNotFoundException) {
            return new ExtensionNamespaceSupport(string, "org.apache.xalan.extensions.ExtensionHandlerJavaPackage", new Object[]{string, "javapackage", string3 + "."});
        }
    }

    private void setPredefinedNamespaces() {
        String string = "http://xml.apache.org/xalan/java";
        String string2 = "org.apache.xalan.extensions.ExtensionHandlerJavaPackage";
        String string3 = "javapackage";
        String string4 = "";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xml.apache.org/xslt/java";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xsl.lotus.com/java";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xml.apache.org/xalan";
        string2 = "org.apache.xalan.extensions.ExtensionHandlerJavaClass";
        string3 = "javaclass";
        string4 = "org.apache.xalan.lib.Extensions";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xml.apache.org/xslt";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xml.apache.org/xalan/redirect";
        string4 = "org.apache.xalan.lib.Redirect";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xml.apache.org/xalan/PipeDocument";
        string4 = "org.apache.xalan.lib.PipeDocument";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://xml.apache.org/xalan/sql";
        string4 = "org.apache.xalan.lib.sql.XConnection";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://exslt.org/common";
        string4 = "org.apache.xalan.lib.ExsltCommon";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://exslt.org/math";
        string4 = "org.apache.xalan.lib.ExsltMath";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://exslt.org/sets";
        string4 = "org.apache.xalan.lib.ExsltSets";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://exslt.org/dates-and-times";
        string4 = "org.apache.xalan.lib.ExsltDatetime";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://exslt.org/dynamic";
        string4 = "org.apache.xalan.lib.ExsltDynamic";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
        string = "http://exslt.org/strings";
        string4 = "org.apache.xalan.lib.ExsltStrings";
        this.m_predefExtensions.add(new ExtensionNamespaceSupport(string, string2, new Object[]{string, string3, string4}));
    }
}

