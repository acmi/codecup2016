/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class DigitalSignature {
    private int headerSignature;
    private int sizeOfData;
    private String signatureData;

    public void setHeaderSignature(int n2) {
        this.headerSignature = n2;
    }

    public void setSizeOfData(int n2) {
        this.sizeOfData = n2;
    }

    public void setSignatureData(String string) {
        this.signatureData = string;
    }
}

