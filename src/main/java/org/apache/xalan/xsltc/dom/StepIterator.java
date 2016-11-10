/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public class StepIterator
extends DTMAxisIteratorBase {
    protected DTMAxisIterator _source;
    protected DTMAxisIterator _iterator;
    private int _pos = -1;

    public StepIterator(DTMAxisIterator dTMAxisIterator, DTMAxisIterator dTMAxisIterator2) {
        this._source = dTMAxisIterator;
        this._iterator = dTMAxisIterator2;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
        this._iterator.setRestartable(true);
    }

    public DTMAxisIterator cloneIterator() {
        this._isRestartable = false;
        try {
            StepIterator stepIterator = (StepIterator)Object.super.clone();
            stepIterator._source = this._source.cloneIterator();
            stepIterator._iterator = this._iterator.cloneIterator();
            stepIterator._iterator.setRestartable(true);
            stepIterator._isRestartable = false;
            return stepIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            this._startNode = n2;
            this._source.setStartNode(this._startNode);
            this._iterator.setStartNode(this._includeSelf ? this._startNode : this._source.next());
            return this.resetPosition();
        }
        return this;
    }

    public DTMAxisIterator reset() {
        this._source.reset();
        this._iterator.setStartNode(this._includeSelf ? this._startNode : this._source.next());
        return this.resetPosition();
    }

    public int next() {
        int n2;
        while ((n2 = this._iterator.next()) == -1) {
            n2 = this._source.next();
            if (n2 == -1) {
                return -1;
            }
            this._iterator.setStartNode(n2);
        }
        return this.returnNode(n2);
    }

    public void setMark() {
        this._source.setMark();
        this._iterator.setMark();
    }

    public void gotoMark() {
        this._source.gotoMark();
        this._iterator.gotoMark();
    }
}

