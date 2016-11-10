/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LimitedByteArrayOutputStream
extends ByteArrayOutputStream {
    private final int maxSize;
    private final boolean throwIfExceeded;

    public LimitedByteArrayOutputStream(int n2, boolean bl) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Argument 'maxSize' (" + n2 + " B) is negative.");
        }
        this.maxSize = n2;
        this.throwIfExceeded = bl;
    }

    @Override
    public synchronized void write(int n2) {
        if (this.size() < this.maxSize) {
            super.write(n2);
        } else if (this.throwIfExceeded) {
            throw new IllegalStateException("Buffer size (" + this.maxSize + " B) exceeded.");
        }
    }

    @Override
    public synchronized void write(byte[] arrby, int n2, int n3) {
        if (this.size() + n3 <= this.maxSize) {
            super.write(arrby, n2, n3);
        } else {
            if (this.throwIfExceeded) {
                throw new IllegalStateException("Buffer size (" + this.maxSize + " B) exceeded.");
            }
            super.write(arrby, n2, this.maxSize - this.size());
        }
    }

    @Override
    public synchronized void write(byte[] arrby) throws IOException {
        if (this.size() + arrby.length <= this.maxSize) {
            super.write(arrby);
        } else {
            if (this.throwIfExceeded) {
                throw new IllegalStateException("Buffer size (" + this.maxSize + " B) exceeded.");
            }
            super.write(arrby, 0, this.maxSize - this.size());
        }
    }
}

