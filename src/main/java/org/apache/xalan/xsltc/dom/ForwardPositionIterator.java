/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class ForwardPositionIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;

    public ForwardPositionIterator(DTMAxisIterator dTMAxisIterator) {
        this._source = dTMAxisIterator;
    }

    public DTMAxisIterator cloneIterator() {
        try {
            ForwardPositionIterator forwardPositionIterator = (ForwardPositionIterator)Object.super.clone();
            forwardPositionIterator._source = this._source.cloneIterator();
            forwardPositionIterator._isRestartable = false;
            return forwardPositionIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public int next() {
        return this.returnNode(this._source.next());
    }

    public DTMAxisIterator setStartNode(int n2) {
        this._source.setStartNode(n2);
        return this;
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

