/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Vector;
import javax.xml.transform.Transformer;
import org.apache.xml.serializer.ElemContext;
import org.apache.xml.serializer.SerializerBase;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.TransformStateSetter;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

public abstract class ToSAXHandler
extends SerializerBase {
    protected ContentHandler m_saxHandler;
    protected LexicalHandler m_lexHandler;
    private boolean m_shouldGenerateNSAttribute = true;
    protected TransformStateSetter m_state = null;

    public ToSAXHandler() {
    }

    public ToSAXHandler(ContentHandler contentHandler, LexicalHandler lexicalHandler, String string) {
        this.setContentHandler(contentHandler);
        this.setLexHandler(lexicalHandler);
        this.setEncoding(string);
    }

    public ToSAXHandler(ContentHandler contentHandler, String string) {
        this.setContentHandler(contentHandler);
        this.setEncoding(string);
    }

    protected void startDocumentInternal() throws SAXException {
        if (this.m_needToCallStartDocument) {
            super.startDocumentInternal();
            this.m_saxHandler.startDocument();
            this.m_needToCallStartDocument = false;
        }
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
    }

    public void characters(String string) throws SAXException {
        int n2 = string.length();
        if (n2 > this.m_charsBuff.length) {
            this.m_charsBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_charsBuff, 0);
        this.characters(this.m_charsBuff, 0, n2);
    }

    public void comment(String string) throws SAXException {
        this.flushPending();
        if (this.m_lexHandler != null) {
            int n2 = string.length();
            if (n2 > this.m_charsBuff.length) {
                this.m_charsBuff = new char[n2 * 2 + 1];
            }
            string.getChars(0, n2, this.m_charsBuff, 0);
            this.m_lexHandler.comment(this.m_charsBuff, 0, n2);
            if (this.m_tracer != null) {
                super.fireCommentEvent(this.m_charsBuff, 0, n2);
            }
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
    }

    protected void closeStartTag() throws SAXException {
    }

    protected void closeCDATA() throws SAXException {
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (this.m_state != null) {
            this.m_state.resetState(this.getTransformer());
        }
        if (this.m_tracer != null) {
            super.fireStartElem(string3);
        }
    }

    public void setLexHandler(LexicalHandler lexicalHandler) {
        this.m_lexHandler = lexicalHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.m_saxHandler = contentHandler;
        if (this.m_lexHandler == null && contentHandler instanceof LexicalHandler) {
            this.m_lexHandler = (LexicalHandler)((Object)contentHandler);
        }
    }

    public void setCdataSectionElements(Vector vector) {
    }

    public void setShouldOutputNSAttr(boolean bl) {
        this.m_shouldGenerateNSAttribute = bl;
    }

    boolean getShouldOutputNSAttr() {
        return this.m_shouldGenerateNSAttribute;
    }

    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        if (this.m_cdataTagOpen) {
            this.closeCDATA();
            this.m_cdataTagOpen = false;
        }
    }

    public void setTransformState(TransformStateSetter transformStateSetter) {
        this.m_state = transformStateSetter;
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        if (this.m_state != null) {
            this.m_state.resetState(this.getTransformer());
        }
        if (this.m_tracer != null) {
            super.fireStartElem(string3);
        }
    }

    public void startElement(String string) throws SAXException {
        if (this.m_state != null) {
            this.m_state.resetState(this.getTransformer());
        }
        if (this.m_tracer != null) {
            super.fireStartElem(string);
        }
    }

    public void characters(Node node) throws SAXException {
        String string;
        if (this.m_state != null) {
            this.m_state.setCurrentNode(node);
        }
        if ((string = node.getNodeValue()) != null) {
            this.characters(string);
        }
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        super.fatalError(sAXParseException);
        this.m_needToCallStartDocument = false;
        if (this.m_saxHandler instanceof ErrorHandler) {
            ((ErrorHandler)((Object)this.m_saxHandler)).fatalError(sAXParseException);
        }
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        super.error(sAXParseException);
        if (this.m_saxHandler instanceof ErrorHandler) {
            ((ErrorHandler)((Object)this.m_saxHandler)).error(sAXParseException);
        }
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        super.warning(sAXParseException);
        if (this.m_saxHandler instanceof ErrorHandler) {
            ((ErrorHandler)((Object)this.m_saxHandler)).warning(sAXParseException);
        }
    }

    public boolean reset() {
        boolean bl = false;
        if (super.reset()) {
            this.resetToSAXHandler();
            bl = true;
        }
        return bl;
    }

    private void resetToSAXHandler() {
        this.m_lexHandler = null;
        this.m_saxHandler = null;
        this.m_state = null;
        this.m_shouldGenerateNSAttribute = false;
    }
}

