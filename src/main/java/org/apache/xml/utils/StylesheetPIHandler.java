/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StylesheetPIHandler
extends DefaultHandler {
    String m_baseID;
    String m_media;
    String m_title;
    String m_charset;
    Vector m_stylesheets = new Vector();
    URIResolver m_uriResolver;

    public void setURIResolver(URIResolver uRIResolver) {
        this.m_uriResolver = uRIResolver;
    }

    public StylesheetPIHandler(String string, String string2, String string3, String string4) {
        this.m_baseID = string;
        this.m_media = string2;
        this.m_title = string3;
        this.m_charset = string4;
    }

    public Source getAssociatedStylesheet() {
        int n2 = this.m_stylesheets.size();
        if (n2 > 0) {
            Source source = (Source)this.m_stylesheets.elementAt(n2 - 1);
            return source;
        }
        return null;
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (string.equals("xml-stylesheet")) {
            String string3 = null;
            String string4 = null;
            String string5 = null;
            String string6 = null;
            String string7 = null;
            boolean bl = false;
            StringTokenizer stringTokenizer = new StringTokenizer(string2, " \t=\n", true);
            boolean bl2 = false;
            Source source = null;
            String string8 = "";
            while (stringTokenizer.hasMoreTokens()) {
                if (!bl2) {
                    string8 = stringTokenizer.nextToken();
                } else {
                    bl2 = false;
                }
                if (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) continue;
                String string9 = string8;
                if (string9.equals("type")) {
                    string8 = stringTokenizer.nextToken();
                    while (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) {
                        string8 = stringTokenizer.nextToken();
                    }
                    string4 = string8.substring(1, string8.length() - 1);
                    continue;
                }
                if (string9.equals("href")) {
                    string8 = stringTokenizer.nextToken();
                    while (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) {
                        string8 = stringTokenizer.nextToken();
                    }
                    string3 = string8;
                    if (stringTokenizer.hasMoreTokens()) {
                        string8 = stringTokenizer.nextToken();
                        while (string8.equals("=") && stringTokenizer.hasMoreTokens()) {
                            string3 = string3 + string8 + stringTokenizer.nextToken();
                            if (!stringTokenizer.hasMoreTokens()) break;
                            string8 = stringTokenizer.nextToken();
                            bl2 = true;
                        }
                    }
                    string3 = string3.substring(1, string3.length() - 1);
                    try {
                        if (this.m_uriResolver != null) {
                            source = this.m_uriResolver.resolve(string3, this.m_baseID);
                            continue;
                        }
                        string3 = SystemIDResolver.getAbsoluteURI(string3, this.m_baseID);
                        source = new SAXSource(new InputSource(string3));
                        continue;
                    }
                    catch (TransformerException transformerException) {
                        throw new SAXException(transformerException);
                    }
                }
                if (string9.equals("title")) {
                    string8 = stringTokenizer.nextToken();
                    while (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) {
                        string8 = stringTokenizer.nextToken();
                    }
                    string5 = string8.substring(1, string8.length() - 1);
                    continue;
                }
                if (string9.equals("media")) {
                    string8 = stringTokenizer.nextToken();
                    while (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) {
                        string8 = stringTokenizer.nextToken();
                    }
                    string6 = string8.substring(1, string8.length() - 1);
                    continue;
                }
                if (string9.equals("charset")) {
                    string8 = stringTokenizer.nextToken();
                    while (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) {
                        string8 = stringTokenizer.nextToken();
                    }
                    string7 = string8.substring(1, string8.length() - 1);
                    continue;
                }
                if (!string9.equals("alternate")) continue;
                string8 = stringTokenizer.nextToken();
                while (stringTokenizer.hasMoreTokens() && (string8.equals(" ") || string8.equals("\t") || string8.equals("="))) {
                    string8 = stringTokenizer.nextToken();
                }
                bl = string8.substring(1, string8.length() - 1).equals("yes");
            }
            if (null != string4 && (string4.equals("text/xsl") || string4.equals("text/xml") || string4.equals("application/xml+xslt")) && null != string3) {
                if (null != this.m_media) {
                    if (null != string6) {
                        if (!string6.equals(this.m_media)) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                if (null != this.m_charset) {
                    if (null != string7) {
                        if (!string7.equals(this.m_charset)) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                if (null != this.m_title) {
                    if (null != string5) {
                        if (!string5.equals(this.m_title)) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                this.m_stylesheets.addElement(source);
            }
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        throw new StopParseException();
    }

    public void setBaseId(String string) {
        this.m_baseID = string;
    }
}

