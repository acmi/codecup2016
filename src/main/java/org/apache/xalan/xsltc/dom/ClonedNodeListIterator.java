/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.dom.CachedNodeListIterator;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class ClonedNodeListIterator
extends DTMAxisIteratorBase {
    private CachedNodeListIterator _source;
    private int _index = 0;

    public ClonedNodeListIterator(CachedNodeListIterator cachedNodeListIterator) {
        this._source = cachedNodeListIterator;
    }

    public void setRestartable(boolean bl) {
    }

    public DTMAxisIterator setStartNode(int n2) {
        return this;
    }

    public int next() {
        return this._source.getNode(this._index++);
    }

    public int getPosition() {
        return this._index == 0 ? 1 : this._index;
    }

    public int getNodeByPosition(int n2) {
        return this._source.getNode(n2);
    }

    public DTMAxisIterator cloneIterator() {
        return this._source.cloneIterator();
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

