/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import org.apache.xml.utils.XMLString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class XMLStringDefault
implements XMLString {
    private String m_str;

    public XMLStringDefault(String string) {
        this.m_str = string;
    }

    public void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException {
    }

    public void dispatchAsComment(LexicalHandler lexicalHandler) throws SAXException {
    }

    public XMLString fixWhiteSpace(boolean bl, boolean bl2, boolean bl3) {
        return new XMLStringDefault(this.m_str.trim());
    }

    public int length() {
        return this.m_str.length();
    }

    public char charAt(int n2) {
        return this.m_str.charAt(n2);
    }

    public boolean equals(String string) {
        return this.m_str.equals(string);
    }

    public boolean equals(XMLString xMLString) {
        return this.m_str.equals(xMLString.toString());
    }

    public boolean equals(Object object) {
        return this.m_str.equals(object);
    }

    public boolean startsWith(XMLString xMLString) {
        return this.m_str.startsWith(xMLString.toString());
    }

    public int hashCode() {
        return this.m_str.hashCode();
    }

    public int indexOf(int n2) {
        return this.m_str.indexOf(n2);
    }

    public int indexOf(XMLString xMLString) {
        return this.m_str.indexOf(xMLString.toString());
    }

    public XMLString substring(int n2) {
        return new XMLStringDefault(this.m_str.substring(n2));
    }

    public XMLString substring(int n2, int n3) {
        return new XMLStringDefault(this.m_str.substring(n2, n3));
    }

    public String toString() {
        return this.m_str;
    }

    public boolean hasString() {
        return true;
    }

    public double toDouble() {
        try {
            return Double.valueOf(this.m_str);
        }
        catch (NumberFormatException numberFormatException) {
            return Double.NaN;
        }
    }
}

