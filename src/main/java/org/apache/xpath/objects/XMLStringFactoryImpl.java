/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XStringForChars;
import org.apache.xpath.objects.XStringForFSB;

public class XMLStringFactoryImpl
extends XMLStringFactory {
    private static XMLStringFactory m_xstringfactory = new XMLStringFactoryImpl();

    public static XMLStringFactory getFactory() {
        return m_xstringfactory;
    }

    public XMLString newstr(String string) {
        return new XString(string);
    }

    public XMLString newstr(FastStringBuffer fastStringBuffer, int n2, int n3) {
        return new XStringForFSB(fastStringBuffer, n2, n3);
    }

    public XMLString newstr(char[] arrc, int n2, int n3) {
        return new XStringForChars(arrc, n2, n3);
    }

    public XMLString emptystr() {
        return XString.EMPTYSTRING;
    }
}

