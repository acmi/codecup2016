/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.dom.NodeSortRecord;
import org.apache.xalan.xsltc.dom.NodeSortRecordFactory;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class SortingIterator
extends DTMAxisIteratorBase {
    private static final int INIT_DATA_SIZE = 16;
    private DTMAxisIterator _source;
    private NodeSortRecordFactory _factory;
    private NodeSortRecord[] _data;
    private int _free = 0;
    private int _current;

    public SortingIterator(DTMAxisIterator dTMAxisIterator, NodeSortRecordFactory nodeSortRecordFactory) {
        this._source = dTMAxisIterator;
        this._factory = nodeSortRecordFactory;
    }

    public int next() {
        int n2 = this._current < this._free ? this._data[this._current++].getNode() : -1;
        return n2;
    }

    public DTMAxisIterator setStartNode(int n2) {
        try {
            this._startNode = n2;
            this._source.setStartNode(this._startNode);
            this._data = new NodeSortRecord[16];
            this._free = 0;
            while ((n2 = this._source.next()) != -1) {
                this.addRecord(this._factory.makeNodeSortRecord(n2, this._free));
            }
            this.quicksort(0, this._free - 1);
            this._current = 0;
            return this;
        }
        catch (Exception exception) {
            return this;
        }
    }

    public int getPosition() {
        return this._current == 0 ? 1 : this._current;
    }

    public int getLast() {
        return this._free;
    }

    public void setMark() {
        this._source.setMark();
        this._markedNode = this._current;
    }

    public void gotoMark() {
        this._source.gotoMark();
        this._current = this._markedNode;
    }

    public DTMAxisIterator cloneIterator() {
        try {
            SortingIterator sortingIterator = (SortingIterator)Object.super.clone();
            sortingIterator._source = this._source.cloneIterator();
            sortingIterator._factory = this._factory;
            sortingIterator._data = this._data;
            sortingIterator._free = this._free;
            sortingIterator._current = this._current;
            sortingIterator.setRestartable(false);
            return sortingIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    private void addRecord(NodeSortRecord nodeSortRecord) {
        if (this._free == this._data.length) {
            NodeSortRecord[] arrnodeSortRecord = new NodeSortRecord[this._data.length * 2];
            System.arraycopy(this._data, 0, arrnodeSortRecord, 0, this._free);
            this._data = arrnodeSortRecord;
        }
        this._data[this._free++] = nodeSortRecord;
    }

    private void quicksort(int n2, int n3) {
        while (n2 < n3) {
            int n4 = this.partition(n2, n3);
            this.quicksort(n2, n4);
            n2 = n4 + 1;
        }
    }

    private int partition(int n2, int n3) {
        NodeSortRecord nodeSortRecord = this._data[n2 + n3 >>> 1];
        int n4 = n2 - 1;
        int n5 = n3 + 1;
        do {
            if (nodeSortRecord.compareTo(this._data[--n5]) < 0) {
                continue;
            }
            while (nodeSortRecord.compareTo(this._data[++n4]) > 0) {
            }
            if (n4 >= n5) break;
            NodeSortRecord nodeSortRecord2 = this._data[n4];
            this._data[n4] = this._data[n5];
            this._data[n5] = nodeSortRecord2;
        } while (true);
        return n5;
    }
}

