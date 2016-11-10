/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import java.util.AbstractList;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSException;

public final class ShortListImpl
extends AbstractList
implements ShortList {
    public static final ShortListImpl EMPTY_LIST = new ShortListImpl(new short[0], 0);
    private final short[] fArray;
    private final int fLength;

    public ShortListImpl(short[] arrs, int n2) {
        this.fArray = arrs;
        this.fLength = n2;
    }

    public int getLength() {
        return this.fLength;
    }

    public boolean contains(short s2) {
        int n2 = 0;
        while (n2 < this.fLength) {
            if (this.fArray[n2] == s2) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public short item(int n2) throws XSException {
        if (n2 < 0 || n2 >= this.fLength) {
            throw new XSException(2, null);
        }
        return this.fArray[n2];
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof ShortList)) {
            return false;
        }
        ShortList shortList = (ShortList)object;
        if (this.fLength != shortList.getLength()) {
            return false;
        }
        int n2 = 0;
        while (n2 < this.fLength) {
            if (this.fArray[n2] != shortList.item(n2)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public Object get(int n2) {
        if (n2 >= 0 && n2 < this.fLength) {
            return new Short(this.fArray[n2]);
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    public int size() {
        return this.getLength();
    }
}

