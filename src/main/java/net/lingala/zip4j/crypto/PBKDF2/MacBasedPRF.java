/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto.PBKDF2;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import net.lingala.zip4j.crypto.PBKDF2.PRF;

public class MacBasedPRF
implements PRF {
    protected Mac mac;
    protected int hLen;
    protected String macAlgorithm;

    public MacBasedPRF(String string) {
        this.macAlgorithm = string;
        try {
            this.mac = Mac.getInstance(string);
            this.hLen = this.mac.getMacLength();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    public byte[] doFinal(byte[] arrby) {
        byte[] arrby2 = this.mac.doFinal(arrby);
        return arrby2;
    }

    public byte[] doFinal() {
        byte[] arrby = this.mac.doFinal();
        return arrby;
    }

    public int getHLen() {
        return this.hLen;
    }

    public void init(byte[] arrby) {
        try {
            this.mac.init(new SecretKeySpec(arrby, this.macAlgorithm));
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new RuntimeException(invalidKeyException);
        }
    }

    public void update(byte[] arrby, int n2, int n3) {
        try {
            this.mac.update(arrby, n2, n3);
        }
        catch (IllegalStateException illegalStateException) {
            throw new RuntimeException(illegalStateException);
        }
    }
}

