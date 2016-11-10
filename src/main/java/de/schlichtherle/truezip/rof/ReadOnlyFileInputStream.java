/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.rof;

import de.schlichtherle.truezip.rof.ReadOnlyFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadOnlyFileInputStream
extends InputStream {
    protected ReadOnlyFile rof;
    private long mark = -1;

    public ReadOnlyFileInputStream(ReadOnlyFile readOnlyFile) {
        this.rof = readOnlyFile;
    }

    @Override
    public int read() throws IOException {
        return this.rof.read();
    }

    @Override
    public int read(byte[] arrby) throws IOException {
        return this.rof.read(arrby);
    }

    @Override
    public int read(byte[] arrby, int n2, int n3) throws IOException {
        return this.rof.read(arrby, n2, n3);
    }

    @Override
    public long skip(long l2) throws IOException {
        if (l2 <= 0) {
            return 0;
        }
        long l3 = this.rof.getFilePointer();
        long l4 = this.rof.length();
        long l5 = l4 - l3;
        if (l2 > l5) {
            l2 = (int)l5;
        }
        this.rof.seek(l3 + l2);
        return l2;
    }

    @Override
    public int available() throws IOException {
        long l2 = this.rof.length() - this.rof.getFilePointer();
        return l2 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l2;
    }

    @Override
    public void close() throws IOException {
        this.rof.close();
    }

    @Override
    public void mark(int n2) {
        try {
            this.mark = this.rof.getFilePointer();
        }
        catch (IOException iOException) {
            Logger.getLogger(ReadOnlyFileInputStream.class.getName()).log(Level.WARNING, iOException.getLocalizedMessage(), iOException);
            this.mark = -2;
        }
    }

    @Override
    public void reset() throws IOException {
        if (this.mark < 0) {
            throw new IOException(this.mark == -1 ? "no mark set" : "mark()/reset() not supported by underlying file");
        }
        this.rof.seek(this.mark);
    }

    @Override
    public boolean markSupported() {
        try {
            this.rof.seek(this.rof.getFilePointer());
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }
}

