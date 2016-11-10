/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.io;

import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class ByteSource {
    protected ByteSource() {
    }

    public abstract InputStream openStream() throws IOException;

    public Optional<Long> sizeIfKnown() {
        return Optional.absent();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long size() throws IOException {
        Optional<Long> optional = this.sizeIfKnown();
        if (optional.isPresent()) {
            return optional.get();
        }
        Closer closer = Closer.create();
        try {
            InputStream inputStream = closer.register(this.openStream());
            long l2 = this.countBySkipping(inputStream);
            return l2;
        }
        catch (IOException iOException) {}
        finally {
            closer.close();
        }
        closer = Closer.create();
        try {
            InputStream inputStream = closer.register(this.openStream());
            long l3 = this.countByReading(inputStream);
            return l3;
        }
        catch (Throwable throwable) {
            throw closer.rethrow(throwable);
        }
        finally {
            closer.close();
        }
    }

    private long countBySkipping(InputStream inputStream) throws IOException {
        long l2;
        long l3 = 0;
        while ((l2 = ByteStreams.skipUpTo(inputStream, Integer.MAX_VALUE)) > 0) {
            l3 += l2;
        }
        return l3;
    }

    private long countByReading(InputStream inputStream) throws IOException {
        long l2;
        long l3 = 0;
        while ((l2 = (long)inputStream.read(ByteStreams.skipBuffer)) != -1) {
            l3 += l2;
        }
        return l3;
    }
}

