/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public final class UCSReader
extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final short UCS2LE = 1;
    public static final short UCS2BE = 2;
    public static final short UCS4LE = 4;
    public static final short UCS4BE = 8;
    protected final InputStream fInputStream;
    protected final byte[] fBuffer;
    protected final short fEncoding;

    public UCSReader(InputStream inputStream, short s2) {
        this(inputStream, 8192, s2);
    }

    public UCSReader(InputStream inputStream, int n2, short s2) {
        this(inputStream, new byte[n2], s2);
    }

    public UCSReader(InputStream inputStream, byte[] arrby, short s2) {
        this.fInputStream = inputStream;
        this.fBuffer = arrby;
        this.fEncoding = s2;
    }

    public int read() throws IOException {
        int n2 = this.fInputStream.read() & 255;
        if (n2 == 255) {
            return -1;
        }
        int n3 = this.fInputStream.read() & 255;
        if (n3 == 255) {
            return -1;
        }
        if (this.fEncoding >= 4) {
            int n4 = this.fInputStream.read() & 255;
            if (n4 == 255) {
                return -1;
            }
            int n5 = this.fInputStream.read() & 255;
            if (n5 == 255) {
                return -1;
            }
            if (this.fEncoding == 8) {
                return (n2 << 24) + (n3 << 16) + (n4 << 8) + n5;
            }
            return (n5 << 24) + (n4 << 16) + (n3 << 8) + n2;
        }
        if (this.fEncoding == 2) {
            return (n2 << 8) + n3;
        }
        return (n3 << 8) + n2;
    }

    public int read(char[] arrc, int n2, int n3) throws IOException {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9 = n3 << (this.fEncoding >= 4 ? 2 : 1);
        if (n9 > this.fBuffer.length) {
            n9 = this.fBuffer.length;
        }
        if ((n7 = this.fInputStream.read(this.fBuffer, 0, n9)) == -1) {
            return -1;
        }
        if (this.fEncoding >= 4) {
            n4 = 4 - (n7 & 3) & 3;
            n6 = 0;
            while (n6 < n4) {
                n8 = this.fInputStream.read();
                if (n8 == -1) {
                    n5 = n6;
                    while (n5 < n4) {
                        this.fBuffer[n7 + n5] = 0;
                        ++n5;
                    }
                    break;
                }
                this.fBuffer[n7 + n6] = (byte)n8;
                ++n6;
            }
            n7 += n4;
        } else {
            n4 = n7 & 1;
            if (n4 != 0) {
                n6 = this.fInputStream.read();
                this.fBuffer[++n7] = n6 == -1 ? 0 : (byte)n6;
            }
        }
        n4 = n7 >> (this.fEncoding >= 4 ? 2 : 1);
        n6 = 0;
        n8 = 0;
        while (n8 < n4) {
            n5 = this.fBuffer[n6++] & 255;
            int n10 = this.fBuffer[n6++] & 255;
            if (this.fEncoding >= 4) {
                int n11 = this.fBuffer[n6++] & 255;
                int n12 = this.fBuffer[n6++] & 255;
                arrc[n2 + n8] = this.fEncoding == 8 ? (char)((n5 << 24) + (n10 << 16) + (n11 << 8) + n12) : (char)((n12 << 24) + (n11 << 16) + (n10 << 8) + n5);
            } else {
                arrc[n2 + n8] = this.fEncoding == 2 ? (char)((n5 << 8) + n10) : (char)((n10 << 8) + n5);
            }
            ++n8;
        }
        return n4;
    }

    public long skip(long l2) throws IOException {
        int n2 = this.fEncoding >= 4 ? 2 : 1;
        long l3 = this.fInputStream.skip(l2 << n2);
        if ((l3 & (long)(n2 | 1)) == 0) {
            return l3 >> n2;
        }
        return (l3 >> n2) + 1;
    }

    public boolean ready() throws IOException {
        return false;
    }

    public boolean markSupported() {
        return this.fInputStream.markSupported();
    }

    public void mark(int n2) throws IOException {
        this.fInputStream.mark(n2);
    }

    public void reset() throws IOException {
        this.fInputStream.reset();
    }

    public void close() throws IOException {
        this.fInputStream.close();
    }
}

