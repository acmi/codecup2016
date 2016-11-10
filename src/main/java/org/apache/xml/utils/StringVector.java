/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.Serializable;

public class StringVector
implements Serializable {
    protected int m_blocksize;
    protected String[] m_map;
    protected int m_firstFree = 0;
    protected int m_mapSize;

    public StringVector() {
        this.m_mapSize = this.m_blocksize = 8;
        this.m_map = new String[this.m_blocksize];
    }

    public StringVector(int n2) {
        this.m_blocksize = n2;
        this.m_mapSize = n2;
        this.m_map = new String[n2];
    }

    public final int size() {
        return this.m_firstFree;
    }

    public final void addElement(String string) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            String[] arrstring = new String[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrstring, 0, this.m_firstFree + 1);
            this.m_map = arrstring;
        }
        this.m_map[this.m_firstFree] = string;
        ++this.m_firstFree;
    }

    public final String elementAt(int n2) {
        return this.m_map[n2];
    }

    public final boolean contains(String string) {
        if (null == string) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            if (!this.m_map[i2].equals(string)) continue;
            return true;
        }
        return false;
    }
}

