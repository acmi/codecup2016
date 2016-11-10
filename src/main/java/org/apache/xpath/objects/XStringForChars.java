/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xpath.objects.XString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class XStringForChars
extends XString {
    int m_start;
    int m_length;
    protected String m_strCache = null;

    public XStringForChars(char[] arrc, int n2, int n3) {
        super(arrc);
        this.m_start = n2;
        this.m_length = n3;
        if (null == arrc) {
            throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
        }
    }

    public void appendToFsb(FastStringBuffer fastStringBuffer) {
        fastStringBuffer.append((char[])this.m_obj, this.m_start, this.m_length);
    }

    public boolean hasString() {
        return null != this.m_strCache;
    }

    public String str() {
        if (null == this.m_strCache) {
            this.m_strCache = new String((char[])this.m_obj, this.m_start, this.m_length);
        }
        return this.m_strCache;
    }

    public Object object() {
        return this.str();
    }

    public void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException {
        contentHandler.characters((char[])this.m_obj, this.m_start, this.m_length);
    }

    public void dispatchAsComment(LexicalHandler lexicalHandler) throws SAXException {
        lexicalHandler.comment((char[])this.m_obj, this.m_start, this.m_length);
    }

    public int length() {
        return this.m_length;
    }

    public char charAt(int n2) {
        return ((char[])this.m_obj)[n2 + this.m_start];
    }

    public void getChars(int n2, int n3, char[] arrc, int n4) {
        System.arraycopy((char[])this.m_obj, this.m_start + n2, arrc, n4, n3);
    }
}

