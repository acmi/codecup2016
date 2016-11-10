/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xalan.lib.sql.ObjectArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class DTMDocument
extends DTMDefaultBaseIterators {
    private boolean DEBUG = false;
    protected static final String S_NAMESPACE = "http://xml.apache.org/xalan/SQLExtension";
    protected static final String S_ATTRIB_NOT_SUPPORTED = "Not Supported";
    protected static final String S_ISTRUE = "true";
    protected static final String S_ISFALSE = "false";
    protected static final String S_DOCUMENT = "#root";
    protected static final String S_TEXT_NODE = "#text";
    protected static final String S_ELEMENT_NODE = "#element";
    protected int m_Document_TypeID = 0;
    protected int m_TextNode_TypeID = 0;
    protected ObjectArray m_ObjectArray = new ObjectArray();
    protected SuballocatedIntVector m_attribute = new SuballocatedIntVector(512);
    protected int m_DocumentIdx;

    public DTMDocument(DTMManager dTMManager, int n2) {
        super(dTMManager, null, n2, null, dTMManager.getXMLStringFactory(), true);
    }

    private int allocateNodeObject(Object object) {
        ++this.m_size;
        return this.m_ObjectArray.append(object);
    }

    protected int addElementWithData(Object object, int n2, int n3, int n4, int n5) {
        int n6 = this.addElement(n2, n3, n4, n5);
        int n7 = this.allocateNodeObject(object);
        this.m_firstch.setElementAt(n7, n6);
        this.m_exptype.setElementAt(this.m_TextNode_TypeID, n7);
        this.m_parent.setElementAt(n6, n7);
        this.m_prevsib.setElementAt(-1, n7);
        this.m_nextsib.setElementAt(-1, n7);
        this.m_attribute.setElementAt(-1, n7);
        this.m_firstch.setElementAt(-1, n7);
        return n6;
    }

    protected int addElement(int n2, int n3, int n4, int n5) {
        int n6 = -1;
        try {
            n6 = this.allocateNodeObject("#element");
            this.m_exptype.setElementAt(n3, n6);
            this.m_nextsib.setElementAt(-1, n6);
            this.m_prevsib.setElementAt(n5, n6);
            this.m_parent.setElementAt(n4, n6);
            this.m_firstch.setElementAt(-1, n6);
            this.m_attribute.setElementAt(-1, n6);
            if (n5 != -1) {
                if (this.m_nextsib.elementAt(n5) != -1) {
                    this.m_nextsib.setElementAt(this.m_nextsib.elementAt(n5), n6);
                }
                this.m_nextsib.setElementAt(n6, n5);
            }
            if (n4 != -1 && this.m_prevsib.elementAt(n6) == -1) {
                this.m_firstch.setElementAt(n6, n4);
            }
        }
        catch (Exception exception) {
            this.error("Error in addElement: " + exception.getMessage());
        }
        return n6;
    }

    protected int addAttributeToNode(Object object, int n2, int n3) {
        int n4 = -1;
        int n5 = -1;
        try {
            n4 = this.allocateNodeObject(object);
            this.m_attribute.setElementAt(-1, n4);
            this.m_exptype.setElementAt(n2, n4);
            this.m_nextsib.setElementAt(-1, n4);
            this.m_prevsib.setElementAt(-1, n4);
            this.m_parent.setElementAt(n3, n4);
            this.m_firstch.setElementAt(-1, n4);
            if (this.m_attribute.elementAt(n3) != -1) {
                n5 = this.m_attribute.elementAt(n3);
                this.m_nextsib.setElementAt(n5, n4);
                this.m_prevsib.setElementAt(n4, n5);
            }
            this.m_attribute.setElementAt(n4, n3);
        }
        catch (Exception exception) {
            this.error("Error in addAttributeToNode: " + exception.getMessage());
        }
        return n4;
    }

    protected void cloneAttributeFromNode(int n2, int n3) {
        try {
            if (this.m_attribute.elementAt(n2) != -1) {
                this.error("Cloneing Attributes, where from Node already had addtibures assigned");
            }
            this.m_attribute.setElementAt(this.m_attribute.elementAt(n3), n2);
        }
        catch (Exception exception) {
            this.error("Cloning attributes");
        }
    }

    public int getFirstAttribute(int n2) {
        int n3;
        if (this.DEBUG) {
            System.out.println("getFirstAttribute(" + n2 + ")");
        }
        if ((n3 = this.makeNodeIdentity(n2)) != -1) {
            int n4 = this.m_attribute.elementAt(n3);
            return this.makeNodeHandle(n4);
        }
        return -1;
    }

    public String getNodeValue(int n2) {
        if (this.DEBUG) {
            System.out.println("getNodeValue(" + n2 + ")");
        }
        try {
            Object object = this.m_ObjectArray.getAt(this.makeNodeIdentity(n2));
            if (object != null && object != "#element") {
                return object.toString();
            }
            return "";
        }
        catch (Exception exception) {
            this.error("Getting String Value");
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XMLString getStringValue(int n2) {
        Object object;
        int n3 = this.makeNodeIdentity(n2);
        if (this.DEBUG) {
            System.out.println("getStringValue(" + n3 + ")");
        }
        if ((object = this.m_ObjectArray.getAt(n3)) == "#element") {
            String string;
            FastStringBuffer fastStringBuffer = StringBufferPool.get();
            try {
                this.getNodeData(n3, fastStringBuffer);
                string = fastStringBuffer.length() > 0 ? fastStringBuffer.toString() : "";
            }
            finally {
                StringBufferPool.free(fastStringBuffer);
            }
            return this.m_xstrf.newstr(string);
        }
        if (object != null) {
            return this.m_xstrf.newstr(object.toString());
        }
        return this.m_xstrf.emptystr();
    }

    protected void getNodeData(int n2, FastStringBuffer fastStringBuffer) {
        int n3 = this._firstch(n2);
        while (n3 != -1) {
            Object object = this.m_ObjectArray.getAt(n3);
            if (object == "#element") {
                this.getNodeData(n3, fastStringBuffer);
            } else if (object != null) {
                fastStringBuffer.append(object.toString());
            }
            n3 = this._nextsib(n3);
        }
    }

    public int getNextAttribute(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (this.DEBUG) {
            System.out.println("getNextAttribute(" + n3 + ")");
        }
        if (n3 != -1) {
            return this.makeNodeHandle(this.m_nextsib.elementAt(n3));
        }
        return -1;
    }

    protected int getNumberOfNodes() {
        if (this.DEBUG) {
            System.out.println("getNumberOfNodes()");
        }
        return this.m_size;
    }

    protected boolean nextNode() {
        if (this.DEBUG) {
            System.out.println("nextNode()");
        }
        return false;
    }

    protected void createExpandedNameTable() {
        this.m_Document_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "#root", 9);
        this.m_TextNode_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "#text", 3);
    }

    public void dumpDTM() {
        try {
            File file = new File("DTMDump.txt");
            System.err.println("Dumping... " + file.getAbsolutePath());
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            while (this.nextNode()) {
            }
            int n2 = this.m_size;
            printStream.println("Total nodes: " + n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                String string;
                printStream.println("=========== " + i2 + " ===========");
                printStream.println("NodeName: " + this.getNodeName(this.makeNodeHandle(i2)));
                printStream.println("NodeNameX: " + this.getNodeNameX(this.makeNodeHandle(i2)));
                printStream.println("LocalName: " + this.getLocalName(this.makeNodeHandle(i2)));
                printStream.println("NamespaceURI: " + this.getNamespaceURI(this.makeNodeHandle(i2)));
                printStream.println("Prefix: " + this.getPrefix(this.makeNodeHandle(i2)));
                int n3 = this.getExpandedTypeID(this.makeNodeHandle(i2));
                printStream.println("Expanded Type ID: " + Integer.toHexString(n3));
                short s2 = this.getNodeType(this.makeNodeHandle(i2));
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
                        string = "DOCUMENT_NODE";
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
                    case 13: {
                        string = "NAMESPACE_NODE";
                        break;
                    }
                    case 12: {
                        string = "NOTATION_NODE";
                        break;
                    }
                    case -1: {
                        string = "NULL";
                        break;
                    }
                    case 7: {
                        string = "PROCESSING_INSTRUCTION_NODE";
                        break;
                    }
                    case 3: {
                        string = "TEXT_NODE";
                        break;
                    }
                    default: {
                        string = "Unknown!";
                    }
                }
                printStream.println("Type: " + string);
                int n4 = this._firstch(i2);
                if (-1 == n4) {
                    printStream.println("First child: DTM.NULL");
                } else if (-2 == n4) {
                    printStream.println("First child: NOTPROCESSED");
                } else {
                    printStream.println("First child: " + n4);
                }
                int n5 = this._prevsib(i2);
                if (-1 == n5) {
                    printStream.println("Prev sibling: DTM.NULL");
                } else if (-2 == n5) {
                    printStream.println("Prev sibling: NOTPROCESSED");
                } else {
                    printStream.println("Prev sibling: " + n5);
                }
                int n6 = this._nextsib(i2);
                if (-1 == n6) {
                    printStream.println("Next sibling: DTM.NULL");
                } else if (-2 == n6) {
                    printStream.println("Next sibling: NOTPROCESSED");
                } else {
                    printStream.println("Next sibling: " + n6);
                }
                int n7 = this._parent(i2);
                if (-1 == n7) {
                    printStream.println("Parent: DTM.NULL");
                } else if (-2 == n7) {
                    printStream.println("Parent: NOTPROCESSED");
                } else {
                    printStream.println("Parent: " + n7);
                }
                int n8 = this._level(i2);
                printStream.println("Level: " + n8);
                printStream.println("Node Value: " + this.getNodeValue(i2));
                printStream.println("String Value: " + this.getStringValue(i2));
                printStream.println("First Attribute Node: " + this.m_attribute.elementAt(i2));
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace(System.err);
            throw new RuntimeException(iOException.getMessage());
        }
    }

    protected static void dispatchNodeData(Node node, ContentHandler contentHandler, int n2) throws SAXException {
        switch (node.getNodeType()) {
            case 1: 
            case 9: 
            case 11: {
                for (Node node2 = node.getFirstChild(); null != node2; node2 = node2.getNextSibling()) {
                    DTMDocument.dispatchNodeData(node2, contentHandler, n2 + 1);
                }
                break;
            }
            case 7: 
            case 8: {
                if (0 != n2) break;
            }
            case 2: 
            case 3: 
            case 4: {
                String string = node.getNodeValue();
                if (contentHandler instanceof CharacterNodeHandler) {
                    ((CharacterNodeHandler)((Object)contentHandler)).characters(node);
                    break;
                }
                contentHandler.characters(string.toCharArray(), 0, string.length());
                break;
            }
        }
    }

    public void setProperty(String string, Object object) {
    }

    public SourceLocator getSourceLocatorFor(int n2) {
        return null;
    }

    protected int getNextNodeIdentity(int n2) {
        if (this.DEBUG) {
            System.out.println("getNextNodeIdenty(" + n2 + ")");
        }
        return -1;
    }

    public int getAttributeNode(int n2, String string, String string2) {
        if (this.DEBUG) {
            System.out.println("getAttributeNode(" + n2 + "," + string + "," + string2 + ")");
        }
        return -1;
    }

    public String getLocalName(int n2) {
        int n3 = this.getExpandedTypeID(n2);
        if (this.DEBUG) {
            this.DEBUG = false;
            System.out.print("getLocalName(" + n2 + ") -> ");
            System.out.println("..." + this.getLocalNameFromExpandedNameID(n3));
            this.DEBUG = true;
        }
        return this.getLocalNameFromExpandedNameID(n3);
    }

    public String getNodeName(int n2) {
        int n3 = this.getExpandedTypeID(n2);
        if (this.DEBUG) {
            this.DEBUG = false;
            System.out.print("getLocalName(" + n2 + ") -> ");
            System.out.println("..." + this.getLocalNameFromExpandedNameID(n3));
            this.DEBUG = true;
        }
        return this.getLocalNameFromExpandedNameID(n3);
    }

    public boolean isAttributeSpecified(int n2) {
        if (this.DEBUG) {
            System.out.println("isAttributeSpecified(" + n2 + ")");
        }
        return false;
    }

    public String getUnparsedEntityURI(String string) {
        if (this.DEBUG) {
            System.out.println("getUnparsedEntityURI(" + string + ")");
        }
        return "";
    }

    public DTDHandler getDTDHandler() {
        if (this.DEBUG) {
            System.out.println("getDTDHandler()");
        }
        return null;
    }

    public String getPrefix(int n2) {
        if (this.DEBUG) {
            System.out.println("getPrefix(" + n2 + ")");
        }
        return "";
    }

    public EntityResolver getEntityResolver() {
        if (this.DEBUG) {
            System.out.println("getEntityResolver()");
        }
        return null;
    }

    public String getDocumentTypeDeclarationPublicIdentifier() {
        if (this.DEBUG) {
            System.out.println("get_DTD_PubId()");
        }
        return "";
    }

    public LexicalHandler getLexicalHandler() {
        if (this.DEBUG) {
            System.out.println("getLexicalHandler()");
        }
        return null;
    }

    public boolean needsTwoThreads() {
        if (this.DEBUG) {
            System.out.println("needsTwoThreads()");
        }
        return false;
    }

    public ContentHandler getContentHandler() {
        if (this.DEBUG) {
            System.out.println("getContentHandler()");
        }
        return null;
    }

    public void dispatchToEvents(int n2, ContentHandler contentHandler) throws SAXException {
        if (this.DEBUG) {
            System.out.println("dispathcToEvents(" + n2 + "," + contentHandler + ")");
        }
    }

    public String getNamespaceURI(int n2) {
        if (this.DEBUG) {
            System.out.println("getNamespaceURI(" + n2 + ")");
        }
        return "";
    }

    public void dispatchCharactersEvents(int n2, ContentHandler contentHandler, boolean bl) throws SAXException {
        if (this.DEBUG) {
            System.out.println("dispatchCharacterEvents(" + n2 + "," + contentHandler + "," + bl + ")");
        }
        if (bl) {
            XMLString xMLString = this.getStringValue(n2);
            xMLString = xMLString.fixWhiteSpace(true, true, false);
            xMLString.dispatchCharactersEvents(contentHandler);
        } else {
            Node node = this.getNode(n2);
            DTMDocument.dispatchNodeData(node, contentHandler, 0);
        }
    }

    public boolean supportsPreStripping() {
        if (this.DEBUG) {
            System.out.println("supportsPreStripping()");
        }
        return super.supportsPreStripping();
    }

    protected int _exptype(int n2) {
        if (this.DEBUG) {
            System.out.println("_exptype(" + n2 + ")");
        }
        return super._exptype(n2);
    }

    protected SuballocatedIntVector findNamespaceContext(int n2) {
        if (this.DEBUG) {
            System.out.println("SuballocatedIntVector(" + n2 + ")");
        }
        return super.findNamespaceContext(n2);
    }

    protected int _prevsib(int n2) {
        if (this.DEBUG) {
            System.out.println("_prevsib(" + n2 + ")");
        }
        return super._prevsib(n2);
    }

    protected short _type(int n2) {
        if (this.DEBUG) {
            System.out.println("_type(" + n2 + ")");
        }
        return super._type(n2);
    }

    public Node getNode(int n2) {
        if (this.DEBUG) {
            System.out.println("getNode(" + n2 + ")");
        }
        return super.getNode(n2);
    }

    public int getPreviousSibling(int n2) {
        if (this.DEBUG) {
            System.out.println("getPrevSib(" + n2 + ")");
        }
        return super.getPreviousSibling(n2);
    }

    public String getDocumentStandalone(int n2) {
        if (this.DEBUG) {
            System.out.println("getDOcStandAlone(" + n2 + ")");
        }
        return super.getDocumentStandalone(n2);
    }

    public String getNodeNameX(int n2) {
        if (this.DEBUG) {
            System.out.println("getNodeNameX(" + n2 + ")");
        }
        return this.getNodeName(n2);
    }

    public void setFeature(String string, boolean bl) {
        if (this.DEBUG) {
            System.out.println("setFeature(" + string + "," + bl + ")");
        }
        super.setFeature(string, bl);
    }

    protected int _parent(int n2) {
        if (this.DEBUG) {
            System.out.println("_parent(" + n2 + ")");
        }
        return super._parent(n2);
    }

    protected void indexNode(int n2, int n3) {
        if (this.DEBUG) {
            System.out.println("indexNode(" + n2 + "," + n3 + ")");
        }
        super.indexNode(n2, n3);
    }

    protected boolean getShouldStripWhitespace() {
        if (this.DEBUG) {
            System.out.println("getShouldStripWS()");
        }
        return super.getShouldStripWhitespace();
    }

    protected void popShouldStripWhitespace() {
        if (this.DEBUG) {
            System.out.println("popShouldStripWS()");
        }
        super.popShouldStripWhitespace();
    }

    public boolean isNodeAfter(int n2, int n3) {
        if (this.DEBUG) {
            System.out.println("isNodeAfter(" + n2 + "," + n3 + ")");
        }
        return super.isNodeAfter(n2, n3);
    }

    public int getNamespaceType(int n2) {
        if (this.DEBUG) {
            System.out.println("getNamespaceType(" + n2 + ")");
        }
        return super.getNamespaceType(n2);
    }

    protected int _level(int n2) {
        if (this.DEBUG) {
            System.out.println("_level(" + n2 + ")");
        }
        return super._level(n2);
    }

    protected void pushShouldStripWhitespace(boolean bl) {
        if (this.DEBUG) {
            System.out.println("push_ShouldStripWS(" + bl + ")");
        }
        super.pushShouldStripWhitespace(bl);
    }

    public String getDocumentVersion(int n2) {
        if (this.DEBUG) {
            System.out.println("getDocVer(" + n2 + ")");
        }
        return super.getDocumentVersion(n2);
    }

    public boolean isSupported(String string, String string2) {
        if (this.DEBUG) {
            System.out.println("isSupported(" + string + "," + string2 + ")");
        }
        return super.isSupported(string, string2);
    }

    protected void setShouldStripWhitespace(boolean bl) {
        if (this.DEBUG) {
            System.out.println("set_ShouldStripWS(" + bl + ")");
        }
        super.setShouldStripWhitespace(bl);
    }

    protected void ensureSizeOfIndex(int n2, int n3) {
        if (this.DEBUG) {
            System.out.println("ensureSizeOfIndex(" + n2 + "," + n3 + ")");
        }
        super.ensureSizeOfIndex(n2, n3);
    }

    protected void ensureSize(int n2) {
        if (this.DEBUG) {
            System.out.println("ensureSize(" + n2 + ")");
        }
    }

    public String getDocumentEncoding(int n2) {
        if (this.DEBUG) {
            System.out.println("getDocumentEncoding(" + n2 + ")");
        }
        return super.getDocumentEncoding(n2);
    }

    public void appendChild(int n2, boolean bl, boolean bl2) {
        if (this.DEBUG) {
            System.out.println("appendChild(" + n2 + "," + bl + "," + bl2 + ")");
        }
        super.appendChild(n2, bl, bl2);
    }

    public short getLevel(int n2) {
        if (this.DEBUG) {
            System.out.println("getLevel(" + n2 + ")");
        }
        return super.getLevel(n2);
    }

    public String getDocumentBaseURI() {
        if (this.DEBUG) {
            System.out.println("getDocBaseURI()");
        }
        return super.getDocumentBaseURI();
    }

    public int getNextNamespaceNode(int n2, int n3, boolean bl) {
        if (this.DEBUG) {
            System.out.println("getNextNamesapceNode(" + n2 + "," + n3 + "," + bl + ")");
        }
        return super.getNextNamespaceNode(n2, n3, bl);
    }

    public void appendTextChild(String string) {
        if (this.DEBUG) {
            System.out.println("appendTextChild(" + string + ")");
        }
        super.appendTextChild(string);
    }

    protected int findGTE(int[] arrn, int n2, int n3, int n4) {
        if (this.DEBUG) {
            System.out.println("findGTE(" + arrn + "," + n2 + "," + n3 + ")");
        }
        return super.findGTE(arrn, n2, n3, n4);
    }

    public int getFirstNamespaceNode(int n2, boolean bl) {
        if (this.DEBUG) {
            System.out.println("getFirstNamespaceNode()");
        }
        return super.getFirstNamespaceNode(n2, bl);
    }

    public int getStringValueChunkCount(int n2) {
        if (this.DEBUG) {
            System.out.println("getStringChunkCount(" + n2 + ")");
        }
        return super.getStringValueChunkCount(n2);
    }

    public int getLastChild(int n2) {
        if (this.DEBUG) {
            System.out.println("getLastChild(" + n2 + ")");
        }
        return super.getLastChild(n2);
    }

    public boolean hasChildNodes(int n2) {
        if (this.DEBUG) {
            System.out.println("hasChildNodes(" + n2 + ")");
        }
        return super.hasChildNodes(n2);
    }

    public short getNodeType(int n2) {
        if (this.DEBUG) {
            this.DEBUG = false;
            System.out.print("getNodeType(" + n2 + ") ");
            int n3 = this.getExpandedTypeID(n2);
            String string = this.getLocalNameFromExpandedNameID(n3);
            System.out.println(".. Node name [" + string + "]" + "[" + this.getNodeType(n2) + "]");
            this.DEBUG = true;
        }
        return super.getNodeType(n2);
    }

    public boolean isCharacterElementContentWhitespace(int n2) {
        if (this.DEBUG) {
            System.out.println("isCharacterElementContentWhitespace(" + n2 + ")");
        }
        return super.isCharacterElementContentWhitespace(n2);
    }

    public int getFirstChild(int n2) {
        if (this.DEBUG) {
            System.out.println("getFirstChild(" + n2 + ")");
        }
        return super.getFirstChild(n2);
    }

    public String getDocumentSystemIdentifier(int n2) {
        if (this.DEBUG) {
            System.out.println("getDocSysID(" + n2 + ")");
        }
        return super.getDocumentSystemIdentifier(n2);
    }

    protected void declareNamespaceInContext(int n2, int n3) {
        if (this.DEBUG) {
            System.out.println("declareNamespaceContext(" + n2 + "," + n3 + ")");
        }
        super.declareNamespaceInContext(n2, n3);
    }

    public String getNamespaceFromExpandedNameID(int n2) {
        if (this.DEBUG) {
            this.DEBUG = false;
            System.out.print("getNamespaceFromExpandedNameID(" + n2 + ")");
            System.out.println("..." + super.getNamespaceFromExpandedNameID(n2));
            this.DEBUG = true;
        }
        return super.getNamespaceFromExpandedNameID(n2);
    }

    public String getLocalNameFromExpandedNameID(int n2) {
        if (this.DEBUG) {
            this.DEBUG = false;
            System.out.print("getLocalNameFromExpandedNameID(" + n2 + ")");
            System.out.println("..." + super.getLocalNameFromExpandedNameID(n2));
            this.DEBUG = true;
        }
        return super.getLocalNameFromExpandedNameID(n2);
    }

    public int getExpandedTypeID(int n2) {
        if (this.DEBUG) {
            System.out.println("getExpandedTypeID(" + n2 + ")");
        }
        return super.getExpandedTypeID(n2);
    }

    public int getDocument() {
        if (this.DEBUG) {
            System.out.println("getDocument()");
        }
        return super.getDocument();
    }

    protected int findInSortedSuballocatedIntVector(SuballocatedIntVector suballocatedIntVector, int n2) {
        if (this.DEBUG) {
            System.out.println("findInSortedSubAlloctedVector(" + suballocatedIntVector + "," + n2 + ")");
        }
        return super.findInSortedSuballocatedIntVector(suballocatedIntVector, n2);
    }

    public boolean isDocumentAllDeclarationsProcessed(int n2) {
        if (this.DEBUG) {
            System.out.println("isDocumentAllDeclProc(" + n2 + ")");
        }
        return super.isDocumentAllDeclarationsProcessed(n2);
    }

    protected void error(String string) {
        if (this.DEBUG) {
            System.out.println("error(" + string + ")");
        }
        super.error(string);
    }

    protected int _firstch(int n2) {
        if (this.DEBUG) {
            System.out.println("_firstch(" + n2 + ")");
        }
        return super._firstch(n2);
    }

    public int getOwnerDocument(int n2) {
        if (this.DEBUG) {
            System.out.println("getOwnerDoc(" + n2 + ")");
        }
        return super.getOwnerDocument(n2);
    }

    protected int _nextsib(int n2) {
        if (this.DEBUG) {
            System.out.println("_nextSib(" + n2 + ")");
        }
        return super._nextsib(n2);
    }

    public int getNextSibling(int n2) {
        if (this.DEBUG) {
            System.out.println("getNextSibling(" + n2 + ")");
        }
        return super.getNextSibling(n2);
    }

    public boolean getDocumentAllDeclarationsProcessed() {
        if (this.DEBUG) {
            System.out.println("getDocAllDeclProc()");
        }
        return super.getDocumentAllDeclarationsProcessed();
    }

    public int getParent(int n2) {
        if (this.DEBUG) {
            System.out.println("getParent(" + n2 + ")");
        }
        return super.getParent(n2);
    }

    public int getExpandedTypeID(String string, String string2, int n2) {
        if (this.DEBUG) {
            System.out.println("getExpandedTypeID()");
        }
        return super.getExpandedTypeID(string, string2, n2);
    }

    public void setDocumentBaseURI(String string) {
        if (this.DEBUG) {
            System.out.println("setDocBaseURI()");
        }
        super.setDocumentBaseURI(string);
    }

    public char[] getStringValueChunk(int n2, int n3, int[] arrn) {
        if (this.DEBUG) {
            System.out.println("getStringChunkValue(" + n2 + "," + n3 + ")");
        }
        return super.getStringValueChunk(n2, n3, arrn);
    }

    public DTMAxisTraverser getAxisTraverser(int n2) {
        if (this.DEBUG) {
            System.out.println("getAxixTraverser(" + n2 + ")");
        }
        return super.getAxisTraverser(n2);
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        if (this.DEBUG) {
            System.out.println("getTypedAxisIterator(" + n2 + "," + n3 + ")");
        }
        return super.getTypedAxisIterator(n2, n3);
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        if (this.DEBUG) {
            System.out.println("getAxisIterator(" + n2 + ")");
        }
        return super.getAxisIterator(n2);
    }

    public int getElementById(String string) {
        if (this.DEBUG) {
            System.out.println("getElementByID(" + string + ")");
        }
        return -1;
    }

    public DeclHandler getDeclHandler() {
        if (this.DEBUG) {
            System.out.println("getDeclHandler()");
        }
        return null;
    }

    public ErrorHandler getErrorHandler() {
        if (this.DEBUG) {
            System.out.println("getErrorHandler()");
        }
        return null;
    }

    public String getDocumentTypeDeclarationSystemIdentifier() {
        if (this.DEBUG) {
            System.out.println("get_DTD-SID()");
        }
        return null;
    }

    public static interface CharacterNodeHandler {
        public void characters(Node var1) throws SAXException;
    }

}

