/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.apache.xml.serializer.AttributesImplSerializer;
import org.apache.xml.serializer.ElemContext;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToSAXHandler;
import org.apache.xml.serializer.TransformStateSetter;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public final class ToXMLSAXHandler
extends ToSAXHandler {
    protected boolean m_escapeSetting = true;

    public ToXMLSAXHandler() {
        this.m_prefixMap = new NamespaceMappings();
        this.initCDATA();
    }

    public Properties getOutputFormat() {
        return null;
    }

    public OutputStream getOutputStream() {
        return null;
    }

    public Writer getWriter() {
        return null;
    }

    public void serialize(Node node) throws IOException {
    }

    public boolean setEscaping(boolean bl) throws SAXException {
        boolean bl2 = this.m_escapeSetting;
        this.m_escapeSetting = bl;
        if (bl) {
            this.processingInstruction("javax.xml.transform.enable-output-escaping", "");
        } else {
            this.processingInstruction("javax.xml.transform.disable-output-escaping", "");
        }
        return bl2;
    }

    public void setOutputFormat(Properties properties) {
    }

    public void setOutputStream(OutputStream outputStream) {
    }

    public void setWriter(Writer writer) {
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
    }

    public void elementDecl(String string, String string2) throws SAXException {
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
    }

    public void endDocument() throws SAXException {
        this.flushPending();
        this.m_saxHandler.endDocument();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    protected void closeStartTag() throws SAXException {
        this.m_elemContext.m_startTagOpen = false;
        String string = ToXMLSAXHandler.getLocalName(this.m_elemContext.m_elementName);
        String string2 = this.getNamespaceURI(this.m_elemContext.m_elementName, true);
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
        }
        this.m_saxHandler.startElement(string2, string, this.m_elemContext.m_elementName, this.m_attributes);
        this.m_attributes.clear();
        if (this.m_state != null) {
            this.m_state.setCurrentNode(null);
        }
    }

    public void closeCDATA() throws SAXException {
        if (this.m_lexHandler != null && this.m_cdataTagOpen) {
            this.m_lexHandler.endCDATA();
        }
        this.m_cdataTagOpen = false;
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.flushPending();
        if (string == null) {
            string = this.m_elemContext.m_elementURI != null ? this.m_elemContext.m_elementURI : this.getNamespaceURI(string3, true);
        }
        if (string2 == null) {
            string2 = this.m_elemContext.m_elementLocalName != null ? this.m_elemContext.m_elementLocalName : ToXMLSAXHandler.getLocalName(string3);
        }
        this.m_saxHandler.endElement(string, string2, string3);
        if (this.m_tracer != null) {
            super.fireEndElem(string3);
        }
        this.m_prefixMap.popNamespaces(this.m_elemContext.m_currentElemDepth, this.m_saxHandler);
        this.m_elemContext = this.m_elemContext.m_prev;
    }

    public void endPrefixMapping(String string) throws SAXException {
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        this.m_saxHandler.ignorableWhitespace(arrc, n2, n3);
    }

    public void setDocumentLocator(Locator locator) {
        this.m_saxHandler.setDocumentLocator(locator);
    }

    public void skippedEntity(String string) throws SAXException {
        this.m_saxHandler.skippedEntity(string);
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this.startPrefixMapping(string, string2, true);
    }

    public boolean startPrefixMapping(String string, String string2, boolean bl) throws SAXException {
        int n2;
        if (bl) {
            this.flushPending();
            n2 = this.m_elemContext.m_currentElemDepth + 1;
        } else {
            n2 = this.m_elemContext.m_currentElemDepth;
        }
        boolean bl2 = this.m_prefixMap.pushNamespace(string, string2, n2);
        if (bl2) {
            this.m_saxHandler.startPrefixMapping(string, string2);
            if (this.getShouldOutputNSAttr()) {
                if ("".equals(string)) {
                    String string3 = "xmlns";
                    this.addAttributeAlways("http://www.w3.org/2000/xmlns/", string3, string3, "CDATA", string2, false);
                } else if (!"".equals(string2)) {
                    String string4 = "xmlns:" + string;
                    this.addAttributeAlways("http://www.w3.org/2000/xmlns/", string, string4, "CDATA", string2, false);
                }
            }
        }
        return bl2;
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        this.flushPending();
        if (this.m_lexHandler != null) {
            this.m_lexHandler.comment(arrc, n2, n3);
        }
        if (this.m_tracer != null) {
            super.fireCommentEvent(arrc, n2, n3);
        }
    }

    public void endCDATA() throws SAXException {
    }

    public void endDTD() throws SAXException {
        if (this.m_lexHandler != null) {
            this.m_lexHandler.endDTD();
        }
    }

    public void startEntity(String string) throws SAXException {
        if (this.m_lexHandler != null) {
            this.m_lexHandler.startEntity(string);
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

    public ToXMLSAXHandler(ContentHandler contentHandler, String string) {
        super(contentHandler, string);
        this.initCDATA();
        this.m_prefixMap = new NamespaceMappings();
    }

    public ToXMLSAXHandler(ContentHandler contentHandler, LexicalHandler lexicalHandler, String string) {
        super(contentHandler, lexicalHandler, string);
        this.initCDATA();
        this.m_prefixMap = new NamespaceMappings();
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        this.startElement(string, string2, string3, null);
    }

    public void startElement(String string) throws SAXException {
        this.startElement(null, null, string, null);
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        if (this.m_elemContext.m_isCdataSection && !this.m_cdataTagOpen && this.m_lexHandler != null) {
            this.m_lexHandler.startCDATA();
            this.m_cdataTagOpen = true;
        }
        this.m_saxHandler.characters(arrc, n2, n3);
        if (this.m_tracer != null) {
            this.fireCharEvent(arrc, n2, n3);
        }
    }

    public void endElement(String string) throws SAXException {
        this.endElement(null, null, string);
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
        this.startPrefixMapping(string, string2, false);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.flushPending();
        this.m_saxHandler.processingInstruction(string, string2);
        if (this.m_tracer != null) {
            super.fireEscapingEvent(string, string2);
        }
    }

    public void startCDATA() throws SAXException {
        if (!this.m_cdataTagOpen) {
            this.flushPending();
            if (this.m_lexHandler != null) {
                this.m_lexHandler.startCDATA();
                this.m_cdataTagOpen = true;
            }
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        this.flushPending();
        super.startElement(string, string2, string3, attributes);
        if (this.m_needToOutputDocTypeDecl) {
            String string4 = this.getDoctypeSystem();
            if (string4 != null && this.m_lexHandler != null) {
                String string5 = this.getDoctypePublic();
                if (string4 != null) {
                    this.m_lexHandler.startDTD(string3, string5, string4);
                }
            }
            this.m_needToOutputDocTypeDecl = false;
        }
        this.m_elemContext = this.m_elemContext.push(string, string2, string3);
        if (string != null) {
            this.ensurePrefixIsDeclared(string, string3);
        }
        if (attributes != null) {
            this.addAttributes(attributes);
        }
        this.m_elemContext.m_isCdataSection = this.isCdataSection();
    }

    private void ensurePrefixIsDeclared(String string, String string2) throws SAXException {
        if (string != null && string.length() > 0) {
            String string3;
            String string4;
            int n2 = string2.indexOf(":");
            boolean bl = n2 < 0;
            String string5 = string4 = bl ? "" : string2.substring(0, n2);
            if (!(null == string4 || null != (string3 = this.m_prefixMap.lookupNamespace(string4)) && string3.equals(string))) {
                this.startPrefixMapping(string4, string, false);
                if (this.getShouldOutputNSAttr()) {
                    this.addAttributeAlways("http://www.w3.org/2000/xmlns/", bl ? "xmlns" : string4, bl ? "xmlns" : "xmlns:" + string4, "CDATA", string, false);
                }
            }
        }
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5, boolean bl) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            this.ensurePrefixIsDeclared(string, string3);
            this.addAttributeAlways(string, string2, string3, string4, string5, false);
        }
    }

    public boolean reset() {
        boolean bl = false;
        if (super.reset()) {
            this.resetToXMLSAXHandler();
            bl = true;
        }
        return bl;
    }

    private void resetToXMLSAXHandler() {
        this.m_escapeSetting = true;
    }
}

