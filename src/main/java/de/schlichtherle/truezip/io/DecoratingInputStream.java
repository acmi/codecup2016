/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class DecoratingInputStream
extends InputStream {
    protected InputStream delegate;

    protected DecoratingInputStream(InputStream inputStream) {
        this.delegate = inputStream;
    }

    @Override
    public int read() throws IOException {
        return this.delegate.read();
    }

    @Override
    public final int read(byte[] arrby) throws IOException {
        return this.read(arrby, 0, arrby.length);
    }

    @Override
    public int read(byte[] arrby, int n2, int n3) throws IOException {
        return this.delegate.read(arrby, n2, n3);
    }

    @Override
    public long skip(long l2) throws IOException {
        return this.delegate.skip(l2);
    }

    @Override
    public int available() throws IOException {
        return this.delegate.available();
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override
    public void mark(int n2) {
        this.delegate.mark(n2);
    }

    @Override
    public void reset() throws IOException {
        this.delegate.reset();
    }

    @Override
    public boolean markSupported() {
        return this.delegate.markSupported();
    }

    public String toString() {
        return String.format("%s[delegate=%s]", this.getClass().getName(), this.delegate);
    }
}

