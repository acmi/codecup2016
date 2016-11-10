/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Node;

public abstract class ChildNode
extends NodeImpl {
    static final long serialVersionUID = -6112455738802414002L;
    protected ChildNode previousSibling;
    protected ChildNode nextSibling;

    protected ChildNode(CoreDocumentImpl coreDocumentImpl) {
        super(coreDocumentImpl);
    }

    public ChildNode() {
    }

    public Node cloneNode(boolean bl) {
        ChildNode childNode = (ChildNode)super.cloneNode(bl);
        childNode.previousSibling = null;
        childNode.nextSibling = null;
        childNode.isFirstChild(false);
        return childNode;
    }

    public Node getParentNode() {
        return this.isOwned() ? this.ownerNode : null;
    }

    final NodeImpl parentNode() {
        return this.isOwned() ? this.ownerNode : null;
    }

    public Node getNextSibling() {
        return this.nextSibling;
    }

    public Node getPreviousSibling() {
        return this.isFirstChild() ? null : this.previousSibling;
    }

    final ChildNode previousSibling() {
        return this.isFirstChild() ? null : this.previousSibling;
    }
}

