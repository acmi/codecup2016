/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import org.apache.xml.serializer.EmptySerializer;
import org.xml.sax.SAXException;

public final class StringValueHandler
extends EmptySerializer {
    private StringBuffer _buffer = new StringBuffer();
    private String _str = null;
    private static final String EMPTY_STR = "";
    private boolean m_escaping = false;
    private int _nestedLevel = 0;

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this._nestedLevel > 0) {
            return;
        }
        if (this._str != null) {
            this._buffer.append(this._str);
            this._str = null;
        }
        this._buffer.append(arrc, n2, n3);
    }

    public String getValue() {
        if (this._buffer.length() != 0) {
            String string = this._buffer.toString();
            this._buffer.setLength(0);
            return string;
        }
        String string = this._str;
        this._str = null;
        return string != null ? string : "";
    }

    public void characters(String string) throws SAXException {
        if (this._nestedLevel > 0) {
            return;
        }
        if (this._str == null && this._buffer.length() == 0) {
            this._str = string;
        } else {
            if (this._str != null) {
                this._buffer.append(this._str);
                this._str = null;
            }
            this._buffer.append(string);
        }
    }

    public void startElement(String string) throws SAXException {
        ++this._nestedLevel;
    }

    public void endElement(String string) throws SAXException {
        --this._nestedLevel;
    }

    public boolean setEscaping(boolean bl) {
        boolean bl2 = this.m_escaping;
        this.m_escaping = bl;
        return bl;
    }

    public String getValueOfPI() {
        String string = this.getValue();
        if (string.indexOf("?>") > 0) {
            int n2 = string.length();
            StringBuffer stringBuffer = new StringBuffer();
            int n3 = 0;
            while (n3 < n2) {
                char c2;
                if ((c2 = string.charAt(n3++)) == '?' && string.charAt(n3) == '>') {
                    stringBuffer.append("? >");
                    ++n3;
                    continue;
                }
                stringBuffer.append(c2);
            }
            return stringBuffer.toString();
        }
        return string;
    }
}

