/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.util;

import java.util.AbstractList;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.datatypes.ByteList;

public class ByteListImpl
extends AbstractList
implements ByteList {
    protected final byte[] data;
    protected String canonical;

    public ByteListImpl(byte[] arrby) {
        this.data = arrby;
    }

    public int getLength() {
        return this.data.length;
    }

    public boolean contains(byte by) {
        int n2 = 0;
        while (n2 < this.data.length) {
            if (this.data[n2] == by) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public byte item(int n2) throws XSException {
        if (n2 < 0 || n2 > this.data.length - 1) {
            throw new XSException(2, null);
        }
        return this.data[n2];
    }

    public Object get(int n2) {
        if (n2 >= 0 && n2 < this.data.length) {
            return new Byte(this.data[n2]);
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    public int size() {
        return this.getLength();
    }
}

