/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.utils.WrappedRuntimeException;

public abstract class DTMAxisIteratorBase
implements DTMAxisIterator {
    protected int _last = -1;
    protected int _position = 0;
    protected int _markedNode;
    protected int _startNode = -1;
    protected boolean _includeSelf = false;
    protected boolean _isRestartable = true;

    public int getStartNode() {
        return this._startNode;
    }

    public DTMAxisIterator reset() {
        boolean bl = this._isRestartable;
        this._isRestartable = true;
        this.setStartNode(this._startNode);
        this._isRestartable = bl;
        return this;
    }

    public DTMAxisIterator includeSelf() {
        this._includeSelf = true;
        return this;
    }

    public int getLast() {
        if (this._last == -1) {
            int n2 = this._position;
            this.setMark();
            this.reset();
            do {
                ++this._last;
            } while (this.next() != -1);
            this.gotoMark();
            this._position = n2;
        }
        return this._last;
    }

    public int getPosition() {
        return this._position == 0 ? 1 : this._position;
    }

    public boolean isReverse() {
        return false;
    }

    public DTMAxisIterator cloneIterator() {
        try {
            DTMAxisIteratorBase dTMAxisIteratorBase = (DTMAxisIteratorBase)super.clone();
            dTMAxisIteratorBase._isRestartable = false;
            return dTMAxisIteratorBase;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new WrappedRuntimeException(cloneNotSupportedException);
        }
    }

    protected final int returnNode(int n2) {
        ++this._position;
        return n2;
    }

    protected final DTMAxisIterator resetPosition() {
        this._position = 0;
        return this;
    }

    public boolean isDocOrdered() {
        return true;
    }

    public int getAxis() {
        return -1;
    }

    public void setRestartable(boolean bl) {
        this._isRestartable = bl;
    }

    public int getNodeByPosition(int n2) {
        if (n2 > 0) {
            int n3;
            int n4;
            int n5 = n3 = this.isReverse() ? this.getLast() - n2 + 1 : n2;
            while ((n4 = this.next()) != -1) {
                if (n3 != this.getPosition()) continue;
                return n4;
            }
        }
        return -1;
    }
}

