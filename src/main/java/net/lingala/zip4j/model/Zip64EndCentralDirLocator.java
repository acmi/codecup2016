/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class Zip64EndCentralDirLocator {
    private long signature;
    private int noOfDiskStartOfZip64EndOfCentralDirRec;
    private long offsetZip64EndOfCentralDirRec;
    private int totNumberOfDiscs;

    public void setSignature(long l2) {
        this.signature = l2;
    }

    public void setNoOfDiskStartOfZip64EndOfCentralDirRec(int n2) {
        this.noOfDiskStartOfZip64EndOfCentralDirRec = n2;
    }

    public long getOffsetZip64EndOfCentralDirRec() {
        return this.offsetZip64EndOfCentralDirRec;
    }

    public void setOffsetZip64EndOfCentralDirRec(long l2) {
        this.offsetZip64EndOfCentralDirRec = l2;
    }

    public void setTotNumberOfDiscs(int n2) {
        this.totNumberOfDiscs = n2;
    }
}

