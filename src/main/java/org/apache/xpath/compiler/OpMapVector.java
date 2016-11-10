/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

public class OpMapVector {
    protected int m_blocksize;
    protected int[] m_map;
    protected int m_lengthPos = 0;
    protected int m_mapSize;

    public OpMapVector(int n2, int n3, int n4) {
        this.m_blocksize = n3;
        this.m_mapSize = n2;
        this.m_lengthPos = n4;
        this.m_map = new int[n2];
    }

    public final int elementAt(int n2) {
        return this.m_map[n2];
    }

    public final void setElementAt(int n2, int n3) {
        if (n3 >= this.m_mapSize) {
            int n4 = this.m_mapSize;
            this.m_mapSize += this.m_blocksize;
            int[] arrn = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrn, 0, n4);
            this.m_map = arrn;
        }
        this.m_map[n3] = n2;
    }

    public final void setToSize(int n2) {
        int[] arrn = new int[n2];
        System.arraycopy(this.m_map, 0, arrn, 0, this.m_map[this.m_lengthPos]);
        this.m_mapSize = n2;
        this.m_map = arrn;
    }
}

