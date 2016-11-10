/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class FilterIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;
    private final DTMFilter _filter;
    private final boolean _isReverse;

    public FilterIterator(DTMAxisIterator dTMAxisIterator, DTMFilter dTMFilter) {
        this._source = dTMAxisIterator;
        this._filter = dTMFilter;
        this._isReverse = dTMAxisIterator.isReverse();
    }

    public boolean isReverse() {
        return this._isReverse;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
    }

    public DTMAxisIterator cloneIterator() {
        try {
            FilterIterator filterIterator = (FilterIterator)Object.super.clone();
            filterIterator._source = this._source.cloneIterator();
            filterIterator._isRestartable = false;
            return filterIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public DTMAxisIterator reset() {
        this._source.reset();
        return this.resetPosition();
    }

    public int next() {
        int n2;
        while ((n2 = this._source.next()) != -1) {
            if (this._filter.acceptNode(n2, -1) != 1) continue;
            return this.returnNode(n2);
        }
        return -1;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            this._startNode = n2;
            this._source.setStartNode(this._startNode);
            return this.resetPosition();
        }
        return this;
    }

    public void setMark() {
        this._source.setMark();
    }

    public void gotoMark() {
        this._source.gotoMark();
    }
}

