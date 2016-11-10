/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref.dom2dtm;

import org.apache.xml.dtm.DTMException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class DOM2DTMdefaultNamespaceDeclarationNode
implements Attr,
TypeInfo {
    Element pseudoparent;
    String prefix;
    String uri;
    String nodename;
    int handle;

    DOM2DTMdefaultNamespaceDeclarationNode(Element element, String string, String string2, int n2) {
        this.pseudoparent = element;
        this.prefix = string;
        this.uri = string2;
        this.handle = n2;
        this.nodename = "xmlns:" + string;
    }

    public String getNodeName() {
        return this.nodename;
    }

    public String getName() {
        return this.nodename;
    }

    public String getNamespaceURI() {
        return "http://www.w3.org/2000/xmlns/";
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getLocalName() {
        return this.prefix;
    }

    public String getNodeValue() {
        return this.uri;
    }

    public String getValue() {
        return this.uri;
    }

    public Element getOwnerElement() {
        return this.pseudoparent;
    }

    public boolean isSupported(String string, String string2) {
        return false;
    }

    public boolean hasChildNodes() {
        return false;
    }

    public boolean hasAttributes() {
        return false;
    }

    public Node getParentNode() {
        return null;
    }

    public Node getFirstChild() {
        return null;
    }

    public Node getLastChild() {
        return null;
    }

    public Node getPreviousSibling() {
        return null;
    }

    public Node getNextSibling() {
        return null;
    }

    public boolean getSpecified() {
        return false;
    }

    public void normalize() {
    }

    public NodeList getChildNodes() {
        return null;
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    public short getNodeType() {
        return 2;
    }

    public void setNodeValue(String string) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public void setValue(String string) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public void setPrefix(String string) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public Node insertBefore(Node node, Node node2) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public Node replaceChild(Node node, Node node2) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public Node appendChild(Node node) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public Node removeChild(Node node) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public Document getOwnerDocument() {
        return this.pseudoparent.getOwnerDocument();
    }

    public Node cloneNode(boolean bl) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public String getTypeName() {
        return null;
    }

    public String getTypeNamespace() {
        return null;
    }

    public boolean isDerivedFrom(String string, String string2, int n2) {
        return false;
    }

    public TypeInfo getSchemaTypeInfo() {
        return this;
    }

    public boolean isId() {
        return false;
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
}

