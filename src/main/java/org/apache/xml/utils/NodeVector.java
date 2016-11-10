/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.Serializable;

public class NodeVector
implements Serializable,
Cloneable {
    private int m_blocksize = 32;
    private int[] m_map;
    protected int m_firstFree = 0;
    private int m_mapSize = 0;

    public Object clone() throws CloneNotSupportedException {
        NodeVector nodeVector = (NodeVector)super.clone();
        if (null != this.m_map && this.m_map == nodeVector.m_map) {
            nodeVector.m_map = new int[this.m_map.length];
            System.arraycopy(this.m_map, 0, nodeVector.m_map, 0, this.m_map.length);
        }
        return nodeVector;
    }

    public int size() {
        return this.m_firstFree;
    }

    public void addElement(int n2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new int[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                int[] arrn = new int[this.m_mapSize];
                System.arraycopy(this.m_map, 0, arrn, 0, this.m_firstFree + 1);
                this.m_map = arrn;
            }
        }
        this.m_map[this.m_firstFree] = n2;
        ++this.m_firstFree;
    }

    public final void push(int n2) {
        int n3 = this.m_firstFree;
        if (n3 + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new int[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                int[] arrn = new int[this.m_mapSize];
                System.arraycopy(this.m_map, 0, arrn, 0, n3 + 1);
                this.m_map = arrn;
            }
        }
        this.m_map[n3] = n2;
        this.m_firstFree = ++n3;
    }

    public final int pop() {
        --this.m_firstFree;
        int n2 = this.m_map[this.m_firstFree];
        this.m_map[this.m_firstFree] = -1;
        return n2;
    }

    public final int peepTail() {
        return this.m_map[this.m_firstFree - 1];
    }

    public void insertElementAt(int n2, int n3) {
        if (null == this.m_map) {
            this.m_map = new int[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        } else if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] arrn = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrn, 0, this.m_firstFree + 1);
            this.m_map = arrn;
        }
        if (n3 <= this.m_firstFree - 1) {
            System.arraycopy(this.m_map, n3, this.m_map, n3 + 1, this.m_firstFree - n3);
        }
        this.m_map[n3] = n2;
        ++this.m_firstFree;
    }

    public void removeAllElements() {
        if (null == this.m_map) {
            return;
        }
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            this.m_map[i2] = -1;
        }
        this.m_firstFree = 0;
    }

    public void RemoveAllNoClear() {
        if (null == this.m_map) {
            return;
        }
        this.m_firstFree = 0;
    }

    public void setElementAt(int n2, int n3) {
        if (null == this.m_map) {
            this.m_map = new int[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        }
        if (n3 == -1) {
            this.addElement(n2);
        }
        this.m_map[n3] = n2;
    }

    public int elementAt(int n2) {
        if (null == this.m_map) {
            return -1;
        }
        return this.m_map[n2];
    }

    public boolean contains(int n2) {
        if (null == this.m_map) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            int n3 = this.m_map[i2];
            if (n3 != n2) continue;
            return true;
        }
        return false;
    }
}

