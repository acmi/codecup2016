/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.AdaptiveResultTreeImpl;
import org.apache.xalan.xsltc.dom.BitArray;
import org.apache.xalan.xsltc.dom.DOMBuilder;
import org.apache.xalan.xsltc.dom.DupFilterIterator;
import org.apache.xalan.xsltc.dom.SimpleResultTreeImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMAxisIterNodeList;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.dtm.ref.EmptyIterator;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public final class SAXImpl
extends SAX2DTM2
implements DOMEnhancedForDTM,
DOMBuilder {
    private int _uriCount = 0;
    private int _prefixCount = 0;
    private int[] _xmlSpaceStack;
    private int _idx = 1;
    private boolean _preserve = false;
    private static final String XML_STRING = "xml:";
    private static final String XML_PREFIX = "xml";
    private static final String XMLSPACE_STRING = "xml:space";
    private static final String PRESERVE_STRING = "preserve";
    private static final String XMLNS_PREFIX = "xmlns";
    private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
    private boolean _escaping = true;
    private boolean _disableEscaping = false;
    private int _textNodeToProcess = -1;
    private static final String EMPTYSTRING = "";
    private static final DTMAxisIterator EMPTYITERATOR = EmptyIterator.getInstance();
    private int _namesSize = -1;
    private Hashtable _nsIndex = new Hashtable();
    private int _size = 0;
    private BitArray _dontEscape = null;
    private String _documentURI = null;
    private static int _documentURIIndex = 0;
    private Document _document;
    private Hashtable _node2Ids = null;
    private boolean _hasDOMSource = false;
    private XSLTCDTMManager _dtmManager;
    private Node[] _nodes;
    private NodeList[] _nodeLists;
    private static final String XML_LANG_ATTRIBUTE = "http://www.w3.org/XML/1998/namespace:@lang";

    public void setDocumentURI(String string) {
        if (string != null) {
            this.setDocumentBaseURI(SystemIDResolver.getAbsoluteURI(string));
        }
    }

    public String getDocumentURI() {
        String string = this.getDocumentBaseURI();
        String string2 = string != null ? string : "rtf" + _documentURIIndex++;
        return string2;
    }

    public String getDocumentURI(int n2) {
        return this.getDocumentURI();
    }

    public void setupMapping(String[] arrstring, String[] arrstring2, int[] arrn, String[] arrstring3) {
    }

    public String lookupNamespace(int n2, String string) throws TransletException {
        int n3;
        SAX2DTM2.AncestorIterator ancestorIterator = new SAX2DTM2.AncestorIterator(this);
        if (this.isElement(n2)) {
            ancestorIterator.includeSelf();
        }
        ancestorIterator.setStartNode(n2);
        while ((n3 = ancestorIterator.next()) != -1) {
            int n4;
            DTMDefaultBaseIterators.NamespaceIterator namespaceIterator = new DTMDefaultBaseIterators.NamespaceIterator(this);
            namespaceIterator.setStartNode(n3);
            while ((n4 = namespaceIterator.next()) != -1) {
                if (!this.getLocalName(n4).equals(string)) continue;
                return this.getNodeValue(n4);
            }
        }
        BasisLibrary.runTimeError("NAMESPACE_PREFIX_ERR", string);
        return null;
    }

    public boolean isElement(int n2) {
        return this.getNodeType(n2) == 1;
    }

    public boolean isAttribute(int n2) {
        return this.getNodeType(n2) == 2;
    }

    public int getSize() {
        return this.getNumberOfNodes();
    }

    public void setFilter(StripFilter stripFilter) {
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

    public Node makeNode(int n2) {
        int n3;
        if (this._nodes == null) {
            this._nodes = new Node[this._namesSize];
        }
        if ((n3 = this.makeNodeIdentity(n2)) < 0) {
            return null;
        }
        if (n3 < this._nodes.length) {
            return this._nodes[n3] != null ? this._nodes[n3] : new DTMNodeProxy(this, n2);
        }
        return new DTMNodeProxy(this, n2);
    }

    public Node makeNode(DTMAxisIterator dTMAxisIterator) {
        return this.makeNode(dTMAxisIterator.next());
    }

    public NodeList makeNodeList(int n2) {
        int n3;
        if (this._nodeLists == null) {
            this._nodeLists = new NodeList[this._namesSize];
        }
        if ((n3 = this.makeNodeIdentity(n2)) < 0) {
            return null;
        }
        if (n3 < this._nodeLists.length) {
            return this._nodeLists[n3] != null ? this._nodeLists[n3] : new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, n2));
        }
        return new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, n2));
    }

    public NodeList makeNodeList(DTMAxisIterator dTMAxisIterator) {
        return new DTMAxisIterNodeList(this, dTMAxisIterator);
    }

    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
        return new NodeValueIterator(this, dTMAxisIterator, n2, string, bl);
    }

    public DTMAxisIterator orderNodes(DTMAxisIterator dTMAxisIterator, int n2) {
        return new DupFilterIterator(dTMAxisIterator);
    }

    public DTMAxisIterator getIterator() {
        return new DTMDefaultBaseIterators.SingletonIterator(this, this.getDocument(), true);
    }

    public int getNSType(int n2) {
        String string = this.getNamespaceURI(n2);
        if (string == null) {
            return 0;
        }
        int n3 = this.getIdForNamespace(string);
        return (Integer)this._nsIndex.get(new Integer(n3));
    }

    public int getNamespaceType(int n2) {
        return super.getNamespaceType(n2);
    }

    private int[] setupMapping(String[] arrstring, String[] arrstring2, int[] arrn, int n2) {
        int[] arrn2 = new int[this.m_expandedNameTable.getSize()];
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            arrn2[n3] = n3 = this.m_expandedNameTable.getExpandedTypeID(arrstring2[i2], arrstring[i2], arrn[i2], false);
        }
        return arrn2;
    }

    public int getGeneralizedType(String string) {
        return this.getGeneralizedType(string, true);
    }

    public int getGeneralizedType(String string, boolean bl) {
        int n2;
        int n3;
        String string2 = null;
        int n4 = -1;
        n4 = string.lastIndexOf(58);
        if (n4 > -1) {
            string2 = string.substring(0, n4);
        }
        if (string.charAt(n3 = n4 + 1) == '@') {
            n2 = 2;
            ++n3;
        } else {
            n2 = 1;
        }
        String string3 = n3 == 0 ? string : string.substring(n3);
        return this.m_expandedNameTable.getExpandedTypeID(string2, string3, n2, bl);
    }

    public short[] getMapping(String[] arrstring, String[] arrstring2, int[] arrn) {
        int n2;
        if (this._namesSize < 0) {
            return this.getMapping2(arrstring, arrstring2, arrn);
        }
        int n3 = arrstring.length;
        int n4 = this.m_expandedNameTable.getSize();
        short[] arrs = new short[n4];
        for (n2 = 0; n2 < 14; ++n2) {
            arrs[n2] = (short)n2;
        }
        for (n2 = 14; n2 < n4; ++n2) {
            arrs[n2] = this.m_expandedNameTable.getType(n2);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            int n5 = this.m_expandedNameTable.getExpandedTypeID(arrstring2[n2], arrstring[n2], arrn[n2], true);
            if (n5 < 0 || n5 >= n4) continue;
            arrs[n5] = (short)(n2 + 14);
        }
        return arrs;
    }

    public int[] getReverseMapping(String[] arrstring, String[] arrstring2, int[] arrn) {
        int[] arrn2 = new int[arrstring.length + 14];
        int n2 = 0;
        while (n2 < 14) {
            arrn2[n2] = n2++;
        }
        for (n2 = 0; n2 < arrstring.length; ++n2) {
            int n3;
            arrn2[n2 + 14] = n3 = this.m_expandedNameTable.getExpandedTypeID(arrstring2[n2], arrstring[n2], arrn[n2], true);
        }
        return arrn2;
    }

    private short[] getMapping2(String[] arrstring, String[] arrstring2, int[] arrn) {
        int n2;
        int n3 = arrstring.length;
        int n4 = this.m_expandedNameTable.getSize();
        int[] arrn2 = null;
        if (n3 > 0) {
            arrn2 = new int[n3];
        }
        int n5 = n4;
        for (n2 = 0; n2 < n3; ++n2) {
            arrn2[n2] = this.m_expandedNameTable.getExpandedTypeID(arrstring2[n2], arrstring[n2], arrn[n2], false);
            if (this._namesSize >= 0 || arrn2[n2] < n5) continue;
            n5 = arrn2[n2] + 1;
        }
        short[] arrs = new short[n5];
        for (n2 = 0; n2 < 14; ++n2) {
            arrs[n2] = (short)n2;
        }
        for (n2 = 14; n2 < n4; ++n2) {
            arrs[n2] = this.m_expandedNameTable.getType(n2);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            int n6 = arrn2[n2];
            if (n6 < 0 || n6 >= n5) continue;
            arrs[n6] = (short)(n2 + 14);
        }
        return arrs;
    }

    public short[] getNamespaceMapping(String[] arrstring) {
        int n2;
        int n3 = arrstring.length;
        int n4 = this._uriCount;
        short[] arrs = new short[n4];
        for (n2 = 0; n2 < n4; ++n2) {
            arrs[n2] = -1;
        }
        for (n2 = 0; n2 < n3; ++n2) {
            int n5 = this.getIdForNamespace(arrstring[n2]);
            Integer n6 = (Integer)this._nsIndex.get(new Integer(n5));
            if (n6 == null) continue;
            arrs[n6.intValue()] = (short)n2;
        }
        return arrs;
    }

    public short[] getReverseNamespaceMapping(String[] arrstring) {
        int n2 = arrstring.length;
        short[] arrs = new short[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = this.getIdForNamespace(arrstring[i2]);
            Integer n4 = (Integer)this._nsIndex.get(new Integer(n3));
            arrs[i2] = n4 == null ? -1 : (int)n4.shortValue();
        }
        return arrs;
    }

    public SAXImpl(XSLTCDTMManager xSLTCDTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, boolean bl2) {
        this(xSLTCDTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, 512, bl2, false);
    }

    public SAXImpl(XSLTCDTMManager xSLTCDTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, int n3, boolean bl2, boolean bl3) {
        super(xSLTCDTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, n3, false, bl2, bl3);
        this._dtmManager = xSLTCDTMManager;
        this._size = n3;
        this._xmlSpaceStack = new int[n3 <= 64 ? 4 : 64];
        this._xmlSpaceStack[0] = 0;
        if (source instanceof DOMSource) {
            this._hasDOMSource = true;
            DOMSource dOMSource = (DOMSource)source;
            Node node = dOMSource.getNode();
            this._document = node instanceof Document ? (Document)node : node.getOwnerDocument();
            this._node2Ids = new Hashtable();
        }
    }

    public void migrateTo(DTMManager dTMManager) {
        super.migrateTo(dTMManager);
        if (dTMManager instanceof XSLTCDTMManager) {
            this._dtmManager = (XSLTCDTMManager)dTMManager;
        }
    }

    public int getElementById(String string) {
        Element element = this._document.getElementById(string);
        if (element != null) {
            Integer n2 = (Integer)this._node2Ids.get(element);
            return n2 != null ? n2 : -1;
        }
        return -1;
    }

    public boolean hasDOMSource() {
        return this._hasDOMSource;
    }

    private void xmlSpaceDefine(String string, int n2) {
        boolean bl = string.equals("preserve");
        if (bl != this._preserve) {
            this._xmlSpaceStack[this._idx++] = n2;
            this._preserve = bl;
        }
    }

    private void xmlSpaceRevert(int n2) {
        if (n2 == this._xmlSpaceStack[this._idx - 1]) {
            --this._idx;
            this._preserve = !this._preserve;
        }
    }

    protected boolean getShouldStripWhitespace() {
        return this._preserve ? false : super.getShouldStripWhitespace();
    }

    private void handleTextEscaping() {
        if (this._disableEscaping && this._textNodeToProcess != -1 && this._type(this._textNodeToProcess) == 3) {
            if (this._dontEscape == null) {
                this._dontEscape = new BitArray(this._size);
            }
            if (this._textNodeToProcess >= this._dontEscape.size()) {
                this._dontEscape.resize(this._dontEscape.size() * 2);
            }
            this._dontEscape.setBit(this._textNodeToProcess);
            this._disableEscaping = false;
        }
        this._textNodeToProcess = -1;
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        super.characters(arrc, n2, n3);
        this._disableEscaping = !this._escaping;
        this._textNodeToProcess = this.getNumberOfNodes();
    }

    public void startDocument() throws SAXException {
        super.startDocument();
        this._nsIndex.put(new Integer(0), new Integer(this._uriCount++));
        this.definePrefixAndUri("xml", "http://www.w3.org/XML/1998/namespace");
    }

    public void endDocument() throws SAXException {
        super.endDocument();
        this.handleTextEscaping();
        this._namesSize = this.m_expandedNameTable.getSize();
    }

    public void startElement(String string, String string2, String string3, Attributes attributes, Node node) throws SAXException {
        this.startElement(string, string2, string3, attributes);
        if (this.m_buildIdIndex) {
            this._node2Ids.put(node, new Integer(this.m_parents.peek()));
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        int n2;
        super.startElement(string, string2, string3, attributes);
        this.handleTextEscaping();
        if (this.m_wsfilter != null && (n2 = attributes.getIndex("xml:space")) >= 0) {
            this.xmlSpaceDefine(attributes.getValue(n2), this.m_parents.peek());
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        super.endElement(string, string2, string3);
        this.handleTextEscaping();
        if (this.m_wsfilter != null) {
            this.xmlSpaceRevert(this.m_previous);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        super.processingInstruction(string, string2);
        this.handleTextEscaping();
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        super.ignorableWhitespace(arrc, n2, n3);
        this._textNodeToProcess = this.getNumberOfNodes();
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        super.startPrefixMapping(string, string2);
        this.handleTextEscaping();
        this.definePrefixAndUri(string, string2);
    }

    private void definePrefixAndUri(String string, String string2) throws SAXException {
        Integer n2 = new Integer(this.getIdForNamespace(string2));
        if ((Integer)this._nsIndex.get(n2) == null) {
            this._nsIndex.put(n2, new Integer(this._uriCount++));
        }
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        super.comment(arrc, n2, n3);
        this.handleTextEscaping();
    }

    public boolean setEscaping(boolean bl) {
        boolean bl2 = this._escaping;
        this._escaping = bl;
        return bl2;
    }

    public void print(int n2, int n3) {
        switch (this.getNodeType(n2)) {
            case 0: 
            case 9: {
                this.print(this.getFirstChild(n2), n3);
                break;
            }
            case 3: 
            case 7: 
            case 8: {
                System.out.print(this.getStringValueX(n2));
                break;
            }
            default: {
                String string = this.getNodeName(n2);
                System.out.print("<" + string);
                int n4 = this.getFirstAttribute(n2);
                while (n4 != -1) {
                    System.out.print("\n" + this.getNodeName(n4) + "=\"" + this.getStringValueX(n4) + "\"");
                    n4 = this.getNextAttribute(n4);
                }
                System.out.print('>');
                n4 = this.getFirstChild(n2);
                while (n4 != -1) {
                    this.print(n4, n3 + 1);
                    n4 = this.getNextSibling(n4);
                }
                System.out.println("</" + string + '>');
            }
        }
    }

    public String getNodeName(int n2) {
        int n3 = n2;
        short s2 = this.getNodeType(n3);
        switch (s2) {
            case 0: 
            case 3: 
            case 8: 
            case 9: {
                return "";
            }
            case 13: {
                return this.getLocalName(n3);
            }
        }
        return super.getNodeName(n3);
    }

    public String getNamespaceName(int n2) {
        if (n2 == -1) {
            return "";
        }
        String string = this.getNamespaceURI(n2);
        return string == null ? "" : string;
    }

    public int getAttributeNode(int n2, int n3) {
        int n4 = this.getFirstAttribute(n3);
        while (n4 != -1) {
            if (this.getExpandedTypeID(n4) == n2) {
                return n4;
            }
            n4 = this.getNextAttribute(n4);
        }
        return -1;
    }

    public String getAttributeValue(int n2, int n3) {
        int n4 = this.getAttributeNode(n2, n3);
        return n4 != -1 ? this.getStringValueX(n4) : "";
    }

    public String getAttributeValue(String string, int n2) {
        return this.getAttributeValue(this.getGeneralizedType(string), n2);
    }

    public DTMAxisIterator getChildren(int n2) {
        return new SAX2DTM2.ChildrenIterator(this).setStartNode(n2);
    }

    public DTMAxisIterator getTypedChildren(int n2) {
        return new SAX2DTM2.TypedChildrenIterator(this, n2);
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        switch (n2) {
            case 13: {
                return new DTMDefaultBaseIterators.SingletonIterator(this);
            }
            case 3: {
                return new SAX2DTM2.ChildrenIterator(this);
            }
            case 10: {
                return new SAX2DTM2.ParentIterator(this);
            }
            case 0: {
                return new SAX2DTM2.AncestorIterator(this);
            }
            case 1: {
                return new SAX2DTM2.AncestorIterator(this).includeSelf();
            }
            case 2: {
                return new SAX2DTM2.AttributeIterator(this);
            }
            case 4: {
                return new SAX2DTM2.DescendantIterator(this);
            }
            case 5: {
                return new SAX2DTM2.DescendantIterator(this).includeSelf();
            }
            case 6: {
                return new SAX2DTM2.FollowingIterator(this);
            }
            case 11: {
                return new SAX2DTM2.PrecedingIterator(this);
            }
            case 7: {
                return new SAX2DTM2.FollowingSiblingIterator(this);
            }
            case 12: {
                return new SAX2DTM2.PrecedingSiblingIterator(this);
            }
            case 9: {
                return new DTMDefaultBaseIterators.NamespaceIterator(this);
            }
            case 19: {
                return new DTMDefaultBaseIterators.RootIterator(this);
            }
        }
        BasisLibrary.runTimeError("AXIS_SUPPORT_ERR", Axis.getNames(n2));
        return null;
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        if (n2 == 3) {
            return new SAX2DTM2.TypedChildrenIterator(this, n3);
        }
        if (n3 == -1) {
            return EMPTYITERATOR;
        }
        switch (n2) {
            case 13: {
                return new SAX2DTM2.TypedSingletonIterator(this, n3);
            }
            case 3: {
                return new SAX2DTM2.TypedChildrenIterator(this, n3);
            }
            case 10: {
                return new SAX2DTM2.ParentIterator(this).setNodeType(n3);
            }
            case 0: {
                return new SAX2DTM2.TypedAncestorIterator(this, n3);
            }
            case 1: {
                return new SAX2DTM2.TypedAncestorIterator(this, n3).includeSelf();
            }
            case 2: {
                return new SAX2DTM2.TypedAttributeIterator(this, n3);
            }
            case 4: {
                return new SAX2DTM2.TypedDescendantIterator(this, n3);
            }
            case 5: {
                return new SAX2DTM2.TypedDescendantIterator(this, n3).includeSelf();
            }
            case 6: {
                return new SAX2DTM2.TypedFollowingIterator(this, n3);
            }
            case 11: {
                return new SAX2DTM2.TypedPrecedingIterator(this, n3);
            }
            case 7: {
                return new SAX2DTM2.TypedFollowingSiblingIterator(this, n3);
            }
            case 12: {
                return new SAX2DTM2.TypedPrecedingSiblingIterator(this, n3);
            }
            case 9: {
                return new TypedNamespaceIterator(this, n3);
            }
            case 19: {
                return new SAX2DTM2.TypedRootIterator(this, n3);
            }
        }
        BasisLibrary.runTimeError("TYPED_AXIS_SUPPORT_ERR", Axis.getNames(n2));
        return null;
    }

    public DTMAxisIterator getNamespaceAxisIterator(int n2, int n3) {
        Object var3_3 = null;
        if (n3 == -1) {
            return EMPTYITERATOR;
        }
        switch (n2) {
            case 3: {
                return new NamespaceChildrenIterator(this, n3);
            }
            case 2: {
                return new NamespaceAttributeIterator(this, n3);
            }
        }
        return new NamespaceWildcardIterator(this, n2, n3);
    }

    public DTMAxisIterator getTypedDescendantIterator(int n2) {
        return new SAX2DTM2.TypedDescendantIterator(this, n2);
    }

    public DTMAxisIterator getNthDescendant(int n2, int n3, boolean bl) {
        SAX2DTM2.TypedDescendantIterator typedDescendantIterator = new SAX2DTM2.TypedDescendantIterator(this, n2);
        return new DTMDefaultBaseIterators.NthDescendantIterator(this, n3);
    }

    public void characters(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (n2 != -1) {
            try {
                this.dispatchCharactersEvents(n2, serializationHandler, false);
            }
            catch (SAXException sAXException) {
                throw new TransletException(sAXException);
            }
        }
    }

    public void copy(DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException {
        int n2;
        while ((n2 = dTMAxisIterator.next()) != -1) {
            this.copy(n2, serializationHandler);
        }
    }

    public void copy(SerializationHandler serializationHandler) throws TransletException {
        this.copy(this.getDocument(), serializationHandler);
    }

    public void copy(int n2, SerializationHandler serializationHandler) throws TransletException {
        this.copy(n2, serializationHandler, false);
    }

    private final void copy(int n2, SerializationHandler serializationHandler, boolean bl) throws TransletException {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._exptype2(n3);
        int n5 = this._exptype2Type(n4);
        try {
            switch (n5) {
                case 0: 
                case 9: {
                    int n6 = this._firstch2(n3);
                    while (n6 != -1) {
                        this.copy(this.makeNodeHandle(n6), serializationHandler, true);
                        n6 = this._nextsib2(n6);
                    }
                    break;
                }
                case 7: {
                    this.copyPI(n2, serializationHandler);
                    break;
                }
                case 8: {
                    serializationHandler.comment(this.getStringValueX(n2));
                    break;
                }
                case 3: {
                    boolean bl2 = false;
                    boolean bl3 = false;
                    if (this._dontEscape != null && (bl3 = this._dontEscape.getBit(this.getNodeIdent(n2)))) {
                        bl2 = serializationHandler.setEscaping(false);
                    }
                    this.copyTextNode(n3, serializationHandler);
                    if (bl3) {
                        serializationHandler.setEscaping(bl2);
                        break;
                    }
                    break;
                }
                case 2: {
                    this.copyAttribute(n3, n4, serializationHandler);
                    break;
                }
                case 13: {
                    serializationHandler.namespaceAfterStartElement(this.getNodeNameX(n2), this.getNodeValue(n2));
                    break;
                }
                default: {
                    if (n5 == 1) {
                        String string = this.copyElement(n3, n4, serializationHandler);
                        this.copyNS(n3, serializationHandler, !bl);
                        this.copyAttributes(n3, serializationHandler);
                        int n7 = this._firstch2(n3);
                        while (n7 != -1) {
                            this.copy(this.makeNodeHandle(n7), serializationHandler, true);
                            n7 = this._nextsib2(n7);
                        }
                        serializationHandler.endElement(string);
                        break;
                    }
                    String string = this.getNamespaceName(n2);
                    if (string.length() != 0) {
                        String string2 = this.getPrefix(n2);
                        serializationHandler.namespaceAfterStartElement(string2, string);
                    }
                    serializationHandler.addAttribute(this.getNodeName(n2), this.getNodeValue(n2));
                }
            }
        }
        catch (Exception exception) {
            throw new TransletException(exception);
        }
    }

    private void copyPI(int n2, SerializationHandler serializationHandler) throws TransletException {
        String string = this.getNodeName(n2);
        String string2 = this.getStringValueX(n2);
        try {
            serializationHandler.processingInstruction(string, string2);
        }
        catch (Exception exception) {
            throw new TransletException(exception);
        }
    }

    public String shallowCopy(int n2, SerializationHandler serializationHandler) throws TransletException {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._exptype2(n3);
        int n5 = this._exptype2Type(n4);
        try {
            switch (n5) {
                case 1: {
                    String string = this.copyElement(n3, n4, serializationHandler);
                    this.copyNS(n3, serializationHandler, true);
                    return string;
                }
                case 0: 
                case 9: {
                    return "";
                }
                case 3: {
                    this.copyTextNode(n3, serializationHandler);
                    return null;
                }
                case 7: {
                    this.copyPI(n2, serializationHandler);
                    return null;
                }
                case 8: {
                    serializationHandler.comment(this.getStringValueX(n2));
                    return null;
                }
                case 13: {
                    serializationHandler.namespaceAfterStartElement(this.getNodeNameX(n2), this.getNodeValue(n2));
                    return null;
                }
                case 2: {
                    this.copyAttribute(n3, n4, serializationHandler);
                    return null;
                }
            }
            String string = this.getNamespaceName(n2);
            if (string.length() != 0) {
                String string2 = this.getPrefix(n2);
                serializationHandler.namespaceAfterStartElement(string2, string);
            }
            serializationHandler.addAttribute(this.getNodeName(n2), this.getNodeValue(n2));
            return null;
        }
        catch (Exception exception) {
            throw new TransletException(exception);
        }
    }

    public String getLanguage(int n2) {
        int n3 = n2;
        while (-1 != n3) {
            int n4;
            if (1 == this.getNodeType(n3) && -1 != (n4 = this.getAttributeNode(n3, "http://www.w3.org/XML/1998/namespace", "lang"))) {
                return this.getNodeValue(n4);
            }
            n3 = this.getParent(n3);
        }
        return null;
    }

    public DOMBuilder getBuilder() {
        return this;
    }

    public SerializationHandler getOutputDomBuilder() {
        return new ToXMLSAXHandler(this, "UTF-8");
    }

    public DOM getResultTreeFrag(int n2, int n3) {
        return this.getResultTreeFrag(n2, n3, true);
    }

    public DOM getResultTreeFrag(int n2, int n3, boolean bl) {
        if (n3 == 0) {
            if (bl) {
                int n4 = this._dtmManager.getFirstFreeDTMID();
                SimpleResultTreeImpl simpleResultTreeImpl = new SimpleResultTreeImpl(this._dtmManager, n4 << 16);
                this._dtmManager.addDTM(simpleResultTreeImpl, n4, 0);
                return simpleResultTreeImpl;
            }
            return new SimpleResultTreeImpl(this._dtmManager, 0);
        }
        if (n3 == 1) {
            if (bl) {
                int n5 = this._dtmManager.getFirstFreeDTMID();
                AdaptiveResultTreeImpl adaptiveResultTreeImpl = new AdaptiveResultTreeImpl(this._dtmManager, n5 << 16, this.m_wsfilter, n2, this.m_buildIdIndex);
                this._dtmManager.addDTM(adaptiveResultTreeImpl, n5, 0);
                return adaptiveResultTreeImpl;
            }
            return new AdaptiveResultTreeImpl(this._dtmManager, 0, this.m_wsfilter, n2, this.m_buildIdIndex);
        }
        return (DOM)((Object)this._dtmManager.getDTM(null, true, this.m_wsfilter, true, false, false, n2, this.m_buildIdIndex));
    }

    public Hashtable getElementsWithIDs() {
        if (this.m_idAttributes == null) {
            return null;
        }
        Iterator iterator = this.m_idAttributes.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        Hashtable hashtable = new Hashtable();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            hashtable.put(entry.getKey(), entry.getValue());
        }
        return hashtable;
    }

    public String getUnparsedEntityURI(String string) {
        if (this._document != null) {
            String string2 = "";
            DocumentType documentType = this._document.getDoctype();
            if (documentType != null) {
                NamedNodeMap namedNodeMap = documentType.getEntities();
                if (namedNodeMap == null) {
                    return string2;
                }
                Entity entity = (Entity)namedNodeMap.getNamedItem(string);
                if (entity == null) {
                    return string2;
                }
                String string3 = entity.getNotationName();
                if (string3 != null && (string2 = entity.getSystemId()) == null) {
                    string2 = entity.getPublicId();
                }
            }
            return string2;
        }
        return super.getUnparsedEntityURI(string);
    }

    static ExpandedNameTable access$000(SAXImpl sAXImpl) {
        return sAXImpl.m_expandedNameTable;
    }

    static ExpandedNameTable access$100(SAXImpl sAXImpl) {
        return sAXImpl.m_expandedNameTable;
    }

    static int access$200(SAXImpl sAXImpl, int n2) {
        return sAXImpl._firstch(n2);
    }

    static int access$300(SAXImpl sAXImpl, int n2) {
        return sAXImpl._nextsib(n2);
    }

    static int access$400(SAXImpl sAXImpl, int n2) {
        return sAXImpl._nextsib(n2);
    }

    public final class NamespaceAttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nsType;
        private final SAXImpl this$0;

        public NamespaceAttributeIterator(SAXImpl sAXImpl, int n2) {
            super(sAXImpl);
            this.this$0 = sAXImpl;
            this._nsType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                int n3 = this._nsType;
                this._startNode = n2;
                n2 = this.this$0.getFirstAttribute(n2);
                while (n2 != -1 && this.this$0.getNSType(n2) != n3) {
                    n2 = this.this$0.getNextAttribute(n2);
                }
                this._currentNode = n2;
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            int n3 = this._nsType;
            if (n2 == -1) {
                return -1;
            }
            int n4 = this.this$0.getNextAttribute(n2);
            while (n4 != -1 && this.this$0.getNSType(n4) != n3) {
                n4 = this.this$0.getNextAttribute(n4);
            }
            this._currentNode = n4;
            return this.returnNode(n2);
        }
    }

    public final class NamespaceChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nsType;
        private final SAXImpl this$0;

        public NamespaceChildrenIterator(SAXImpl sAXImpl, int n2) {
            super(sAXImpl);
            this.this$0 = sAXImpl;
            this._nsType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = n2 == -1 ? -1 : -2;
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            if (this._currentNode != -1) {
                int n2;
                int n3 = n2 = -2 == this._currentNode ? SAXImpl.access$200(this.this$0, this.this$0.makeNodeIdentity(this._startNode)) : SAXImpl.access$300(this.this$0, this._currentNode);
                while (n2 != -1) {
                    int n4 = this.this$0.makeNodeHandle(n2);
                    if (this.this$0.getNSType(n4) == this._nsType) {
                        this._currentNode = n2;
                        return this.returnNode(n4);
                    }
                    n2 = SAXImpl.access$400(this.this$0, n2);
                }
            }
            return -1;
        }
    }

    public final class NamespaceWildcardIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        protected int m_nsType;
        protected DTMAxisIterator m_baseIterator;
        private final SAXImpl this$0;

        public NamespaceWildcardIterator(SAXImpl sAXImpl, int n2, int n3) {
            super(sAXImpl);
            this.this$0 = sAXImpl;
            this.m_nsType = n3;
            switch (n2) {
                case 2: {
                    this.m_baseIterator = sAXImpl.getAxisIterator(n2);
                }
                case 9: {
                    this.m_baseIterator = sAXImpl.getAxisIterator(n2);
                }
            }
            this.m_baseIterator = sAXImpl.getTypedAxisIterator(n2, 1);
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (this._isRestartable) {
                this._startNode = n2;
                this.m_baseIterator.setStartNode(n2);
                this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2;
            while ((n2 = this.m_baseIterator.next()) != -1) {
                if (this.this$0.getNSType(n2) != this.m_nsType) continue;
                return this.returnNode(n2);
            }
            return -1;
        }

        public DTMAxisIterator cloneIterator() {
            try {
                DTMAxisIterator dTMAxisIterator = this.m_baseIterator.cloneIterator();
                NamespaceWildcardIterator namespaceWildcardIterator = (NamespaceWildcardIterator)Object.super.clone();
                namespaceWildcardIterator.m_baseIterator = dTMAxisIterator;
                namespaceWildcardIterator.m_nsType = this.m_nsType;
                namespaceWildcardIterator._isRestartable = false;
                return namespaceWildcardIterator;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
                return null;
            }
        }

        public boolean isReverse() {
            return this.m_baseIterator.isReverse();
        }

        public void setMark() {
            this.m_baseIterator.setMark();
        }

        public void gotoMark() {
            this.m_baseIterator.gotoMark();
        }
    }

    private final class NodeValueIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private DTMAxisIterator _source;
        private String _value;
        private boolean _op;
        private final boolean _isReverse;
        private int _returnType;
        private final SAXImpl this$0;

        public NodeValueIterator(SAXImpl sAXImpl, DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
            super(sAXImpl);
            this.this$0 = sAXImpl;
            this._returnType = 1;
            this._source = dTMAxisIterator;
            this._returnType = n2;
            this._value = string;
            this._op = bl;
            this._isReverse = dTMAxisIterator.isReverse();
        }

        public boolean isReverse() {
            return this._isReverse;
        }

        public DTMAxisIterator cloneIterator() {
            try {
                NodeValueIterator nodeValueIterator = (NodeValueIterator)Object.super.clone();
                nodeValueIterator._isRestartable = false;
                nodeValueIterator._source = this._source.cloneIterator();
                nodeValueIterator._value = this._value;
                nodeValueIterator._op = this._op;
                return nodeValueIterator.reset();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
                return null;
            }
        }

        public void setRestartable(boolean bl) {
            this._isRestartable = bl;
            this._source.setRestartable(bl);
        }

        public DTMAxisIterator reset() {
            this._source.reset();
            return this.resetPosition();
        }

        public int next() {
            int n2;
            while ((n2 = this._source.next()) != -1) {
                String string = this.this$0.getStringValueX(n2);
                if (this._value.equals(string) != this._op) continue;
                if (this._returnType == 0) {
                    return this.returnNode(n2);
                }
                return this.returnNode(this.this$0.getParent(n2));
            }
            return -1;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (this._isRestartable) {
                this._startNode = n2;
                this._source.setStartNode(this._startNode);
                return this.resetPosition();
            }
            return this;
        }

        public void setMark() {
            this._source.setMark();
        }

        public void gotoMark() {
            this._source.gotoMark();
        }
    }

    public class TypedNamespaceIterator
    extends DTMDefaultBaseIterators.NamespaceIterator {
        private String _nsPrefix;
        private final SAXImpl this$0;

        public TypedNamespaceIterator(SAXImpl sAXImpl, int n2) {
            super(sAXImpl);
            this.this$0 = sAXImpl;
            if (SAXImpl.access$000(sAXImpl) != null) {
                this._nsPrefix = SAXImpl.access$100(sAXImpl).getLocalName(n2);
            }
        }

        public int next() {
            if (this._nsPrefix == null || this._nsPrefix.length() == 0) {
                return -1;
            }
            int n2 = -1;
            n2 = super.next();
            while (n2 != -1) {
                if (this._nsPrefix.compareTo(this.this$0.getLocalName(n2)) == 0) {
                    return this.returnNode(n2);
                }
                n2 = super.next();
            }
            return -1;
        }
    }

}

