/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public class SingletonIterator
extends DTMAxisIteratorBase {
    private int _node;
    private final boolean _isConstant;

    public SingletonIterator() {
        this(Integer.MIN_VALUE, false);
    }

    public SingletonIterator(int n2) {
        this(n2, false);
    }

    public SingletonIterator(int n2, boolean bl) {
        this._node = this._startNode = n2;
        this._isConstant = bl;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (this._isConstant) {
            this._node = this._startNode;
            return this.resetPosition();
        }
        if (this._isRestartable) {
            if (this._node <= 0) {
                this._node = this._startNode = n2;
            }
            return this.resetPosition();
        }
        return this;
    }

    public DTMAxisIterator reset() {
        if (this._isConstant) {
            this._node = this._startNode;
            return this.resetPosition();
        }
        boolean bl = this._isRestartable;
        this._isRestartable = true;
        this.setStartNode(this._startNode);
        this._isRestartable = bl;
        return this;
    }

    public int next() {
        int n2 = this._node;
        this._node = -1;
        return this.returnNode(n2);
    }

    public void setMark() {
        this._markedNode = this._node;
    }

    public void gotoMark() {
        this._node = this._markedNode;
    }
}

