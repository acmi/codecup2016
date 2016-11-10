/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.TextImpl;

public class DeferredTextImpl
extends TextImpl
implements DeferredNode {
    static final long serialVersionUID = 2310613872100393425L;
    protected transient int fNodeIndex;

    DeferredTextImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
        super(deferredDocumentImpl, null);
        this.fNodeIndex = n2;
        this.needsSyncData(true);
    }

    public int getNodeIndex() {
        return this.fNodeIndex;
    }

    protected void synchronizeData() {
        this.needsSyncData(false);
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument();
        this.data = deferredDocumentImpl.getNodeValueString(this.fNodeIndex);
        this.isIgnorableWhitespace(deferredDocumentImpl.getNodeExtra(this.fNodeIndex) == 1);
    }
}

