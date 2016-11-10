/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class MatchingIterator
extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;
    private final int _match;

    public MatchingIterator(int n2, DTMAxisIterator dTMAxisIterator) {
        this._source = dTMAxisIterator;
        this._match = n2;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
    }

    public DTMAxisIterator cloneIterator() {
        try {
            MatchingIterator matchingIterator = (MatchingIterator)Object.super.clone();
            matchingIterator._source = this._source.cloneIterator();
            matchingIterator._isRestartable = false;
            return matchingIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            this._source.setStartNode(n2);
            this._position = 1;
            while ((n2 = this._source.next()) != -1 && n2 != this._match) {
                ++this._position;
            }
        }
        return this;
    }

    public DTMAxisIterator reset() {
        this._source.reset();
        return this.resetPosition();
    }

    public int next() {
        return this._source.next();
    }

    public int getLast() {
        if (this._last == -1) {
            this._last = this._source.getLast();
        }
        return this._last;
    }

    public int getPosition() {
        return this._position;
    }

    public void setMark() {
        this._source.setMark();
    }

    public void gotoMark() {
        this._source.gotoMark();
    }
}
