/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.ParentNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DeferredElementImpl
extends ElementImpl
implements DeferredNode {
    static final long serialVersionUID = -7670981133940934842L;
    protected transient int fNodeIndex;

    DeferredElementImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
        super(deferredDocumentImpl, null);
        this.fNodeIndex = n2;
        this.needsSyncChildren(true);
    }

    public final int getNodeIndex() {
        return this.fNodeIndex;
    }

    protected final void synchronizeData() {
        this.needsSyncData(false);
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument;
        boolean bl = deferredDocumentImpl.mutationEvents;
        deferredDocumentImpl.mutationEvents = false;
        this.name = deferredDocumentImpl.getNodeName(this.fNodeIndex);
        this.setupDefaultAttributes();
        int n2 = deferredDocumentImpl.getNodeExtra(this.fNodeIndex);
        if (n2 != -1) {
            NamedNodeMap namedNodeMap = this.getAttributes();
            do {
                NodeImpl nodeImpl = (NodeImpl)((Object)deferredDocumentImpl.getNodeObject(n2));
                namedNodeMap.setNamedItem(nodeImpl);
            } while ((n2 = deferredDocumentImpl.getPrevSibling(n2)) != -1);
        }
        deferredDocumentImpl.mutationEvents = bl;
    }

    protected final void synchronizeChildren() {
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument();
        deferredDocumentImpl.synchronizeChildren(this, this.fNodeIndex);
    }
}

