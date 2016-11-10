/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class AESExtraDataRecord {
    private long signature = -1;
    private int dataSize = -1;
    private int versionNumber = -1;
    private String vendorID = null;
    private int aesStrength = -1;
    private int compressionMethod = -1;

    public void setSignature(long l2) {
        this.signature = l2;
    }

    public void setDataSize(int n2) {
        this.dataSize = n2;
    }

    public void setVersionNumber(int n2) {
        this.versionNumber = n2;
    }

    public void setVendorID(String string) {
        this.vendorID = string;
    }

    public int getAesStrength() {
        return this.aesStrength;
    }

    public void setAesStrength(int n2) {
        this.aesStrength = n2;
    }

    public int getCompressionMethod() {
        return this.compressionMethod;
    }

    public void setCompressionMethod(int n2) {
        this.compressionMethod = n2;
    }
}

