/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.WriterChain;

final class SerializerTraceWriter
extends Writer
implements WriterChain {
    private final Writer m_writer;
    private final SerializerTrace m_tracer;
    private int buf_length;
    private byte[] buf;
    private int count;

    private void setBufferSize(int n2) {
        this.buf = new byte[n2 + 3];
        this.buf_length = n2;
        this.count = 0;
    }

    public SerializerTraceWriter(Writer writer, SerializerTrace serializerTrace) {
        this.m_writer = writer;
        this.m_tracer = serializerTrace;
        this.setBufferSize(1024);
    }

    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            char[] arrc = new char[this.count];
            for (int i2 = 0; i2 < this.count; ++i2) {
                arrc[i2] = this.buf[i2];
            }
            if (this.m_tracer != null) {
                this.m_tracer.fireGenerateEvent(12, arrc, 0, arrc.length);
            }
            this.count = 0;
        }
    }

    public void flush() throws IOException {
        if (this.m_writer != null) {
            this.m_writer.flush();
        }
        this.flushBuffer();
    }

    public void close() throws IOException {
        if (this.m_writer != null) {
            this.m_writer.close();
        }
        this.flushBuffer();
    }

    public void write(int n2) throws IOException {
        if (this.m_writer != null) {
            this.m_writer.write(n2);
        }
        if (this.count >= this.buf_length) {
            this.flushBuffer();
        }
        if (n2 < 128) {
            this.buf[this.count++] = (byte)n2;
        } else if (n2 < 2048) {
            this.buf[this.count++] = (byte)(192 + (n2 >> 6));
            this.buf[this.count++] = (byte)(128 + (n2 & 63));
        } else {
            this.buf[this.count++] = (byte)(224 + (n2 >> 12));
            this.buf[this.count++] = (byte)(128 + (n2 >> 6 & 63));
            this.buf[this.count++] = (byte)(128 + (n2 & 63));
        }
    }

    public void write(char[] arrc, int n2, int n3) throws IOException {
        int n4;
        if (this.m_writer != null) {
            this.m_writer.write(arrc, n2, n3);
        }
        if ((n4 = (n3 << 1) + n3) >= this.buf_length) {
            this.flushBuffer();
            this.setBufferSize(2 * n4);
        }
        if (n4 > this.buf_length - this.count) {
            this.flushBuffer();
        }
        int n5 = n3 + n2;
        for (int i2 = n2; i2 < n5; ++i2) {
            char c2 = arrc[i2];
            if (c2 < '') {
                this.buf[this.count++] = (byte)c2;
                continue;
            }
            if (c2 < '\u0800') {
                this.buf[this.count++] = (byte)(192 + (c2 >> 6));
                this.buf[this.count++] = (byte)(128 + (c2 & 63));
                continue;
            }
            this.buf[this.count++] = (byte)(224 + (c2 >> 12));
            this.buf[this.count++] = (byte)(128 + (c2 >> 6 & 63));
            this.buf[this.count++] = (byte)(128 + (c2 & 63));
        }
    }

    public void write(String string) throws IOException {
        int n2;
        int n3;
        if (this.m_writer != null) {
            this.m_writer.write(string);
        }
        if ((n3 = ((n2 = string.length()) << 1) + n2) >= this.buf_length) {
            this.flushBuffer();
            this.setBufferSize(2 * n3);
        }
        if (n3 > this.buf_length - this.count) {
            this.flushBuffer();
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if (c2 < '') {
                this.buf[this.count++] = (byte)c2;
                continue;
            }
            if (c2 < '\u0800') {
                this.buf[this.count++] = (byte)(192 + (c2 >> 6));
                this.buf[this.count++] = (byte)(128 + (c2 & 63));
                continue;
            }
            this.buf[this.count++] = (byte)(224 + (c2 >> 12));
            this.buf[this.count++] = (byte)(128 + (c2 >> 6 & 63));
            this.buf[this.count++] = (byte)(128 + (c2 & 63));
        }
    }

    public Writer getWriter() {
        return this.m_writer;
    }
}

