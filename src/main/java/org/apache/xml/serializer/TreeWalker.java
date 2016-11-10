/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.File;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.utils.AttList;
import org.apache.xml.serializer.utils.DOM2Helper;
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

public final class TreeWalker {
    private final ContentHandler m_contentHandler;
    private final SerializationHandler m_Serializer;
    protected final DOM2Helper m_dh;
    private final LocatorImpl m_locator = new LocatorImpl();
    boolean nextIsRaw = false;

    public TreeWalker(ContentHandler contentHandler) {
        this(contentHandler, null);
    }

    public TreeWalker(ContentHandler contentHandler, String string) {
        this.m_contentHandler = contentHandler;
        this.m_Serializer = this.m_contentHandler instanceof SerializationHandler ? (SerializationHandler)this.m_contentHandler : null;
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
        this.m_contentHandler.endDocument();
    }

    private final void dispatachChars(Node node) throws SAXException {
        if (this.m_Serializer != null) {
            this.m_Serializer.characters(node);
        } else {
            String string = ((Text)node).getData();
            this.m_contentHandler.characters(string.toCharArray(), 0, string.length());
        }
    }

    protected void startNode(Node node) throws SAXException {
        Object object;
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
                object = (Element)node;
                Object object2 = object.getNamespaceURI();
                if (object2 != null) {
                    String string = object.getPrefix();
                    if (string == null) {
                        string = "";
                    }
                    this.m_contentHandler.startPrefixMapping(string, (String)object2);
                }
                object2 = object.getAttributes();
                int n2 = object2.getLength();
                for (int i2 = 0; i2 < n2; ++i2) {
                    String string;
                    Node node2 = object2.item(i2);
                    String string2 = node2.getNodeName();
                    int n3 = string2.indexOf(58);
                    if (string2.equals("xmlns") || string2.startsWith("xmlns:")) {
                        string = n3 < 0 ? "" : string2.substring(n3 + 1);
                        this.m_contentHandler.startPrefixMapping(string, node2.getNodeValue());
                        continue;
                    }
                    if (n3 <= 0) continue;
                    string = string2.substring(0, n3);
                    String string3 = node2.getNamespaceURI();
                    if (string3 == null) continue;
                    this.m_contentHandler.startPrefixMapping(string, string3);
                }
                String string = this.m_dh.getNamespaceOfNode(node);
                if (null == string) {
                    string = "";
                }
                this.m_contentHandler.startElement(string, this.m_dh.getLocalNameOfNode(node), node.getNodeName(), new AttList((NamedNodeMap)object2, this.m_dh));
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
                Object object;
                String string = this.m_dh.getNamespaceOfNode(node);
                if (null == string) {
                    string = "";
                }
                this.m_contentHandler.endElement(string, this.m_dh.getLocalNameOfNode(node), node.getNodeName());
                if (this.m_Serializer != null) break;
                Element element = (Element)node;
                NamedNodeMap namedNodeMap = element.getAttributes();
                int n2 = namedNodeMap.getLength();
                for (int i2 = n2 - 1; 0 <= i2; --i2) {
                    String string2;
                    object = namedNodeMap.item(i2);
                    String string3 = object.getNodeName();
                    int n3 = string3.indexOf(58);
                    if (string3.equals("xmlns") || string3.startsWith("xmlns:")) {
                        string2 = n3 < 0 ? "" : string3.substring(n3 + 1);
                        this.m_contentHandler.endPrefixMapping(string2);
                        continue;
                    }
                    if (n3 <= 0) continue;
                    string2 = string3.substring(0, n3);
                    this.m_contentHandler.endPrefixMapping(string2);
                }
                String string4 = element.getNamespaceURI();
                if (string4 == null) break;
                object = element.getPrefix();
                if (object == null) {
                    object = "";
                }
                this.m_contentHandler.endPrefixMapping((String)object);
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

