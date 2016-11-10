/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.NodeConsumer;
import org.apache.xml.utils.XMLString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public class DTMTreeWalker {
    private ContentHandler m_contentHandler = null;
    protected DTM m_dtm;
    boolean nextIsRaw = false;

    public void setDTM(DTM dTM) {
        this.m_dtm = dTM;
    }

    public ContentHandler getcontentHandler() {
        return this.m_contentHandler;
    }

    public void setcontentHandler(ContentHandler contentHandler) {
        this.m_contentHandler = contentHandler;
    }

    public DTMTreeWalker() {
    }

    public DTMTreeWalker(ContentHandler contentHandler, DTM dTM) {
        this.m_contentHandler = contentHandler;
        this.m_dtm = dTM;
    }

    public void traverse(int n2) throws SAXException {
        int n3 = n2;
        while (-1 != n2) {
            this.startNode(n2);
            int n4 = this.m_dtm.getFirstChild(n2);
            while (-1 == n4) {
                this.endNode(n2);
                if (n3 == n2) break;
                n4 = this.m_dtm.getNextSibling(n2);
                if (-1 != n4 || -1 != (n2 = this.m_dtm.getParent(n2)) && n3 != n2) continue;
                if (-1 != n2) {
                    this.endNode(n2);
                }
                n4 = -1;
                break;
            }
            n2 = n4;
        }
    }

    public void traverse(int n2, int n3) throws SAXException {
        while (-1 != n2) {
            this.startNode(n2);
            int n4 = this.m_dtm.getFirstChild(n2);
            while (-1 == n4) {
                this.endNode(n2);
                if (-1 != n3 && n3 == n2) break;
                n4 = this.m_dtm.getNextSibling(n2);
                if (-1 != n4 || -1 != (n2 = this.m_dtm.getParent(n2)) && (-1 == n3 || n3 != n2)) continue;
                n4 = -1;
                break;
            }
            n2 = n4;
        }
    }

    private final void dispatachChars(int n2) throws SAXException {
        this.m_dtm.dispatchCharactersEvents(n2, this.m_contentHandler, false);
    }

    protected void startNode(int n2) throws SAXException {
        if (this.m_contentHandler instanceof NodeConsumer) {
            // empty if block
        }
        switch (this.m_dtm.getNodeType(n2)) {
            case 8: {
                XMLString xMLString = this.m_dtm.getStringValue(n2);
                if (!(this.m_contentHandler instanceof LexicalHandler)) break;
                LexicalHandler lexicalHandler = (LexicalHandler)((Object)this.m_contentHandler);
                xMLString.dispatchAsComment(lexicalHandler);
                break;
            }
            case 11: {
                break;
            }
            case 9: {
                this.m_contentHandler.startDocument();
                break;
            }
            case 1: {
                Object object;
                DTM dTM = this.m_dtm;
                int n3 = dTM.getFirstNamespaceNode(n2, true);
                while (-1 != n3) {
                    object = dTM.getNodeNameX(n3);
                    this.m_contentHandler.startPrefixMapping((String)object, dTM.getNodeValue(n3));
                    n3 = dTM.getNextNamespaceNode(n2, n3, true);
                }
                String string = dTM.getNamespaceURI(n2);
                if (null == string) {
                    string = "";
                }
                object = new AttributesImpl();
                int n4 = dTM.getFirstAttribute(n2);
                while (n4 != -1) {
                    object.addAttribute(dTM.getNamespaceURI(n4), dTM.getLocalName(n4), dTM.getNodeName(n4), "CDATA", dTM.getNodeValue(n4));
                    n4 = dTM.getNextAttribute(n4);
                }
                this.m_contentHandler.startElement(string, this.m_dtm.getLocalName(n2), this.m_dtm.getNodeName(n2), (Attributes)object);
                break;
            }
            case 7: {
                String string = this.m_dtm.getNodeName(n2);
                if (string.equals("xslt-next-is-raw")) {
                    this.nextIsRaw = true;
                    break;
                }
                this.m_contentHandler.processingInstruction(string, this.m_dtm.getNodeValue(n2));
                break;
            }
            case 4: {
                LexicalHandler lexicalHandler;
                boolean bl = this.m_contentHandler instanceof LexicalHandler;
                LexicalHandler lexicalHandler2 = lexicalHandler = bl ? (LexicalHandler)((Object)this.m_contentHandler) : null;
                if (bl) {
                    lexicalHandler.startCDATA();
                }
                this.dispatachChars(n2);
                if (!bl) break;
                lexicalHandler.endCDATA();
                break;
            }
            case 3: {
                if (this.nextIsRaw) {
                    this.nextIsRaw = false;
                    this.m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
                    this.dispatachChars(n2);
                    this.m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
                    break;
                }
                this.dispatachChars(n2);
                break;
            }
            case 5: {
                if (!(this.m_contentHandler instanceof LexicalHandler)) break;
                ((LexicalHandler)((Object)this.m_contentHandler)).startEntity(this.m_dtm.getNodeName(n2));
                break;
            }
        }
    }

    protected void endNode(int n2) throws SAXException {
        switch (this.m_dtm.getNodeType(n2)) {
            case 9: {
                this.m_contentHandler.endDocument();
                break;
            }
            case 1: {
                String string = this.m_dtm.getNamespaceURI(n2);
                if (null == string) {
                    string = "";
                }
                this.m_contentHandler.endElement(string, this.m_dtm.getLocalName(n2), this.m_dtm.getNodeName(n2));
                int n3 = this.m_dtm.getFirstNamespaceNode(n2, true);
                while (-1 != n3) {
                    String string2 = this.m_dtm.getNodeNameX(n3);
                    this.m_contentHandler.endPrefixMapping(string2);
                    n3 = this.m_dtm.getNextNamespaceNode(n2, n3, true);
                }
                break;
            }
            case 4: {
                break;
            }
            case 5: {
                if (!(this.m_contentHandler instanceof LexicalHandler)) break;
                LexicalHandler lexicalHandler = (LexicalHandler)((Object)this.m_contentHandler);
                lexicalHandler.endEntity(this.m_dtm.getNodeName(n2));
                break;
            }
        }
    }
}

