/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.PrintStream;
import org.apache.xml.res.XMLMessages;
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

public class UnImplNode
implements Document,
Element,
Node,
NodeList {
    protected String fDocumentURI;
    protected String actualEncoding;
    private String xmlEncoding;
    private boolean xmlStandalone;
    private String xmlVersion;

    public void error(String string) {
        System.out.println("DOM ERROR! class: " + this.getClass().getName());
        throw new RuntimeException(XMLMessages.createXMLMessage(string, null));
    }

    public void error(String string, Object[] arrobject) {
        System.out.println("DOM ERROR! class: " + this.getClass().getName());
        throw new RuntimeException(XMLMessages.createXMLMessage(string, arrobject));
    }

    public Node appendChild(Node node) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public boolean hasChildNodes() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public short getNodeType() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return 0;
    }

    public Node getParentNode() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public NodeList getChildNodes() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node getFirstChild() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node getLastChild() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node getNextSibling() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public int getLength() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return 0;
    }

    public Node item(int n2) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Document getOwnerDocument() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public String getTagName() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public String getNodeName() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void normalize() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public NodeList getElementsByTagName(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Attr removeAttributeNode(Attr attr) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Attr setAttributeNode(Attr attr) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public boolean hasAttribute(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public boolean hasAttributeNS(String string, String string2) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public Attr getAttributeNode(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void removeAttribute(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void setAttribute(String string, String string2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public String getAttribute(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public boolean hasAttributes() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public NodeList getElementsByTagNameNS(String string, String string2) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Attr setAttributeNodeNS(Attr attr) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Attr getAttributeNodeNS(String string, String string2) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void removeAttributeNS(String string, String string2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void setAttributeNS(String string, String string2, String string3) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public String getAttributeNS(String string, String string2) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node getPreviousSibling() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node cloneNode(boolean bl) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public String getNodeValue() throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setNodeValue(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void setValue(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public Element getOwnerElement() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public boolean getSpecified() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public NamedNodeMap getAttributes() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node removeChild(Node node) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public boolean isSupported(String string, String string2) {
        return false;
    }

    public String getNamespaceURI() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public String getPrefix() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setPrefix(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public String getLocalName() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public DocumentType getDoctype() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public DOMImplementation getImplementation() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Element getDocumentElement() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Element createElement(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public DocumentFragment createDocumentFragment() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Text createTextNode(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Comment createComment(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public CDATASection createCDATASection(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public ProcessingInstruction createProcessingInstruction(String string, String string2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Attr createAttribute(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public EntityReference createEntityReference(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node importNode(Node node, boolean bl) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Element createElementNS(String string, String string2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Attr createAttributeNS(String string, String string2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Element getElementById(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setData(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public String substringData(int n2, int n3) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void appendData(String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void insertData(int n2, String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void deleteData(int n2, int n3) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void replaceData(int n2, int n3, String string) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public Text splitText(int n2) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public Node adoptNode(Node node) throws DOMException {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public String getInputEncoding() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setInputEncoding(String string) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public boolean getStrictErrorChecking() {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public void setStrictErrorChecking(boolean bl) {
        this.error("ER_FUNCTION_NOT_SUPPORTED");
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
        return this.getNodeValue();
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

    public String getActualEncoding() {
        return this.actualEncoding;
    }

    public void setActualEncoding(String string) {
        this.actualEncoding = string;
    }

    public Text replaceWholeText(String string) throws DOMException {
        return null;
    }

    public String getWholeText() {
        return null;
    }

    public boolean isWhitespaceInElementContent() {
        return false;
    }

    public void setIdAttribute(boolean bl) {
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

    public void setXmlEncoding(String string) {
        this.xmlEncoding = string;
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
}

