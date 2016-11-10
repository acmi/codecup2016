/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.BaseInputStream;
import net.lingala.zip4j.unzip.UnzipEngine;

public class ZipInputStream
extends InputStream {
    private BaseInputStream is;

    public ZipInputStream(BaseInputStream baseInputStream) {
        this.is = baseInputStream;
    }

    public int read() throws IOException {
        int n2 = this.is.read();
        if (n2 != -1) {
            this.is.getUnzipEngine().updateCRC(n2);
        }
        return n2;
    }

    public int read(byte[] arrby) throws IOException {
        return this.read(arrby, 0, arrby.length);
    }

    public int read(byte[] arrby, int n2, int n3) throws IOException {
        int n4 = this.is.read(arrby, n2, n3);
        if (n4 > 0 && this.is.getUnzipEngine() != null) {
            this.is.getUnzipEngine().updateCRC(arrby, n2, n4);
        }
        return n4;
    }

    public void close() throws IOException {
        this.close(false);
    }

    public void close(boolean bl) throws IOException {
        try {
            this.is.close();
            if (!bl && this.is.getUnzipEngine() != null) {
                this.is.getUnzipEngine().checkCRC();
            }
        }
        catch (ZipException zipException) {
            throw new IOException(zipException.getMessage());
        }
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public long skip(long l2) throws IOException {
        return this.is.skip(l2);
    }
}

