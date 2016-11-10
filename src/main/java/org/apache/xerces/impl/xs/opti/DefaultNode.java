/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class DefaultNode
implements Node {
    public String getNodeName() {
        return null;
    }

    public String getNodeValue() throws DOMException {
        return null;
    }

    public short getNodeType() {
        return -1;
    }

    public Node getParentNode() {
        return null;
    }

    public NodeList getChildNodes() {
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

    public NamedNodeMap getAttributes() {
        return null;
    }

    public Document getOwnerDocument() {
        return null;
    }

    public boolean hasChildNodes() {
        return false;
    }

    public Node cloneNode(boolean bl) {
        return null;
    }

    public void normalize() {
    }

    public boolean isSupported(String string, String string2) {
        return false;
    }

    public String getNamespaceURI() {
        return null;
    }

    public String getPrefix() {
        return null;
    }

    public String getLocalName() {
        return null;
    }

    public String getBaseURI() {
        return null;
    }

    public boolean hasAttributes() {
        return false;
    }

    public void setNodeValue(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node removeChild(Node node) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node appendChild(Node node) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void setPrefix(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public short compareDocumentPosition(Node node) {
        throw new DOMException(9, "Method not supported");
    }

    public String getTextContent() throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void setTextContent(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public boolean isSameNode(Node node) {
        throw new DOMException(9, "Method not supported");
    }

    public String lookupPrefix(String string) {
        throw new DOMException(9, "Method not supported");
    }

    public boolean isDefaultNamespace(String string) {
        throw new DOMException(9, "Method not supported");
    }

    public String lookupNamespaceURI(String string) {
        throw new DOMException(9, "Method not supported");
    }

    public boolean isEqualNode(Node node) {
        throw new DOMException(9, "Method not supported");
    }

    public Object getFeature(String string, String string2) {
        return null;
    }

    public Object setUserData(String string, Object object, UserDataHandler userDataHandler) {
        throw new DOMException(9, "Method not supported");
    }

    public Object getUserData(String string) {
        return null;
    }
}

