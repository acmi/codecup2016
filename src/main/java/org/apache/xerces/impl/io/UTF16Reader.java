/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.MessageFormatter;

public final class UTF16Reader
extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    protected final InputStream fInputStream;
    protected final byte[] fBuffer;
    protected final boolean fIsBigEndian;
    private final MessageFormatter fFormatter;
    private final Locale fLocale;

    public UTF16Reader(InputStream inputStream, boolean bl) {
        this(inputStream, 4096, bl, (MessageFormatter)new XMLMessageFormatter(), Locale.getDefault());
    }

    public UTF16Reader(InputStream inputStream, boolean bl, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, 4096, bl, messageFormatter, locale);
    }

    public UTF16Reader(InputStream inputStream, int n2, boolean bl, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, new byte[n2], bl, messageFormatter, locale);
    }

    public UTF16Reader(InputStream inputStream, byte[] arrby, boolean bl, MessageFormatter messageFormatter, Locale locale) {
        this.fInputStream = inputStream;
        this.fBuffer = arrby;
        this.fIsBigEndian = bl;
        this.fFormatter = messageFormatter;
        this.fLocale = locale;
    }

    public int read() throws IOException {
        int n2 = this.fInputStream.read();
        if (n2 == -1) {
            return -1;
        }
        int n3 = this.fInputStream.read();
        if (n3 == -1) {
            this.expectedTwoBytes();
        }
        if (this.fIsBigEndian) {
            return n2 << 8 | n3;
        }
        return n3 << 8 | n2;
    }

    public int read(char[] arrc, int n2, int n3) throws IOException {
        int n4;
        int n5;
        int n6 = n3 << 1;
        if (n6 > this.fBuffer.length) {
            n6 = this.fBuffer.length;
        }
        if ((n5 = this.fInputStream.read(this.fBuffer, 0, n6)) == -1) {
            return -1;
        }
        if ((n5 & 1) != 0) {
            n4 = this.fInputStream.read();
            if (n4 == -1) {
                this.expectedTwoBytes();
            }
            this.fBuffer[n5++] = (byte)n4;
        }
        n4 = n5 >> 1;
        if (this.fIsBigEndian) {
            this.processBE(arrc, n2, n4);
        } else {
            this.processLE(arrc, n2, n4);
        }
        return n4;
    }

    public long skip(long l2) throws IOException {
        long l3 = this.fInputStream.skip(l2 << 1);
        if ((l3 & 1) != 0) {
            int n2 = this.fInputStream.read();
            if (n2 == -1) {
                this.expectedTwoBytes();
            }
            ++l3;
        }
        return l3 >> 1;
    }

    public boolean ready() throws IOException {
        return false;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int n2) throws IOException {
        throw new IOException(this.fFormatter.formatMessage(this.fLocale, "OperationNotSupported", new Object[]{"mark()", "UTF-16"}));
    }

    public void reset() throws IOException {
    }

    public void close() throws IOException {
        this.fInputStream.close();
    }

    private void processBE(char[] arrc, int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        while (n5 < n3) {
            int n6 = this.fBuffer[n4++] & 255;
            int n7 = this.fBuffer[n4++] & 255;
            arrc[n2++] = (char)(n6 << 8 | n7);
            ++n5;
        }
    }

    private void processLE(char[] arrc, int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        while (n5 < n3) {
            int n6 = this.fBuffer[n4++] & 255;
            int n7 = this.fBuffer[n4++] & 255;
            arrc[n2++] = (char)(n7 << 8 | n6);
            ++n5;
        }
    }

    private void expectedTwoBytes() throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "ExpectedByte", new Object[]{"2", "2"});
    }
}

