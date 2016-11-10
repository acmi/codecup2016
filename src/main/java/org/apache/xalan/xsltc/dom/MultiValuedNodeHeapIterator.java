/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public abstract class MultiValuedNodeHeapIterator
extends DTMAxisIteratorBase {
    private static final int InitSize = 8;
    private int _heapSize = 0;
    private int _size = 8;
    private HeapNode[] _heap = new HeapNode[8];
    private int _free = 0;
    private int _returnedLast;
    private int _cachedReturnedLast = -1;
    private int _cachedHeapSize;

    public DTMAxisIterator cloneIterator() {
        this._isRestartable = false;
        HeapNode[] arrheapNode = new HeapNode[this._heap.length];
        try {
            MultiValuedNodeHeapIterator multiValuedNodeHeapIterator = (MultiValuedNodeHeapIterator)Object.super.clone();
            for (int i2 = 0; i2 < this._free; ++i2) {
                arrheapNode[i2] = this._heap[i2].cloneHeapNode();
            }
            multiValuedNodeHeapIterator.setRestartable(false);
            multiValuedNodeHeapIterator._heap = arrheapNode;
            return multiValuedNodeHeapIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    protected void addHeapNode(HeapNode heapNode) {
        if (this._free == this._size) {
            HeapNode[] arrheapNode = new HeapNode[this._size *= 2];
            System.arraycopy(this._heap, 0, arrheapNode, 0, this._free);
            this._heap = arrheapNode;
        }
        ++this._heapSize;
        this._heap[this._free++] = heapNode;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int next() {
        while (this._heapSize > 0) {
            int n2 = this._heap[0]._node;
            if (n2 == -1) {
                if (this._heapSize <= 1) return -1;
                HeapNode heapNode = this._heap[0];
                this._heap[0] = this._heap[--this._heapSize];
                this._heap[this._heapSize] = heapNode;
            } else if (n2 == this._returnedLast) {
                this._heap[0].step();
            } else {
                this._heap[0].step();
                this.heapify(0);
                this._returnedLast = n2;
                return this.returnNode(this._returnedLast);
            }
            this.heapify(0);
        }
        return -1;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            int n3;
            this._startNode = n2;
            for (n3 = 0; n3 < this._free; ++n3) {
                if (this._heap[n3]._isStartSet) continue;
                this._heap[n3].setStartNode(n2);
                this._heap[n3].step();
                this._heap[n3]._isStartSet = true;
            }
            this._heapSize = this._free;
            for (n3 = this._heapSize / 2; n3 >= 0; --n3) {
                this.heapify(n3);
            }
            this._returnedLast = -1;
            return this.resetPosition();
        }
        return this;
    }

    protected void init() {
        for (int i2 = 0; i2 < this._free; ++i2) {
            this._heap[i2] = null;
        }
        this._heapSize = 0;
        this._free = 0;
    }

    private void heapify(int n2) {
        do {
            int n3;
            int n4;
            int n5;
            int n6 = n4 = (n5 = (n3 = n2 + 1 << 1) - 1) < this._heapSize && this._heap[n5].isLessThan(this._heap[n2]) ? n5 : n2;
            if (n3 < this._heapSize && this._heap[n3].isLessThan(this._heap[n4])) {
                n4 = n3;
            }
            if (n4 == n2) break;
            HeapNode heapNode = this._heap[n4];
            this._heap[n4] = this._heap[n2];
            this._heap[n2] = heapNode;
            n2 = n4;
        } while (true);
    }

    public void setMark() {
        for (int i2 = 0; i2 < this._free; ++i2) {
            this._heap[i2].setMark();
        }
        this._cachedReturnedLast = this._returnedLast;
        this._cachedHeapSize = this._heapSize;
    }

    public void gotoMark() {
        int n2;
        for (n2 = 0; n2 < this._free; ++n2) {
            this._heap[n2].gotoMark();
        }
        this._heapSize = this._cachedHeapSize;
        for (n2 = this._heapSize / 2; n2 >= 0; --n2) {
            this.heapify(n2);
        }
        this._returnedLast = this._cachedReturnedLast;
    }

    public DTMAxisIterator reset() {
        int n2;
        for (n2 = 0; n2 < this._free; ++n2) {
            this._heap[n2].reset();
            this._heap[n2].step();
        }
        this._heapSize = this._free;
        for (n2 = this._heapSize / 2; n2 >= 0; --n2) {
            this.heapify(n2);
        }
        this._returnedLast = -1;
        return this.resetPosition();
    }

    public abstract class HeapNode
    implements Cloneable {
        protected int _node;
        protected int _markedNode;
        protected boolean _isStartSet;
        private final MultiValuedNodeHeapIterator this$0;

        public HeapNode(MultiValuedNodeHeapIterator multiValuedNodeHeapIterator) {
            this.this$0 = multiValuedNodeHeapIterator;
            this._isStartSet = false;
        }

        public abstract int step();

        public HeapNode cloneHeapNode() {
            HeapNode heapNode;
            try {
                heapNode = (HeapNode)super.clone();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
                return null;
            }
            heapNode._node = this._node;
            heapNode._markedNode = this._node;
            return heapNode;
        }

        public void setMark() {
            this._markedNode = this._node;
        }

        public void gotoMark() {
            this._node = this._markedNode;
        }

        public abstract boolean isLessThan(HeapNode var1);

        public abstract HeapNode setStartNode(int var1);

        public abstract HeapNode reset();
    }

}

