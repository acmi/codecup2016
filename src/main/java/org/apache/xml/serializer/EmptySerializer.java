/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.apache.xml.serializer.DOMSerializer;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class EmptySerializer
implements SerializationHandler {
    protected static final String ERR = "EmptySerializer method not over-ridden";

    protected void couldThrowIOException() throws IOException {
    }

    protected void couldThrowSAXException() throws SAXException {
    }

    protected void couldThrowSAXException(char[] arrc, int n2, int n3) throws SAXException {
    }

    protected void couldThrowSAXException(String string) throws SAXException {
    }

    protected void couldThrowException() throws Exception {
    }

    void aMethodIsCalled() {
    }

    public ContentHandler asContentHandler() throws IOException {
        this.couldThrowIOException();
        return null;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.aMethodIsCalled();
    }

    public void close() {
        this.aMethodIsCalled();
    }

    public Properties getOutputFormat() {
        this.aMethodIsCalled();
        return null;
    }

    public OutputStream getOutputStream() {
        this.aMethodIsCalled();
        return null;
    }

    public Writer getWriter() {
        this.aMethodIsCalled();
        return null;
    }

    public boolean reset() {
        this.aMethodIsCalled();
        return false;
    }

    public void serialize(Node node) throws IOException {
        this.couldThrowIOException();
    }

    public void setCdataSectionElements(Vector vector) {
        this.aMethodIsCalled();
    }

    public boolean setEscaping(boolean bl) throws SAXException {
        this.couldThrowSAXException();
        return false;
    }

    public void setIndent(boolean bl) {
        this.aMethodIsCalled();
    }

    public void setIndentAmount(int n2) {
        this.aMethodIsCalled();
    }

    public void setOutputFormat(Properties properties) {
        this.aMethodIsCalled();
    }

    public void setOutputStream(OutputStream outputStream) {
        this.aMethodIsCalled();
    }

    public void setVersion(String string) {
        this.aMethodIsCalled();
    }

    public void setWriter(Writer writer) {
        this.aMethodIsCalled();
    }

    public void setTransformer(Transformer transformer) {
        this.aMethodIsCalled();
    }

    public Transformer getTransformer() {
        this.aMethodIsCalled();
        return null;
    }

    public void flushPending() throws SAXException {
        this.couldThrowSAXException();
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5, boolean bl) throws SAXException {
        this.couldThrowSAXException();
    }

    public void addAttributes(Attributes attributes) throws SAXException {
        this.couldThrowSAXException();
    }

    public void addAttribute(String string, String string2) {
        this.aMethodIsCalled();
    }

    public void characters(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void endElement(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void startDocument() throws SAXException {
        this.couldThrowSAXException();
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        this.couldThrowSAXException(string3);
    }

    public void startElement(String string) throws SAXException {
        this.couldThrowSAXException(string);
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
        this.couldThrowSAXException();
    }

    public boolean startPrefixMapping(String string, String string2, boolean bl) throws SAXException {
        this.couldThrowSAXException();
        return false;
    }

    public void entityReference(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public NamespaceMappings getNamespaceMappings() {
        this.aMethodIsCalled();
        return null;
    }

    public String getPrefix(String string) {
        this.aMethodIsCalled();
        return null;
    }

    public String getNamespaceURI(String string, boolean bl) {
        this.aMethodIsCalled();
        return null;
    }

    public String getNamespaceURIFromPrefix(String string) {
        this.aMethodIsCalled();
        return null;
    }

    public void setDocumentLocator(Locator locator) {
        this.aMethodIsCalled();
    }

    public void endDocument() throws SAXException {
        this.couldThrowSAXException();
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this.couldThrowSAXException();
    }

    public void endPrefixMapping(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        this.couldThrowSAXException();
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.couldThrowSAXException();
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        this.couldThrowSAXException(arrc, n2, n3);
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        this.couldThrowSAXException();
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.couldThrowSAXException();
    }

    public void skippedEntity(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void comment(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        this.couldThrowSAXException();
    }

    public void endDTD() throws SAXException {
        this.couldThrowSAXException();
    }

    public void startEntity(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void endEntity(String string) throws SAXException {
        this.couldThrowSAXException();
    }

    public void startCDATA() throws SAXException {
        this.couldThrowSAXException();
    }

    public void endCDATA() throws SAXException {
        this.couldThrowSAXException();
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        this.couldThrowSAXException();
    }

    public String getDoctypePublic() {
        this.aMethodIsCalled();
        return null;
    }

    public String getDoctypeSystem() {
        this.aMethodIsCalled();
        return null;
    }

    public String getEncoding() {
        this.aMethodIsCalled();
        return null;
    }

    public boolean getIndent() {
        this.aMethodIsCalled();
        return false;
    }

    public int getIndentAmount() {
        this.aMethodIsCalled();
        return 0;
    }

    public String getMediaType() {
        this.aMethodIsCalled();
        return null;
    }

    public boolean getOmitXMLDeclaration() {
        this.aMethodIsCalled();
        return false;
    }

    public String getStandalone() {
        this.aMethodIsCalled();
        return null;
    }

    public String getVersion() {
        this.aMethodIsCalled();
        return null;
    }

    public void setCdataSectionElements(Hashtable hashtable) throws Exception {
        this.couldThrowException();
    }

    public void setDoctype(String string, String string2) {
        this.aMethodIsCalled();
    }

    public void setDoctypePublic(String string) {
        this.aMethodIsCalled();
    }

    public void setDoctypeSystem(String string) {
        this.aMethodIsCalled();
    }

    public void setEncoding(String string) {
        this.aMethodIsCalled();
    }

    public void setMediaType(String string) {
        this.aMethodIsCalled();
    }

    public void setOmitXMLDeclaration(boolean bl) {
        this.aMethodIsCalled();
    }

    public void setStandalone(String string) {
        this.aMethodIsCalled();
    }

    public void elementDecl(String string, String string2) throws SAXException {
        this.couldThrowSAXException();
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
        this.couldThrowSAXException();
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
        this.couldThrowSAXException();
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
        this.couldThrowSAXException();
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        this.couldThrowSAXException();
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        this.couldThrowSAXException();
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        this.couldThrowSAXException();
    }

    public DOMSerializer asDOMSerializer() throws IOException {
        this.couldThrowIOException();
        return null;
    }

    public void setNamespaceMappings(NamespaceMappings namespaceMappings) {
        this.aMethodIsCalled();
    }

    public void setSourceLocator(SourceLocator sourceLocator) {
        this.aMethodIsCalled();
    }

    public void addUniqueAttribute(String string, String string2, int n2) throws SAXException {
        this.couldThrowSAXException();
    }

    public void characters(Node node) throws SAXException {
        this.couldThrowSAXException();
    }

    public void addXSLAttribute(String string, String string2, String string3) {
        this.aMethodIsCalled();
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5) throws SAXException {
        this.couldThrowSAXException();
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
        this.couldThrowSAXException();
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        this.couldThrowSAXException();
    }

    public void setDTDEntityExpansion(boolean bl) {
        this.aMethodIsCalled();
    }

    public String getOutputProperty(String string) {
        this.aMethodIsCalled();
        return null;
    }

    public String getOutputPropertyDefault(String string) {
        this.aMethodIsCalled();
        return null;
    }

    public void setOutputProperty(String string, String string2) {
        this.aMethodIsCalled();
    }

    public void setOutputPropertyDefault(String string, String string2) {
        this.aMethodIsCalled();
    }

    public Object asDOM3Serializer() throws IOException {
        this.couldThrowIOException();
        return null;
    }
}

