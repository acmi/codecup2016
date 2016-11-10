/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.NodeListCache;
import org.apache.xerces.dom.TextImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

public abstract class ParentNode
extends ChildNode {
    static final long serialVersionUID = 2815829867152120872L;
    protected CoreDocumentImpl ownerDocument;
    protected ChildNode firstChild = null;
    protected transient NodeListCache fNodeListCache = null;

    protected ParentNode(CoreDocumentImpl coreDocumentImpl) {
        super(coreDocumentImpl);
        this.ownerDocument = coreDocumentImpl;
    }

    public ParentNode() {
    }

    public Node cloneNode(boolean bl) {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        ParentNode parentNode = (ParentNode)super.cloneNode(bl);
        parentNode.ownerDocument = this.ownerDocument;
        parentNode.firstChild = null;
        parentNode.fNodeListCache = null;
        if (bl) {
            ChildNode childNode = this.firstChild;
            while (childNode != null) {
                parentNode.appendChild(childNode.cloneNode(true));
                childNode = childNode.nextSibling;
            }
        }
        return parentNode;
    }

    public Document getOwnerDocument() {
        return this.ownerDocument;
    }

    CoreDocumentImpl ownerDocument() {
        return this.ownerDocument;
    }

    protected void setOwnerDocument(CoreDocumentImpl coreDocumentImpl) {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        super.setOwnerDocument(coreDocumentImpl);
        this.ownerDocument = coreDocumentImpl;
        ChildNode childNode = this.firstChild;
        while (childNode != null) {
            childNode.setOwnerDocument(coreDocumentImpl);
            childNode = childNode.nextSibling;
        }
    }

    public boolean hasChildNodes() {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        return this.firstChild != null;
    }

    public NodeList getChildNodes() {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        return this;
    }

    public Node getFirstChild() {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        return this.firstChild;
    }

    public Node getLastChild() {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        return this.lastChild();
    }

    final ChildNode lastChild() {
        return this.firstChild != null ? this.firstChild.previousSibling : null;
    }

    final void lastChild(ChildNode childNode) {
        if (this.firstChild != null) {
            this.firstChild.previousSibling = childNode;
        }
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        return this.internalInsertBefore(node, node2, false);
    }

    Node internalInsertBefore(Node node, Node node2, boolean bl) throws DOMException {
        NodeImpl nodeImpl;
        boolean bl2 = this.ownerDocument.errorChecking;
        if (node.getNodeType() == 11) {
            if (bl2) {
                Node node3 = node.getFirstChild();
                while (node3 != null) {
                    if (!this.ownerDocument.isKidOK(this, node3)) {
                        throw new DOMException(3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
                    }
                    node3 = node3.getNextSibling();
                }
            }
            while (node.hasChildNodes()) {
                this.insertBefore(node.getFirstChild(), node2);
            }
            return node;
        }
        if (node == node2) {
            node2 = node2.getNextSibling();
            this.removeChild(node);
            this.insertBefore(node, node2);
            return node;
        }
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        if (bl2) {
            if (this.isReadOnly()) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
            }
            if (node.getOwnerDocument() != this.ownerDocument && node != this.ownerDocument) {
                throw new DOMException(4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
            }
            if (!this.ownerDocument.isKidOK(this, node)) {
                throw new DOMException(3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
            }
            if (node2 != null && node2.getParentNode() != this) {
                throw new DOMException(8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
            }
            boolean bl3 = true;
            nodeImpl = this;
            while (bl3 && nodeImpl != null) {
                bl3 = node != nodeImpl;
                nodeImpl = nodeImpl.parentNode();
            }
            if (!bl3) {
                throw new DOMException(3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
            }
        }
        this.ownerDocument.insertingNode(this, bl);
        ChildNode childNode = (ChildNode)node;
        nodeImpl = childNode.parentNode();
        if (nodeImpl != null) {
            nodeImpl.removeChild(childNode);
        }
        ChildNode childNode2 = (ChildNode)node2;
        childNode.ownerNode = this;
        childNode.isOwned(true);
        if (this.firstChild == null) {
            this.firstChild = childNode;
            childNode.isFirstChild(true);
            childNode.previousSibling = childNode;
        } else if (childNode2 == null) {
            ChildNode childNode3 = this.firstChild.previousSibling;
            childNode3.nextSibling = childNode;
            childNode.previousSibling = childNode3;
            this.firstChild.previousSibling = childNode;
        } else if (node2 == this.firstChild) {
            this.firstChild.isFirstChild(false);
            childNode.nextSibling = this.firstChild;
            childNode.previousSibling = this.firstChild.previousSibling;
            this.firstChild.previousSibling = childNode;
            this.firstChild = childNode;
            childNode.isFirstChild(true);
        } else {
            ChildNode childNode4 = childNode2.previousSibling;
            childNode.nextSibling = childNode2;
            childNode4.nextSibling = childNode;
            childNode2.previousSibling = childNode;
            childNode.previousSibling = childNode4;
        }
        this.changed();
        if (this.fNodeListCache != null) {
            if (this.fNodeListCache.fLength != -1) {
                ++this.fNodeListCache.fLength;
            }
            if (this.fNodeListCache.fChildIndex != -1) {
                if (this.fNodeListCache.fChild == childNode2) {
                    this.fNodeListCache.fChild = childNode;
                } else {
                    this.fNodeListCache.fChildIndex = -1;
                }
            }
        }
        this.ownerDocument.insertedNode(this, childNode, bl);
        this.checkNormalizationAfterInsert(childNode);
        return node;
    }

    public Node removeChild(Node node) throws DOMException {
        return this.internalRemoveChild(node, false);
    }

    Node internalRemoveChild(Node node, boolean bl) throws DOMException {
        ChildNode childNode;
        CoreDocumentImpl coreDocumentImpl = this.ownerDocument();
        if (coreDocumentImpl.errorChecking) {
            if (this.isReadOnly()) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
            }
            if (node != null && node.getParentNode() != this) {
                throw new DOMException(8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
            }
        }
        ChildNode childNode2 = (ChildNode)node;
        coreDocumentImpl.removingNode(this, childNode2, bl);
        if (this.fNodeListCache != null) {
            if (this.fNodeListCache.fLength != -1) {
                --this.fNodeListCache.fLength;
            }
            if (this.fNodeListCache.fChildIndex != -1) {
                if (this.fNodeListCache.fChild == childNode2) {
                    --this.fNodeListCache.fChildIndex;
                    this.fNodeListCache.fChild = childNode2.previousSibling();
                } else {
                    this.fNodeListCache.fChildIndex = -1;
                }
            }
        }
        if (childNode2 == this.firstChild) {
            childNode2.isFirstChild(false);
            this.firstChild = childNode2.nextSibling;
            if (this.firstChild != null) {
                this.firstChild.isFirstChild(true);
                this.firstChild.previousSibling = childNode2.previousSibling;
            }
        } else {
            ChildNode childNode3;
            childNode = childNode2.previousSibling;
            childNode.nextSibling = childNode3 = childNode2.nextSibling;
            if (childNode3 == null) {
                this.firstChild.previousSibling = childNode;
            } else {
                childNode3.previousSibling = childNode;
            }
        }
        childNode = childNode2.previousSibling();
        childNode2.ownerNode = coreDocumentImpl;
        childNode2.isOwned(false);
        childNode2.nextSibling = null;
        childNode2.previousSibling = null;
        this.changed();
        coreDocumentImpl.removedNode(this, bl);
        this.checkNormalizationAfterRemove(childNode);
        return childNode2;
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        this.ownerDocument.replacingNode(this);
        this.internalInsertBefore(node, node2, true);
        if (node != node2) {
            this.internalRemoveChild(node2, true);
        }
        this.ownerDocument.replacedNode(this);
        return node2;
    }

    public String getTextContent() throws DOMException {
        Node node = this.getFirstChild();
        if (node != null) {
            Node node2 = node.getNextSibling();
            if (node2 == null) {
                return this.hasTextContent(node) ? ((NodeImpl)node).getTextContent() : "";
            }
            StringBuffer stringBuffer = new StringBuffer();
            this.getTextContent(stringBuffer);
            return stringBuffer.toString();
        }
        return "";
    }

    void getTextContent(StringBuffer stringBuffer) throws DOMException {
        Node node = this.getFirstChild();
        while (node != null) {
            if (this.hasTextContent(node)) {
                ((NodeImpl)node).getTextContent(stringBuffer);
            }
            node = node.getNextSibling();
        }
    }

    final boolean hasTextContent(Node node) {
        return node.getNodeType() != 8 && node.getNodeType() != 7 && (node.getNodeType() != 3 || !((TextImpl)node).isIgnorableWhitespace());
    }

    public void setTextContent(String string) throws DOMException {
        Node node;
        while ((node = this.getFirstChild()) != null) {
            this.removeChild(node);
        }
        if (string != null && string.length() != 0) {
            this.appendChild(this.ownerDocument().createTextNode(string));
        }
    }

    private int nodeListGetLength() {
        if (this.fNodeListCache == null) {
            if (this.needsSyncChildren()) {
                this.synchronizeChildren();
            }
            if (this.firstChild == null) {
                return 0;
            }
            if (this.firstChild == this.lastChild()) {
                return 1;
            }
            this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
        }
        if (this.fNodeListCache.fLength == -1) {
            ChildNode childNode;
            int n2;
            if (this.fNodeListCache.fChildIndex != -1 && this.fNodeListCache.fChild != null) {
                n2 = this.fNodeListCache.fChildIndex;
                childNode = this.fNodeListCache.fChild;
            } else {
                childNode = this.firstChild;
                n2 = 0;
            }
            while (childNode != null) {
                ++n2;
                childNode = childNode.nextSibling;
            }
            this.fNodeListCache.fLength = n2;
        }
        return this.fNodeListCache.fLength;
    }

    public int getLength() {
        return this.nodeListGetLength();
    }

    private Node nodeListItem(int n2) {
        if (this.fNodeListCache == null) {
            if (this.needsSyncChildren()) {
                this.synchronizeChildren();
            }
            if (this.firstChild == this.lastChild()) {
                return n2 == 0 ? this.firstChild : null;
            }
            this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
        }
        int n3 = this.fNodeListCache.fChildIndex;
        ChildNode childNode = this.fNodeListCache.fChild;
        boolean bl = true;
        if (n3 != -1 && childNode != null) {
            bl = false;
            if (n3 < n2) {
                while (n3 < n2 && childNode != null) {
                    ++n3;
                    childNode = childNode.nextSibling;
                }
            } else if (n3 > n2) {
                while (n3 > n2 && childNode != null) {
                    --n3;
                    childNode = childNode.previousSibling();
                }
            }
        } else {
            if (n2 < 0) {
                return null;
            }
            childNode = this.firstChild;
            n3 = 0;
            while (n3 < n2 && childNode != null) {
                childNode = childNode.nextSibling;
                ++n3;
            }
        }
        if (!(bl || childNode != this.firstChild && childNode != this.lastChild())) {
            this.fNodeListCache.fChildIndex = -1;
            this.fNodeListCache.fChild = null;
            this.ownerDocument.freeNodeListCache(this.fNodeListCache);
        } else {
            this.fNodeListCache.fChildIndex = n3;
            this.fNodeListCache.fChild = childNode;
        }
        return childNode;
    }

    public Node item(int n2) {
        return this.nodeListItem(n2);
    }

    protected final NodeList getChildNodesUnoptimized() {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        return new NodeList(this){
            private final ParentNode this$0;

            public int getLength() {
                return ParentNode.access$000(this.this$0);
            }

            public Node item(int n2) {
                return ParentNode.access$100(this.this$0, n2);
            }
        };
    }

    public void normalize() {
        if (this.isNormalized()) {
            return;
        }
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        ChildNode childNode = this.firstChild;
        while (childNode != null) {
            childNode.normalize();
            childNode = childNode.nextSibling;
        }
        this.isNormalized(true);
    }

    public boolean isEqualNode(Node node) {
        if (!super.isEqualNode(node)) {
            return false;
        }
        Node node2 = this.getFirstChild();
        Node node3 = node.getFirstChild();
        while (node2 != null && node3 != null) {
            if (!node2.isEqualNode(node3)) {
                return false;
            }
            node2 = node2.getNextSibling();
            node3 = node3.getNextSibling();
        }
        if (node2 != node3) {
            return false;
        }
        return true;
    }

    public void setReadOnly(boolean bl, boolean bl2) {
        super.setReadOnly(bl, bl2);
        if (bl2) {
            if (this.needsSyncChildren()) {
                this.synchronizeChildren();
            }
            ChildNode childNode = this.firstChild;
            while (childNode != null) {
                if (childNode.getNodeType() != 5) {
                    childNode.setReadOnly(bl, true);
                }
                childNode = childNode.nextSibling;
            }
        }
    }

    protected void synchronizeChildren() {
        this.needsSyncChildren(false);
    }

    void checkNormalizationAfterInsert(ChildNode childNode) {
        if (childNode.getNodeType() == 3) {
            ChildNode childNode2 = childNode.previousSibling();
            ChildNode childNode3 = childNode.nextSibling;
            if (childNode2 != null && childNode2.getNodeType() == 3 || childNode3 != null && childNode3.getNodeType() == 3) {
                this.isNormalized(false);
            }
        } else if (!childNode.isNormalized()) {
            this.isNormalized(false);
        }
    }

    void checkNormalizationAfterRemove(ChildNode childNode) {
        ChildNode childNode2;
        if (childNode != null && childNode.getNodeType() == 3 && (childNode2 = childNode.nextSibling) != null && childNode2.getNodeType() == 3) {
            this.isNormalized(false);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.needsSyncChildren()) {
            this.synchronizeChildren();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.needsSyncChildren(false);
    }

    static int access$000(ParentNode parentNode) {
        return parentNode.nodeListGetLength();
    }

    static Node access$100(ParentNode parentNode, int n2) {
        return parentNode.nodeListItem(n2);
    }

    class UserDataRecord
    implements Serializable {
        private static final long serialVersionUID = 3258126977134310455L;
        Object fData;
        UserDataHandler fHandler;
        private final ParentNode this$0;

        UserDataRecord(ParentNode parentNode, Object object, UserDataHandler userDataHandler) {
            this.this$0 = parentNode;
            this.fData = object;
            this.fHandler = userDataHandler;
        }
    }

}

