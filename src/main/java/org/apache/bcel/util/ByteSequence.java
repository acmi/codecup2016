/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.InputStream;

public final class ByteSequence
extends DataInputStream {
    private ByteArrayStream byte_stream;

    public ByteSequence(byte[] arrby) {
        super(new ByteArrayStream(arrby));
        this.byte_stream = (ByteArrayStream)this.in;
    }

    public final int getIndex() {
        return this.byte_stream.getPosition();
    }

    private static final class ByteArrayStream
    extends ByteArrayInputStream {
        ByteArrayStream(byte[] arrby) {
            super(arrby);
        }

        final int getPosition() {
            return this.pos;
        }
    }

}

