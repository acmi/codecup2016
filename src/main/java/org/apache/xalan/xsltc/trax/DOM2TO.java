/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class DOM2TO
implements Locator,
XMLReader {
    private static final String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";
    private Node _dom;
    private SerializationHandler _handler;

    public DOM2TO(Node node, SerializationHandler serializationHandler) {
        this._dom = node;
        this._handler = serializationHandler;
    }

    public ContentHandler getContentHandler() {
        return null;
    }

    public void setContentHandler(ContentHandler contentHandler) {
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        this.parse(this._dom);
    }

    public void parse() throws IOException, SAXException {
        if (this._dom != null) {
            boolean bl;
            boolean bl2 = bl = this._dom.getNodeType() != 9;
            if (bl) {
                this._handler.startDocument();
                this.parse(this._dom);
                this._handler.endDocument();
            } else {
                this.parse(this._dom);
            }
        }
    }

    private void parse(Node node) throws IOException, SAXException {
        if (node == null) {
            return;
        }
        switch (node.getNodeType()) {
            case 2: 
            case 5: 
            case 6: 
            case 10: 
            case 12: {
                break;
            }
            case 4: {
                this._handler.startCDATA();
                this._handler.characters(node.getNodeValue());
                this._handler.endCDATA();
                break;
            }
            case 8: {
                this._handler.comment(node.getNodeValue());
                break;
            }
            case 9: {
                this._handler.startDocument();
                for (Node node2 = node.getFirstChild(); node2 != null; node2 = node2.getNextSibling()) {
                    this.parse(node2);
                }
                this._handler.endDocument();
                break;
            }
            case 11: {
                for (Node node3 = node.getFirstChild(); node3 != null; node3 = node3.getNextSibling()) {
                    this.parse(node3);
                }
                break;
            }
            case 1: {
                Object object;
                String string;
                int n2;
                String string2;
                String string3 = node.getNodeName();
                this._handler.startElement(null, null, string3);
                NamedNodeMap namedNodeMap = node.getAttributes();
                int n3 = namedNodeMap.getLength();
                for (int i2 = 0; i2 < n3; ++i2) {
                    Node node4 = namedNodeMap.item(i2);
                    object = node4.getNodeName();
                    if (!object.startsWith("xmlns")) continue;
                    string2 = node4.getNodeValue();
                    n2 = object.lastIndexOf(58);
                    string = n2 > 0 ? object.substring(n2 + 1) : "";
                    this._handler.namespaceAfterStartElement(string, string2);
                }
                NamespaceMappings namespaceMappings = new NamespaceMappings();
                for (int i3 = 0; i3 < n3; ++i3) {
                    object = namedNodeMap.item(i3);
                    string2 = object.getNodeName();
                    if (string2.startsWith("xmlns")) continue;
                    String string4 = object.getNamespaceURI();
                    if (string4 != null && !string4.equals("")) {
                        n2 = string2.lastIndexOf(58);
                        String string5 = namespaceMappings.lookupPrefix(string4);
                        if (string5 == null) {
                            string5 = namespaceMappings.generateNextPrefix();
                        }
                        string = n2 > 0 ? string2.substring(0, n2) : string5;
                        this._handler.namespaceAfterStartElement(string, string4);
                        this._handler.addAttribute(string + ":" + string2, object.getNodeValue());
                        continue;
                    }
                    this._handler.addAttribute(string2, object.getNodeValue());
                }
                String string6 = node.getNamespaceURI();
                object = node.getLocalName();
                if (string6 != null) {
                    n2 = string3.lastIndexOf(58);
                    string = n2 > 0 ? string3.substring(0, n2) : "";
                    this._handler.namespaceAfterStartElement(string, string6);
                } else if (string6 == null && object != null) {
                    string = "";
                    this._handler.namespaceAfterStartElement(string, "");
                }
                for (Node node5 = node.getFirstChild(); node5 != null; node5 = node5.getNextSibling()) {
                    this.parse(node5);
                }
                this._handler.endElement(string3);
                break;
            }
            case 7: {
                this._handler.processingInstruction(node.getNodeName(), node.getNodeValue());
                break;
            }
            case 3: {
                this._handler.characters(node.getNodeValue());
            }
        }
    }

    public DTDHandler getDTDHandler() {
        return null;
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    public void setFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public void parse(String string) throws IOException, SAXException {
        throw new IOException("This method is not yet implemented.");
    }

    public void setDTDHandler(DTDHandler dTDHandler) throws NullPointerException {
    }

    public void setEntityResolver(EntityResolver entityResolver) throws NullPointerException {
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public void setErrorHandler(ErrorHandler errorHandler) throws NullPointerException {
    }

    public void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    public int getColumnNumber() {
        return 0;
    }

    public int getLineNumber() {
        return 0;
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return null;
    }

    private String getNodeTypeFromCode(short s2) {
        String string = null;
        switch (s2) {
            case 2: {
                string = "ATTRIBUTE_NODE";
                break;
            }
            case 4: {
                string = "CDATA_SECTION_NODE";
                break;
            }
            case 8: {
                string = "COMMENT_NODE";
                break;
            }
            case 11: {
                string = "DOCUMENT_FRAGMENT_NODE";
                break;
            }
            case 9: {
                string = "DOCUMENT_NODE";
                break;
            }
            case 10: {
                string = "DOCUMENT_TYPE_NODE";
                break;
            }
            case 1: {
                string = "ELEMENT_NODE";
                break;
            }
            case 6: {
                string = "ENTITY_NODE";
                break;
            }
            case 5: {
                string = "ENTITY_REFERENCE_NODE";
                break;
            }
            case 12: {
                string = "NOTATION_NODE";
                break;
            }
            case 7: {
                string = "PROCESSING_INSTRUCTION_NODE";
                break;
            }
            case 3: {
                string = "TEXT_NODE";
            }
        }
        return string;
    }
}

