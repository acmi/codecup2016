/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.AttrNSImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;

public final class DeferredAttrNSImpl
extends AttrNSImpl
implements DeferredNode {
    static final long serialVersionUID = 6074924934945957154L;
    protected transient int fNodeIndex;

    DeferredAttrNSImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
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
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument();
        this.name = deferredDocumentImpl.getNodeName(this.fNodeIndex);
        int n2 = this.name.indexOf(58);
        this.localName = n2 < 0 ? this.name : this.name.substring(n2 + 1);
        int n3 = deferredDocumentImpl.getNodeExtra(this.fNodeIndex);
        this.isSpecified((n3 & 32) != 0);
        this.isIdAttribute((n3 & 512) != 0);
        this.namespaceURI = deferredDocumentImpl.getNodeURI(this.fNodeIndex);
        int n4 = deferredDocumentImpl.getLastChild(this.fNodeIndex);
        this.type = deferredDocumentImpl.getTypeInfo(n4);
    }

    protected void synchronizeChildren() {
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument();
        deferredDocumentImpl.synchronizeChildren(this, this.fNodeIndex);
    }
}

