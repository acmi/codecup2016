/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.util.Vector;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMDOMException;
import org.apache.xml.dtm.ref.DTMChildIterNodeList;
import org.apache.xml.dtm.ref.DTMNamedNodeMap;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class DTMNodeProxy
implements Attr,
Comment,
Document,
DocumentFragment,
Element,
Node,
ProcessingInstruction,
Text {
    public DTM dtm;
    int node;
    static final DOMImplementation implementation = new DTMNodeProxyImplementation();
    protected String fDocumentURI;
    private String xmlEncoding;
    private boolean xmlStandalone;
    private String xmlVersion;

    public DTMNodeProxy(DTM dTM, int n2) {
        this.dtm = dTM;
        this.node = n2;
    }

    public final DTM getDTM() {
        return this.dtm;
    }

    public final int getDTMNodeNumber() {
        return this.node;
    }

    public final boolean equals(Node node) {
        try {
            DTMNodeProxy dTMNodeProxy = (DTMNodeProxy)node;
            return dTMNodeProxy.node == this.node && dTMNodeProxy.dtm == this.dtm;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public final boolean equals(Object object) {
        try {
            return this.equals((Node)object);
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public final String getNodeName() {
        return this.dtm.getNodeName(this.node);
    }

    public final String getTarget() {
        return this.dtm.getNodeName(this.node);
    }

    public final String getLocalName() {
        return this.dtm.getLocalName(this.node);
    }

    public final String getPrefix() {
        return this.dtm.getPrefix(this.node);
    }

    public final void setPrefix(String string) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final String getNamespaceURI() {
        return this.dtm.getNamespaceURI(this.node);
    }

    public final boolean isSupported(String string, String string2) {
        return implementation.hasFeature(string, string2);
    }

    public final String getNodeValue() throws DOMException {
        return this.dtm.getNodeValue(this.node);
    }

    public final String getStringValue() throws DOMException {
        return this.dtm.getStringValue(this.node).toString();
    }

    public final void setNodeValue(String string) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final short getNodeType() {
        return this.dtm.getNodeType(this.node);
    }

    public final Node getParentNode() {
        if (this.getNodeType() == 2) {
            return null;
        }
        int n2 = this.dtm.getParent(this.node);
        return n2 == -1 ? null : this.dtm.getNode(n2);
    }

    public final NodeList getChildNodes() {
        return new DTMChildIterNodeList(this.dtm, this.node);
    }

    public final Node getFirstChild() {
        int n2 = this.dtm.getFirstChild(this.node);
        return n2 == -1 ? null : this.dtm.getNode(n2);
    }

    public final Node getLastChild() {
        int n2 = this.dtm.getLastChild(this.node);
        return n2 == -1 ? null : this.dtm.getNode(n2);
    }

    public final Node getPreviousSibling() {
        int n2 = this.dtm.getPreviousSibling(this.node);
        return n2 == -1 ? null : this.dtm.getNode(n2);
    }

    public final Node getNextSibling() {
        if (this.dtm.getNodeType(this.node) == 2) {
            return null;
        }
        int n2 = this.dtm.getNextSibling(this.node);
        return n2 == -1 ? null : this.dtm.getNode(n2);
    }

    public final NamedNodeMap getAttributes() {
        return new DTMNamedNodeMap(this.dtm, this.node);
    }

    public boolean hasAttribute(String string) {
        return -1 != this.dtm.getAttributeNode(this.node, null, string);
    }

    public boolean hasAttributeNS(String string, String string2) {
        return -1 != this.dtm.getAttributeNode(this.node, string, string2);
    }

    public final Document getOwnerDocument() {
        return (Document)this.dtm.getNode(this.dtm.getOwnerDocument(this.node));
    }

    public final Node insertBefore(Node node, Node node2) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final Node replaceChild(Node node, Node node2) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final Node removeChild(Node node) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final Node appendChild(Node node) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final boolean hasChildNodes() {
        return -1 != this.dtm.getFirstChild(this.node);
    }

    public final Node cloneNode(boolean bl) {
        throw new DTMDOMException(9);
    }

    public final DocumentType getDoctype() {
        return null;
    }

    public final DOMImplementation getImplementation() {
        return implementation;
    }

    public final Element getDocumentElement() {
        int n2 = this.dtm.getDocument();
        int n3 = -1;
        int n4 = this.dtm.getFirstChild(n2);
        while (n4 != -1) {
            switch (this.dtm.getNodeType(n4)) {
                case 1: {
                    if (n3 != -1) {
                        n3 = -1;
                        n4 = this.dtm.getLastChild(n2);
                        break;
                    }
                    n3 = n4;
                    break;
                }
                case 7: 
                case 8: 
                case 10: {
                    break;
                }
                default: {
                    n3 = -1;
                    n4 = this.dtm.getLastChild(n2);
                }
            }
            n4 = this.dtm.getNextSibling(n4);
        }
        if (n3 == -1) {
            throw new DTMDOMException(9);
        }
        return (Element)this.dtm.getNode(n3);
    }

    public final Element createElement(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final DocumentFragment createDocumentFragment() {
        throw new DTMDOMException(9);
    }

    public final Text createTextNode(String string) {
        throw new DTMDOMException(9);
    }

    public final Comment createComment(String string) {
        throw new DTMDOMException(9);
    }

    public final CDATASection createCDATASection(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final ProcessingInstruction createProcessingInstruction(String string, String string2) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final Attr createAttribute(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final EntityReference createEntityReference(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final NodeList getElementsByTagName(String string) {
        int n2;
        NodeList nodeList;
        int n3;
        Vector vector = new Vector();
        Node node = this.dtm.getNode(this.node);
        if (node != null) {
            n3 = (int)"*".equals(string) ? 1 : 0;
            if (1 == node.getNodeType()) {
                nodeList = node.getChildNodes();
                for (n2 = 0; n2 < nodeList.getLength(); ++n2) {
                    this.traverseChildren(vector, nodeList.item(n2), string, (boolean)n3);
                }
            } else if (9 == node.getNodeType()) {
                this.traverseChildren(vector, this.dtm.getNode(this.node), string, (boolean)n3);
            }
        }
        n3 = vector.size();
        nodeList = new NodeSet(n3);
        for (n2 = 0; n2 < n3; ++n2) {
            nodeList.addNode((Node)vector.elementAt(n2));
        }
        return nodeList;
    }

    private final void traverseChildren(Vector vector, Node node, String string, boolean bl) {
        if (node == null) {
            return;
        }
        if (node.getNodeType() == 1 && (bl || node.getNodeName().equals(string))) {
            vector.add(node);
        }
        if (node.hasChildNodes()) {
            NodeList nodeList = node.getChildNodes();
            for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
                this.traverseChildren(vector, nodeList.item(i2), string, bl);
            }
        }
    }

    public final Node importNode(Node node, boolean bl) throws DOMException {
        throw new DTMDOMException(7);
    }

    public final Element createElementNS(String string, String string2) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final Attr createAttributeNS(String string, String string2) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final NodeList getElementsByTagNameNS(String string, String string2) {
        boolean bl;
        Vector vector = new Vector();
        Node node = this.dtm.getNode(this.node);
        if (node != null) {
            bl = "*".equals(string);
            boolean bl2 = "*".equals(string2);
            if (1 == node.getNodeType()) {
                NodeList nodeList = node.getChildNodes();
                for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
                    this.traverseChildren(vector, nodeList.item(i2), string, string2, bl, bl2);
                }
            } else if (9 == node.getNodeType()) {
                this.traverseChildren(vector, this.dtm.getNode(this.node), string, string2, bl, bl2);
            }
        }
        bl = vector.size();
        NodeSet nodeSet = new NodeSet((int)bl ? 1 : 0);
        for (boolean bl3 = false; bl3 < bl; bl3 += 1) {
            nodeSet.addNode((Node)vector.elementAt((int)bl3 ? 1 : 0));
        }
        return nodeSet;
    }

    private final void traverseChildren(Vector vector, Node node, String string, String string2, boolean bl, boolean bl2) {
        Object object;
        if (node == null) {
            return;
        }
        if (node.getNodeType() == 1 && (bl2 || node.getLocalName().equals(string2))) {
            object = node.getNamespaceURI();
            if (string == null && object == null || bl || string != null && string.equals(object)) {
                vector.add(node);
            }
        }
        if (node.hasChildNodes()) {
            object = node.getChildNodes();
            for (int i2 = 0; i2 < object.getLength(); ++i2) {
                this.traverseChildren(vector, object.item(i2), string, string2, bl, bl2);
            }
        }
    }

    public final Element getElementById(String string) {
        return (Element)this.dtm.getNode(this.dtm.getElementById(string));
    }

    public final Text splitText(int n2) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final String getData() throws DOMException {
        return this.dtm.getNodeValue(this.node);
    }

    public final void setData(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final int getLength() {
        return this.dtm.getNodeValue(this.node).length();
    }

    public final String substringData(int n2, int n3) throws DOMException {
        return this.getData().substring(n2, n2 + n3);
    }

    public final void appendData(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final void insertData(int n2, String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final void deleteData(int n2, int n3) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final void replaceData(int n2, int n3, String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final String getTagName() {
        return this.dtm.getNodeName(this.node);
    }

    public final String getAttribute(String string) {
        DTMNamedNodeMap dTMNamedNodeMap = new DTMNamedNodeMap(this.dtm, this.node);
        Node node = dTMNamedNodeMap.getNamedItem(string);
        return null == node ? "" : node.getNodeValue();
    }

    public final void setAttribute(String string, String string2) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final void removeAttribute(String string) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final Attr getAttributeNode(String string) {
        DTMNamedNodeMap dTMNamedNodeMap = new DTMNamedNodeMap(this.dtm, this.node);
        return (Attr)dTMNamedNodeMap.getNamedItem(string);
    }

    public final Attr setAttributeNode(Attr attr) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final Attr removeAttributeNode(Attr attr) throws DOMException {
        throw new DTMDOMException(9);
    }

    public boolean hasAttributes() {
        return -1 != this.dtm.getFirstAttribute(this.node);
    }

    public final void normalize() {
        throw new DTMDOMException(9);
    }

    public final String getAttributeNS(String string, String string2) {
        Node node = null;
        int n2 = this.dtm.getAttributeNode(this.node, string, string2);
        if (n2 != -1) {
            node = this.dtm.getNode(n2);
        }
        return null == node ? "" : node.getNodeValue();
    }

    public final void setAttributeNS(String string, String string2, String string3) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final void removeAttributeNS(String string, String string2) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final Attr getAttributeNodeNS(String string, String string2) {
        Attr attr = null;
        int n2 = this.dtm.getAttributeNode(this.node, string, string2);
        if (n2 != -1) {
            attr = (Attr)this.dtm.getNode(n2);
        }
        return attr;
    }

    public final Attr setAttributeNodeNS(Attr attr) throws DOMException {
        throw new DTMDOMException(9);
    }

    public final String getName() {
        return this.dtm.getNodeName(this.node);
    }

    public final boolean getSpecified() {
        return true;
    }

    public final String getValue() {
        return this.dtm.getNodeValue(this.node);
    }

    public final void setValue(String string) {
        throw new DTMDOMException(9);
    }

    public final Element getOwnerElement() {
        if (this.getNodeType() != 2) {
            return null;
        }
        int n2 = this.dtm.getParent(this.node);
        return n2 == -1 ? null : (Element)this.dtm.getNode(n2);
    }

    public Node adoptNode(Node node) throws DOMException {
        throw new DTMDOMException(9);
    }

    public String getInputEncoding() {
        throw new DTMDOMException(9);
    }

    public boolean getStrictErrorChecking() {
        throw new DTMDOMException(9);
    }

    public void setStrictErrorChecking(boolean bl) {
        throw new DTMDOMException(9);
    }

    public Object setUserData(String string, Object object, UserDataHandler userDataHandler) {
        return this.getOwnerDocument().setUserData(string, object, userDataHandler);
    }

    public Object getUserData(String string) {
        return this.getOwnerDocument().getUserData(string);
    }

    public Object getFeature(String string, String string2) {
        return this.isSupported(string, string2) ? this : null;
    }

    public boolean isEqualNode(Node node) {
        if (node == this) {
            return true;
        }
        if (node.getNodeType() != this.getNodeType()) {
            return false;
        }
        if (this.getNodeName() == null ? node.getNodeName() != null : !this.getNodeName().equals(node.getNodeName())) {
            return false;
        }
        if (this.getLocalName() == null ? node.getLocalName() != null : !this.getLocalName().equals(node.getLocalName())) {
            return false;
        }
        if (this.getNamespaceURI() == null ? node.getNamespaceURI() != null : !this.getNamespaceURI().equals(node.getNamespaceURI())) {
            return false;
        }
        if (this.getPrefix() == null ? node.getPrefix() != null : !this.getPrefix().equals(node.getPrefix())) {
            return false;
        }
        if (this.getNodeValue() == null ? node.getNodeValue() != null : !this.getNodeValue().equals(node.getNodeValue())) {
            return false;
        }
        return true;
    }

    public String lookupNamespaceURI(String string) {
        short s2 = this.getNodeType();
        switch (s2) {
            case 1: {
                String string2 = this.getNamespaceURI();
                String string3 = this.getPrefix();
                if (string2 != null) {
                    if (string == null && string3 == string) {
                        return string2;
                    }
                    if (string3 != null && string3.equals(string)) {
                        return string2;
                    }
                }
                if (this.hasAttributes()) {
                    NamedNodeMap namedNodeMap = this.getAttributes();
                    int n2 = namedNodeMap.getLength();
                    for (int i2 = 0; i2 < n2; ++i2) {
                        Node node = namedNodeMap.item(i2);
                        String string4 = node.getPrefix();
                        String string5 = node.getNodeValue();
                        string2 = node.getNamespaceURI();
                        if (string2 == null || !string2.equals("http://www.w3.org/2000/xmlns/")) continue;
                        if (string == null && node.getNodeName().equals("xmlns")) {
                            return string5;
                        }
                        if (string4 == null || !string4.equals("xmlns") || !node.getLocalName().equals(string)) continue;
                        return string5;
                    }
                }
                return null;
            }
            case 6: 
            case 10: 
            case 11: 
            case 12: {
                return null;
            }
            case 2: {
                if (this.getOwnerElement().getNodeType() == 1) {
                    return this.getOwnerElement().lookupNamespaceURI(string);
                }
                return null;
            }
        }
        return null;
    }

    public boolean isDefaultNamespace(String string) {
        return false;
    }

    public String lookupPrefix(String string) {
        if (string == null) {
            return null;
        }
        short s2 = this.getNodeType();
        switch (s2) {
            case 6: 
            case 10: 
            case 11: 
            case 12: {
                return null;
            }
            case 2: {
                if (this.getOwnerElement().getNodeType() == 1) {
                    return this.getOwnerElement().lookupPrefix(string);
                }
                return null;
            }
        }
        return null;
    }

    public boolean isSameNode(Node node) {
        return this == node;
    }

    public void setTextContent(String string) throws DOMException {
        this.setNodeValue(string);
    }

    public String getTextContent() throws DOMException {
        return this.dtm.getStringValue(this.node).toString();
    }

    public short compareDocumentPosition(Node node) throws DOMException {
        return 0;
    }

    public String getBaseURI() {
        return null;
    }

    public Node renameNode(Node node, String string, String string2) throws DOMException {
        return node;
    }

    public void normalizeDocument() {
    }

    public DOMConfiguration getDomConfig() {
        return null;
    }

    public void setDocumentURI(String string) {
        this.fDocumentURI = string;
    }

    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    public Text replaceWholeText(String string) throws DOMException {
        return null;
    }

    public String getWholeText() {
        return null;
    }

    public boolean isElementContentWhitespace() {
        return false;
    }

    public void setIdAttribute(String string, boolean bl) {
    }

    public void setIdAttributeNode(Attr attr, boolean bl) {
    }

    public void setIdAttributeNS(String string, String string2, boolean bl) {
    }

    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public boolean isId() {
        return false;
    }

    public String getXmlEncoding() {
        return this.xmlEncoding;
    }

    public boolean getXmlStandalone() {
        return this.xmlStandalone;
    }

    public void setXmlStandalone(boolean bl) throws DOMException {
        this.xmlStandalone = bl;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public void setXmlVersion(String string) throws DOMException {
        this.xmlVersion = string;
    }

    static class DTMNodeProxyImplementation
    implements DOMImplementation {
        DTMNodeProxyImplementation() {
        }

        public DocumentType createDocumentType(String string, String string2, String string3) {
            throw new DTMDOMException(9);
        }

        public Document createDocument(String string, String string2, DocumentType documentType) {
            throw new DTMDOMException(9);
        }

        public boolean hasFeature(String string, String string2) {
            if (("CORE".equals(string.toUpperCase()) || "XML".equals(string.toUpperCase())) && ("1.0".equals(string2) || "2.0".equals(string2))) {
                return true;
            }
            return false;
        }

        public Object getFeature(String string, String string2) {
            return null;
        }
    }

}

