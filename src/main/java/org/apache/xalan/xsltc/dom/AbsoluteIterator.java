/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class AbsoluteIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;

    public AbsoluteIterator(DTMAxisIterator dTMAxisIterator) {
        this._source = dTMAxisIterator;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
    }

    public DTMAxisIterator setStartNode(int n2) {
        this._startNode = 0;
        if (this._isRestartable) {
            this._source.setStartNode(this._startNode);
            this.resetPosition();
        }
        return this;
    }

    public int next() {
        return this.returnNode(this._source.next());
    }

    public DTMAxisIterator cloneIterator() {
        try {
            AbsoluteIterator absoluteIterator = (AbsoluteIterator)Object.super.clone();
            absoluteIterator._source = this._source.cloneIterator();
            absoluteIterator.resetPosition();
            absoluteIterator._isRestartable = false;
            return absoluteIterator;
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

    public void setMark() {
        this._source.setMark();
    }

    public void gotoMark() {
        this._source.gotoMark();
    }
}

