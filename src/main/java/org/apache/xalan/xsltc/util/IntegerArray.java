/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.util;

import java.io.PrintStream;

public final class IntegerArray
implements Cloneable {
    private static final int InitialSize = 32;
    private int[] _array;
    private int _size;
    private int _free = 0;

    public IntegerArray() {
        this(32);
    }

    public IntegerArray(int n2) {
        this._size = n2;
        this._array = new int[this._size];
    }

    public IntegerArray(int[] arrn) {
        this(arrn.length);
        this._free = this._size;
        System.arraycopy(arrn, 0, this._array, 0, this._free);
    }

    public void clear() {
        this._free = 0;
    }

    public Object clone() {
        IntegerArray integerArray = new IntegerArray(this._free > 0 ? this._free : 1);
        System.arraycopy(this._array, 0, integerArray._array, 0, this._free);
        integerArray._free = this._free;
        return integerArray;
    }

    public int[] toIntArray() {
        int[] arrn = new int[this.cardinality()];
        System.arraycopy(this._array, 0, arrn, 0, this.cardinality());
        return arrn;
    }

    public final int at(int n2) {
        return this._array[n2];
    }

    public final void set(int n2, int n3) {
        this._array[n2] = n3;
    }

    public int indexOf(int n2) {
        for (int i2 = 0; i2 < this._free; ++i2) {
            if (n2 != this._array[i2]) continue;
            return i2;
        }
        return -1;
    }

    public final void add(int n2) {
        if (this._free == this._size) {
            this.growArray(this._size * 2);
        }
        this._array[this._free++] = n2;
    }

    public void addNew(int n2) {
        for (int i2 = 0; i2 < this._free; ++i2) {
            if (this._array[i2] != n2) continue;
            return;
        }
        this.add(n2);
    }

    public void reverse() {
        int n2 = 0;
        int n3 = this._free - 1;
        while (n2 < n3) {
            int n4 = this._array[n2];
            this._array[n2++] = this._array[n3];
            this._array[n3--] = n4;
        }
    }

    public void merge(IntegerArray integerArray) {
        int n2 = this._free + integerArray._free;
        int[] arrn = new int[n2];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n3 < this._free && n4 < integerArray._free) {
            int n6 = this._array[n3];
            int n7 = integerArray._array[n4];
            if (n6 < n7) {
                arrn[n5] = n6;
                ++n3;
            } else if (n6 > n7) {
                arrn[n5] = n7;
                ++n4;
            } else {
                arrn[n5] = n6;
                ++n3;
                ++n4;
            }
            ++n5;
        }
        if (n3 >= this._free) {
            while (n4 < integerArray._free) {
                arrn[n5++] = integerArray._array[n4++];
            }
        } else {
            while (n3 < this._free) {
                arrn[n5++] = this._array[n3++];
            }
        }
        this._array = arrn;
        this._free = this._size = n2;
    }

    public void sort() {
        IntegerArray.quicksort(this._array, 0, this._free - 1);
    }

    private static void quicksort(int[] arrn, int n2, int n3) {
        if (n2 < n3) {
            int n4 = IntegerArray.partition(arrn, n2, n3);
            IntegerArray.quicksort(arrn, n2, n4);
            IntegerArray.quicksort(arrn, n4 + 1, n3);
        }
    }

    private static int partition(int[] arrn, int n2, int n3) {
        int n4 = arrn[n2 + n3 >>> 1];
        int n5 = n2 - 1;
        int n6 = n3 + 1;
        do {
            if (n4 < arrn[--n6]) {
                continue;
            }
            while (n4 > arrn[++n5]) {
            }
            if (n5 >= n6) break;
            int n7 = arrn[n5];
            arrn[n5] = arrn[n6];
            arrn[n6] = n7;
        } while (true);
        return n6;
    }

    private void growArray(int n2) {
        this._size = n2;
        int[] arrn = new int[this._size];
        System.arraycopy(this._array, 0, arrn, 0, this._free);
        this._array = arrn;
    }

    public int popLast() {
        return this._array[--this._free];
    }

    public int last() {
        return this._array[this._free - 1];
    }

    public void setLast(int n2) {
        this._array[this._free - 1] = n2;
    }

    public void pop() {
        --this._free;
    }

    public void pop(int n2) {
        this._free -= n2;
    }

    public final int cardinality() {
        return this._free;
    }

    public void print(PrintStream printStream) {
        if (this._free > 0) {
            for (int i2 = 0; i2 < this._free - 1; ++i2) {
                printStream.print(this._array[i2]);
                printStream.print(' ');
            }
            printStream.println(this._array[this._free - 1]);
        } else {
            printStream.println("IntegerArray: empty");
        }
    }
}

