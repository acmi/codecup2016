/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref.dom2dtm;

import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.dom.DOMSource;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.TreeWalker;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class DOM2DTM
extends DTMDefaultBaseIterators {
    private transient Node m_pos;
    private int m_last_parent = 0;
    private int m_last_kid = -1;
    private transient Node m_root;
    boolean m_processedFirstElement = false;
    private transient boolean m_nodesAreProcessed;
    protected Vector m_nodes = new Vector();
    TreeWalker m_walker = new TreeWalker(null);

    public DOM2DTM(DTMManager dTMManager, DOMSource dOMSource, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        super(dTMManager, dOMSource, n2, dTMWSFilter, xMLStringFactory, bl);
        this.m_pos = this.m_root = dOMSource.getNode();
        this.m_last_kid = -1;
        this.m_last_parent = -1;
        this.m_last_kid = this.addNode(this.m_root, this.m_last_parent, this.m_last_kid, -1);
        if (1 == this.m_root.getNodeType()) {
            int n3;
            NamedNodeMap namedNodeMap = this.m_root.getAttributes();
            int n4 = n3 = namedNodeMap == null ? 0 : namedNodeMap.getLength();
            if (n3 > 0) {
                int n5 = -1;
                for (int i2 = 0; i2 < n3; ++i2) {
                    n5 = this.addNode(namedNodeMap.item(i2), 0, n5, -1);
                    this.m_firstch.setElementAt(-1, n5);
                }
                this.m_nextsib.setElementAt(-1, n5);
            }
        }
        this.m_nodesAreProcessed = false;
    }

    protected int addNode(Node node, int n2, int n3, int n4) {
        String string;
        String string2;
        int n5 = this.m_nodes.size();
        if (this.m_dtmIdent.size() == n5 >>> 16) {
            try {
                if (this.m_mgr == null) {
                    throw new ClassCastException();
                }
                DTMManagerDefault dTMManagerDefault = (DTMManagerDefault)this.m_mgr;
                int n6 = dTMManagerDefault.getFirstFreeDTMID();
                dTMManagerDefault.addDTM(this, n6, n5);
                this.m_dtmIdent.addElement(n6 << 16);
            }
            catch (ClassCastException classCastException) {
                this.error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
            }
        }
        ++this.m_size;
        int n7 = -1 == n4 ? (int)node.getNodeType() : n4;
        if (2 == n7 && ((string2 = node.getNodeName()).startsWith("xmlns:") || string2.equals("xmlns"))) {
            n7 = 13;
        }
        this.m_nodes.addElement(node);
        this.m_firstch.setElementAt(-2, n5);
        this.m_nextsib.setElementAt(-2, n5);
        this.m_prevsib.setElementAt(n3, n5);
        this.m_parent.setElementAt(n2, n5);
        if (-1 != n2 && n7 != 2 && n7 != 13 && -2 == this.m_firstch.elementAt(n2)) {
            this.m_firstch.setElementAt(n5, n2);
        }
        String string3 = node.getNamespaceURI();
        String string4 = string = n7 == 7 ? node.getNodeName() : node.getLocalName();
        if ((n7 == 1 || n7 == 2) && null == string) {
            string = node.getNodeName();
        }
        ExpandedNameTable expandedNameTable = this.m_expandedNameTable;
        if (node.getLocalName() != null || n7 == 1 || n7 == 2) {
            // empty if block
        }
        int n8 = null != string ? expandedNameTable.getExpandedTypeID(string3, string, n7) : expandedNameTable.getExpandedTypeID(n7);
        this.m_exptype.setElementAt(n8, n5);
        this.indexNode(n8, n5);
        if (-1 != n3) {
            this.m_nextsib.setElementAt(n5, n3);
        }
        if (n7 == 13) {
            this.declareNamespaceInContext(n2, n5);
        }
        return n5;
    }

    public int getNumberOfNodes() {
        return this.m_nodes.size();
    }

    protected boolean nextNode() {
        int n2;
        if (this.m_nodesAreProcessed) {
            return false;
        }
        Node node = this.m_pos;
        Node node2 = null;
        int n3 = -1;
        do {
            if (node.hasChildNodes()) {
                node2 = node.getFirstChild();
                if (node2 != null && 10 == node2.getNodeType()) {
                    node2 = node2.getNextSibling();
                }
                if (5 != node.getNodeType()) {
                    this.m_last_parent = this.m_last_kid;
                    this.m_last_kid = -1;
                    if (null != this.m_wsfilter) {
                        n2 = this.m_wsfilter.getShouldStripSpace(this.makeNodeHandle(this.m_last_parent), this);
                        boolean bl = 3 == n2 ? this.getShouldStripWhitespace() : 2 == n2;
                        this.pushShouldStripWhitespace(bl);
                    }
                }
            } else {
                if (this.m_last_kid != -1 && this.m_firstch.elementAt(this.m_last_kid) == -2) {
                    this.m_firstch.setElementAt(-1, this.m_last_kid);
                }
                while (this.m_last_parent != -1) {
                    node2 = node.getNextSibling();
                    if (node2 != null && 10 == node2.getNodeType()) {
                        node2 = node2.getNextSibling();
                    }
                    if (node2 != null) break;
                    if ((node = node.getParentNode()) == null) {
                        // empty if block
                    }
                    if (node != null && 5 == node.getNodeType()) continue;
                    this.popShouldStripWhitespace();
                    if (this.m_last_kid == -1) {
                        this.m_firstch.setElementAt(-1, this.m_last_parent);
                    } else {
                        this.m_nextsib.setElementAt(-1, this.m_last_kid);
                    }
                    this.m_last_kid = this.m_last_parent;
                    this.m_last_parent = this.m_parent.elementAt(this.m_last_kid);
                }
                if (this.m_last_parent == -1) {
                    node2 = null;
                }
            }
            if (node2 != null) {
                n3 = node2.getNodeType();
            }
            if (5 != n3) continue;
            node = node2;
        } while (5 == n3);
        if (node2 == null) {
            this.m_nextsib.setElementAt(-1, 0);
            this.m_nodesAreProcessed = true;
            this.m_pos = null;
            return false;
        }
        n2 = 0;
        Node node3 = null;
        n3 = node2.getNodeType();
        if (3 == n3 || 4 == n3) {
            n2 = null != this.m_wsfilter && this.getShouldStripWhitespace() ? 1 : 0;
            Node node4 = node2;
            while (node4 != null) {
                node3 = node4;
                if (3 == node4.getNodeType()) {
                    n3 = 3;
                }
                n2 &= XMLCharacterRecognizer.isWhiteSpace(node4.getNodeValue());
                node4 = this.logicalNextDOMTextNode(node4);
            }
        } else if (7 == n3) {
            n2 = (int)node.getNodeName().toLowerCase().equals("xml") ? 1 : 0;
        }
        if (n2 == 0) {
            int n4;
            this.m_last_kid = n4 = this.addNode(node2, this.m_last_parent, this.m_last_kid, n3);
            if (1 == n3) {
                int n5;
                int n6 = -1;
                NamedNodeMap namedNodeMap = node2.getAttributes();
                int n7 = n5 = namedNodeMap == null ? 0 : namedNodeMap.getLength();
                if (n5 > 0) {
                    for (int i2 = 0; i2 < n5; ++i2) {
                        n6 = this.addNode(namedNodeMap.item(i2), n4, n6, -1);
                        this.m_firstch.setElementAt(-1, n6);
                        if (this.m_processedFirstElement || !"xmlns:xml".equals(namedNodeMap.item(i2).getNodeName())) continue;
                        this.m_processedFirstElement = true;
                    }
                }
                if (!this.m_processedFirstElement) {
                    n6 = this.addNode(new DOM2DTMdefaultNamespaceDeclarationNode((Element)node2, "xml", "http://www.w3.org/XML/1998/namespace", this.makeNodeHandle((n6 == -1 ? n4 : n6) + 1)), n4, n6, -1);
                    this.m_firstch.setElementAt(-1, n6);
                    this.m_processedFirstElement = true;
                }
                if (n6 != -1) {
                    this.m_nextsib.setElementAt(-1, n6);
                }
            }
        }
        if (3 == n3 || 4 == n3) {
            node2 = node3;
        }
        this.m_pos = node2;
        return true;
    }

    public Node getNode(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        return (Node)this.m_nodes.elementAt(n3);
    }

    protected Node lookupNode(int n2) {
        return (Node)this.m_nodes.elementAt(n2);
    }

    protected int getNextNodeIdentity(int n2) {
        if (++n2 >= this.m_nodes.size() && !this.nextNode()) {
            n2 = -1;
        }
        return n2;
    }

    private int getHandleFromNode(Node node) {
        if (null != node) {
            int n2 = this.m_nodes.size();
            int n3 = 0;
            do {
                if (n3 < n2) {
                    if (this.m_nodes.elementAt(n3) == node) {
                        return this.makeNodeHandle(n3);
                    }
                    ++n3;
                    continue;
                }
                boolean bl = this.nextNode();
                n2 = this.m_nodes.size();
                if (!bl && n3 >= n2) break;
            } while (true);
        }
        return -1;
    }

    public int getHandleOfNode(Node node) {
        if (null != node && (this.m_root == node || this.m_root.getNodeType() == 9 && this.m_root == node.getOwnerDocument() || this.m_root.getNodeType() != 9 && this.m_root.getOwnerDocument() == node.getOwnerDocument())) {
            Node node2 = node;
            while (node2 != null) {
                if (node2 == this.m_root) {
                    return this.getHandleFromNode(node);
                }
                node2 = node2.getNodeType() != 2 ? node2.getParentNode() : ((Attr)node2).getOwnerElement();
            }
        }
        return -1;
    }

    public int getAttributeNode(int n2, String string, String string2) {
        short s2;
        if (null == string) {
            string = "";
        }
        if (1 == (s2 = this.getNodeType(n2))) {
            int n3 = this.makeNodeIdentity(n2);
            while (-1 != (n3 = this.getNextNodeIdentity(n3)) && ((s2 = this._type(n3)) == 2 || s2 == 13)) {
                Node node = this.lookupNode(n3);
                String string3 = node.getNamespaceURI();
                if (null == string3) {
                    string3 = "";
                }
                String string4 = node.getLocalName();
                if (!string3.equals(string) || !string2.equals(string4)) continue;
                return this.makeNodeHandle(n3);
            }
        }
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XMLString getStringValue(int n2) {
        short s2 = this.getNodeType(n2);
        Node node = this.getNode(n2);
        if (1 == s2 || 9 == s2 || 11 == s2) {
            String string;
            FastStringBuffer fastStringBuffer = StringBufferPool.get();
            try {
                DOM2DTM.getNodeData(node, fastStringBuffer);
                string = fastStringBuffer.length() > 0 ? fastStringBuffer.toString() : "";
            }
            finally {
                StringBufferPool.free(fastStringBuffer);
            }
            return this.m_xstrf.newstr(string);
        }
        if (3 == s2 || 4 == s2) {
            FastStringBuffer fastStringBuffer = StringBufferPool.get();
            while (node != null) {
                fastStringBuffer.append(node.getNodeValue());
                node = this.logicalNextDOMTextNode(node);
            }
            String string = fastStringBuffer.length() > 0 ? fastStringBuffer.toString() : "";
            StringBufferPool.free(fastStringBuffer);
            return this.m_xstrf.newstr(string);
        }
        return this.m_xstrf.newstr(node.getNodeValue());
    }

    protected static void getNodeData(Node node, FastStringBuffer fastStringBuffer) {
        switch (node.getNodeType()) {
            case 1: 
            case 9: 
            case 11: {
                for (Node node2 = node.getFirstChild(); null != node2; node2 = node2.getNextSibling()) {
                    DOM2DTM.getNodeData(node2, fastStringBuffer);
                }
                break;
            }
            case 2: 
            case 3: 
            case 4: {
                fastStringBuffer.append(node.getNodeValue());
                break;
            }
            case 7: {
                break;
            }
        }
    }

    public String getNodeName(int n2) {
        Node node = this.getNode(n2);
        return node.getNodeName();
    }

    public String getNodeNameX(int n2) {
        String string;
        short s2 = this.getNodeType(n2);
        switch (s2) {
            case 13: {
                Node node = this.getNode(n2);
                string = node.getNodeName();
                if (string.startsWith("xmlns:")) {
                    string = QName.getLocalPart(string);
                    break;
                }
                if (!string.equals("xmlns")) break;
                string = "";
                break;
            }
            case 1: 
            case 2: 
            case 5: 
            case 7: {
                Node node = this.getNode(n2);
                string = node.getNodeName();
                break;
            }
            default: {
                string = "";
            }
        }
        return string;
    }

    public String getLocalName(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (-1 == n3) {
            return null;
        }
        Node node = (Node)this.m_nodes.elementAt(n3);
        String string = node.getLocalName();
        if (null == string) {
            int n4;
            String string2 = node.getNodeName();
            string = '#' == string2.charAt(0) ? "" : ((n4 = string2.indexOf(58)) < 0 ? string2 : string2.substring(n4 + 1));
        }
        return string;
    }

    public String getPrefix(int n2) {
        String string;
        short s2 = this.getNodeType(n2);
        switch (s2) {
            case 13: {
                Node node = this.getNode(n2);
                String string2 = node.getNodeName();
                int n3 = string2.indexOf(58);
                string = n3 < 0 ? "" : string2.substring(n3 + 1);
                break;
            }
            case 1: 
            case 2: {
                Node node = this.getNode(n2);
                String string3 = node.getNodeName();
                int n4 = string3.indexOf(58);
                string = n4 < 0 ? "" : string3.substring(0, n4);
                break;
            }
            default: {
                string = "";
            }
        }
        return string;
    }

    public String getNamespaceURI(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return null;
        }
        Node node = (Node)this.m_nodes.elementAt(n3);
        return node.getNamespaceURI();
    }

    private Node logicalNextDOMTextNode(Node node) {
        short s2;
        Node node2 = node.getNextSibling();
        if (node2 == null) {
            for (node = node.getParentNode(); node != null && 5 == node.getNodeType() && (node2 = node.getNextSibling()) == null; node = node.getParentNode()) {
            }
        }
        node = node2;
        while (node != null && 5 == node.getNodeType()) {
            if (node.hasChildNodes()) {
                node = node.getFirstChild();
                continue;
            }
            node = node.getNextSibling();
        }
        if (node != null && 3 != (s2 = node.getNodeType()) && 4 != s2) {
            node = null;
        }
        return node;
    }

    public String getNodeValue(int n2) {
        int n3 = this._exptype(this.makeNodeIdentity(n2));
        int n4 = n3 = -1 != n3 ? (int)this.getNodeType(n2) : -1;
        if (3 != n3 && 4 != n3) {
            return this.getNode(n2).getNodeValue();
        }
        Node node = this.getNode(n2);
        Node node2 = this.logicalNextDOMTextNode(node);
        if (node2 == null) {
            return node.getNodeValue();
        }
        FastStringBuffer fastStringBuffer = StringBufferPool.get();
        fastStringBuffer.append(node.getNodeValue());
        while (node2 != null) {
            fastStringBuffer.append(node2.getNodeValue());
            node2 = this.logicalNextDOMTextNode(node2);
        }
        String string = fastStringBuffer.length() > 0 ? fastStringBuffer.toString() : "";
        StringBufferPool.free(fastStringBuffer);
        return string;
    }

    public String getDocumentTypeDeclarationSystemIdentifier() {
        DocumentType documentType;
        Document document = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
        if (null != document && null != (documentType = document.getDoctype())) {
            return documentType.getSystemId();
        }
        return null;
    }

    public String getDocumentTypeDeclarationPublicIdentifier() {
        DocumentType documentType;
        Document document = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
        if (null != document && null != (documentType = document.getDoctype())) {
            return documentType.getPublicId();
        }
        return null;
    }

    public int getElementById(String string) {
        Document document;
        Element element;
        Document document2 = document = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
        if (null != document && null != (element = document.getElementById(string))) {
            int n2 = this.getHandleFromNode(element);
            if (-1 == n2) {
                int n3 = this.m_nodes.size() - 1;
                while (-1 != (n3 = this.getNextNodeIdentity(n3))) {
                    Node node = this.getNode(n3);
                    if (node != element) continue;
                    n2 = this.getHandleFromNode(element);
                    break;
                }
            }
            return n2;
        }
        return -1;
    }

    public String getUnparsedEntityURI(String string) {
        DocumentType documentType;
        Document document;
        String string2 = "";
        Document document2 = document = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
        if (null != document && null != (documentType = document.getDoctype())) {
            NamedNodeMap namedNodeMap = documentType.getEntities();
            if (null == namedNodeMap) {
                return string2;
            }
            Entity entity = (Entity)namedNodeMap.getNamedItem(string);
            if (null == entity) {
                return string2;
            }
            String string3 = entity.getNotationName();
            if (null != string3 && null == (string2 = entity.getSystemId())) {
                string2 = entity.getPublicId();
            }
        }
        return string2;
    }

    public boolean isAttributeSpecified(int n2) {
        short s2 = this.getNodeType(n2);
        if (2 == s2) {
            Attr attr = (Attr)this.getNode(n2);
            return attr.getSpecified();
        }
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

    public void dispatchCharactersEvents(int n2, ContentHandler contentHandler, boolean bl) throws SAXException {
        if (bl) {
            XMLString xMLString = this.getStringValue(n2);
            xMLString = xMLString.fixWhiteSpace(true, true, false);
            xMLString.dispatchCharactersEvents(contentHandler);
        } else {
            short s2 = this.getNodeType(n2);
            Node node = this.getNode(n2);
            DOM2DTM.dispatchNodeData(node, contentHandler, 0);
            if (3 == s2 || 4 == s2) {
                while (null != (node = this.logicalNextDOMTextNode(node))) {
                    DOM2DTM.dispatchNodeData(node, contentHandler, 0);
                }
            }
        }
    }

    protected static void dispatchNodeData(Node node, ContentHandler contentHandler, int n2) throws SAXException {
        switch (node.getNodeType()) {
            case 1: 
            case 9: 
            case 11: {
                for (Node node2 = node.getFirstChild(); null != node2; node2 = node2.getNextSibling()) {
                    DOM2DTM.dispatchNodeData(node2, contentHandler, n2 + 1);
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void dispatchToEvents(int n2, ContentHandler contentHandler) throws SAXException {
        TreeWalker treeWalker = this.m_walker;
        ContentHandler contentHandler2 = treeWalker.getContentHandler();
        if (null != contentHandler2) {
            treeWalker = new TreeWalker(null);
        }
        treeWalker.setContentHandler(contentHandler);
        try {
            Node node = this.getNode(n2);
            treeWalker.traverseFragment(node);
        }
        finally {
            treeWalker.setContentHandler(null);
        }
    }

    public SourceLocator getSourceLocatorFor(int n2) {
        return null;
    }

    public static interface CharacterNodeHandler {
        public void characters(Node var1) throws SAXException;
    }

}

