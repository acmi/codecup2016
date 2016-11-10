/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.CharInfo;
import org.apache.xml.serializer.ElemContext;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToStream;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.SAXException;

public class ToXMLStream
extends ToStream {
    private CharInfo m_xmlcharInfo;

    public ToXMLStream() {
        this.m_charInfo = this.m_xmlcharInfo = CharInfo.getCharInfo(CharInfo.XML_ENTITIES_RESOURCE, "xml");
        this.initCDATA();
        this.m_prefixMap = new NamespaceMappings();
    }

    public void startDocumentInternal() throws SAXException {
        if (this.m_needToCallStartDocument) {
            super.startDocumentInternal();
            this.m_needToCallStartDocument = false;
            if (this.m_inEntityRef) {
                return;
            }
            this.m_needToOutputDocTypeDecl = true;
            this.m_startNewLine = false;
            String string = this.getXMLVersion();
            if (!this.getOmitXMLDeclaration()) {
                String string2 = Encodings.getMimeEncoding(this.getEncoding());
                String string3 = this.m_standaloneWasSpecified ? " standalone=\"" + this.getStandalone() + "\"" : "";
                try {
                    Writer writer = this.m_writer;
                    writer.write("<?xml version=\"");
                    writer.write(string);
                    writer.write("\" encoding=\"");
                    writer.write(string2);
                    writer.write(34);
                    writer.write(string3);
                    writer.write("?>");
                    if (this.m_doIndent && (this.m_standaloneWasSpecified || this.getDoctypePublic() != null || this.getDoctypeSystem() != null)) {
                        writer.write(this.m_lineSep, 0, this.m_lineSepLen);
                    }
                }
                catch (IOException iOException) {
                    throw new SAXException(iOException);
                }
            }
        }
    }

    public void endDocument() throws SAXException {
        this.flushPending();
        if (this.m_doIndent && !this.m_isprevtext) {
            try {
                this.outputLineSep();
            }
            catch (IOException iOException) {
                throw new SAXException(iOException);
            }
        }
        this.flushWriter();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        this.flushPending();
        if (string.equals("javax.xml.transform.disable-output-escaping")) {
            this.startNonEscaping();
        } else if (string.equals("javax.xml.transform.enable-output-escaping")) {
            this.endNonEscaping();
        } else {
            try {
                int n2;
                if (this.m_elemContext.m_startTagOpen) {
                    this.closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                } else if (this.m_needToCallStartDocument) {
                    this.startDocumentInternal();
                }
                if (this.shouldIndent()) {
                    this.indent();
                }
                Writer writer = this.m_writer;
                writer.write("<?");
                writer.write(string);
                if (string2.length() > 0 && !Character.isSpaceChar(string2.charAt(0))) {
                    writer.write(32);
                }
                if ((n2 = string2.indexOf("?>")) >= 0) {
                    if (n2 > 0) {
                        writer.write(string2.substring(0, n2));
                    }
                    writer.write("? >");
                    if (n2 + 2 < string2.length()) {
                        writer.write(string2.substring(n2 + 2));
                    }
                } else {
                    writer.write(string2);
                }
                writer.write(63);
                writer.write(62);
                this.m_startNewLine = true;
            }
            catch (IOException iOException) {
                throw new SAXException(iOException);
            }
        }
        if (this.m_tracer != null) {
            super.fireEscapingEvent(string, string2);
        }
    }

    public void entityReference(String string) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        try {
            if (this.shouldIndent()) {
                this.indent();
            }
            Writer writer = this.m_writer;
            writer.write(38);
            writer.write(string);
            writer.write(59);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
        if (this.m_tracer != null) {
            super.fireEntityReference(string);
        }
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5, boolean bl) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            String string6;
            boolean bl2 = this.addAttributeAlways(string, string2, string3, string4, string5, bl);
            if (bl2 && !bl && !string3.startsWith("xmlns") && (string6 = this.ensureAttributesNamespaceIsDeclared(string, string2, string3)) != null && string3 != null && !string3.startsWith(string6)) {
                string3 = string6 + ":" + string2;
            }
            this.addAttributeAlways(string, string2, string3, string4, string5, bl);
        } else {
            String string7 = Utils.messages.createMessage("ER_ILLEGAL_ATTRIBUTE_POSITION", new Object[]{string2});
            try {
                Transformer transformer = super.getTransformer();
                ErrorListener errorListener = transformer.getErrorListener();
                if (null != errorListener && this.m_sourceLocator != null) {
                    errorListener.warning(new TransformerException(string7, this.m_sourceLocator));
                } else {
                    System.out.println(string7);
                }
            }
            catch (TransformerException transformerException) {
                SAXException sAXException = new SAXException(transformerException);
                throw sAXException;
            }
        }
    }

    public void endElement(String string) throws SAXException {
        this.endElement(null, null, string);
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
        String string3;
        if (this.m_elemContext.m_elementURI == null && (string3 = ToXMLStream.getPrefixPart(this.m_elemContext.m_elementName)) == null && "".equals(string)) {
            this.m_elemContext.m_elementURI = string2;
        }
        this.startPrefixMapping(string, string2, false);
    }

    public boolean reset() {
        boolean bl = false;
        if (super.reset()) {
            bl = true;
        }
        return bl;
    }

    private String getXMLVersion() {
        String string = this.getVersion();
        if (string == null || string.equals("1.0")) {
            string = "1.0";
        } else if (string.equals("1.1")) {
            string = "1.1";
        } else {
            String string2 = Utils.messages.createMessage("ER_XML_VERSION_NOT_SUPPORTED", new Object[]{string});
            try {
                Transformer transformer = super.getTransformer();
                ErrorListener errorListener = transformer.getErrorListener();
                if (null != errorListener && this.m_sourceLocator != null) {
                    errorListener.warning(new TransformerException(string2, this.m_sourceLocator));
                } else {
                    System.out.println(string2);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            string = "1.0";
        }
        return string;
    }
}

