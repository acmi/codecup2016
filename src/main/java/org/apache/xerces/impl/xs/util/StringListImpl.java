/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Vector;
import org.apache.xerces.xs.StringList;

public final class StringListImpl
extends AbstractList
implements StringList {
    public static final StringListImpl EMPTY_LIST = new StringListImpl(new String[0], 0);
    private final String[] fArray;
    private final int fLength;
    private final Vector fVector;

    public StringListImpl(Vector vector) {
        this.fVector = vector;
        this.fLength = vector == null ? 0 : vector.size();
        this.fArray = null;
    }

    public StringListImpl(String[] arrstring, int n2) {
        this.fArray = arrstring;
        this.fLength = n2;
        this.fVector = null;
    }

    public int getLength() {
        return this.fLength;
    }

    public boolean contains(String string) {
        if (this.fVector != null) {
            return this.fVector.contains(string);
        }
        if (string == null) {
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
                if (string.equals(this.fArray[n3])) {
                    return true;
                }
                ++n3;
            }
        }
        return false;
    }

    public String item(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        if (this.fVector != null) {
            return (String)this.fVector.elementAt(n2);
        }
        return this.fArray[n2];
    }

    public Object get(int n2) {
        if (n2 >= 0 && n2 < this.fLength) {
            if (this.fVector != null) {
                return this.fVector.elementAt(n2);
            }
            return this.fArray[n2];
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    public int size() {
        return this.getLength();
    }

    public Object[] toArray() {
        if (this.fVector != null) {
            return this.fVector.toArray();
        }
        Object[] arrobject = new Object[this.fLength];
        this.toArray0(arrobject);
        return arrobject;
    }

    public Object[] toArray(Object[] arrobject) {
        if (this.fVector != null) {
            return this.fVector.toArray(arrobject);
        }
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

