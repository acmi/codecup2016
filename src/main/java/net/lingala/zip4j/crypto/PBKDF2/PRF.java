/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto.PBKDF2;

interface PRF {
    public void init(byte[] var1);

    public byte[] doFinal(byte[] var1);

    public int getHLen();
}

