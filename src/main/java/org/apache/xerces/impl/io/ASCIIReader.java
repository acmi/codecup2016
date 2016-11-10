/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.MessageFormatter;

public final class ASCIIReader
extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 2048;
    protected final InputStream fInputStream;
    protected final byte[] fBuffer;
    private final MessageFormatter fFormatter;
    private final Locale fLocale;

    public ASCIIReader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, 2048, messageFormatter, locale);
    }

    public ASCIIReader(InputStream inputStream, int n2, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, new byte[n2], messageFormatter, locale);
    }

    public ASCIIReader(InputStream inputStream, byte[] arrby, MessageFormatter messageFormatter, Locale locale) {
        this.fInputStream = inputStream;
        this.fBuffer = arrby;
        this.fFormatter = messageFormatter;
        this.fLocale = locale;
    }

    public int read() throws IOException {
        int n2 = this.fInputStream.read();
        if (n2 >= 128) {
            throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[]{Integer.toString(n2)});
        }
        return n2;
    }

    public int read(char[] arrc, int n2, int n3) throws IOException {
        if (n3 > this.fBuffer.length) {
            n3 = this.fBuffer.length;
        }
        int n4 = this.fInputStream.read(this.fBuffer, 0, n3);
        int n5 = 0;
        while (n5 < n4) {
            char c2 = this.fBuffer[n5];
            if (c2 < '\u0000') {
                throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[]{Integer.toString(c2 & 255)});
            }
            arrc[n2 + n5] = c2;
            ++n5;
        }
        return n4;
    }

    public long skip(long l2) throws IOException {
        return this.fInputStream.skip(l2);
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

