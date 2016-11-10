/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class ObjectVector
implements Cloneable {
    protected int m_blocksize;
    protected Object[] m_map;
    protected int m_firstFree = 0;
    protected int m_mapSize;

    public ObjectVector() {
        this.m_mapSize = this.m_blocksize = 32;
        this.m_map = new Object[this.m_blocksize];
    }

    public ObjectVector(int n2) {
        this.m_blocksize = n2;
        this.m_mapSize = n2;
        this.m_map = new Object[n2];
    }

    public ObjectVector(int n2, int n3) {
        this.m_blocksize = n3;
        this.m_mapSize = n2;
        this.m_map = new Object[n2];
    }

    public ObjectVector(ObjectVector objectVector) {
        this.m_map = new Object[objectVector.m_mapSize];
        this.m_mapSize = objectVector.m_mapSize;
        this.m_firstFree = objectVector.m_firstFree;
        this.m_blocksize = objectVector.m_blocksize;
        System.arraycopy(objectVector.m_map, 0, this.m_map, 0, this.m_firstFree);
    }

    public final int size() {
        return this.m_firstFree;
    }

    public final void addElement(Object object) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Object[] arrobject = new Object[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrobject, 0, this.m_firstFree + 1);
            this.m_map = arrobject;
        }
        this.m_map[this.m_firstFree] = object;
        ++this.m_firstFree;
    }

    public final void removeAllElements() {
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            this.m_map[i2] = null;
        }
        this.m_firstFree = 0;
    }

    public final void setElementAt(Object object, int n2) {
        this.m_map[n2] = object;
    }

    public final Object elementAt(int n2) {
        return this.m_map[n2];
    }

    public final void setToSize(int n2) {
        Object[] arrobject = new Object[n2];
        System.arraycopy(this.m_map, 0, arrobject, 0, this.m_firstFree);
        this.m_mapSize = n2;
        this.m_map = arrobject;
    }

    public Object clone() throws CloneNotSupportedException {
        return new ObjectVector(this);
    }
}

