/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import org.apache.xml.serializer.EncodingInfo;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToStream;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ToTextStream
extends ToStream {
    protected void startDocumentInternal() throws SAXException {
        super.startDocumentInternal();
        this.m_needToCallStartDocument = false;
    }

    public void endDocument() throws SAXException {
        this.flushPending();
        this.flushWriter();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (this.m_tracer != null) {
            super.fireStartElem(string3);
            this.firePseudoAttributes();
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEndElem(string3);
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        this.flushPending();
        try {
            if (this.inTemporaryOutputState()) {
                this.m_writer.write(arrc, n2, n3);
            } else {
                this.writeNormalizedChars(arrc, n2, n3, this.m_lineSepUse);
            }
            if (this.m_tracer != null) {
                super.fireCharEvent(arrc, n2, n3);
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void charactersRaw(char[] arrc, int n2, int n3) throws SAXException {
        try {
            this.writeNormalizedChars(arrc, n2, n3, this.m_lineSepUse);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    void writeNormalizedChars(char[] arrc, int n2, int n3, boolean bl) throws IOException, SAXException {
        String string = this.getEncoding();
        Writer writer = this.m_writer;
        int n4 = n2 + n3;
        int n5 = 10;
        for (int i2 = n2; i2 < n4; ++i2) {
            String string2;
            char c2 = arrc[i2];
            if ('\n' == c2 && bl) {
                writer.write(this.m_lineSep, 0, this.m_lineSepLen);
                continue;
            }
            if (this.m_encodingInfo.isInEncoding(c2)) {
                writer.write(c2);
                continue;
            }
            if (Encodings.isHighUTF16Surrogate(c2)) {
                int n6 = this.writeUTF16Surrogate(c2, arrc, i2, n4);
                if (n6 != 0) {
                    string2 = Integer.toString(n6);
                    String string3 = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[]{string2, string});
                    System.err.println(string3);
                }
                ++i2;
                continue;
            }
            if (string != null) {
                writer.write(38);
                writer.write(35);
                writer.write(Integer.toString(c2));
                writer.write(59);
                String string4 = Integer.toString(c2);
                string2 = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[]{string4, string});
                System.err.println(string2);
                continue;
            }
            writer.write(c2);
        }
    }

    public void cdata(char[] arrc, int n2, int n3) throws SAXException {
        try {
            this.writeNormalizedChars(arrc, n2, n3, this.m_lineSepUse);
            if (this.m_tracer != null) {
                super.fireCDATAEvent(arrc, n2, n3);
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        try {
            this.writeNormalizedChars(arrc, n2, n3, this.m_lineSepUse);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.flushPending();
        if (this.m_tracer != null) {
            super.fireEscapingEvent(string, string2);
        }
    }

    public void comment(String string) throws SAXException {
        int n2 = string.length();
        if (n2 > this.m_charsBuff.length) {
            this.m_charsBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_charsBuff, 0);
        this.comment(this.m_charsBuff, 0, n2);
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        this.flushPending();
        if (this.m_tracer != null) {
            super.fireCommentEvent(arrc, n2, n3);
        }
    }

    public void entityReference(String string) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEntityReference(string);
        }
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5, boolean bl) {
    }

    public void endCDATA() throws SAXException {
    }

    public void endElement(String string) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEndElem(string);
        }
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
        }
        if (this.m_tracer != null) {
            super.fireStartElem(string3);
            this.firePseudoAttributes();
        }
    }

    public void characters(String string) throws SAXException {
        int n2 = string.length();
        if (n2 > this.m_charsBuff.length) {
            this.m_charsBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_charsBuff, 0);
        this.characters(this.m_charsBuff, 0, n2);
    }

    public void addAttribute(String string, String string2) {
    }

    public boolean startPrefixMapping(String string, String string2, boolean bl) throws SAXException {
        return false;
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
    }

    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
    }
}

