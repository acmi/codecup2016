/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.dom.CurrentNodeListFilter;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class CurrentNodeListIterator
extends DTMAxisIteratorBase {
    private boolean _docOrder;
    private DTMAxisIterator _source;
    private final CurrentNodeListFilter _filter;
    private IntegerArray _nodes = new IntegerArray();
    private int _currentIndex;
    private final int _currentNode;
    private AbstractTranslet _translet;

    public CurrentNodeListIterator(DTMAxisIterator dTMAxisIterator, CurrentNodeListFilter currentNodeListFilter, int n2, AbstractTranslet abstractTranslet) {
        this(dTMAxisIterator, !dTMAxisIterator.isReverse(), currentNodeListFilter, n2, abstractTranslet);
    }

    public CurrentNodeListIterator(DTMAxisIterator dTMAxisIterator, boolean bl, CurrentNodeListFilter currentNodeListFilter, int n2, AbstractTranslet abstractTranslet) {
        this._source = dTMAxisIterator;
        this._filter = currentNodeListFilter;
        this._translet = abstractTranslet;
        this._docOrder = bl;
        this._currentNode = n2;
    }

    public DTMAxisIterator forceNaturalOrder() {
        this._docOrder = true;
        return this;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
        this._source.setRestartable(bl);
    }

    public boolean isReverse() {
        return !this._docOrder;
    }

    public DTMAxisIterator cloneIterator() {
        try {
            CurrentNodeListIterator currentNodeListIterator = (CurrentNodeListIterator)Object.super.clone();
            currentNodeListIterator._nodes = (IntegerArray)this._nodes.clone();
            currentNodeListIterator._source = this._source.cloneIterator();
            currentNodeListIterator._isRestartable = false;
            return currentNodeListIterator.reset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
            return null;
        }
    }

    public DTMAxisIterator reset() {
        this._currentIndex = 0;
        return this.resetPosition();
    }

    public int next() {
        int n2 = this._nodes.cardinality();
        int n3 = this._currentNode;
        AbstractTranslet abstractTranslet = this._translet;
        int n4 = this._currentIndex;
        while (n4 < n2) {
            int n5;
            int n6;
            int n7 = n6 = this._docOrder ? n4 + 1 : n2 - n4;
            if (!this._filter.test(n5 = this._nodes.at(n4++), n6, n2, n3, abstractTranslet, this)) continue;
            this._currentIndex = n4;
            return this.returnNode(n5);
        }
        return -1;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isRestartable) {
            this._startNode = n2;
            this._source.setStartNode(this._startNode);
            this._nodes.clear();
            while ((n2 = this._source.next()) != -1) {
                this._nodes.add(n2);
            }
            this._currentIndex = 0;
            this.resetPosition();
        }
        return this;
    }

    public int getLast() {
        if (this._last == -1) {
            this._last = this.computePositionOfLast();
        }
        return this._last;
    }

    public void setMark() {
        this._markedNode = this._currentIndex;
    }

    public void gotoMark() {
        this._currentIndex = this._markedNode;
    }

    private int computePositionOfLast() {
        int n2 = this._nodes.cardinality();
        int n3 = this._currentNode;
        AbstractTranslet abstractTranslet = this._translet;
        int n4 = this._position;
        int n5 = this._currentIndex;
        while (n5 < n2) {
            int n6;
            int n7;
            int n8 = n6 = this._docOrder ? n5 + 1 : n2 - n5;
            if (!this._filter.test(n7 = this._nodes.at(n5++), n6, n2, n3, abstractTranslet, this)) continue;
            ++n4;
        }
        return n4;
    }
}

