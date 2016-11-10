/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.BitArray;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.serializer.EmptySerializer;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringDefault;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class SimpleResultTreeImpl
extends EmptySerializer
implements DOM,
DTM {
    private static final DTMAxisIterator EMPTY_ITERATOR = new DTMAxisIteratorBase(){

        public DTMAxisIterator reset() {
            return this;
        }

        public DTMAxisIterator setStartNode(int n2) {
            return this;
        }

        public int next() {
            return -1;
        }

        public void setMark() {
        }

        public void gotoMark() {
        }

        public int getLast() {
            return 0;
        }

        public int getPosition() {
            return 0;
        }

        public DTMAxisIterator cloneIterator() {
            return this;
        }

        public void setRestartable(boolean bl) {
        }
    };
    public static final int RTF_ROOT = 0;
    public static final int RTF_TEXT = 1;
    public static final int NUMBER_OF_NODES = 2;
    private static int _documentURIIndex = 0;
    private static final String EMPTY_STR = "";
    private String _text;
    protected String[] _textArray;
    protected XSLTCDTMManager _dtmManager;
    protected int _size = 0;
    private int _documentID;
    private BitArray _dontEscape = null;
    private boolean _escaping = true;

    public SimpleResultTreeImpl(XSLTCDTMManager xSLTCDTMManager, int n2) {
        this._dtmManager = xSLTCDTMManager;
        this._documentID = n2;
        this._textArray = new String[4];
    }

    public DTMManagerDefault getDTMManager() {
        return this._dtmManager;
    }

    public int getDocument() {
        return this._documentID;
    }

    public String getStringValue() {
        return this._text;
    }

    public DTMAxisIterator getIterator() {
        return new SingletonIterator(this, this.getDocument());
    }

    public DTMAxisIterator getChildren(int n2) {
        return new SimpleIterator(this).setStartNode(n2);
    }

    public DTMAxisIterator getTypedChildren(int n2) {
        return new SimpleIterator(this, 1, n2);
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        switch (n2) {
            case 3: 
            case 4: {
                return new SimpleIterator(this, 1);
            }
            case 0: 
            case 10: {
                return new SimpleIterator(this, 0);
            }
            case 1: {
                return new SimpleIterator(this, 0).includeSelf();
            }
            case 5: {
                return new SimpleIterator(this, 1).includeSelf();
            }
            case 13: {
                return new SingletonIterator(this);
            }
        }
        return EMPTY_ITERATOR;
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        switch (n2) {
            case 3: 
            case 4: {
                return new SimpleIterator(this, 1, n3);
            }
            case 0: 
            case 10: {
                return new SimpleIterator(this, 0, n3);
            }
            case 1: {
                return new SimpleIterator(this, 0, n3).includeSelf();
            }
            case 5: {
                return new SimpleIterator(this, 1, n3).includeSelf();
            }
            case 13: {
                return new SingletonIterator(this, n3);
            }
        }
        return EMPTY_ITERATOR;
    }

    public DTMAxisIterator getNthDescendant(int n2, int n3, boolean bl) {
        return null;
    }

    public DTMAxisIterator getNamespaceAxisIterator(int n2, int n3) {
        return null;
    }

    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
        return null;
    }

    public DTMAxisIterator orderNodes(DTMAxisIterator dTMAxisIterator, int n2) {
        return dTMAxisIterator;
    }

    public String getNodeName(int n2) {
        if (this.getNodeIdent(n2) == 1) {
            return "#text";
        }
        return "";
    }

    public String getNodeNameX(int n2) {
        return "";
    }

    public String getNamespaceName(int n2) {
        return "";
    }

    public int getExpandedTypeID(int n2) {
        int n3 = this.getNodeIdent(n2);
        if (n3 == 1) {
            return 3;
        }
        if (n3 == 0) {
            return 0;
        }
        return -1;
    }

    public int getNamespaceType(int n2) {
        return 0;
    }

    public int getParent(int n2) {
        int n3 = this.getNodeIdent(n2);
        return n3 == 1 ? this.getNodeHandle(0) : -1;
    }

    public int getAttributeNode(int n2, int n3) {
        return -1;
    }

    public String getStringValueX(int n2) {
        int n3 = this.getNodeIdent(n2);
        if (n3 == 0 || n3 == 1) {
            return this._text;
        }
        return "";
    }

    public void copy(int n2, SerializationHandler serializationHandler) throws TransletException {
        this.characters(n2, serializationHandler);
    }

    public void copy(DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException {
        int n2;
        while ((n2 = dTMAxisIterator.next()) != -1) {
            this.copy(n2, serializationHandler);
        }
    }

    public String shallowCopy(int n2, SerializationHandler serializationHandler) throws TransletException {
        this.characters(n2, serializationHandler);
        return null;
    }

    public boolean lessThan(int n2, int n3) {
        if (n2 == -1) {
            return false;
        }
        if (n3 == -1) {
            return true;
        }
        return n2 < n3;
    }

    public void characters(int n2, SerializationHandler serializationHandler) throws TransletException {
        int n3 = this.getNodeIdent(n2);
        if (n3 == 0 || n3 == 1) {
            boolean bl = false;
            boolean bl2 = false;
            try {
                for (int i2 = 0; i2 < this._size; ++i2) {
                    if (this._dontEscape != null && (bl = this._dontEscape.getBit(i2))) {
                        bl2 = serializationHandler.setEscaping(false);
                    }
                    serializationHandler.characters(this._textArray[i2]);
                    if (!bl) continue;
                    serializationHandler.setEscaping(bl2);
                }
            }
            catch (SAXException sAXException) {
                throw new TransletException(sAXException);
            }
        }
    }

    public Node makeNode(int n2) {
        return null;
    }

    public Node makeNode(DTMAxisIterator dTMAxisIterator) {
        return null;
    }

    public NodeList makeNodeList(int n2) {
        return null;
    }

    public NodeList makeNodeList(DTMAxisIterator dTMAxisIterator) {
        return null;
    }

    public String getLanguage(int n2) {
        return null;
    }

    public int getSize() {
        return 2;
    }

    public String getDocumentURI(int n2) {
        return "simple_rtf" + _documentURIIndex++;
    }

    public void setFilter(StripFilter stripFilter) {
    }

    public void setupMapping(String[] arrstring, String[] arrstring2, int[] arrn, String[] arrstring3) {
    }

    public boolean isElement(int n2) {
        return false;
    }

    public boolean isAttribute(int n2) {
        return false;
    }

    public String lookupNamespace(int n2, String string) throws TransletException {
        return null;
    }

    public int getNodeIdent(int n2) {
        return n2 != -1 ? n2 - this._documentID : -1;
    }

    public int getNodeHandle(int n2) {
        return n2 != -1 ? n2 + this._documentID : -1;
    }

    public DOM getResultTreeFrag(int n2, int n3) {
        return null;
    }

    public DOM getResultTreeFrag(int n2, int n3, boolean bl) {
        return null;
    }

    public SerializationHandler getOutputDomBuilder() {
        return this;
    }

    public int getNSType(int n2) {
        return 0;
    }

    public String getUnparsedEntityURI(String string) {
        return null;
    }

    public Hashtable getElementsWithIDs() {
        return null;
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
        if (this._size == 1) {
            this._text = this._textArray[0];
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < this._size; ++i2) {
                stringBuffer.append(this._textArray[i2]);
            }
            this._text = stringBuffer.toString();
        }
    }

    public void characters(String string) throws SAXException {
        if (this._size >= this._textArray.length) {
            String[] arrstring = new String[this._textArray.length * 2];
            System.arraycopy(this._textArray, 0, arrstring, 0, this._textArray.length);
            this._textArray = arrstring;
        }
        if (!this._escaping) {
            if (this._dontEscape == null) {
                this._dontEscape = new BitArray(8);
            }
            if (this._size >= this._dontEscape.size()) {
                this._dontEscape.resize(this._dontEscape.size() * 2);
            }
            this._dontEscape.setBit(this._size);
        }
        this._textArray[this._size++] = string;
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this._size >= this._textArray.length) {
            String[] arrstring = new String[this._textArray.length * 2];
            System.arraycopy(this._textArray, 0, arrstring, 0, this._textArray.length);
            this._textArray = arrstring;
        }
        if (!this._escaping) {
            if (this._dontEscape == null) {
                this._dontEscape = new BitArray(8);
            }
            if (this._size >= this._dontEscape.size()) {
                this._dontEscape.resize(this._dontEscape.size() * 2);
            }
            this._dontEscape.setBit(this._size);
        }
        this._textArray[this._size++] = new String(arrc, n2, n3);
    }

    public boolean setEscaping(boolean bl) throws SAXException {
        boolean bl2 = this._escaping;
        this._escaping = bl;
        return bl2;
    }

    public void setFeature(String string, boolean bl) {
    }

    public void setProperty(String string, Object object) {
    }

    public DTMAxisTraverser getAxisTraverser(int n2) {
        return null;
    }

    public boolean hasChildNodes(int n2) {
        return this.getNodeIdent(n2) == 0;
    }

    public int getFirstChild(int n2) {
        int n3 = this.getNodeIdent(n2);
        if (n3 == 0) {
            return this.getNodeHandle(1);
        }
        return -1;
    }

    public int getLastChild(int n2) {
        return this.getFirstChild(n2);
    }

    public int getAttributeNode(int n2, String string, String string2) {
        return -1;
    }

    public int getFirstAttribute(int n2) {
        return -1;
    }

    public int getFirstNamespaceNode(int n2, boolean bl) {
        return -1;
    }

    public int getNextSibling(int n2) {
        return -1;
    }

    public int getPreviousSibling(int n2) {
        return -1;
    }

    public int getNextAttribute(int n2) {
        return -1;
    }

    public int getNextNamespaceNode(int n2, int n3, boolean bl) {
        return -1;
    }

    public int getOwnerDocument(int n2) {
        return this.getDocument();
    }

    public int getDocumentRoot(int n2) {
        return this.getDocument();
    }

    public XMLString getStringValue(int n2) {
        return new XMLStringDefault(this.getStringValueX(n2));
    }

    public int getStringValueChunkCount(int n2) {
        return 0;
    }

    public char[] getStringValueChunk(int n2, int n3, int[] arrn) {
        return null;
    }

    public int getExpandedTypeID(String string, String string2, int n2) {
        return -1;
    }

    public String getLocalNameFromExpandedNameID(int n2) {
        return "";
    }

    public String getNamespaceFromExpandedNameID(int n2) {
        return "";
    }

    public String getLocalName(int n2) {
        return "";
    }

    public String getPrefix(int n2) {
        return null;
    }

    public String getNamespaceURI(int n2) {
        return "";
    }

    public String getNodeValue(int n2) {
        return this.getNodeIdent(n2) == 1 ? this._text : null;
    }

    public short getNodeType(int n2) {
        int n3 = this.getNodeIdent(n2);
        if (n3 == 1) {
            return 3;
        }
        if (n3 == 0) {
            return 0;
        }
        return -1;
    }

    public short getLevel(int n2) {
        int n3 = this.getNodeIdent(n2);
        if (n3 == 1) {
            return 2;
        }
        if (n3 == 0) {
            return 1;
        }
        return -1;
    }

    public boolean isSupported(String string, String string2) {
        return false;
    }

    public String getDocumentBaseURI() {
        return "";
    }

    public void setDocumentBaseURI(String string) {
    }

    public String getDocumentSystemIdentifier(int n2) {
        return null;
    }

    public String getDocumentEncoding(int n2) {
        return null;
    }

    public String getDocumentStandalone(int n2) {
        return null;
    }

    public String getDocumentVersion(int n2) {
        return null;
    }

    public boolean getDocumentAllDeclarationsProcessed() {
        return false;
    }

    public String getDocumentTypeDeclarationSystemIdentifier() {
        return null;
    }

    public String getDocumentTypeDeclarationPublicIdentifier() {
        return null;
    }

    public int getElementById(String string) {
        return -1;
    }

    public boolean supportsPreStripping() {
        return false;
    }

    public boolean isNodeAfter(int n2, int n3) {
        return this.lessThan(n2, n3);
    }

    public boolean isCharacterElementContentWhitespace(int n2) {
        return false;
    }

    public boolean isDocumentAllDeclarationsProcessed(int n2) {
        return false;
    }

    public boolean isAttributeSpecified(int n2) {
        return false;
    }

    public void dispatchCharactersEvents(int n2, ContentHandler contentHandler, boolean bl) throws SAXException {
    }

    public void dispatchToEvents(int n2, ContentHandler contentHandler) throws SAXException {
    }

    public Node getNode(int n2) {
        return this.makeNode(n2);
    }

    public boolean needsTwoThreads() {
        return false;
    }

    public ContentHandler getContentHandler() {
        return null;
    }

    public LexicalHandler getLexicalHandler() {
        return null;
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public DTDHandler getDTDHandler() {
        return null;
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public DeclHandler getDeclHandler() {
        return null;
    }

    public void appendChild(int n2, boolean bl, boolean bl2) {
    }

    public void appendTextChild(String string) {
    }

    public SourceLocator getSourceLocatorFor(int n2) {
        return null;
    }

    public void documentRegistration() {
    }

    public void documentRelease() {
    }

    public void migrateTo(DTMManager dTMManager) {
    }

    public final class SingletonIterator
    extends DTMAxisIteratorBase {
        static final int NO_TYPE = -1;
        int _type;
        int _currentNode;
        private final SimpleResultTreeImpl this$0;

        public SingletonIterator(SimpleResultTreeImpl simpleResultTreeImpl) {
            this.this$0 = simpleResultTreeImpl;
            this._type = -1;
        }

        public SingletonIterator(SimpleResultTreeImpl simpleResultTreeImpl, int n2) {
            this.this$0 = simpleResultTreeImpl;
            this._type = -1;
            this._type = n2;
        }

        public void setMark() {
            this._markedNode = this._currentNode;
        }

        public void gotoMark() {
            this._currentNode = this._markedNode;
        }

        public DTMAxisIterator setStartNode(int n2) {
            this._currentNode = this._startNode = this.this$0.getNodeIdent(n2);
            return this;
        }

        public int next() {
            if (this._currentNode == -1) {
                return -1;
            }
            this._currentNode = -1;
            if (this._type != -1) {
                if (this._currentNode == 0 && this._type == 0 || this._currentNode == 1 && this._type == 3) {
                    return this.this$0.getNodeHandle(this._currentNode);
                }
            } else {
                return this.this$0.getNodeHandle(this._currentNode);
            }
            return -1;
        }
    }

    public final class SimpleIterator
    extends DTMAxisIteratorBase {
        static final int DIRECTION_UP = 0;
        static final int DIRECTION_DOWN = 1;
        static final int NO_TYPE = -1;
        int _direction;
        int _type;
        int _currentNode;
        private final SimpleResultTreeImpl this$0;

        public SimpleIterator(SimpleResultTreeImpl simpleResultTreeImpl) {
            this.this$0 = simpleResultTreeImpl;
            this._direction = 1;
            this._type = -1;
        }

        public SimpleIterator(SimpleResultTreeImpl simpleResultTreeImpl, int n2) {
            this.this$0 = simpleResultTreeImpl;
            this._direction = 1;
            this._type = -1;
            this._direction = n2;
        }

        public SimpleIterator(SimpleResultTreeImpl simpleResultTreeImpl, int n2, int n3) {
            this.this$0 = simpleResultTreeImpl;
            this._direction = 1;
            this._type = -1;
            this._direction = n2;
            this._type = n3;
        }

        public int next() {
            if (this._direction == 1) {
                while (this._currentNode < 2) {
                    if (this._type != -1) {
                        if (this._currentNode == 0 && this._type == 0 || this._currentNode == 1 && this._type == 3) {
                            return this.returnNode(this.this$0.getNodeHandle(this._currentNode++));
                        }
                        ++this._currentNode;
                        continue;
                    }
                    return this.returnNode(this.this$0.getNodeHandle(this._currentNode++));
                }
                return -1;
            }
            while (this._currentNode >= 0) {
                if (this._type != -1) {
                    if (this._currentNode == 0 && this._type == 0 || this._currentNode == 1 && this._type == 3) {
                        return this.returnNode(this.this$0.getNodeHandle(this._currentNode--));
                    }
                    --this._currentNode;
                    continue;
                }
                return this.returnNode(this.this$0.getNodeHandle(this._currentNode--));
            }
            return -1;
        }

        public DTMAxisIterator setStartNode(int n2) {
            int n3;
            this._startNode = n3 = this.this$0.getNodeIdent(n2);
            if (!this._includeSelf && n3 != -1) {
                if (this._direction == 1) {
                    ++n3;
                } else if (this._direction == 0) {
                    --n3;
                }
            }
            this._currentNode = n3;
            return this;
        }

        public void setMark() {
            this._markedNode = this._currentNode;
        }

        public void gotoMark() {
            this._currentNode = this._markedNode;
        }
    }

}

