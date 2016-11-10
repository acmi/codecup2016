/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.rof;

import java.io.Closeable;
import java.io.IOException;

public interface ReadOnlyFile
extends Closeable {
    public long length() throws IOException;

    public long getFilePointer() throws IOException;

    public void seek(long var1) throws IOException;

    public int read() throws IOException;

    public int read(byte[] var1) throws IOException;

    public int read(byte[] var1, int var2, int var3) throws IOException;

    @Override
    public void close() throws IOException;
}

