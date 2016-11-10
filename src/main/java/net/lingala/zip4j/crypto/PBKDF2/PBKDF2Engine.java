/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto.PBKDF2;

import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.PBKDF2.PRF;
import net.lingala.zip4j.util.Raw;

public class PBKDF2Engine {
    protected PBKDF2Parameters parameters;
    protected PRF prf;

    public PBKDF2Engine() {
        this.parameters = null;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters pBKDF2Parameters) {
        this.parameters = pBKDF2Parameters;
        this.prf = null;
    }

    public byte[] deriveKey(char[] arrc, int n2) {
        byte[] arrby = null;
        byte[] arrby2 = null;
        if (arrc == null) {
            throw new NullPointerException();
        }
        arrby2 = Raw.convertCharArrayToByteArray(arrc);
        this.assertPRF(arrby2);
        if (n2 == 0) {
            n2 = this.prf.getHLen();
        }
        arrby = this.PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), n2);
        return arrby;
    }

    protected void assertPRF(byte[] arrby) {
        if (this.prf == null) {
            this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
        }
        this.prf.init(arrby);
    }

    protected byte[] PBKDF2(PRF pRF, byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            arrby = new byte[]{};
        }
        int n4 = pRF.getHLen();
        int n5 = this.ceil(n3, n4);
        int n6 = n3 - (n5 - 1) * n4;
        byte[] arrby2 = new byte[n5 * n4];
        int n7 = 0;
        for (int i2 = 1; i2 <= n5; ++i2) {
            this._F(arrby2, n7, pRF, arrby, n2, i2);
            n7 += n4;
        }
        if (n6 < n4) {
            byte[] arrby3 = new byte[n3];
            System.arraycopy(arrby2, 0, arrby3, 0, n3);
            return arrby3;
        }
        return arrby2;
    }

    protected int ceil(int n2, int n3) {
        int n4 = 0;
        if (n2 % n3 > 0) {
            n4 = 1;
        }
        return n2 / n3 + n4;
    }

    protected void _F(byte[] arrby, int n2, PRF pRF, byte[] arrby2, int n3, int n4) {
        int n5 = pRF.getHLen();
        byte[] arrby3 = new byte[n5];
        byte[] arrby4 = new byte[arrby2.length + 4];
        System.arraycopy(arrby2, 0, arrby4, 0, arrby2.length);
        this.INT(arrby4, arrby2.length, n4);
        for (int i2 = 0; i2 < n3; ++i2) {
            arrby4 = pRF.doFinal(arrby4);
            this.xor(arrby3, arrby4);
        }
        System.arraycopy(arrby3, 0, arrby, n2, n5);
    }

    protected void xor(byte[] arrby, byte[] arrby2) {
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            byte[] arrby3 = arrby;
            int n2 = i2;
            arrby3[n2] = (byte)(arrby3[n2] ^ arrby2[i2]);
        }
    }

    protected void INT(byte[] arrby, int n2, int n3) {
        arrby[n2 + 0] = (byte)(n3 / 16777216);
        arrby[n2 + 1] = (byte)(n3 / 65536);
        arrby[n2 + 2] = (byte)(n3 / 256);
        arrby[n2 + 3] = (byte)n3;
    }
}

