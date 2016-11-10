/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.SimpleResultTreeImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class AdaptiveResultTreeImpl
extends SimpleResultTreeImpl {
    private static int _documentURIIndex = 0;
    private SAXImpl _dom;
    private DTMWSFilter _wsfilter;
    private int _initSize;
    private boolean _buildIdIndex;
    private final AttributeList _attributes = new AttributeList();
    private String _openElementName;

    public AdaptiveResultTreeImpl(XSLTCDTMManager xSLTCDTMManager, int n2, DTMWSFilter dTMWSFilter, int n3, boolean bl) {
        super(xSLTCDTMManager, n2);
        this._wsfilter = dTMWSFilter;
        this._initSize = n3;
        this._buildIdIndex = bl;
    }

    public DOM getNestedDOM() {
        return this._dom;
    }

    public int getDocument() {
        if (this._dom != null) {
            return this._dom.getDocument();
        }
        return super.getDocument();
    }

    public String getStringValue() {
        if (this._dom != null) {
            return this._dom.getStringValue();
        }
        return super.getStringValue();
    }

    public DTMAxisIterator getIterator() {
        if (this._dom != null) {
            return this._dom.getIterator();
        }
        return super.getIterator();
    }

    public DTMAxisIterator getChildren(int n2) {
        if (this._dom != null) {
            return this._dom.getChildren(n2);
        }
        return super.getChildren(n2);
    }

    public DTMAxisIterator getTypedChildren(int n2) {
        if (this._dom != null) {
            return this._dom.getTypedChildren(n2);
        }
        return super.getTypedChildren(n2);
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        if (this._dom != null) {
            return this._dom.getAxisIterator(n2);
        }
        return super.getAxisIterator(n2);
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        if (this._dom != null) {
            return this._dom.getTypedAxisIterator(n2, n3);
        }
        return super.getTypedAxisIterator(n2, n3);
    }

    public DTMAxisIterator getNthDescendant(int n2, int n3, boolean bl) {
        if (this._dom != null) {
            return this._dom.getNthDescendant(n2, n3, bl);
        }
        return super.getNthDescendant(n2, n3, bl);
    }

    public DTMAxisIterator getNamespaceAxisIterator(int n2, int n3) {
        if (this._dom != null) {
            return this._dom.getNamespaceAxisIterator(n2, n3);
        }
        return super.getNamespaceAxisIterator(n2, n3);
    }

    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
        if (this._dom != null) {
            return this._dom.getNodeValueIterator(dTMAxisIterator, n2, string, bl);
        }
        return super.getNodeValueIterator(dTMAxisIterator, n2, string, bl);
    }

    public DTMAxisIterator orderNodes(DTMAxisIterator dTMAxisIterator, int n2) {
        if (this._dom != null) {
            return this._dom.orderNodes(dTMAxisIterator, n2);
        }
        return super.orderNodes(dTMAxisIterator, n2);
    }

    public String getNodeName(int n2) {
        if (this._dom != null) {
            return this._dom.getNodeName(n2);
        }
        return super.getNodeName(n2);
    }

    public String getNodeNameX(int n2) {
        if (this._dom != null) {
            return this._dom.getNodeNameX(n2);
        }
        return super.getNodeNameX(n2);
    }

    public String getNamespaceName(int n2) {
        if (this._dom != null) {
            return this._dom.getNamespaceName(n2);
        }
        return super.getNamespaceName(n2);
    }

    public int getExpandedTypeID(int n2) {
        if (this._dom != null) {
            return this._dom.getExpandedTypeID(n2);
        }
        return super.getExpandedTypeID(n2);
    }

    public int getNamespaceType(int n2) {
        if (this._dom != null) {
            return this._dom.getNamespaceType(n2);
        }
        return super.getNamespaceType(n2);
    }

    public int getParent(int n2) {
        if (this._dom != null) {
            return this._dom.getParent(n2);
        }
        return super.getParent(n2);
    }

    public int getAttributeNode(int n2, int n3) {
        if (this._dom != null) {
            return this._dom.getAttributeNode(n2, n3);
        }
        return super.getAttributeNode(n2, n3);
    }

    public String getStringValueX(int n2) {
        if (this._dom != null) {
            return this._dom.getStringValueX(n2);
        }
        return super.getStringValueX(n2);
    }

    public void copy(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (this._dom != null) {
            this._dom.copy(n2, serializationHandler);
        } else {
            super.copy(n2, serializationHandler);
        }
    }

    public void copy(DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException {
        if (this._dom != null) {
            this._dom.copy(dTMAxisIterator, serializationHandler);
        } else {
            super.copy(dTMAxisIterator, serializationHandler);
        }
    }

    public String shallowCopy(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (this._dom != null) {
            return this._dom.shallowCopy(n2, serializationHandler);
        }
        return super.shallowCopy(n2, serializationHandler);
    }

    public boolean lessThan(int n2, int n3) {
        if (this._dom != null) {
            return this._dom.lessThan(n2, n3);
        }
        return super.lessThan(n2, n3);
    }

    public void characters(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (this._dom != null) {
            this._dom.characters(n2, serializationHandler);
        } else {
            super.characters(n2, serializationHandler);
        }
    }

    public Node makeNode(int n2) {
        if (this._dom != null) {
            return this._dom.makeNode(n2);
        }
        return super.makeNode(n2);
    }

    public Node makeNode(DTMAxisIterator dTMAxisIterator) {
        if (this._dom != null) {
            return this._dom.makeNode(dTMAxisIterator);
        }
        return super.makeNode(dTMAxisIterator);
    }

    public NodeList makeNodeList(int n2) {
        if (this._dom != null) {
            return this._dom.makeNodeList(n2);
        }
        return super.makeNodeList(n2);
    }

    public NodeList makeNodeList(DTMAxisIterator dTMAxisIterator) {
        if (this._dom != null) {
            return this._dom.makeNodeList(dTMAxisIterator);
        }
        return super.makeNodeList(dTMAxisIterator);
    }

    public String getLanguage(int n2) {
        if (this._dom != null) {
            return this._dom.getLanguage(n2);
        }
        return super.getLanguage(n2);
    }

    public int getSize() {
        if (this._dom != null) {
            return this._dom.getSize();
        }
        return super.getSize();
    }

    public String getDocumentURI(int n2) {
        if (this._dom != null) {
            return this._dom.getDocumentURI(n2);
        }
        return "adaptive_rtf" + _documentURIIndex++;
    }

    public void setFilter(StripFilter stripFilter) {
        if (this._dom != null) {
            this._dom.setFilter(stripFilter);
        } else {
            super.setFilter(stripFilter);
        }
    }

    public void setupMapping(String[] arrstring, String[] arrstring2, int[] arrn, String[] arrstring3) {
        if (this._dom != null) {
            this._dom.setupMapping(arrstring, arrstring2, arrn, arrstring3);
        } else {
            super.setupMapping(arrstring, arrstring2, arrn, arrstring3);
        }
    }

    public boolean isElement(int n2) {
        if (this._dom != null) {
            return this._dom.isElement(n2);
        }
        return super.isElement(n2);
    }

    public boolean isAttribute(int n2) {
        if (this._dom != null) {
            return this._dom.isAttribute(n2);
        }
        return super.isAttribute(n2);
    }

    public String lookupNamespace(int n2, String string) throws TransletException {
        if (this._dom != null) {
            return this._dom.lookupNamespace(n2, string);
        }
        return super.lookupNamespace(n2, string);
    }

    public final int getNodeIdent(int n2) {
        if (this._dom != null) {
            return this._dom.getNodeIdent(n2);
        }
        return super.getNodeIdent(n2);
    }

    public final int getNodeHandle(int n2) {
        if (this._dom != null) {
            return this._dom.getNodeHandle(n2);
        }
        return super.getNodeHandle(n2);
    }

    public DOM getResultTreeFrag(int n2, int n3) {
        if (this._dom != null) {
            return this._dom.getResultTreeFrag(n2, n3);
        }
        return super.getResultTreeFrag(n2, n3);
    }

    public SerializationHandler getOutputDomBuilder() {
        return this;
    }

    public int getNSType(int n2) {
        if (this._dom != null) {
            return this._dom.getNSType(n2);
        }
        return super.getNSType(n2);
    }

    public String getUnparsedEntityURI(String string) {
        if (this._dom != null) {
            return this._dom.getUnparsedEntityURI(string);
        }
        return super.getUnparsedEntityURI(string);
    }

    public Hashtable getElementsWithIDs() {
        if (this._dom != null) {
            return this._dom.getElementsWithIDs();
        }
        return super.getElementsWithIDs();
    }

    private void maybeEmitStartElement() throws SAXException {
        if (this._openElementName != null) {
            int n2 = this._openElementName.indexOf(":");
            if (n2 < 0) {
                this._dom.startElement(null, this._openElementName, this._openElementName, this._attributes);
            } else {
                this._dom.startElement(null, this._openElementName.substring(n2 + 1), this._openElementName, this._attributes);
            }
            this._openElementName = null;
        }
    }

    private void prepareNewDOM() throws SAXException {
        this._dom = (SAXImpl)this._dtmManager.getDTM(null, true, this._wsfilter, true, false, false, this._initSize, this._buildIdIndex);
        this._dom.startDocument();
        for (int i2 = 0; i2 < this._size; ++i2) {
            String string = this._textArray[i2];
            this._dom.characters(string.toCharArray(), 0, string.length());
        }
        this._size = 0;
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
        if (this._dom != null) {
            this._dom.endDocument();
        } else {
            super.endDocument();
        }
    }

    public void characters(String string) throws SAXException {
        if (this._dom != null) {
            this.characters(string.toCharArray(), 0, string.length());
        } else {
            super.characters(string);
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this._dom != null) {
            this.maybeEmitStartElement();
            this._dom.characters(arrc, n2, n3);
        } else {
            super.characters(arrc, n2, n3);
        }
    }

    public boolean setEscaping(boolean bl) throws SAXException {
        if (this._dom != null) {
            return this._dom.setEscaping(bl);
        }
        return super.setEscaping(bl);
    }

    public void startElement(String string) throws SAXException {
        if (this._dom == null) {
            this.prepareNewDOM();
        }
        this.maybeEmitStartElement();
        this._openElementName = string;
        this._attributes.clear();
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        this.startElement(string3);
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        this.startElement(string3);
    }

    public void endElement(String string) throws SAXException {
        this.maybeEmitStartElement();
        this._dom.endElement(null, null, string);
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.endElement(string3);
    }

    public void addUniqueAttribute(String string, String string2, int n2) throws SAXException {
        this.addAttribute(string, string2);
    }

    public void addAttribute(String string, String string2) {
        if (this._openElementName != null) {
            this._attributes.add(string, string2);
        } else {
            BasisLibrary.runTimeError("STRAY_ATTRIBUTE_ERR", string);
        }
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
        if (this._dom == null) {
            this.prepareNewDOM();
        }
        this._dom.startPrefixMapping(string, string2);
    }

    public void comment(String string) throws SAXException {
        if (this._dom == null) {
            this.prepareNewDOM();
        }
        this.maybeEmitStartElement();
        char[] arrc = string.toCharArray();
        this._dom.comment(arrc, 0, arrc.length);
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (this._dom == null) {
            this.prepareNewDOM();
        }
        this.maybeEmitStartElement();
        this._dom.comment(arrc, n2, n3);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (this._dom == null) {
            this.prepareNewDOM();
        }
        this.maybeEmitStartElement();
        this._dom.processingInstruction(string, string2);
    }

    public void setFeature(String string, boolean bl) {
        if (this._dom != null) {
            this._dom.setFeature(string, bl);
        }
    }

    public void setProperty(String string, Object object) {
        if (this._dom != null) {
            this._dom.setProperty(string, object);
        }
    }

    public DTMAxisTraverser getAxisTraverser(int n2) {
        if (this._dom != null) {
            return this._dom.getAxisTraverser(n2);
        }
        return super.getAxisTraverser(n2);
    }

    public boolean hasChildNodes(int n2) {
        if (this._dom != null) {
            return this._dom.hasChildNodes(n2);
        }
        return super.hasChildNodes(n2);
    }

    public int getFirstChild(int n2) {
        if (this._dom != null) {
            return this._dom.getFirstChild(n2);
        }
        return super.getFirstChild(n2);
    }

    public int getLastChild(int n2) {
        if (this._dom != null) {
            return this._dom.getLastChild(n2);
        }
        return super.getLastChild(n2);
    }

    public int getAttributeNode(int n2, String string, String string2) {
        if (this._dom != null) {
            return this._dom.getAttributeNode(n2, string, string2);
        }
        return super.getAttributeNode(n2, string, string2);
    }

    public int getFirstAttribute(int n2) {
        if (this._dom != null) {
            return this._dom.getFirstAttribute(n2);
        }
        return super.getFirstAttribute(n2);
    }

    public int getFirstNamespaceNode(int n2, boolean bl) {
        if (this._dom != null) {
            return this._dom.getFirstNamespaceNode(n2, bl);
        }
        return super.getFirstNamespaceNode(n2, bl);
    }

    public int getNextSibling(int n2) {
        if (this._dom != null) {
            return this._dom.getNextSibling(n2);
        }
        return super.getNextSibling(n2);
    }

    public int getPreviousSibling(int n2) {
        if (this._dom != null) {
            return this._dom.getPreviousSibling(n2);
        }
        return super.getPreviousSibling(n2);
    }

    public int getNextAttribute(int n2) {
        if (this._dom != null) {
            return this._dom.getNextAttribute(n2);
        }
        return super.getNextAttribute(n2);
    }

    public int getNextNamespaceNode(int n2, int n3, boolean bl) {
        if (this._dom != null) {
            return this._dom.getNextNamespaceNode(n2, n3, bl);
        }
        return super.getNextNamespaceNode(n2, n3, bl);
    }

    public int getOwnerDocument(int n2) {
        if (this._dom != null) {
            return this._dom.getOwnerDocument(n2);
        }
        return super.getOwnerDocument(n2);
    }

    public int getDocumentRoot(int n2) {
        if (this._dom != null) {
            return this._dom.getDocumentRoot(n2);
        }
        return super.getDocumentRoot(n2);
    }

    public XMLString getStringValue(int n2) {
        if (this._dom != null) {
            return this._dom.getStringValue(n2);
        }
        return super.getStringValue(n2);
    }

    public int getStringValueChunkCount(int n2) {
        if (this._dom != null) {
            return this._dom.getStringValueChunkCount(n2);
        }
        return super.getStringValueChunkCount(n2);
    }

    public char[] getStringValueChunk(int n2, int n3, int[] arrn) {
        if (this._dom != null) {
            return this._dom.getStringValueChunk(n2, n3, arrn);
        }
        return super.getStringValueChunk(n2, n3, arrn);
    }

    public int getExpandedTypeID(String string, String string2, int n2) {
        if (this._dom != null) {
            return this._dom.getExpandedTypeID(string, string2, n2);
        }
        return super.getExpandedTypeID(string, string2, n2);
    }

    public String getLocalNameFromExpandedNameID(int n2) {
        if (this._dom != null) {
            return this._dom.getLocalNameFromExpandedNameID(n2);
        }
        return super.getLocalNameFromExpandedNameID(n2);
    }

    public String getNamespaceFromExpandedNameID(int n2) {
        if (this._dom != null) {
            return this._dom.getNamespaceFromExpandedNameID(n2);
        }
        return super.getNamespaceFromExpandedNameID(n2);
    }

    public String getLocalName(int n2) {
        if (this._dom != null) {
            return this._dom.getLocalName(n2);
        }
        return super.getLocalName(n2);
    }

    public String getPrefix(int n2) {
        if (this._dom != null) {
            return this._dom.getPrefix(n2);
        }
        return super.getPrefix(n2);
    }

    public String getNamespaceURI(int n2) {
        if (this._dom != null) {
            return this._dom.getNamespaceURI(n2);
        }
        return super.getNamespaceURI(n2);
    }

    public String getNodeValue(int n2) {
        if (this._dom != null) {
            return this._dom.getNodeValue(n2);
        }
        return super.getNodeValue(n2);
    }

    public short getNodeType(int n2) {
        if (this._dom != null) {
            return this._dom.getNodeType(n2);
        }
        return super.getNodeType(n2);
    }

    public short getLevel(int n2) {
        if (this._dom != null) {
            return this._dom.getLevel(n2);
        }
        return super.getLevel(n2);
    }

    public boolean isSupported(String string, String string2) {
        if (this._dom != null) {
            return this._dom.isSupported(string, string2);
        }
        return super.isSupported(string, string2);
    }

    public String getDocumentBaseURI() {
        if (this._dom != null) {
            return this._dom.getDocumentBaseURI();
        }
        return super.getDocumentBaseURI();
    }

    public void setDocumentBaseURI(String string) {
        if (this._dom != null) {
            this._dom.setDocumentBaseURI(string);
        } else {
            super.setDocumentBaseURI(string);
        }
    }

    public String getDocumentSystemIdentifier(int n2) {
        if (this._dom != null) {
            return this._dom.getDocumentSystemIdentifier(n2);
        }
        return super.getDocumentSystemIdentifier(n2);
    }

    public String getDocumentEncoding(int n2) {
        if (this._dom != null) {
            return this._dom.getDocumentEncoding(n2);
        }
        return super.getDocumentEncoding(n2);
    }

    public String getDocumentStandalone(int n2) {
        if (this._dom != null) {
            return this._dom.getDocumentStandalone(n2);
        }
        return super.getDocumentStandalone(n2);
    }

    public String getDocumentVersion(int n2) {
        if (this._dom != null) {
            return this._dom.getDocumentVersion(n2);
        }
        return super.getDocumentVersion(n2);
    }

    public boolean getDocumentAllDeclarationsProcessed() {
        if (this._dom != null) {
            return this._dom.getDocumentAllDeclarationsProcessed();
        }
        return super.getDocumentAllDeclarationsProcessed();
    }

    public String getDocumentTypeDeclarationSystemIdentifier() {
        if (this._dom != null) {
            return this._dom.getDocumentTypeDeclarationSystemIdentifier();
        }
        return super.getDocumentTypeDeclarationSystemIdentifier();
    }

    public String getDocumentTypeDeclarationPublicIdentifier() {
        if (this._dom != null) {
            return this._dom.getDocumentTypeDeclarationPublicIdentifier();
        }
        return super.getDocumentTypeDeclarationPublicIdentifier();
    }

    public int getElementById(String string) {
        if (this._dom != null) {
            return this._dom.getElementById(string);
        }
        return super.getElementById(string);
    }

    public boolean supportsPreStripping() {
        if (this._dom != null) {
            return this._dom.supportsPreStripping();
        }
        return super.supportsPreStripping();
    }

    public boolean isNodeAfter(int n2, int n3) {
        if (this._dom != null) {
            return this._dom.isNodeAfter(n2, n3);
        }
        return super.isNodeAfter(n2, n3);
    }

    public boolean isCharacterElementContentWhitespace(int n2) {
        if (this._dom != null) {
            return this._dom.isCharacterElementContentWhitespace(n2);
        }
        return super.isCharacterElementContentWhitespace(n2);
    }

    public boolean isDocumentAllDeclarationsProcessed(int n2) {
        if (this._dom != null) {
            return this._dom.isDocumentAllDeclarationsProcessed(n2);
        }
        return super.isDocumentAllDeclarationsProcessed(n2);
    }

    public boolean isAttributeSpecified(int n2) {
        if (this._dom != null) {
            return this._dom.isAttributeSpecified(n2);
        }
        return super.isAttributeSpecified(n2);
    }

    public void dispatchCharactersEvents(int n2, ContentHandler contentHandler, boolean bl) throws SAXException {
        if (this._dom != null) {
            this._dom.dispatchCharactersEvents(n2, contentHandler, bl);
        } else {
            super.dispatchCharactersEvents(n2, contentHandler, bl);
        }
    }

    public void dispatchToEvents(int n2, ContentHandler contentHandler) throws SAXException {
        if (this._dom != null) {
            this._dom.dispatchToEvents(n2, contentHandler);
        } else {
            super.dispatchToEvents(n2, contentHandler);
        }
    }

    public Node getNode(int n2) {
        if (this._dom != null) {
            return this._dom.getNode(n2);
        }
        return super.getNode(n2);
    }

    public boolean needsTwoThreads() {
        if (this._dom != null) {
            return this._dom.needsTwoThreads();
        }
        return super.needsTwoThreads();
    }

    public ContentHandler getContentHandler() {
        if (this._dom != null) {
            return this._dom.getContentHandler();
        }
        return super.getContentHandler();
    }

    public LexicalHandler getLexicalHandler() {
        if (this._dom != null) {
            return this._dom.getLexicalHandler();
        }
        return super.getLexicalHandler();
    }

    public EntityResolver getEntityResolver() {
        if (this._dom != null) {
            return this._dom.getEntityResolver();
        }
        return super.getEntityResolver();
    }

    public DTDHandler getDTDHandler() {
        if (this._dom != null) {
            return this._dom.getDTDHandler();
        }
        return super.getDTDHandler();
    }

    public ErrorHandler getErrorHandler() {
        if (this._dom != null) {
            return this._dom.getErrorHandler();
        }
        return super.getErrorHandler();
    }

    public DeclHandler getDeclHandler() {
        if (this._dom != null) {
            return this._dom.getDeclHandler();
        }
        return super.getDeclHandler();
    }

    public void appendChild(int n2, boolean bl, boolean bl2) {
        if (this._dom != null) {
            this._dom.appendChild(n2, bl, bl2);
        } else {
            super.appendChild(n2, bl, bl2);
        }
    }

    public void appendTextChild(String string) {
        if (this._dom != null) {
            this._dom.appendTextChild(string);
        } else {
            super.appendTextChild(string);
        }
    }

    public SourceLocator getSourceLocatorFor(int n2) {
        if (this._dom != null) {
            return this._dom.getSourceLocatorFor(n2);
        }
        return super.getSourceLocatorFor(n2);
    }

    public void documentRegistration() {
        if (this._dom != null) {
            this._dom.documentRegistration();
        } else {
            super.documentRegistration();
        }
    }

    public void documentRelease() {
        if (this._dom != null) {
            this._dom.documentRelease();
        } else {
            super.documentRelease();
        }
    }
}

