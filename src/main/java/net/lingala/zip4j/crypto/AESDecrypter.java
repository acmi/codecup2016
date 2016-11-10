/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto;

import java.util.Arrays;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.Raw;

public class AESDecrypter
implements IDecrypter {
    private LocalFileHeader localFileHeader;
    private AESEngine aesEngine;
    private MacBasedPRF mac;
    private final int PASSWORD_VERIFIER_LENGTH = 2;
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private int SALT_LENGTH;
    private byte[] aesKey;
    private byte[] macKey;
    private byte[] derivedPasswordVerifier;
    private byte[] storedMac;
    private int nonce = 1;
    private byte[] iv;
    private byte[] counterBlock;
    private int loopCount = 0;

    public AESDecrypter(LocalFileHeader localFileHeader, byte[] arrby, byte[] arrby2) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("one of the input parameters is null in AESDecryptor Constructor");
        }
        this.localFileHeader = localFileHeader;
        this.storedMac = null;
        this.iv = new byte[16];
        this.counterBlock = new byte[16];
        this.init(arrby, arrby2);
    }

    private void init(byte[] arrby, byte[] arrby2) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("invalid file header in init method of AESDecryptor");
        }
        AESExtraDataRecord aESExtraDataRecord = this.localFileHeader.getAesExtraDataRecord();
        if (aESExtraDataRecord == null) {
            throw new ZipException("invalid aes extra data record - in init method of AESDecryptor");
        }
        switch (aESExtraDataRecord.getAesStrength()) {
            case 1: {
                this.KEY_LENGTH = 16;
                this.MAC_LENGTH = 16;
                this.SALT_LENGTH = 8;
                break;
            }
            case 2: {
                this.KEY_LENGTH = 24;
                this.MAC_LENGTH = 24;
                this.SALT_LENGTH = 12;
                break;
            }
            case 3: {
                this.KEY_LENGTH = 32;
                this.MAC_LENGTH = 32;
                this.SALT_LENGTH = 16;
                break;
            }
            default: {
                throw new ZipException("invalid aes key strength for file: " + this.localFileHeader.getFileName());
            }
        }
        if (this.localFileHeader.getPassword() == null || this.localFileHeader.getPassword().length <= 0) {
            throw new ZipException("empty or null password provided for AES Decryptor");
        }
        byte[] arrby3 = this.deriveKey(arrby, this.localFileHeader.getPassword());
        if (arrby3 == null || arrby3.length != this.KEY_LENGTH + this.MAC_LENGTH + 2) {
            throw new ZipException("invalid derived key");
        }
        this.aesKey = new byte[this.KEY_LENGTH];
        this.macKey = new byte[this.MAC_LENGTH];
        this.derivedPasswordVerifier = new byte[2];
        System.arraycopy(arrby3, 0, this.aesKey, 0, this.KEY_LENGTH);
        System.arraycopy(arrby3, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
        System.arraycopy(arrby3, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
        if (this.derivedPasswordVerifier == null) {
            throw new ZipException("invalid derived password verifier for AES");
        }
        if (!Arrays.equals(arrby2, this.derivedPasswordVerifier)) {
            throw new ZipException("Wrong Password for file: " + this.localFileHeader.getFileName(), 5);
        }
        this.aesEngine = new AESEngine(this.aesKey);
        this.mac = new MacBasedPRF("HmacSHA1");
        this.mac.init(this.macKey);
    }

    public int decryptData(byte[] arrby, int n2, int n3) throws ZipException {
        if (this.aesEngine == null) {
            throw new ZipException("AES not initialized properly");
        }
        try {
            for (int i2 = n2; i2 < n2 + n3; i2 += 16) {
                this.loopCount = i2 + 16 <= n2 + n3 ? 16 : n2 + n3 - i2;
                this.mac.update(arrby, i2, this.loopCount);
                Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
                this.aesEngine.processBlock(this.iv, this.counterBlock);
                for (int i3 = 0; i3 < this.loopCount; ++i3) {
                    arrby[i2 + i3] = (byte)(arrby[i2 + i3] ^ this.counterBlock[i3]);
                }
                ++this.nonce;
            }
            return n3;
        }
        catch (ZipException zipException) {
            throw zipException;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    private byte[] deriveKey(byte[] arrby, char[] arrc) throws ZipException {
        try {
            PBKDF2Parameters pBKDF2Parameters = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", arrby, 1000);
            PBKDF2Engine pBKDF2Engine = new PBKDF2Engine(pBKDF2Parameters);
            byte[] arrby2 = pBKDF2Engine.deriveKey(arrc, this.KEY_LENGTH + this.MAC_LENGTH + 2);
            return arrby2;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    public int getPasswordVerifierLength() {
        return 2;
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }

    public byte[] getCalculatedAuthenticationBytes() {
        return this.mac.doFinal();
    }

    public void setStoredMac(byte[] arrby) {
        this.storedMac = arrby;
    }

    public byte[] getStoredMac() {
        return this.storedMac;
    }
}

