/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.codec.binary;

import java.util.Arrays;

public abstract class BaseNCodec {
    @Deprecated
    protected final byte PAD = 61;
    protected final byte pad;
    private final int unencodedBlockSize;
    private final int encodedBlockSize;
    protected final int lineLength;
    private final int chunkSeparatorLength;

    protected BaseNCodec(int n2, int n3, int n4, int n5) {
        this(n2, n3, n4, n5, 61);
    }

    protected BaseNCodec(int n2, int n3, int n4, int n5, byte by) {
        this.unencodedBlockSize = n2;
        this.encodedBlockSize = n3;
        boolean bl = n4 > 0 && n5 > 0;
        this.lineLength = bl ? n4 / n3 * n3 : 0;
        this.chunkSeparatorLength = n5;
        this.pad = by;
    }

    int available(Context context) {
        return context.buffer != null ? context.pos - context.readPos : 0;
    }

    protected int getDefaultBufferSize() {
        return 8192;
    }

    private byte[] resizeBuffer(Context context) {
        if (context.buffer == null) {
            context.buffer = new byte[this.getDefaultBufferSize()];
            context.pos = 0;
            context.readPos = 0;
        } else {
            byte[] arrby = new byte[context.buffer.length * 2];
            System.arraycopy(context.buffer, 0, arrby, 0, context.buffer.length);
            context.buffer = arrby;
        }
        return context.buffer;
    }

    protected byte[] ensureBufferSize(int n2, Context context) {
        if (context.buffer == null || context.buffer.length < context.pos + n2) {
            return this.resizeBuffer(context);
        }
        return context.buffer;
    }

    int readResults(byte[] arrby, int n2, int n3, Context context) {
        if (context.buffer != null) {
            int n4 = Math.min(this.available(context), n3);
            System.arraycopy(context.buffer, context.readPos, arrby, n2, n4);
            context.readPos += n4;
            if (context.readPos >= context.pos) {
                context.buffer = null;
            }
            return n4;
        }
        return context.eof ? -1 : 0;
    }

    public byte[] encode(byte[] arrby) {
        if (arrby == null || arrby.length == 0) {
            return arrby;
        }
        Context context = new Context();
        this.encode(arrby, 0, arrby.length, context);
        this.encode(arrby, 0, -1, context);
        byte[] arrby2 = new byte[context.pos - context.readPos];
        this.readResults(arrby2, 0, arrby2.length, context);
        return arrby2;
    }

    abstract void encode(byte[] var1, int var2, int var3, Context var4);

    protected abstract boolean isInAlphabet(byte var1);

    protected boolean containsAlphabetOrPad(byte[] arrby) {
        if (arrby == null) {
            return false;
        }
        for (byte by : arrby) {
            if (this.pad != by && !this.isInAlphabet(by)) continue;
            return true;
        }
        return false;
    }

    public long getEncodedLength(byte[] arrby) {
        long l2 = (long)((arrby.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize) * (long)this.encodedBlockSize;
        if (this.lineLength > 0) {
            l2 += (l2 + (long)this.lineLength - 1) / (long)this.lineLength * (long)this.chunkSeparatorLength;
        }
        return l2;
    }

    static class Context {
        int ibitWorkArea;
        long lbitWorkArea;
        byte[] buffer;
        int pos;
        int readPos;
        boolean eof;
        int currentLinePos;
        int modulus;

        Context() {
        }

        public String toString() {
            return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", this.getClass().getSimpleName(), Arrays.toString(this.buffer), this.currentLinePos, this.eof, this.ibitWorkArea, this.lbitWorkArea, this.modulus, this.pos, this.readPos);
        }
    }

}

