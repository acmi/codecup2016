/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class EndCentralDirRecord {
    private long signature;
    private int noOfThisDisk;
    private int noOfThisDiskStartOfCentralDir;
    private int totNoOfEntriesInCentralDirOnThisDisk;
    private int totNoOfEntriesInCentralDir;
    private int sizeOfCentralDir;
    private long offsetOfStartOfCentralDir;
    private int commentLength;
    private String comment;
    private byte[] commentBytes;

    public void setSignature(long l2) {
        this.signature = l2;
    }

    public int getNoOfThisDisk() {
        return this.noOfThisDisk;
    }

    public void setNoOfThisDisk(int n2) {
        this.noOfThisDisk = n2;
    }

    public void setNoOfThisDiskStartOfCentralDir(int n2) {
        this.noOfThisDiskStartOfCentralDir = n2;
    }

    public void setTotNoOfEntriesInCentralDirOnThisDisk(int n2) {
        this.totNoOfEntriesInCentralDirOnThisDisk = n2;
    }

    public int getTotNoOfEntriesInCentralDir() {
        return this.totNoOfEntriesInCentralDir;
    }

    public void setTotNoOfEntriesInCentralDir(int n2) {
        this.totNoOfEntriesInCentralDir = n2;
    }

    public void setSizeOfCentralDir(int n2) {
        this.sizeOfCentralDir = n2;
    }

    public long getOffsetOfStartOfCentralDir() {
        return this.offsetOfStartOfCentralDir;
    }

    public void setOffsetOfStartOfCentralDir(long l2) {
        this.offsetOfStartOfCentralDir = l2;
    }

    public void setCommentLength(int n2) {
        this.commentLength = n2;
    }

    public void setComment(String string) {
        this.comment = string;
    }

    public void setCommentBytes(byte[] arrby) {
        this.commentBytes = arrby;
    }
}

