/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.dom.MultiValuedNodeHeapIterator;
import org.apache.xml.dtm.DTMAxisIterator;

public final class UnionIterator
extends MultiValuedNodeHeapIterator {
    private final DOM _dom;

    public UnionIterator(DOM dOM) {
        this._dom = dOM;
    }

    public UnionIterator addIterator(DTMAxisIterator dTMAxisIterator) {
        this.addHeapNode(new LookAheadIterator(this, dTMAxisIterator));
        return this;
    }

    static DOM access$000(UnionIterator unionIterator) {
        return unionIterator._dom;
    }

    private final class LookAheadIterator
    extends MultiValuedNodeHeapIterator.HeapNode {
        public DTMAxisIterator iterator;
        private final UnionIterator this$0;

        public LookAheadIterator(UnionIterator unionIterator, DTMAxisIterator dTMAxisIterator) {
            super(unionIterator);
            this.this$0 = unionIterator;
            this.iterator = dTMAxisIterator;
        }

        public int step() {
            this._node = this.iterator.next();
            return this._node;
        }

        public MultiValuedNodeHeapIterator.HeapNode cloneHeapNode() {
            LookAheadIterator lookAheadIterator = (LookAheadIterator)super.cloneHeapNode();
            lookAheadIterator.iterator = this.iterator.cloneIterator();
            return lookAheadIterator;
        }

        public void setMark() {
            super.setMark();
            this.iterator.setMark();
        }

        public void gotoMark() {
            super.gotoMark();
            this.iterator.gotoMark();
        }

        public boolean isLessThan(MultiValuedNodeHeapIterator.HeapNode heapNode) {
            LookAheadIterator lookAheadIterator = (LookAheadIterator)heapNode;
            return UnionIterator.access$000(this.this$0).lessThan(this._node, heapNode._node);
        }

        public MultiValuedNodeHeapIterator.HeapNode setStartNode(int n2) {
            this.iterator.setStartNode(n2);
            return this;
        }

        public MultiValuedNodeHeapIterator.HeapNode reset() {
            this.iterator.reset();
            return this;
        }
    }

}

