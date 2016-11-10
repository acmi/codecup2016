/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class ExtraDataRecord {
    private long header;
    private int sizeOfData;
    private byte[] data;

    public long getHeader() {
        return this.header;
    }

    public void setHeader(long l2) {
        this.header = l2;
    }

    public int getSizeOfData() {
        return this.sizeOfData;
    }

    public void setSizeOfData(int n2) {
        this.sizeOfData = n2;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] arrby) {
        this.data = arrby;
    }
}

