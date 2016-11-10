/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class SuballocatedIntVector {
    protected int m_blocksize;
    protected int m_SHIFT = 0;
    protected int m_MASK;
    protected int m_numblocks = 32;
    protected int[][] m_map;
    protected int m_firstFree = 0;
    protected int[] m_map0;
    protected int[] m_buildCache;
    protected int m_buildCacheStartIndex;

    public SuballocatedIntVector() {
        this(2048);
    }

    public SuballocatedIntVector(int n2, int n3) {
        while (0 != (n2 >>>= 1)) {
            ++this.m_SHIFT;
        }
        this.m_blocksize = 1 << this.m_SHIFT;
        this.m_MASK = this.m_blocksize - 1;
        this.m_numblocks = n3;
        this.m_map0 = new int[this.m_blocksize];
        this.m_map = new int[n3][];
        this.m_map[0] = this.m_map0;
        this.m_buildCache = this.m_map0;
        this.m_buildCacheStartIndex = 0;
    }

    public SuballocatedIntVector(int n2) {
        this(n2, 32);
    }

    public int size() {
        return this.m_firstFree;
    }

    public void setSize(int n2) {
        if (this.m_firstFree > n2) {
            this.m_firstFree = n2;
        }
    }

    public void addElement(int n2) {
        int n3 = this.m_firstFree - this.m_buildCacheStartIndex;
        if (n3 >= 0 && n3 < this.m_blocksize) {
            this.m_buildCache[n3] = n2;
            ++this.m_firstFree;
        } else {
            int[] arrn;
            int n4 = this.m_firstFree >>> this.m_SHIFT;
            int n5 = this.m_firstFree & this.m_MASK;
            if (n4 >= this.m_map.length) {
                int n6 = n4 + this.m_numblocks;
                int[][] arrarrn = new int[n6][];
                System.arraycopy(this.m_map, 0, arrarrn, 0, this.m_map.length);
                this.m_map = arrarrn;
            }
            if (null == (arrn = this.m_map[n4])) {
                this.m_map[n4] = new int[this.m_blocksize];
                arrn = this.m_map[n4];
            }
            arrn[n5] = n2;
            this.m_buildCache = arrn;
            this.m_buildCacheStartIndex = this.m_firstFree - n5;
            ++this.m_firstFree;
        }
    }

    public void setElementAt(int n2, int n3) {
        if (n3 < this.m_blocksize) {
            this.m_map0[n3] = n2;
        } else {
            int[] arrn;
            int n4 = n3 >>> this.m_SHIFT;
            int n5 = n3 & this.m_MASK;
            if (n4 >= this.m_map.length) {
                int n6 = n4 + this.m_numblocks;
                int[][] arrarrn = new int[n6][];
                System.arraycopy(this.m_map, 0, arrarrn, 0, this.m_map.length);
                this.m_map = arrarrn;
            }
            if (null == (arrn = this.m_map[n4])) {
                this.m_map[n4] = new int[this.m_blocksize];
                arrn = this.m_map[n4];
            }
            arrn[n5] = n2;
        }
        if (n3 >= this.m_firstFree) {
            this.m_firstFree = n3 + 1;
        }
    }

    public int elementAt(int n2) {
        if (n2 < this.m_blocksize) {
            return this.m_map0[n2];
        }
        return this.m_map[n2 >>> this.m_SHIFT][n2 & this.m_MASK];
    }

    public int indexOf(int n2, int n3) {
        int[] arrn;
        int n4;
        if (n3 >= this.m_firstFree) {
            return -1;
        }
        int n5 = n3 & this.m_MASK;
        int n6 = this.m_firstFree >>> this.m_SHIFT;
        for (int i2 = n3 >>> this.m_SHIFT; i2 < n6; ++i2) {
            arrn = this.m_map[i2];
            if (arrn != null) {
                for (n4 = n5; n4 < this.m_blocksize; ++n4) {
                    if (arrn[n4] != n2) continue;
                    return n4 + i2 * this.m_blocksize;
                }
            }
            n5 = 0;
        }
        n4 = this.m_firstFree & this.m_MASK;
        arrn = this.m_map[n6];
        for (int i3 = n5; i3 < n4; ++i3) {
            if (arrn[i3] != n2) continue;
            return i3 + n6 * this.m_blocksize;
        }
        return -1;
    }

    public int indexOf(int n2) {
        return this.indexOf(n2, 0);
    }

    public final int[] getMap0() {
        return this.m_map0;
    }

    public final int[][] getMap() {
        return this.m_map;
    }
}

