/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.dom.KeyIndex;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class DupFilterIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;
    private IntegerArray _nodes = new IntegerArray();
    private int _current = 0;
    private int _nodesSize = 0;
    private int _lastNext = -1;
    private int _markedLastNext = -1;

    public DupFilterIterator(DTMAxisIterator dTMAxisIterator) {
        this._source = dTMAxisIterator;
        if (dTMAxisIterator instanceof KeyIndex) {
            this.setStartNode(0);
        }
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            boolean bl = this._source instanceof KeyIndex;
            if (bl && this._startNode == 0) {
                return this;
            }
            if (n2 != this._startNode) {
                this._startNode = n2;
                this._source.setStartNode(this._startNode);
                this._nodes.clear();
                while ((n2 = this._source.next()) != -1) {
                    this._nodes.add(n2);
                }
                if (!bl) {
                    this._nodes.sort();
                }
                this._nodesSize = this._nodes.cardinality();
                this._current = 0;
                this._lastNext = -1;
                this.resetPosition();
            }
        }
        return this;
    }

    public int next() {
        while (this._current < this._nodesSize) {
            int n2;
            if ((n2 = this._nodes.at(this._current++)) == this._lastNext) continue;
            this._lastNext = n2;
            return this.returnNode(this._lastNext);
        }
        return -1;
    }

    public DTMAxisIterator cloneIterator() {
        try {
            DupFilterIterator dupFilterIterator = (DupFilterIterator)Object.super.clone();
            dupFilterIterator._nodes = (IntegerArray)this._nodes.clone();
            dupFilterIterator._source = this._source.cloneIterator();
            dupFilterIterator._isRestartable = false;
            return dupFilterIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
    }

    public void setMark() {
        this._markedNode = this._current;
        this._markedLastNext = this._lastNext;
    }

    public void gotoMark() {
        this._current = this._markedNode;
        this._lastNext = this._markedLastNext;
    }

    public DTMAxisIterator reset() {
        this._current = 0;
        this._lastNext = -1;
        return this.resetPosition();
    }
}

