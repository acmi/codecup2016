/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.objects.XMLStringFactoryImpl;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class XStringForFSB
extends XString {
    int m_start;
    int m_length;
    protected String m_strCache = null;
    protected int m_hash = 0;

    public XStringForFSB(FastStringBuffer fastStringBuffer, int n2, int n3) {
        super(fastStringBuffer);
        this.m_start = n2;
        this.m_length = n3;
        if (null == fastStringBuffer) {
            throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
        }
    }

    public FastStringBuffer fsb() {
        return (FastStringBuffer)this.m_obj;
    }

    public void appendToFsb(FastStringBuffer fastStringBuffer) {
        fastStringBuffer.append(this.str());
    }

    public boolean hasString() {
        return null != this.m_strCache;
    }

    public Object object() {
        return this.str();
    }

    public String str() {
        if (null == this.m_strCache) {
            this.m_strCache = this.fsb().getString(this.m_start, this.m_length);
        }
        return this.m_strCache;
    }

    public void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException {
        this.fsb().sendSAXcharacters(contentHandler, this.m_start, this.m_length);
    }

    public void dispatchAsComment(LexicalHandler lexicalHandler) throws SAXException {
        this.fsb().sendSAXComment(lexicalHandler, this.m_start, this.m_length);
    }

    public int length() {
        return this.m_length;
    }

    public char charAt(int n2) {
        return this.fsb().charAt(this.m_start + n2);
    }

    public void getChars(int n2, int n3, char[] arrc, int n4) {
        int n5 = n3 - n2;
        if (n5 > this.m_length) {
            n5 = this.m_length;
        }
        if (n5 > arrc.length - n4) {
            n5 = arrc.length - n4;
        }
        int n6 = n2 + this.m_start + n5;
        int n7 = n4;
        FastStringBuffer fastStringBuffer = this.fsb();
        for (int i2 = n2 + this.m_start; i2 < n6; ++i2) {
            arrc[n7++] = fastStringBuffer.charAt(i2);
        }
    }

    public boolean equals(XMLString xMLString) {
        if (this == xMLString) {
            return true;
        }
        int n2 = this.m_length;
        if (n2 == xMLString.length()) {
            FastStringBuffer fastStringBuffer = this.fsb();
            int n3 = this.m_start;
            int n4 = 0;
            while (n2-- != 0) {
                if (fastStringBuffer.charAt(n3) != xMLString.charAt(n4)) {
                    return false;
                }
                ++n3;
                ++n4;
            }
            return true;
        }
        return false;
    }

    public boolean equals(XObject xObject) {
        if (this == xObject) {
            return true;
        }
        if (xObject.getType() == 2) {
            return xObject.equals(this);
        }
        int n2 = this.m_length;
        String string = xObject.str();
        if (n2 == string.length()) {
            FastStringBuffer fastStringBuffer = this.fsb();
            int n3 = this.m_start;
            int n4 = 0;
            while (n2-- != 0) {
                if (fastStringBuffer.charAt(n3) != string.charAt(n4)) {
                    return false;
                }
                ++n3;
                ++n4;
            }
            return true;
        }
        return false;
    }

    public boolean equals(String string) {
        int n2 = this.m_length;
        if (n2 == string.length()) {
            FastStringBuffer fastStringBuffer = this.fsb();
            int n3 = this.m_start;
            int n4 = 0;
            while (n2-- != 0) {
                if (fastStringBuffer.charAt(n3) != string.charAt(n4)) {
                    return false;
                }
                ++n3;
                ++n4;
            }
            return true;
        }
        return false;
    }

    public boolean equals(Object object) {
        if (null == object) {
            return false;
        }
        if (object instanceof XNumber) {
            return object.equals(this);
        }
        if (object instanceof XNodeSet) {
            return object.equals(this);
        }
        if (object instanceof XStringForFSB) {
            return this.equals((XMLString)object);
        }
        return this.equals(object.toString());
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean startsWith(XMLString xMLString, int n2) {
        FastStringBuffer fastStringBuffer = this.fsb();
        int n3 = this.m_start + n2;
        int n4 = this.m_start + this.m_length;
        int n5 = 0;
        int n6 = xMLString.length();
        if (n2 < 0 || n2 > this.m_length - n6) {
            return false;
        }
        while (--n6 >= 0) {
            if (fastStringBuffer.charAt(n3) != xMLString.charAt(n5)) {
                return false;
            }
            ++n3;
            ++n5;
        }
        return true;
    }

    public boolean startsWith(XMLString xMLString) {
        return this.startsWith(xMLString, 0);
    }

    public int indexOf(int n2) {
        return this.indexOf(n2, 0);
    }

    public int indexOf(int n2, int n3) {
        int n4 = this.m_start + this.m_length;
        FastStringBuffer fastStringBuffer = this.fsb();
        if (n3 < 0) {
            n3 = 0;
        } else if (n3 >= this.m_length) {
            return -1;
        }
        for (int i2 = this.m_start + n3; i2 < n4; ++i2) {
            if (fastStringBuffer.charAt(i2) != n2) continue;
            return i2 - this.m_start;
        }
        return -1;
    }

    public XMLString substring(int n2) {
        int n3 = this.m_length - n2;
        if (n3 <= 0) {
            return XString.EMPTYSTRING;
        }
        int n4 = this.m_start + n2;
        return new XStringForFSB(this.fsb(), n4, n3);
    }

    public XMLString substring(int n2, int n3) {
        int n4 = n3 - n2;
        if (n4 > this.m_length) {
            n4 = this.m_length;
        }
        if (n4 <= 0) {
            return XString.EMPTYSTRING;
        }
        int n5 = this.m_start + n2;
        return new XStringForFSB(this.fsb(), n5, n4);
    }

    public XMLString trim() {
        return this.fixWhiteSpace(true, true, false);
    }

    private static boolean isSpace(char c2) {
        return XMLCharacterRecognizer.isWhiteSpace(c2);
    }

    public XMLString fixWhiteSpace(boolean bl, boolean bl2, boolean bl3) {
        int n2;
        int n3 = this /* !! */ .m_length + this /* !! */ .m_start;
        char[] arrc = new char[this /* !! */ .m_length];
        FastStringBuffer fastStringBuffer = this /* !! */ .fsb();
        boolean bl4 = false;
        int n4 = 0;
        boolean bl5 = false;
        for (n2 = this /* !! */ .m_start; n2 < n3; ++n2) {
            char c2 = fastStringBuffer.charAt(n2);
            if (XStringForFSB.isSpace(c2)) {
                if (!bl5) {
                    if (' ' != c2) {
                        bl4 = true;
                    }
                    arrc[n4++] = 32;
                    if (bl3 && n4 != 0) {
                        char c3 = arrc[n4 - 1];
                        if (c3 == '.' || c3 == '!' || c3 == '?') continue;
                        bl5 = true;
                        continue;
                    }
                    bl5 = true;
                    continue;
                }
                bl4 = true;
                bl5 = true;
                continue;
            }
            arrc[n4++] = c2;
            bl5 = false;
        }
        if (bl2 && 1 <= n4 && ' ' == arrc[n4 - 1]) {
            bl4 = true;
            --n4;
        }
        n2 = 0;
        if (bl && 0 < n4 && ' ' == arrc[0]) {
            bl4 = true;
            ++n2;
        }
        XMLStringFactory xMLStringFactory = XMLStringFactoryImpl.getFactory();
        return bl4 ? xMLStringFactory.newstr(arrc, n2, n4 - n2) : this /* !! */ ;
    }

    public double toDouble() {
        char c2;
        int n2;
        if (this.m_length == 0) {
            return Double.NaN;
        }
        String string = this.fsb().getString(this.m_start, this.m_length);
        for (n2 = 0; n2 < this.m_length && XMLCharacterRecognizer.isWhiteSpace(string.charAt(n2)); ++n2) {
        }
        if (n2 == this.m_length) {
            return Double.NaN;
        }
        if (string.charAt(n2) == '-') {
            ++n2;
        }
        while (n2 < this.m_length && ((c2 = string.charAt(n2)) == '.' || c2 >= '0' && c2 <= '9')) {
            ++n2;
        }
        while (n2 < this.m_length && XMLCharacterRecognizer.isWhiteSpace(string.charAt(n2))) {
            ++n2;
        }
        if (n2 != this.m_length) {
            return Double.NaN;
        }
        try {
            return new Double(string);
        }
        catch (NumberFormatException numberFormatException) {
            return Double.NaN;
        }
    }
}

