/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.ElementDefinitionImpl;
import org.apache.xerces.dom.NamedNodeMapImpl;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Node;

public class DeferredElementDefinitionImpl
extends ElementDefinitionImpl
implements DeferredNode {
    static final long serialVersionUID = 6703238199538041591L;
    protected transient int fNodeIndex;

    DeferredElementDefinitionImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
        super(deferredDocumentImpl, null);
        this.fNodeIndex = n2;
        this.needsSyncData(true);
        this.needsSyncChildren(true);
    }

    public int getNodeIndex() {
        return this.fNodeIndex;
    }

    protected void synchronizeData() {
        this.needsSyncData(false);
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument;
        this.name = deferredDocumentImpl.getNodeName(this.fNodeIndex);
    }

    protected void synchronizeChildren() {
        boolean bl = this.ownerDocument.getMutationEvents();
        this.ownerDocument.setMutationEvents(false);
        this.needsSyncChildren(false);
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument;
        this.attributes = new NamedNodeMapImpl(deferredDocumentImpl);
        int n2 = deferredDocumentImpl.getLastChild(this.fNodeIndex);
        while (n2 != -1) {
            DeferredNode deferredNode = deferredDocumentImpl.getNodeObject(n2);
            this.attributes.setNamedItem(deferredNode);
            n2 = deferredDocumentImpl.getPrevSibling(n2);
        }
        deferredDocumentImpl.setMutationEvents(bl);
    }
}

