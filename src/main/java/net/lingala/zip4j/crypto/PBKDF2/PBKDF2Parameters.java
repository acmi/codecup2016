/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto.PBKDF2;

public class PBKDF2Parameters {
    protected byte[] salt;
    protected int iterationCount;
    protected String hashAlgorithm;
    protected String hashCharset;
    protected byte[] derivedKey;

    public PBKDF2Parameters() {
        this.hashAlgorithm = null;
        this.hashCharset = "UTF-8";
        this.salt = null;
        this.iterationCount = 1000;
        this.derivedKey = null;
    }

    public PBKDF2Parameters(String string, String string2, byte[] arrby, int n2) {
        this.hashAlgorithm = string;
        this.hashCharset = string2;
        this.salt = arrby;
        this.iterationCount = n2;
        this.derivedKey = null;
    }

    public int getIterationCount() {
        return this.iterationCount;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public String getHashAlgorithm() {
        return this.hashAlgorithm;
    }
}

