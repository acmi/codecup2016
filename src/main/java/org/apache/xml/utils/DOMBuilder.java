/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.Stack;
import java.util.Vector;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class DOMBuilder
implements ContentHandler,
LexicalHandler {
    public Document m_doc;
    protected Node m_currentNode = null;
    protected Node m_root = null;
    protected Node m_nextSibling = null;
    public DocumentFragment m_docFrag = null;
    protected Stack m_elemStack = new Stack();
    protected Vector m_prefixMappings = new Vector();
    protected boolean m_inCData = false;

    public DOMBuilder(Document document, Node node) {
        this.m_doc = document;
        this.m_currentNode = this.m_root = node;
        if (node instanceof Element) {
            this.m_elemStack.push(node);
        }
    }

    public DOMBuilder(Document document, DocumentFragment documentFragment) {
        this.m_doc = document;
        this.m_docFrag = documentFragment;
    }

    public void setNextSibling(Node node) {
        this.m_nextSibling = node;
    }

    protected void append(Node node) throws SAXException {
        Node node2 = this.m_currentNode;
        if (null != node2) {
            if (node2 == this.m_root && this.m_nextSibling != null) {
                node2.insertBefore(node, this.m_nextSibling);
            } else {
                node2.appendChild(node);
            }
        } else if (null != this.m_docFrag) {
            if (this.m_nextSibling != null) {
                this.m_docFrag.insertBefore(node, this.m_nextSibling);
            } else {
                this.m_docFrag.appendChild(node);
            }
        } else {
            boolean bl = true;
            short s2 = node.getNodeType();
            if (s2 == 3) {
                String string = node.getNodeValue();
                if (null != string && string.trim().length() > 0) {
                    throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_OUTPUT_TEXT_BEFORE_DOC", null));
                }
                bl = false;
            } else if (s2 == 1 && this.m_doc.getDocumentElement() != null) {
                bl = false;
                throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_HAVE_MORE_THAN_ONE_ROOT", null));
            }
            if (bl) {
                if (this.m_nextSibling != null) {
                    this.m_doc.insertBefore(node, this.m_nextSibling);
                } else {
                    this.m_doc.appendChild(node);
                }
            }
        }
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        Element element = null == string || string.length() == 0 ? this.m_doc.createElementNS(null, string3) : this.m_doc.createElementNS(string, string3);
        this.append(element);
        try {
            String string4;
            String string5;
            int n2;
            int n3 = attributes.getLength();
            if (0 != n3) {
                for (n2 = 0; n2 < n3; ++n2) {
                    if (attributes.getType(n2).equalsIgnoreCase("ID")) {
                        this.setIDAttribute(attributes.getValue(n2), element);
                    }
                    if ("".equals(string4 = attributes.getURI(n2))) {
                        string4 = null;
                    }
                    if ((string5 = attributes.getQName(n2)).startsWith("xmlns:") || string5.equals("xmlns")) {
                        string4 = "http://www.w3.org/2000/xmlns/";
                    }
                    element.setAttributeNS(string4, string5, attributes.getValue(n2));
                }
            }
            n2 = this.m_prefixMappings.size();
            for (int i2 = 0; i2 < n2; i2 += 2) {
                string4 = (String)this.m_prefixMappings.elementAt(i2);
                if (string4 == null) continue;
                string5 = (String)this.m_prefixMappings.elementAt(i2 + 1);
                element.setAttributeNS("http://www.w3.org/2000/xmlns/", string4, string5);
            }
            this.m_prefixMappings.clear();
            this.m_elemStack.push(element);
            this.m_currentNode = element;
        }
        catch (Exception exception) {
            throw new SAXException(exception);
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.m_elemStack.pop();
        this.m_currentNode = this.m_elemStack.isEmpty() ? null : (Node)this.m_elemStack.peek();
    }

    public void setIDAttribute(String string, Element element) {
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        Node node;
        if (this.isOutsideDocElem() && XMLCharacterRecognizer.isWhiteSpace(arrc, n2, n3)) {
            return;
        }
        if (this.m_inCData) {
            this.cdata(arrc, n2, n3);
            return;
        }
        String string = new String(arrc, n2, n3);
        Node node2 = node = this.m_currentNode != null ? this.m_currentNode.getLastChild() : null;
        if (node != null && node.getNodeType() == 3) {
            ((Text)node).appendData(string);
        } else {
            Text text = this.m_doc.createTextNode(string);
            this.append(text);
        }
    }

    public void startEntity(String string) throws SAXException {
    }

    public void endEntity(String string) throws SAXException {
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (this.isOutsideDocElem()) {
            return;
        }
        String string = new String(arrc, n2, n3);
        this.append(this.m_doc.createTextNode(string));
    }

    private boolean isOutsideDocElem() {
        return null == this.m_docFrag && this.m_elemStack.size() == 0 && (null == this.m_currentNode || this.m_currentNode.getNodeType() == 9);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.append(this.m_doc.createProcessingInstruction(string, string2));
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        this.append(this.m_doc.createComment(new String(arrc, n2, n3)));
    }

    public void startCDATA() throws SAXException {
        this.m_inCData = true;
        this.append(this.m_doc.createCDATASection(""));
    }

    public void endCDATA() throws SAXException {
        this.m_inCData = false;
    }

    public void cdata(char[] arrc, int n2, int n3) throws SAXException {
        if (this.isOutsideDocElem() && XMLCharacterRecognizer.isWhiteSpace(arrc, n2, n3)) {
            return;
        }
        String string = new String(arrc, n2, n3);
        CDATASection cDATASection = (CDATASection)this.m_currentNode.getLastChild();
        cDATASection.appendData(string);
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        string = null == string || string.length() == 0 ? "xmlns" : "xmlns:" + string;
        this.m_prefixMappings.addElement(string);
        this.m_prefixMappings.addElement(string2);
    }

    public void endPrefixMapping(String string) throws SAXException {
    }

    public void skippedEntity(String string) throws SAXException {
    }
}

