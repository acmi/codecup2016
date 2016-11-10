/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.PrintStream;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.NamedNodeMapImpl;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DeferredDocumentTypeImpl
extends DocumentTypeImpl
implements DeferredNode {
    static final long serialVersionUID = -2172579663227313509L;
    protected transient int fNodeIndex;

    DeferredDocumentTypeImpl(DeferredDocumentImpl deferredDocumentImpl, int n2) {
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
        this.publicID = deferredDocumentImpl.getNodeValue(this.fNodeIndex);
        this.systemID = deferredDocumentImpl.getNodeURI(this.fNodeIndex);
        int n2 = deferredDocumentImpl.getNodeExtra(this.fNodeIndex);
        this.internalSubset = deferredDocumentImpl.getNodeValue(n2);
    }

    protected void synchronizeChildren() {
        boolean bl = this.ownerDocument().getMutationEvents();
        this.ownerDocument().setMutationEvents(false);
        this.needsSyncChildren(false);
        DeferredDocumentImpl deferredDocumentImpl = (DeferredDocumentImpl)this.ownerDocument;
        this.entities = new NamedNodeMapImpl(this);
        this.notations = new NamedNodeMapImpl(this);
        this.elements = new NamedNodeMapImpl(this);
        DeferredNode deferredNode = null;
        int n2 = deferredDocumentImpl.getLastChild(this.fNodeIndex);
        while (n2 != -1) {
            DeferredNode deferredNode2 = deferredDocumentImpl.getNodeObject(n2);
            short s2 = deferredNode2.getNodeType();
            switch (s2) {
                case 6: {
                    this.entities.setNamedItem(deferredNode2);
                    break;
                }
                case 12: {
                    this.notations.setNamedItem(deferredNode2);
                    break;
                }
                case 21: {
                    this.elements.setNamedItem(deferredNode2);
                    break;
                }
                case 1: {
                    if (((DocumentImpl)this.getOwnerDocument()).allowGrammarAccess) {
                        this.insertBefore(deferredNode2, deferredNode);
                        deferredNode = deferredNode2;
                        break;
                    }
                }
                default: {
                    System.out.println("DeferredDocumentTypeImpl#synchronizeInfo: node.getNodeType() = " + deferredNode2.getNodeType() + ", class = " + deferredNode2.getClass().getName());
                }
            }
            n2 = deferredDocumentImpl.getPrevSibling(n2);
        }
        this.ownerDocument().setMutationEvents(bl);
        this.setReadOnly(true, false);
    }
}

