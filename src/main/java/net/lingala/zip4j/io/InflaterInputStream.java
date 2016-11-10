/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import net.lingala.zip4j.io.PartInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.unzip.UnzipEngine;

public class InflaterInputStream
extends PartInputStream {
    private Inflater inflater = new Inflater(true);
    private byte[] buff = new byte[4096];
    private byte[] oneByteBuff = new byte[1];
    private UnzipEngine unzipEngine;
    private long bytesWritten;
    private long uncompressedSize;

    public InflaterInputStream(RandomAccessFile randomAccessFile, long l2, long l3, UnzipEngine unzipEngine) {
        super(randomAccessFile, l2, l3, unzipEngine);
        this.unzipEngine = unzipEngine;
        this.bytesWritten = 0;
        this.uncompressedSize = unzipEngine.getFileHeader().getUncompressedSize();
    }

    public int read() throws IOException {
        return this.read(this.oneByteBuff, 0, 1) == -1 ? -1 : this.oneByteBuff[0] & 255;
    }

    public int read(byte[] arrby) throws IOException {
        if (arrby == null) {
            throw new NullPointerException("input buffer is null");
        }
        return this.read(arrby, 0, arrby.length);
    }

    public int read(byte[] arrby, int n2, int n3) throws IOException {
        if (arrby == null) {
            throw new NullPointerException("input buffer is null");
        }
        if (n2 < 0 || n3 < 0 || n3 > arrby.length - n2) {
            throw new IndexOutOfBoundsException();
        }
        if (n3 == 0) {
            return 0;
        }
        try {
            int n4;
            if (this.bytesWritten >= this.uncompressedSize) {
                this.finishInflating();
                return -1;
            }
            while ((n4 = this.inflater.inflate(arrby, n2, n3)) == 0) {
                if (this.inflater.finished() || this.inflater.needsDictionary()) {
                    this.finishInflating();
                    return -1;
                }
                if (!this.inflater.needsInput()) continue;
                this.fill();
            }
            this.bytesWritten += (long)n4;
            return n4;
        }
        catch (DataFormatException dataFormatException) {
            String string = "Invalid ZLIB data format";
            if (dataFormatException.getMessage() != null) {
                string = dataFormatException.getMessage();
            }
            if (this.unzipEngine != null && this.unzipEngine.getLocalFileHeader().isEncrypted() && this.unzipEngine.getLocalFileHeader().getEncryptionMethod() == 0) {
                string = string + " - Wrong Password?";
            }
            throw new IOException(string);
        }
    }

    private void finishInflating() throws IOException {
        byte[] arrby = new byte[1024];
        while (super.read(arrby, 0, 1024) != -1) {
        }
        this.checkAndReadAESMacBytes();
    }

    private void fill() throws IOException {
        int n2 = super.read(this.buff, 0, this.buff.length);
        if (n2 == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        this.inflater.setInput(this.buff, 0, n2);
    }

    public long skip(long l2) throws IOException {
        int n2;
        int n3;
        if (l2 < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        int n4 = (int)Math.min(l2, Integer.MAX_VALUE);
        byte[] arrby = new byte[512];
        for (n2 = 0; n2 < n4; n2 += n3) {
            n3 = n4 - n2;
            if (n3 > arrby.length) {
                n3 = arrby.length;
            }
            if ((n3 = this.read(arrby, 0, n3)) == -1) break;
        }
        return n2;
    }

    public int available() {
        return this.inflater.finished() ? 0 : 1;
    }

    public void close() throws IOException {
        this.inflater.end();
        super.close();
    }

    public UnzipEngine getUnzipEngine() {
        return super.getUnzipEngine();
    }
}

