/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.Serializable;
import java.util.StringTokenizer;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.XML11Char;

public class QName
implements Serializable {
    protected String _localName;
    protected String _namespaceURI;
    protected String _prefix;
    private int m_hashCode;

    public QName() {
    }

    public QName(String string, String string2) {
        this(string, string2, false);
    }

    public QName(String string, String string2, boolean bl) {
        if (string2 == null) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
        }
        if (bl && !XML11Char.isXML11ValidNCName(string2)) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
        }
        this._namespaceURI = string;
        this._localName = string2;
        this.m_hashCode = this.toString().hashCode();
    }

    public QName(String string) {
        this(string, false);
    }

    public QName(String string, boolean bl) {
        if (string == null) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
        }
        if (bl && !XML11Char.isXML11ValidNCName(string)) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
        }
        this._namespaceURI = null;
        this._localName = string;
        this.m_hashCode = this.toString().hashCode();
    }

    public QName(String string, PrefixResolver prefixResolver) {
        this(string, prefixResolver, false);
    }

    public QName(String string, PrefixResolver prefixResolver, boolean bl) {
        String string2 = null;
        this._namespaceURI = null;
        int n2 = string.indexOf(58);
        if (n2 > 0) {
            string2 = string.substring(0, n2);
            this._namespaceURI = string2.equals("xml") ? "http://www.w3.org/XML/1998/namespace" : prefixResolver.getNamespaceForPrefix(string2);
            if (null == this._namespaceURI) {
                throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[]{string2}));
            }
            this._localName = string.substring(n2 + 1);
        } else {
            if (n2 == 0) {
                throw new RuntimeException(XMLMessages.createXMLMessage("ER_NAME_CANT_START_WITH_COLON", null));
            }
            this._localName = string;
        }
        if (bl && (this._localName == null || !XML11Char.isXML11ValidNCName(this._localName))) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
        }
        this.m_hashCode = this.toString().hashCode();
        this._prefix = string2;
    }

    public String getNamespaceURI() {
        return this._namespaceURI;
    }

    public String getPrefix() {
        return this._prefix;
    }

    public String getLocalName() {
        return this._localName;
    }

    public String toString() {
        return this._prefix != null ? this._prefix + ":" + this._localName : (this._namespaceURI != null ? "{" + this._namespaceURI + "}" + this._localName : this._localName);
    }

    public String toNamespacedString() {
        return this._namespaceURI != null ? "{" + this._namespaceURI + "}" + this._localName : this._localName;
    }

    public String getNamespace() {
        return this.getNamespaceURI();
    }

    public String getLocalPart() {
        return this.getLocalName();
    }

    public int hashCode() {
        return this.m_hashCode;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof QName) {
            QName qName = (QName)object;
            String string = this.getNamespaceURI();
            String string2 = qName.getNamespaceURI();
            return this.getLocalName().equals(qName.getLocalName()) && (null != string && null != string2 ? string.equals(string2) : null == string && null == string2);
        }
        return false;
    }

    public static QName getQNameFromString(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, "{}", false);
        String string2 = stringTokenizer.nextToken();
        String string3 = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : null;
        QName qName = null == string3 ? new QName(null, string2) : new QName(string2, string3);
        return qName;
    }

    public static String getLocalPart(String string) {
        int n2 = string.indexOf(58);
        return n2 < 0 ? string : string.substring(n2 + 1);
    }

    public static String getPrefixPart(String string) {
        int n2 = string.indexOf(58);
        return n2 >= 0 ? string.substring(0, n2) : "";
    }
}

