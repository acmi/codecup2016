/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.dom.ClonedNodeListIterator;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class CachedNodeListIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;
    private IntegerArray _nodes = new IntegerArray();
    private int _numCachedNodes = 0;
    private int _index = 0;
    private boolean _isEnded = false;

    public CachedNodeListIterator(DTMAxisIterator dTMAxisIterator) {
        this._source = dTMAxisIterator;
    }

    public void setRestartable(boolean bl) {
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            this._startNode = n2;
            this._source.setStartNode(n2);
            this.resetPosition();
            this._isRestartable = false;
        }
        return this;
    }

    public int next() {
        return this.getNode(this._index++);
    }

    public int getPosition() {
        return this._index == 0 ? 1 : this._index;
    }

    public int getNodeByPosition(int n2) {
        return this.getNode(n2);
    }

    public int getNode(int n2) {
        if (n2 < this._numCachedNodes) {
            return this._nodes.at(n2);
        }
        if (!this._isEnded) {
            int n3 = this._source.next();
            if (n3 != -1) {
                this._nodes.add(n3);
                ++this._numCachedNodes;
            } else {
                this._isEnded = true;
            }
            return n3;
        }
        return -1;
    }

    public DTMAxisIterator cloneIterator() {
        ClonedNodeListIterator clonedNodeListIterator = new ClonedNodeListIterator(this);
        return clonedNodeListIterator;
    }

    public DTMAxisIterator reset() {
        this._index = 0;
        return this;
    }

    public void setMark() {
        this._source.setMark();
    }

    public void gotoMark() {
        this._source.gotoMark();
    }
}

