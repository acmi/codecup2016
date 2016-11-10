/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.XMLString;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public interface DTM {
    public DTMAxisTraverser getAxisTraverser(int var1);

    public DTMAxisIterator getAxisIterator(int var1);

    public int getFirstChild(int var1);

    public int getLastChild(int var1);

    public int getAttributeNode(int var1, String var2, String var3);

    public int getFirstAttribute(int var1);

    public int getFirstNamespaceNode(int var1, boolean var2);

    public int getNextSibling(int var1);

    public int getPreviousSibling(int var1);

    public int getNextAttribute(int var1);

    public int getNextNamespaceNode(int var1, int var2, boolean var3);

    public int getParent(int var1);

    public int getDocument();

    public int getOwnerDocument(int var1);

    public int getDocumentRoot(int var1);

    public XMLString getStringValue(int var1);

    public int getExpandedTypeID(int var1);

    public int getExpandedTypeID(String var1, String var2, int var3);

    public String getLocalNameFromExpandedNameID(int var1);

    public String getNodeName(int var1);

    public String getNodeNameX(int var1);

    public String getLocalName(int var1);

    public String getPrefix(int var1);

    public String getNamespaceURI(int var1);

    public String getNodeValue(int var1);

    public short getNodeType(int var1);

    public String getDocumentBaseURI();

    public void setDocumentBaseURI(String var1);

    public int getElementById(String var1);

    public String getUnparsedEntityURI(String var1);

    public boolean isNodeAfter(int var1, int var2);

    public void dispatchCharactersEvents(int var1, ContentHandler var2, boolean var3) throws SAXException;

    public void dispatchToEvents(int var1, ContentHandler var2) throws SAXException;

    public Node getNode(int var1);

    public ContentHandler getContentHandler();

    public LexicalHandler getLexicalHandler();

    public EntityResolver getEntityResolver();

    public DTDHandler getDTDHandler();

    public ErrorHandler getErrorHandler();

    public void appendTextChild(String var1);

    public SourceLocator getSourceLocatorFor(int var1);

    public void documentRegistration();

    public void documentRelease();

    public void migrateTo(DTMManager var1);
}

