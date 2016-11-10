/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class Zip64EndCentralDirRecord {
    private long signature;
    private long sizeOfZip64EndCentralDirRec;
    private int versionMadeBy;
    private int versionNeededToExtract;
    private int noOfThisDisk;
    private int noOfThisDiskStartOfCentralDir;
    private long totNoOfEntriesInCentralDirOnThisDisk;
    private long totNoOfEntriesInCentralDir;
    private long sizeOfCentralDir;
    private long offsetStartCenDirWRTStartDiskNo;
    private byte[] extensibleDataSector;

    public void setSignature(long l2) {
        this.signature = l2;
    }

    public long getSizeOfZip64EndCentralDirRec() {
        return this.sizeOfZip64EndCentralDirRec;
    }

    public void setSizeOfZip64EndCentralDirRec(long l2) {
        this.sizeOfZip64EndCentralDirRec = l2;
    }

    public void setVersionMadeBy(int n2) {
        this.versionMadeBy = n2;
    }

    public void setVersionNeededToExtract(int n2) {
        this.versionNeededToExtract = n2;
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

    public void setTotNoOfEntriesInCentralDirOnThisDisk(long l2) {
        this.totNoOfEntriesInCentralDirOnThisDisk = l2;
    }

    public long getTotNoOfEntriesInCentralDir() {
        return this.totNoOfEntriesInCentralDir;
    }

    public void setTotNoOfEntriesInCentralDir(long l2) {
        this.totNoOfEntriesInCentralDir = l2;
    }

    public void setSizeOfCentralDir(long l2) {
        this.sizeOfCentralDir = l2;
    }

    public long getOffsetStartCenDirWRTStartDiskNo() {
        return this.offsetStartCenDirWRTStartDiskNo;
    }

    public void setOffsetStartCenDirWRTStartDiskNo(long l2) {
        this.offsetStartCenDirWRTStartDiskNo = l2;
    }

    public void setExtensibleDataSector(byte[] arrby) {
        this.extensibleDataSector = arrby;
    }
}

