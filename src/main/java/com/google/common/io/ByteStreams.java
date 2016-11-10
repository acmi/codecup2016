/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteStreams {
    static final byte[] skipBuffer = new byte[8192];
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream(){

        @Override
        public void write(int n2) {
        }

        @Override
        public void write(byte[] arrby) {
            Preconditions.checkNotNull(arrby);
        }

        @Override
        public void write(byte[] arrby, int n2, int n3) {
            Preconditions.checkNotNull(arrby);
        }

        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }
    };

    static long skipUpTo(InputStream inputStream, long l2) throws IOException {
        long l3;
        long l4;
        int n2;
        long l5;
        for (l3 = 0; l3 < l2 && ((l4 = ByteStreams.skipSafely(inputStream, l5 = l2 - l3)) != 0 || (l4 = (long)inputStream.read(skipBuffer, 0, n2 = (int)Math.min(l5, (long)skipBuffer.length))) != -1); l3 += l4) {
        }
        return l3;
    }

    private static long skipSafely(InputStream inputStream, long l2) throws IOException {
        int n2 = inputStream.available();
        return n2 == 0 ? 0 : inputStream.skip(Math.min((long)n2, l2));
    }

}

