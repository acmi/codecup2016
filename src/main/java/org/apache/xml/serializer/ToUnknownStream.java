/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.apache.xml.serializer.AttributesImplSerializer;
import org.apache.xml.serializer.DOMSerializer;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerBase;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToXMLStream;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public final class ToUnknownStream
extends SerializerBase {
    private SerializationHandler m_handler = new ToXMLStream();
    private boolean m_wrapped_handler_not_initialized = false;
    private String m_firstElementPrefix;
    private String m_firstElementName;
    private String m_firstElementURI;
    private String m_firstElementLocalName = null;
    private boolean m_firstTagNotEmitted = true;
    private Vector m_namespaceURI = null;
    private Vector m_namespacePrefix = null;
    private boolean m_needToCallStartDocument = false;
    private boolean m_setVersion_called = false;
    private boolean m_setDoctypeSystem_called = false;
    private boolean m_setDoctypePublic_called = false;
    private boolean m_setMediaType_called = false;

    public ContentHandler asContentHandler() throws IOException {
        return this;
    }

    public void close() {
        this.m_handler.close();
    }

    public Properties getOutputFormat() {
        return this.m_handler.getOutputFormat();
    }

    public OutputStream getOutputStream() {
        return this.m_handler.getOutputStream();
    }

    public Writer getWriter() {
        return this.m_handler.getWriter();
    }

    public boolean reset() {
        return this.m_handler.reset();
    }

    public void serialize(Node node) throws IOException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.serialize(node);
    }

    public boolean setEscaping(boolean bl) throws SAXException {
        return this.m_handler.setEscaping(bl);
    }

    public void setOutputFormat(Properties properties) {
        this.m_handler.setOutputFormat(properties);
    }

    public void setOutputStream(OutputStream outputStream) {
        this.m_handler.setOutputStream(outputStream);
    }

    public void setWriter(Writer writer) {
        this.m_handler.setWriter(writer);
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5, boolean bl) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.addAttribute(string, string2, string3, string4, string5, bl);
    }

    public void addAttribute(String string, String string2) {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.addAttribute(string, string2);
    }

    public void characters(String string) throws SAXException {
        int n2 = string.length();
        if (n2 > this.m_charsBuff.length) {
            this.m_charsBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_charsBuff, 0);
        this.characters(this.m_charsBuff, 0, n2);
    }

    public void endElement(String string) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.endElement(string);
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this.startPrefixMapping(string, string2, true);
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
        String string3;
        if (this.m_firstTagNotEmitted && this.m_firstElementURI == null && this.m_firstElementName != null && (string3 = ToUnknownStream.getPrefixPart(this.m_firstElementName)) == null && "".equals(string)) {
            this.m_firstElementURI = string2;
        }
        this.startPrefixMapping(string, string2, false);
    }

    public boolean startPrefixMapping(String string, String string2, boolean bl) throws SAXException {
        boolean bl2 = false;
        if (this.m_firstTagNotEmitted) {
            if (this.m_firstElementName != null && bl) {
                this.flush();
                bl2 = this.m_handler.startPrefixMapping(string, string2, bl);
            } else {
                if (this.m_namespacePrefix == null) {
                    this.m_namespacePrefix = new Vector();
                    this.m_namespaceURI = new Vector();
                }
                this.m_namespacePrefix.addElement(string);
                this.m_namespaceURI.addElement(string2);
                if (this.m_firstElementURI == null && string.equals(this.m_firstElementPrefix)) {
                    this.m_firstElementURI = string2;
                }
            }
        } else {
            bl2 = this.m_handler.startPrefixMapping(string, string2, bl);
        }
        return bl2;
    }

    public void setVersion(String string) {
        this.m_handler.setVersion(string);
        this.m_setVersion_called = true;
    }

    public void startDocument() throws SAXException {
        this.m_needToCallStartDocument = true;
    }

    public void startElement(String string) throws SAXException {
        this.startElement(null, null, string, null);
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        this.startElement(string, string2, string3, null);
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            if (this.m_firstElementName != null) {
                this.flush();
                this.m_handler.startElement(string, string2, string3, attributes);
            } else {
                this.m_wrapped_handler_not_initialized = true;
                this.m_firstElementName = string3;
                this.m_firstElementPrefix = this.getPrefixPartUnknown(string3);
                this.m_firstElementURI = string;
                this.m_firstElementLocalName = string2;
                if (this.m_tracer != null) {
                    this.firePseudoElement(string3);
                }
                if (attributes != null) {
                    super.addAttributes(attributes);
                }
                if (attributes != null) {
                    this.flush();
                }
            }
        } else {
            this.m_handler.startElement(string, string2, string3, attributes);
        }
    }

    public void comment(String string) throws SAXException {
        if (this.m_firstTagNotEmitted && this.m_firstElementName != null) {
            this.emitFirstTag();
        } else if (this.m_needToCallStartDocument) {
            this.m_handler.startDocument();
            this.m_needToCallStartDocument = false;
        }
        this.m_handler.comment(string);
    }

    public String getDoctypePublic() {
        return this.m_handler.getDoctypePublic();
    }

    public String getDoctypeSystem() {
        return this.m_handler.getDoctypeSystem();
    }

    public String getEncoding() {
        return this.m_handler.getEncoding();
    }

    public String getMediaType() {
        return this.m_handler.getMediaType();
    }

    public boolean getOmitXMLDeclaration() {
        return this.m_handler.getOmitXMLDeclaration();
    }

    public String getStandalone() {
        return this.m_handler.getStandalone();
    }

    public String getVersion() {
        return this.m_handler.getVersion();
    }

    public void setDoctype(String string, String string2) {
        this.m_handler.setDoctypePublic(string2);
        this.m_handler.setDoctypeSystem(string);
    }

    public void setDoctypePublic(String string) {
        this.m_handler.setDoctypePublic(string);
        this.m_setDoctypePublic_called = true;
    }

    public void setDoctypeSystem(String string) {
        this.m_handler.setDoctypeSystem(string);
        this.m_setDoctypeSystem_called = true;
    }

    public void setEncoding(String string) {
        this.m_handler.setEncoding(string);
    }

    public void setIndent(boolean bl) {
        this.m_handler.setIndent(bl);
    }

    public void setIndentAmount(int n2) {
        this.m_handler.setIndentAmount(n2);
    }

    public void setMediaType(String string) {
        this.m_handler.setMediaType(string);
        this.m_setMediaType_called = true;
    }

    public void setOmitXMLDeclaration(boolean bl) {
        this.m_handler.setOmitXMLDeclaration(bl);
    }

    public void setStandalone(String string) {
        this.m_handler.setStandalone(string);
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
        this.m_handler.attributeDecl(string, string2, string3, string4, string5);
    }

    public void elementDecl(String string, String string2) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.emitFirstTag();
        }
        this.m_handler.elementDecl(string, string2);
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.externalEntityDecl(string, string2, string3);
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.internalEntityDecl(string, string2);
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.characters(arrc, n2, n3);
    }

    public void endDocument() throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.endDocument();
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
            if (string == null && this.m_firstElementURI != null) {
                string = this.m_firstElementURI;
            }
            if (string2 == null && this.m_firstElementLocalName != null) {
                string2 = this.m_firstElementLocalName;
            }
        }
        this.m_handler.endElement(string, string2, string3);
    }

    public void endPrefixMapping(String string) throws SAXException {
        this.m_handler.endPrefixMapping(string);
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.ignorableWhitespace(arrc, n2, n3);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.processingInstruction(string, string2);
    }

    public void setDocumentLocator(Locator locator) {
        this.m_handler.setDocumentLocator(locator);
    }

    public void skippedEntity(String string) throws SAXException {
        this.m_handler.skippedEntity(string);
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.flush();
        }
        this.m_handler.comment(arrc, n2, n3);
    }

    public void endCDATA() throws SAXException {
        this.m_handler.endCDATA();
    }

    public void endDTD() throws SAXException {
        this.m_handler.endDTD();
    }

    public void endEntity(String string) throws SAXException {
        if (this.m_firstTagNotEmitted) {
            this.emitFirstTag();
        }
        this.m_handler.endEntity(string);
    }

    public void startCDATA() throws SAXException {
        this.m_handler.startCDATA();
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        this.m_handler.startDTD(string, string2, string3);
    }

    public void startEntity(String string) throws SAXException {
        this.m_handler.startEntity(string);
    }

    private void initStreamOutput() throws SAXException {
        boolean bl = this.isFirstElemHTML();
        if (bl) {
            SerializationHandler serializationHandler = this.m_handler;
            Properties properties = OutputPropertiesFactory.getDefaultMethodProperties("html");
            Serializer serializer = SerializerFactory.getSerializer(properties);
            this.m_handler = (SerializationHandler)serializer;
            Writer writer = serializationHandler.getWriter();
            if (null != writer) {
                this.m_handler.setWriter(writer);
            } else {
                OutputStream outputStream = serializationHandler.getOutputStream();
                if (null != outputStream) {
                    this.m_handler.setOutputStream(outputStream);
                }
            }
            this.m_handler.setVersion(serializationHandler.getVersion());
            this.m_handler.setDoctypeSystem(serializationHandler.getDoctypeSystem());
            this.m_handler.setDoctypePublic(serializationHandler.getDoctypePublic());
            this.m_handler.setMediaType(serializationHandler.getMediaType());
            this.m_handler.setTransformer(serializationHandler.getTransformer());
        }
        if (this.m_needToCallStartDocument) {
            this.m_handler.startDocument();
            this.m_needToCallStartDocument = false;
        }
        this.m_wrapped_handler_not_initialized = false;
    }

    private void emitFirstTag() throws SAXException {
        if (this.m_firstElementName != null) {
            if (this.m_wrapped_handler_not_initialized) {
                this.initStreamOutput();
                this.m_wrapped_handler_not_initialized = false;
            }
            this.m_handler.startElement(this.m_firstElementURI, null, this.m_firstElementName, this.m_attributes);
            this.m_attributes = null;
            if (this.m_namespacePrefix != null) {
                int n2 = this.m_namespacePrefix.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    String string = (String)this.m_namespacePrefix.elementAt(i2);
                    String string2 = (String)this.m_namespaceURI.elementAt(i2);
                    this.m_handler.startPrefixMapping(string, string2, false);
                }
                this.m_namespacePrefix = null;
                this.m_namespaceURI = null;
            }
            this.m_firstTagNotEmitted = false;
        }
    }

    private String getLocalNameUnknown(String string) {
        int n2 = string.lastIndexOf(58);
        if (n2 >= 0) {
            string = string.substring(n2 + 1);
        }
        if ((n2 = string.lastIndexOf(64)) >= 0) {
            string = string.substring(n2 + 1);
        }
        return string;
    }

    private String getPrefixPartUnknown(String string) {
        int n2 = string.indexOf(58);
        return n2 > 0 ? string.substring(0, n2) : "";
    }

    private boolean isFirstElemHTML() {
        boolean bl = this.getLocalNameUnknown(this.m_firstElementName).equalsIgnoreCase("html");
        if (bl && this.m_firstElementURI != null && !"".equals(this.m_firstElementURI)) {
            bl = false;
        }
        if (bl && this.m_namespacePrefix != null) {
            int n2 = this.m_namespacePrefix.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = (String)this.m_namespacePrefix.elementAt(i2);
                String string2 = (String)this.m_namespaceURI.elementAt(i2);
                if (this.m_firstElementPrefix == null || !this.m_firstElementPrefix.equals(string) || "".equals(string2)) continue;
                bl = false;
                break;
            }
        }
        return bl;
    }

    public DOMSerializer asDOMSerializer() throws IOException {
        return this.m_handler.asDOMSerializer();
    }

    public void setCdataSectionElements(Vector vector) {
        this.m_handler.setCdataSectionElements(vector);
    }

    public void addAttributes(Attributes attributes) throws SAXException {
        this.m_handler.addAttributes(attributes);
    }

    public NamespaceMappings getNamespaceMappings() {
        NamespaceMappings namespaceMappings = null;
        if (this.m_handler != null) {
            namespaceMappings = this.m_handler.getNamespaceMappings();
        }
        return namespaceMappings;
    }

    public void flushPending() throws SAXException {
        this.flush();
        this.m_handler.flushPending();
    }

    private void flush() {
        try {
            if (this.m_firstTagNotEmitted) {
                this.emitFirstTag();
            }
            if (this.m_needToCallStartDocument) {
                this.m_handler.startDocument();
                this.m_needToCallStartDocument = false;
            }
        }
        catch (SAXException sAXException) {
            throw new RuntimeException(sAXException.toString());
        }
    }

    public String getPrefix(String string) {
        return this.m_handler.getPrefix(string);
    }

    public void entityReference(String string) throws SAXException {
        this.m_handler.entityReference(string);
    }

    public String getNamespaceURI(String string, boolean bl) {
        return this.m_handler.getNamespaceURI(string, bl);
    }

    public String getNamespaceURIFromPrefix(String string) {
        return this.m_handler.getNamespaceURIFromPrefix(string);
    }

    public void setTransformer(Transformer transformer) {
        this.m_handler.setTransformer(transformer);
        this.m_tracer = transformer instanceof SerializerTrace && ((SerializerTrace)((Object)transformer)).hasTraceListeners() ? (SerializerTrace)((Object)transformer) : null;
    }

    public Transformer getTransformer() {
        return this.m_handler.getTransformer();
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.m_handler.setContentHandler(contentHandler);
    }

    public void setSourceLocator(SourceLocator sourceLocator) {
        this.m_handler.setSourceLocator(sourceLocator);
    }

    protected void firePseudoElement(String string) {
        if (this.m_tracer != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append('<');
            stringBuffer.append(string);
            char[] arrc = stringBuffer.toString().toCharArray();
            this.m_tracer.fireGenerateEvent(11, arrc, 0, arrc.length);
        }
    }
}

