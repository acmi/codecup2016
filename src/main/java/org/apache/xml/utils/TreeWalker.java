/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.File;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.apache.xml.utils.AttList;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.DOMHelper;
import org.apache.xml.utils.NodeConsumer;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;

public class TreeWalker {
    private ContentHandler m_contentHandler = null;
    protected DOMHelper m_dh;
    private LocatorImpl m_locator = new LocatorImpl();
    boolean nextIsRaw = false;

    public ContentHandler getContentHandler() {
        return this.m_contentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.m_contentHandler = contentHandler;
    }

    public TreeWalker(ContentHandler contentHandler, DOMHelper dOMHelper, String string) {
        this.m_contentHandler = contentHandler;
        this.m_contentHandler.setDocumentLocator(this.m_locator);
        if (string != null) {
            this.m_locator.setSystemId(string);
        } else {
            try {
                this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
        }
        this.m_dh = dOMHelper;
    }

    public TreeWalker(ContentHandler contentHandler) {
        this.m_contentHandler = contentHandler;
        if (this.m_contentHandler != null) {
            this.m_contentHandler.setDocumentLocator(this.m_locator);
        }
        try {
            this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        this.m_dh = new DOM2Helper();
    }

    public void traverse(Node node) throws SAXException {
        this.m_contentHandler.startDocument();
        this.traverseFragment(node);
        this.m_contentHandler.endDocument();
    }

    public void traverseFragment(Node node) throws SAXException {
        Node node2 = node;
        while (null != node) {
            this.startNode(node);
            Node node3 = node.getFirstChild();
            while (null == node3) {
                this.endNode(node);
                if (node2.equals(node)) break;
                node3 = node.getNextSibling();
                if (null != node3 || null != (node = node.getParentNode()) && !node2.equals(node)) continue;
                if (null != node) {
                    this.endNode(node);
                }
                node3 = null;
                break;
            }
            node = node3;
        }
    }

    private final void dispatachChars(Node node) throws SAXException {
        if (this.m_contentHandler instanceof DOM2DTM.CharacterNodeHandler) {
            ((DOM2DTM.CharacterNodeHandler)((Object)this.m_contentHandler)).characters(node);
        } else {
            String string = ((Text)node).getData();
            this.m_contentHandler.characters(string.toCharArray(), 0, string.length());
        }
    }

    protected void startNode(Node node) throws SAXException {
        Object object;
        if (this.m_contentHandler instanceof NodeConsumer) {
            ((NodeConsumer)((Object)this.m_contentHandler)).setOriginatingNode(node);
        }
        if (node instanceof Locator) {
            object = (Locator)((Object)node);
            this.m_locator.setColumnNumber(object.getColumnNumber());
            this.m_locator.setLineNumber(object.getLineNumber());
            this.m_locator.setPublicId(object.getPublicId());
            this.m_locator.setSystemId(object.getSystemId());
        } else {
            this.m_locator.setColumnNumber(0);
            this.m_locator.setLineNumber(0);
        }
        switch (node.getNodeType()) {
            case 8: {
                object = ((Comment)node).getData();
                if (!(this.m_contentHandler instanceof LexicalHandler)) break;
                LexicalHandler lexicalHandler = (LexicalHandler)((Object)this.m_contentHandler);
                lexicalHandler.comment(object.toCharArray(), 0, object.length());
                break;
            }
            case 11: {
                break;
            }
            case 9: {
                break;
            }
            case 1: {
                object = ((Element)node).getAttributes();
                int n2 = object.getLength();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Node node2 = object.item(i2);
                    String string = node2.getNodeName();
                    if (!string.equals("xmlns") && !string.startsWith("xmlns:")) continue;
                    int n3 = string.indexOf(":");
                    String string2 = n3 < 0 ? "" : string.substring(n3 + 1);
                    this.m_contentHandler.startPrefixMapping(string2, node2.getNodeValue());
                }
                String string = this.m_dh.getNamespaceOfNode(node);
                if (null == string) {
                    string = "";
                }
                this.m_contentHandler.startElement(string, this.m_dh.getLocalNameOfNode(node), node.getNodeName(), new AttList((NamedNodeMap)object, this.m_dh));
                break;
            }
            case 7: {
                ProcessingInstruction processingInstruction = (ProcessingInstruction)node;
                String string = processingInstruction.getNodeName();
                if (string.equals("xslt-next-is-raw")) {
                    this.nextIsRaw = true;
                    break;
                }
                this.m_contentHandler.processingInstruction(processingInstruction.getNodeName(), processingInstruction.getData());
                break;
            }
            case 4: {
                LexicalHandler lexicalHandler;
                boolean bl = this.m_contentHandler instanceof LexicalHandler;
                LexicalHandler lexicalHandler2 = lexicalHandler = bl ? (LexicalHandler)((Object)this.m_contentHandler) : null;
                if (bl) {
                    lexicalHandler.startCDATA();
                }
                this.dispatachChars(node);
                if (!bl) break;
                lexicalHandler.endCDATA();
                break;
            }
            case 3: {
                if (this.nextIsRaw) {
                    this.nextIsRaw = false;
                    this.m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
                    this.dispatachChars(node);
                    this.m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
                    break;
                }
                this.dispatachChars(node);
                break;
            }
            case 5: {
                EntityReference entityReference = (EntityReference)node;
                if (this.m_contentHandler instanceof LexicalHandler) {
                    ((LexicalHandler)((Object)this.m_contentHandler)).startEntity(entityReference.getNodeName());
                    break;
                } else {
                    break;
                }
            }
        }
    }

    protected void endNode(Node node) throws SAXException {
        switch (node.getNodeType()) {
            case 9: {
                break;
            }
            case 1: {
                String string = this.m_dh.getNamespaceOfNode(node);
                if (null == string) {
                    string = "";
                }
                this.m_contentHandler.endElement(string, this.m_dh.getLocalNameOfNode(node), node.getNodeName());
                NamedNodeMap namedNodeMap = ((Element)node).getAttributes();
                int n2 = namedNodeMap.getLength();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Node node2 = namedNodeMap.item(i2);
                    String string2 = node2.getNodeName();
                    if (!string2.equals("xmlns") && !string2.startsWith("xmlns:")) continue;
                    int n3 = string2.indexOf(":");
                    String string3 = n3 < 0 ? "" : string2.substring(n3 + 1);
                    this.m_contentHandler.endPrefixMapping(string3);
                }
                break;
            }
            case 4: {
                break;
            }
            case 5: {
                EntityReference entityReference = (EntityReference)node;
                if (this.m_contentHandler instanceof LexicalHandler) {
                    LexicalHandler lexicalHandler = (LexicalHandler)((Object)this.m_contentHandler);
                    lexicalHandler.endEntity(entityReference.getNodeName());
                    break;
                } else {
                    break;
                }
            }
        }
    }
}

