/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XMLStringFactoryImpl;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class XString
extends XObject
implements XMLString {
    public static final XString EMPTYSTRING = new XString("");

    protected XString(Object object) {
        super(object);
    }

    public XString(String string) {
        super(string);
    }

    public int getType() {
        return 3;
    }

    public String getTypeString() {
        return "#STRING";
    }

    public boolean hasString() {
        return true;
    }

    public double num() {
        return this.toDouble();
    }

    public double toDouble() {
        XMLString xMLString = this.trim();
        double d2 = Double.NaN;
        for (int i2 = 0; i2 < xMLString.length(); ++i2) {
            char c2 = xMLString.charAt(i2);
            if (c2 == '-' || c2 == '.' || c2 >= '0' && c2 <= '9') continue;
            return d2;
        }
        try {
            d2 = Double.parseDouble(xMLString.toString());
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return d2;
    }

    public boolean bool() {
        return this.str().length() > 0;
    }

    public XMLString xstr() {
        return this;
    }

    public String str() {
        return null != this.m_obj ? (String)this.m_obj : "";
    }

    public int rtf(XPathContext xPathContext) {
        DTM dTM = xPathContext.createDocumentFragment();
        dTM.appendTextChild(this.str());
        return dTM.getDocument();
    }

    public void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException {
        String string = this.str();
        contentHandler.characters(string.toCharArray(), 0, string.length());
    }

    public void dispatchAsComment(LexicalHandler lexicalHandler) throws SAXException {
        String string = this.str();
        lexicalHandler.comment(string.toCharArray(), 0, string.length());
    }

    public int length() {
        return this.str().length();
    }

    public char charAt(int n2) {
        return this.str().charAt(n2);
    }

    public void getChars(int n2, int n3, char[] arrc, int n4) {
        this.str().getChars(n2, n3, arrc, n4);
    }

    public boolean equals(XObject xObject) {
        int n2 = xObject.getType();
        try {
            if (4 == n2) {
                return xObject.equals(this);
            }
            if (1 == n2) {
                return xObject.bool() == this.bool();
            }
            if (2 == n2) {
                return xObject.num() == this.num();
            }
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
        return this.xstr().equals(xObject.xstr());
    }

    public boolean equals(String string) {
        return this.str().equals(string);
    }

    public boolean equals(XMLString xMLString) {
        if (xMLString != null) {
            if (!xMLString.hasString()) {
                return xMLString.equals(this.str());
            }
            return this.str().equals(xMLString.toString());
        }
        return false;
    }

    public boolean equals(Object object) {
        if (null == object) {
            return false;
        }
        if (object instanceof XNodeSet) {
            return object.equals(this);
        }
        if (object instanceof XNumber) {
            return object.equals(this);
        }
        return this.str().equals(object.toString());
    }

    public boolean startsWith(XMLString xMLString, int n2) {
        int n3 = n2;
        int n4 = this.length();
        int n5 = 0;
        int n6 = xMLString.length();
        if (n2 < 0 || n2 > n4 - n6) {
            return false;
        }
        while (--n6 >= 0) {
            if (this.charAt(n3) != xMLString.charAt(n5)) {
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

    public int hashCode() {
        return this.str().hashCode();
    }

    public int indexOf(int n2) {
        return this.str().indexOf(n2);
    }

    public int indexOf(int n2, int n3) {
        return this.str().indexOf(n2, n3);
    }

    public int indexOf(XMLString xMLString) {
        return this.str().indexOf(xMLString.toString());
    }

    public XMLString substring(int n2) {
        return new XString(this.str().substring(n2));
    }

    public XMLString substring(int n2, int n3) {
        return new XString(this.str().substring(n2, n3));
    }

    public XMLString trim() {
        return new XString(this.str().trim());
    }

    private static boolean isSpace(char c2) {
        return XMLCharacterRecognizer.isWhiteSpace(c2);
    }

    public XMLString fixWhiteSpace(boolean bl, boolean bl2, boolean bl3) {
        int n2;
        int n3;
        int n4 = this /* !! */ .length();
        char[] arrc = new char[n4];
        this /* !! */ .getChars(0, n4, arrc, 0);
        boolean bl4 = false;
        for (n3 = 0; n3 < n4 && !XString.isSpace(arrc[n3]); ++n3) {
        }
        int n5 = n3;
        boolean bl5 = false;
        while (n3 < n4) {
            n2 = arrc[n3];
            if (XString.isSpace((char)n2)) {
                if (!bl5) {
                    if (32 != n2) {
                        bl4 = true;
                    }
                    arrc[n5++] = 32;
                    if (bl3 && n3 != 0) {
                        char c2 = arrc[n3 - 1];
                        if (c2 != '.' && c2 != '!' && c2 != '?') {
                            bl5 = true;
                        }
                    } else {
                        bl5 = true;
                    }
                } else {
                    bl4 = true;
                    bl5 = true;
                }
            } else {
                arrc[n5++] = n2;
                bl5 = false;
            }
            ++n3;
        }
        if (bl2 && 1 <= n5 && ' ' == arrc[n5 - 1]) {
            bl4 = true;
            --n5;
        }
        n2 = 0;
        if (bl && 0 < n5 && ' ' == arrc[0]) {
            bl4 = true;
            ++n2;
        }
        XMLStringFactory xMLStringFactory = XMLStringFactoryImpl.getFactory();
        return bl4 ? xMLStringFactory.newstr(new String(arrc, n2, n5 - n2)) : this /* !! */ ;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        xPathVisitor.visitStringLiteral(expressionOwner, this);
    }
}

