/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public class XSObjectListImpl
extends AbstractList
implements XSObjectList {
    public static final XSObjectListImpl EMPTY_LIST = new XSObjectListImpl(new XSObject[0], 0);
    private static final ListIterator EMPTY_ITERATOR = new ListIterator(){

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return false;
        }

        public Object previous() {
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return -1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(Object object) {
            throw new UnsupportedOperationException();
        }

        public void add(Object object) {
            throw new UnsupportedOperationException();
        }
    };
    private static final int DEFAULT_SIZE = 4;
    private XSObject[] fArray = null;
    private int fLength = 0;

    public XSObjectListImpl() {
        this.fArray = new XSObject[4];
        this.fLength = 0;
    }

    public XSObjectListImpl(XSObject[] arrxSObject, int n2) {
        this.fArray = arrxSObject;
        this.fLength = n2;
    }

    public int getLength() {
        return this.fLength;
    }

    public XSObject item(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fArray[n2];
    }

    public void clearXSObjectList() {
        int n2 = 0;
        while (n2 < this.fLength) {
            this.fArray[n2] = null;
            ++n2;
        }
        this.fArray = null;
        this.fLength = 0;
    }

    public void addXSObject(XSObject xSObject) {
        if (this.fLength == this.fArray.length) {
            XSObject[] arrxSObject = new XSObject[this.fLength + 4];
            System.arraycopy(this.fArray, 0, arrxSObject, 0, this.fLength);
            this.fArray = arrxSObject;
        }
        this.fArray[this.fLength++] = xSObject;
    }

    public void addXSObject(int n2, XSObject xSObject) {
        this.fArray[n2] = xSObject;
    }

    public boolean contains(Object object) {
        return object == null ? this.containsNull() : this.containsObject(object);
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

    public Iterator iterator() {
        return this.listIterator0(0);
    }

    public ListIterator listIterator() {
        return this.listIterator0(0);
    }

    public ListIterator listIterator(int n2) {
        if (n2 >= 0 && n2 < this.fLength) {
            return this.listIterator0(n2);
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    private ListIterator listIterator0(int n2) {
        return this.fLength == 0 ? EMPTY_ITERATOR : new XSObjectListIterator(this, n2);
    }

    private boolean containsObject(Object object) {
        int n2 = this.fLength - 1;
        while (n2 >= 0) {
            if (object.equals(this.fArray[n2])) {
                return true;
            }
            --n2;
        }
        return false;
    }

    private boolean containsNull() {
        int n2 = this.fLength - 1;
        while (n2 >= 0) {
            if (this.fArray[n2] == null) {
                return true;
            }
            --n2;
        }
        return false;
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

    static int access$000(XSObjectListImpl xSObjectListImpl) {
        return xSObjectListImpl.fLength;
    }

    static XSObject[] access$100(XSObjectListImpl xSObjectListImpl) {
        return xSObjectListImpl.fArray;
    }

    private final class XSObjectListIterator
    implements ListIterator {
        private int index;
        private final XSObjectListImpl this$0;

        public XSObjectListIterator(XSObjectListImpl xSObjectListImpl, int n2) {
            this.this$0 = xSObjectListImpl;
            this.index = n2;
        }

        public boolean hasNext() {
            return this.index < XSObjectListImpl.access$000(this.this$0);
        }

        public Object next() {
            if (this.index < XSObjectListImpl.access$000(this.this$0)) {
                return XSObjectListImpl.access$100(this.this$0)[this.index++];
            }
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return this.index > 0;
        }

        public Object previous() {
            if (this.index > 0) {
                return XSObjectListImpl.access$100(this.this$0)[--this.index];
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.index;
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(Object object) {
            throw new UnsupportedOperationException();
        }

        public void add(Object object) {
            throw new UnsupportedOperationException();
        }
    }

}

