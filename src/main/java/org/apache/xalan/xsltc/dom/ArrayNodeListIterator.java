/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;

public class ArrayNodeListIterator
implements DTMAxisIterator {
    private int _pos = 0;
    private int _mark = 0;
    private int[] _nodes;
    private static final int[] EMPTY = new int[0];

    public ArrayNodeListIterator(int[] arrn) {
        this._nodes = arrn;
    }

    public int next() {
        int n2 = this._pos < this._nodes.length ? this._nodes[this._pos++] : -1;
        return n2;
    }

    public DTMAxisIterator reset() {
        this._pos = 0;
        return this;
    }

    public int getLast() {
        return this._nodes.length;
    }

    public int getPosition() {
        return this._pos;
    }

    public void setMark() {
        this._mark = this._pos;
    }

    public void gotoMark() {
        this._pos = this._mark;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (n2 == -1) {
            this._nodes = EMPTY;
        }
        return this;
    }

    public int getStartNode() {
        return -1;
    }

    public boolean isReverse() {
        return false;
    }

    public DTMAxisIterator cloneIterator() {
        return new ArrayNodeListIterator(this._nodes);
    }

    public void setRestartable(boolean bl) {
    }

    public int getNodeByPosition(int n2) {
        return this._nodes[n2 - 1];
    }
}

