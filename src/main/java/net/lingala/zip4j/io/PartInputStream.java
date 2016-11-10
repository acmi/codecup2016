/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.BaseInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.unzip.UnzipEngine;

public class PartInputStream
extends BaseInputStream {
    private RandomAccessFile raf;
    private long bytesRead;
    private long length;
    private UnzipEngine unzipEngine;
    private IDecrypter decrypter;
    private byte[] oneByteBuff = new byte[1];
    private byte[] aesBlockByte = new byte[16];
    private int aesBytesReturned = 0;
    private boolean isAESEncryptedFile = false;
    private int count = -1;

    public PartInputStream(RandomAccessFile randomAccessFile, long l2, long l3, UnzipEngine unzipEngine) {
        this.raf = randomAccessFile;
        this.unzipEngine = unzipEngine;
        this.decrypter = unzipEngine.getDecrypter();
        this.bytesRead = 0;
        this.length = l3;
        this.isAESEncryptedFile = unzipEngine.getFileHeader().isEncrypted() && unzipEngine.getFileHeader().getEncryptionMethod() == 99;
    }

    public int available() {
        long l2 = this.length - this.bytesRead;
        if (l2 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)l2;
    }

    public int read() throws IOException {
        if (this.bytesRead >= this.length) {
            return -1;
        }
        if (this.isAESEncryptedFile) {
            if (this.aesBytesReturned == 0 || this.aesBytesReturned == 16) {
                if (this.read(this.aesBlockByte) == -1) {
                    return -1;
                }
                this.aesBytesReturned = 0;
            }
            return this.aesBlockByte[this.aesBytesReturned++] & 255;
        }
        return this.read(this.oneByteBuff, 0, 1) == -1 ? -1 : this.oneByteBuff[0] & 255;
    }

    public int read(byte[] arrby) throws IOException {
        return this.read(arrby, 0, arrby.length);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int read(byte[] arrby, int n2, int n3) throws IOException {
        if ((long)n3 > this.length - this.bytesRead && (n3 = (int)(this.length - this.bytesRead)) == 0) {
            this.checkAndReadAESMacBytes();
            return -1;
        }
        if (this.unzipEngine.getDecrypter() instanceof AESDecrypter && this.bytesRead + (long)n3 < this.length && n3 % 16 != 0) {
            n3 -= n3 % 16;
        }
        RandomAccessFile randomAccessFile = this.raf;
        synchronized (randomAccessFile) {
            this.count = this.raf.read(arrby, n2, n3);
            if (this.count < n3 && this.unzipEngine.getZipModel().isSplitArchive()) {
                int n4;
                this.raf.close();
                this.raf = this.unzipEngine.startNextSplitFile();
                if (this.count < 0) {
                    this.count = 0;
                }
                if ((n4 = this.raf.read(arrby, this.count, n3 - this.count)) > 0) {
                    this.count += n4;
                }
            }
        }
        if (this.count > 0) {
            if (this.decrypter != null) {
                try {
                    this.decrypter.decryptData(arrby, n2, this.count);
                }
                catch (ZipException zipException) {
                    throw new IOException(zipException.getMessage());
                }
            }
            this.bytesRead += (long)this.count;
        }
        if (this.bytesRead >= this.length) {
            this.checkAndReadAESMacBytes();
        }
        return this.count;
    }

    protected void checkAndReadAESMacBytes() throws IOException {
        if (this.isAESEncryptedFile && this.decrypter != null && this.decrypter instanceof AESDecrypter) {
            if (((AESDecrypter)this.decrypter).getStoredMac() != null) {
                return;
            }
            byte[] arrby = new byte[10];
            int n2 = -1;
            n2 = this.raf.read(arrby);
            if (n2 != 10) {
                if (this.unzipEngine.getZipModel().isSplitArchive()) {
                    this.raf.close();
                    this.raf = this.unzipEngine.startNextSplitFile();
                    int n3 = this.raf.read(arrby, n2, 10 - n2);
                    n2 += n3;
                } else {
                    throw new IOException("Error occured while reading stored AES authentication bytes");
                }
            }
            ((AESDecrypter)this.unzipEngine.getDecrypter()).setStoredMac(arrby);
        }
    }

    public long skip(long l2) throws IOException {
        if (l2 < 0) {
            throw new IllegalArgumentException();
        }
        if (l2 > this.length - this.bytesRead) {
            l2 = this.length - this.bytesRead;
        }
        this.bytesRead += l2;
        return l2;
    }

    public void close() throws IOException {
        this.raf.close();
    }

    public UnzipEngine getUnzipEngine() {
        return this.unzipEngine;
    }
}

