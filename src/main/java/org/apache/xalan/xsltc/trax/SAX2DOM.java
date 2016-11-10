/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.util.Stack;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.xsltc.runtime.Constants;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class SAX2DOM
implements Constants,
ContentHandler,
LexicalHandler {
    private Node _root = null;
    private Document _document = null;
    private Node _nextSibling = null;
    private Stack _nodeStk = new Stack();
    private Vector _namespaceDecls = null;
    private Node _lastSibling = null;

    public SAX2DOM() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this._document = documentBuilderFactory.newDocumentBuilder().newDocument();
        this._root = this._document;
    }

    public SAX2DOM(Node node, Node node2) throws ParserConfigurationException {
        this._root = node;
        if (node instanceof Document) {
            this._document = (Document)node;
        } else if (node != null) {
            this._document = node.getOwnerDocument();
        } else {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this._document = documentBuilderFactory.newDocumentBuilder().newDocument();
            this._root = this._document;
        }
        this._nextSibling = node2;
    }

    public SAX2DOM(Node node) throws ParserConfigurationException {
        this(node, null);
    }

    public Node getDOM() {
        return this._root;
    }

    public void characters(char[] arrc, int n2, int n3) {
        Node node = (Node)this._nodeStk.peek();
        if (node != this._document) {
            String string = new String(arrc, n2, n3);
            if (this._lastSibling != null && this._lastSibling.getNodeType() == 3) {
                ((Text)this._lastSibling).appendData(string);
            } else {
                this._lastSibling = node == this._root && this._nextSibling != null ? node.insertBefore(this._document.createTextNode(string), this._nextSibling) : node.appendChild(this._document.createTextNode(string));
            }
        }
    }

    public void startDocument() {
        this._nodeStk.push(this._root);
    }

    public void endDocument() {
        this._nodeStk.pop();
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) {
        int n2;
        int n3;
        Element element = this._document.createElementNS(string, string3);
        if (this._namespaceDecls != null) {
            n2 = this._namespaceDecls.size();
            for (n3 = 0; n3 < n2; ++n3) {
                String string4;
                if ((string4 = (String)this._namespaceDecls.elementAt(n3++)) == null || string4.equals("")) {
                    element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", (String)this._namespaceDecls.elementAt(n3));
                    continue;
                }
                element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + string4, (String)this._namespaceDecls.elementAt(n3));
            }
            this._namespaceDecls.clear();
        }
        n2 = attributes.getLength();
        for (n3 = 0; n3 < n2; ++n3) {
            if (attributes.getLocalName(n3) == null) {
                element.setAttribute(attributes.getQName(n3), attributes.getValue(n3));
                continue;
            }
            element.setAttributeNS(attributes.getURI(n3), attributes.getQName(n3), attributes.getValue(n3));
        }
        Node node = (Node)this._nodeStk.peek();
        if (node == this._root && this._nextSibling != null) {
            node.insertBefore(element, this._nextSibling);
        } else {
            node.appendChild(element);
        }
        this._nodeStk.push(element);
        this._lastSibling = null;
    }

    public void endElement(String string, String string2, String string3) {
        this._nodeStk.pop();
        this._lastSibling = null;
    }

    public void startPrefixMapping(String string, String string2) {
        if (this._namespaceDecls == null) {
            this._namespaceDecls = new Vector(2);
        }
        this._namespaceDecls.addElement(string);
        this._namespaceDecls.addElement(string2);
    }

    public void endPrefixMapping(String string) {
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) {
    }

    public void processingInstruction(String string, String string2) {
        Node node = (Node)this._nodeStk.peek();
        ProcessingInstruction processingInstruction = this._document.createProcessingInstruction(string, string2);
        if (processingInstruction != null) {
            if (node == this._root && this._nextSibling != null) {
                node.insertBefore(processingInstruction, this._nextSibling);
            } else {
                node.appendChild(processingInstruction);
            }
            this._lastSibling = processingInstruction;
        }
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String string) {
    }

    public void comment(char[] arrc, int n2, int n3) {
        Node node = (Node)this._nodeStk.peek();
        Comment comment = this._document.createComment(new String(arrc, n2, n3));
        if (comment != null) {
            if (node == this._root && this._nextSibling != null) {
                node.insertBefore(comment, this._nextSibling);
            } else {
                node.appendChild(comment);
            }
            this._lastSibling = comment;
        }
    }

    public void startCDATA() {
    }

    public void endCDATA() {
    }

    public void startEntity(String string) {
    }

    public void endDTD() {
    }

    public void endEntity(String string) {
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
    }
}

