/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class IntVector
implements Cloneable {
    protected int m_blocksize;
    protected int[] m_map;
    protected int m_firstFree = 0;
    protected int m_mapSize;

    public IntVector() {
        this.m_mapSize = this.m_blocksize = 32;
        this.m_map = new int[this.m_blocksize];
    }

    public IntVector(int n2) {
        this.m_blocksize = n2;
        this.m_mapSize = n2;
        this.m_map = new int[n2];
    }

    public IntVector(IntVector intVector) {
        this.m_map = new int[intVector.m_mapSize];
        this.m_mapSize = intVector.m_mapSize;
        this.m_firstFree = intVector.m_firstFree;
        this.m_blocksize = intVector.m_blocksize;
        System.arraycopy(intVector.m_map, 0, this.m_map, 0, this.m_firstFree);
    }

    public final int size() {
        return this.m_firstFree;
    }

    public final void addElement(int n2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] arrn = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrn, 0, this.m_firstFree + 1);
            this.m_map = arrn;
        }
        this.m_map[this.m_firstFree] = n2;
        ++this.m_firstFree;
    }

    public final void removeAllElements() {
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            this.m_map[i2] = Integer.MIN_VALUE;
        }
        this.m_firstFree = 0;
    }

    public final void setElementAt(int n2, int n3) {
        this.m_map[n3] = n2;
    }

    public final int elementAt(int n2) {
        return this.m_map[n2];
    }

    public Object clone() throws CloneNotSupportedException {
        return new IntVector(this);
    }
}

