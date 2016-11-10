/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class NthIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;
    private final int _position;
    private boolean _ready;

    public NthIterator(DTMAxisIterator dTMAxisIterator, int n2) {
        this._source = dTMAxisIterator;
        this._position = n2;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
    }

    public DTMAxisIterator cloneIterator() {
        try {
            NthIterator nthIterator = (NthIterator)Object.super.clone();
            nthIterator._source = this._source.cloneIterator();
            nthIterator._isRestartable = false;
            return nthIterator;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public int next() {
        if (this._ready) {
            this._ready = false;
            return this._source.getNodeByPosition(this._position);
        }
        return -1;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            this._source.setStartNode(n2);
            this._ready = true;
        }
        return this;
    }

    public DTMAxisIterator reset() {
        this._source.reset();
        this._ready = true;
        return this;
    }

    public int getLast() {
        return 1;
    }

    public int getPosition() {
        return 1;
    }

    public void setMark() {
        this._source.setMark();
    }

    public void gotoMark() {
        this._source.gotoMark();
    }
}

