/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.EntityImpl;
import org.apache.xerces.dom.ParentNode;

public class DeferredEntityImpl
extends EntityImpl
implements DeferredNode {
    static final long serialVersionUID = 4760180431078941638L;
    protected transient int fNodeIndex;

    DeferredEntityImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
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
        this.publicId = deferredDocumentImpl.getNodeValue(this.fNodeIndex);
        this.systemId = deferredDocumentImpl.getNodeURI(this.fNodeIndex);
        int n2 = deferredDocumentImpl.getNodeExtra(this.fNodeIndex);
        deferredDocumentImpl.getNodeType(n2);
        this.notationName = deferredDocumentImpl.getNodeName(n2);
        this.version = deferredDocumentImpl.getNodeValue(n2);
        this.encoding = deferredDocumentImpl.getNodeURI(n2);
        int n3 = deferredDocumentImpl.getNodeExtra(n2);
        this.baseURI = deferredDocumentImpl.getNodeName(n3);
        this.inputEncoding = deferredDocumentImpl.getNodeValue(n3);
    }

    protected void synchronizeChildren() {
        this.needsSyncChildren(false);
        this.isReadOnly(false);
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument();
        deferredDocumentImpl.synchronizeChildren(this, this.fNodeIndex);
        this.setReadOnly(true, true);
    }
}

