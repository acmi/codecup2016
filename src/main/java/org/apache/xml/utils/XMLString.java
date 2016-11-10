/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public interface XMLString {
    public void dispatchCharactersEvents(ContentHandler var1) throws SAXException;

    public void dispatchAsComment(LexicalHandler var1) throws SAXException;

    public XMLString fixWhiteSpace(boolean var1, boolean var2, boolean var3);

    public int length();

    public char charAt(int var1);

    public boolean equals(XMLString var1);

    public boolean equals(String var1);

    public boolean startsWith(XMLString var1);

    public int indexOf(int var1);

    public int indexOf(XMLString var1);

    public XMLString substring(int var1);

    public XMLString substring(int var1, int var2);

    public String toString();

    public boolean hasString();

    public double toDouble();
}

