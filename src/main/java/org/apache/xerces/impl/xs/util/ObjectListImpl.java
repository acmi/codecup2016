/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import org.apache.xerces.xs.datatypes.ObjectList;

public final class ObjectListImpl
extends AbstractList
implements ObjectList {
    public static final ObjectListImpl EMPTY_LIST = new ObjectListImpl(new Object[0], 0);
    private final Object[] fArray;
    private final int fLength;

    public ObjectListImpl(Object[] arrobject, int n2) {
        this.fArray = arrobject;
        this.fLength = n2;
    }

    public int getLength() {
        return this.fLength;
    }

    public boolean contains(Object object) {
        if (object == null) {
            int n2 = 0;
            while (n2 < this.fLength) {
                if (this.fArray[n2] == null) {
                    return true;
                }
                ++n2;
            }
        } else {
            int n3 = 0;
            while (n3 < this.fLength) {
                if (object.equals(this.fArray[n3])) {
                    return true;
                }
                ++n3;
            }
        }
        return false;
    }

    public Object item(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fArray[n2];
    }

    public Object get(int n2) {
        if (n2 >= 0 && n2 < this.fLength) {
            return this.fArray[n2];
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    public int size() {
        return this.getLength();
    }

    public Object[] toArray() {
        Object[] arrobject = new Object[this.fLength];
        this.toArray0(arrobject);
        return arrobject;
    }

    public Object[] toArray(Object[] arrobject) {
        if (arrobject.length < this.fLength) {
            Class class_ = arrobject.getClass();
            Class class_2 = class_.getComponentType();
            arrobject = (Object[])Array.newInstance(class_2, this.fLength);
        }
        this.toArray0(arrobject);
        if (arrobject.length > this.fLength) {
            arrobject[this.fLength] = null;
        }
        return arrobject;
    }

    private void toArray0(Object[] arrobject) {
        if (this.fLength > 0) {
            System.arraycopy(this.fArray, 0, arrobject, 0, this.fLength);
        }
    }
}

