/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class Zip64ExtendedInfo {
    private long compressedSize = -1;
    private long unCompressedSize = -1;
    private long offsetLocalHeader = -1;
    private int diskNumberStart = -1;

    public long getCompressedSize() {
        return this.compressedSize;
    }

    public void setCompressedSize(long l2) {
        this.compressedSize = l2;
    }

    public long getUnCompressedSize() {
        return this.unCompressedSize;
    }

    public void setUnCompressedSize(long l2) {
        this.unCompressedSize = l2;
    }

    public long getOffsetLocalHeader() {
        return this.offsetLocalHeader;
    }

    public void setOffsetLocalHeader(long l2) {
        this.offsetLocalHeader = l2;
    }

    public int getDiskNumberStart() {
        return this.diskNumberStart;
    }

    public void setDiskNumberStart(int n2) {
        this.diskNumberStart = n2;
    }
}

