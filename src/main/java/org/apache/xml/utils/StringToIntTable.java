/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class StringToIntTable {
    private int m_blocksize;
    private String[] m_map;
    private int[] m_values;
    private int m_firstFree = 0;
    private int m_mapSize;

    public StringToIntTable() {
        this.m_mapSize = this.m_blocksize = 8;
        this.m_map = new String[this.m_blocksize];
        this.m_values = new int[this.m_blocksize];
    }

    public StringToIntTable(int n2) {
        this.m_blocksize = n2;
        this.m_mapSize = n2;
        this.m_map = new String[n2];
        this.m_values = new int[this.m_blocksize];
    }

    public final void put(String string, int n2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            String[] arrstring = new String[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrstring, 0, this.m_firstFree + 1);
            this.m_map = arrstring;
            int[] arrn = new int[this.m_mapSize];
            System.arraycopy(this.m_values, 0, arrn, 0, this.m_firstFree + 1);
            this.m_values = arrn;
        }
        this.m_map[this.m_firstFree] = string;
        this.m_values[this.m_firstFree] = n2;
        ++this.m_firstFree;
    }

    public final int get(String string) {
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            if (!this.m_map[i2].equals(string)) continue;
            return this.m_values[i2];
        }
        return -10000;
    }

    public final String[] keys() {
        String[] arrstring = new String[this.m_firstFree];
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            arrstring[i2] = this.m_map[i2];
        }
        return arrstring;
    }
}

