/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeIteratorImpl
implements NodeIterator {
    private DocumentImpl fDocument;
    private Node fRoot;
    private int fWhatToShow = -1;
    private NodeFilter fNodeFilter;
    private boolean fDetach = false;
    private Node fCurrentNode;
    private boolean fForward = true;
    private boolean fEntityReferenceExpansion;

    public NodeIteratorImpl(DocumentImpl documentImpl, Node node, int n2, NodeFilter nodeFilter, boolean bl) {
        this.fDocument = documentImpl;
        this.fRoot = node;
        this.fCurrentNode = null;
        this.fWhatToShow = n2;
        this.fNodeFilter = nodeFilter;
        this.fEntityReferenceExpansion = bl;
    }

    public Node getRoot() {
        return this.fRoot;
    }

    public int getWhatToShow() {
        return this.fWhatToShow;
    }

    public NodeFilter getFilter() {
        return this.fNodeFilter;
    }

    public boolean getExpandEntityReferences() {
        return this.fEntityReferenceExpansion;
    }

    public Node nextNode() {
        if (this.fDetach) {
            throw new DOMException(11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
        }
        if (this.fRoot == null) {
            return null;
        }
        Node node = this.fCurrentNode;
        boolean bl = false;
        while (!bl) {
            node = !this.fForward && node != null ? this.fCurrentNode : (!this.fEntityReferenceExpansion && node != null && node.getNodeType() == 5 ? this.nextNode(node, false) : this.nextNode(node, true));
            this.fForward = true;
            if (node == null) {
                return null;
            }
            bl = this.acceptNode(node);
            if (!bl) continue;
            this.fCurrentNode = node;
            return this.fCurrentNode;
        }
        return null;
    }

    public Node previousNode() {
        if (this.fDetach) {
            throw new DOMException(11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
        }
        if (this.fRoot == null || this.fCurrentNode == null) {
            return null;
        }
        Node node = this.fCurrentNode;
        boolean bl = false;
        while (!bl) {
            node = this.fForward && node != null ? this.fCurrentNode : this.previousNode(node);
            this.fForward = false;
            if (node == null) {
                return null;
            }
            bl = this.acceptNode(node);
            if (!bl) continue;
            this.fCurrentNode = node;
            return this.fCurrentNode;
        }
        return null;
    }

    boolean acceptNode(Node node) {
        if (this.fNodeFilter == null) {
            return (this.fWhatToShow & 1 << node.getNodeType() - 1) != 0;
        }
        return (this.fWhatToShow & 1 << node.getNodeType() - 1) != 0 && this.fNodeFilter.acceptNode(node) == 1;
    }

    Node matchNodeOrParent(Node node) {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node node2 = this.fCurrentNode;
        while (node2 != this.fRoot) {
            if (node == node2) {
                return node2;
            }
            node2 = node2.getParentNode();
        }
        return null;
    }

    Node nextNode(Node node, boolean bl) {
        if (node == null) {
            return this.fRoot;
        }
        if (bl && node.hasChildNodes()) {
            Node node2 = node.getFirstChild();
            return node2;
        }
        if (node == this.fRoot) {
            return null;
        }
        Node node3 = node.getNextSibling();
        if (node3 != null) {
            return node3;
        }
        Node node4 = node.getParentNode();
        while (node4 != null && node4 != this.fRoot) {
            node3 = node4.getNextSibling();
            if (node3 != null) {
                return node3;
            }
            node4 = node4.getParentNode();
        }
        return null;
    }

    Node previousNode(Node node) {
        if (node == this.fRoot) {
            return null;
        }
        Node node2 = node.getPreviousSibling();
        if (node2 == null) {
            node2 = node.getParentNode();
            return node2;
        }
        if (node2.hasChildNodes() && (this.fEntityReferenceExpansion || node2 == null || node2.getNodeType() != 5)) {
            while (node2.hasChildNodes()) {
                node2 = node2.getLastChild();
            }
        }
        return node2;
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node node2 = this.matchNodeOrParent(node);
        if (node2 == null) {
            return;
        }
        if (this.fForward) {
            this.fCurrentNode = this.previousNode(node2);
        } else {
            Node node3 = this.nextNode(node2, false);
            if (node3 != null) {
                this.fCurrentNode = node3;
            } else {
                this.fCurrentNode = this.previousNode(node2);
                this.fForward = true;
            }
        }
    }

    public void detach() {
        this.fDetach = true;
        this.fDocument.removeNodeIterator(this);
    }
}

