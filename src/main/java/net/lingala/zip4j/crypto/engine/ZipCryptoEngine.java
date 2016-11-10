/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto.engine;

public class ZipCryptoEngine {
    private final int[] keys = new int[3];
    private static final int[] CRC_TABLE = new int[256];

    public void initKeys(char[] arrc) {
        this.keys[0] = 305419896;
        this.keys[1] = 591751049;
        this.keys[2] = 878082192;
        for (int i2 = 0; i2 < arrc.length; ++i2) {
            this.updateKeys((byte)(arrc[i2] & 255));
        }
    }

    public void updateKeys(byte by) {
        this.keys[0] = this.crc32(this.keys[0], by);
        int[] arrn = this.keys;
        arrn[1] = arrn[1] + (this.keys[0] & 255);
        this.keys[1] = this.keys[1] * 134775813 + 1;
        this.keys[2] = this.crc32(this.keys[2], (byte)(this.keys[1] >> 24));
    }

    private int crc32(int n2, byte by) {
        return n2 >>> 8 ^ CRC_TABLE[(n2 ^ by) & 255];
    }

    public byte decryptByte() {
        int n2 = this.keys[2] | 2;
        return (byte)(n2 * (n2 ^ 1) >>> 8);
    }

    static {
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = i2;
            for (int i3 = 0; i3 < 8; ++i3) {
                if ((n2 & 1) == 1) {
                    n2 = n2 >>> 1 ^ -306674912;
                    continue;
                }
                n2 >>>= 1;
            }
            ZipCryptoEngine.CRC_TABLE[i2] = n2;
        }
    }
}

