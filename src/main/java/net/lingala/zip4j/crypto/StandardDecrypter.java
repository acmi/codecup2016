/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class StandardDecrypter
implements IDecrypter {
    private FileHeader fileHeader;
    private byte[] crc = new byte[4];
    private ZipCryptoEngine zipCryptoEngine;

    public StandardDecrypter(FileHeader fileHeader, byte[] arrby) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("one of more of the input parameters were null in StandardDecryptor");
        }
        this.fileHeader = fileHeader;
        this.zipCryptoEngine = new ZipCryptoEngine();
        this.init(arrby);
    }

    public int decryptData(byte[] arrby, int n2, int n3) throws ZipException {
        if (n2 < 0 || n3 < 0) {
            throw new ZipException("one of the input parameters were null in standard decrpyt data");
        }
        try {
            for (int i2 = n2; i2 < n2 + n3; ++i2) {
                int n4 = arrby[i2] & 255;
                n4 = (n4 ^ this.zipCryptoEngine.decryptByte()) & 255;
                this.zipCryptoEngine.updateKeys((byte)n4);
                arrby[i2] = (byte)n4;
            }
            return n3;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    public void init(byte[] arrby) throws ZipException {
        byte[] arrby2 = this.fileHeader.getCrcBuff();
        this.crc[3] = (byte)(arrby2[3] & 255);
        this.crc[2] = (byte)(arrby2[3] >> 8 & 255);
        this.crc[1] = (byte)(arrby2[3] >> 16 & 255);
        this.crc[0] = (byte)(arrby2[3] >> 24 & 255);
        if (this.crc[2] > 0 || this.crc[1] > 0 || this.crc[0] > 0) {
            throw new IllegalStateException("Invalid CRC in File Header");
        }
        if (this.fileHeader.getPassword() == null || this.fileHeader.getPassword().length <= 0) {
            throw new ZipException("Wrong password!", 5);
        }
        this.zipCryptoEngine.initKeys(this.fileHeader.getPassword());
        try {
            byte by = arrby[0];
            for (int i2 = 0; i2 < 12; ++i2) {
                this.zipCryptoEngine.updateKeys((byte)(by ^ this.zipCryptoEngine.decryptByte()));
                if (i2 + 1 == 12) continue;
                by = arrby[i2 + 1];
            }
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }
}

