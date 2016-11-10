/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.ParentNode;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DeferredElementNSImpl
extends ElementNSImpl
implements DeferredNode {
    static final long serialVersionUID = -5001885145370927385L;
    protected transient int fNodeIndex;

    DeferredElementNSImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
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
        int n2 = this.name.indexOf(58);
        this.localName = n2 < 0 ? this.name : this.name.substring(n2 + 1);
        this.namespaceURI = deferredDocumentImpl.getNodeURI(this.fNodeIndex);
        this.type = (XSTypeDefinition)deferredDocumentImpl.getTypeInfo(this.fNodeIndex);
        this.setupDefaultAttributes();
        int n3 = deferredDocumentImpl.getNodeExtra(this.fNodeIndex);
        if (n3 != -1) {
            NamedNodeMap namedNodeMap = this.getAttributes();
            boolean bl2 = false;
            do {
                AttrImpl attrImpl;
                if (!(attrImpl = (AttrImpl)((Object)deferredDocumentImpl.getNodeObject(n3))).getSpecified() && (bl2 || attrImpl.getNamespaceURI() != null && attrImpl.getNamespaceURI() != NamespaceContext.XMLNS_URI && attrImpl.getName().indexOf(58) < 0)) {
                    bl2 = true;
                    namedNodeMap.setNamedItemNS(attrImpl);
                    continue;
                }
                namedNodeMap.setNamedItem(attrImpl);
            } while ((n3 = deferredDocumentImpl.getPrevSibling(n3)) != -1);
        }
        deferredDocumentImpl.mutationEvents = bl;
    }

    protected final void synchronizeChildren() {
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument();
        deferredDocumentImpl.synchronizeChildren(this, this.fNodeIndex);
    }
}

