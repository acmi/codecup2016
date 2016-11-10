/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.xml.serializer.WriterChain;

class WriterToASCI
extends Writer
implements WriterChain {
    private final OutputStream m_os;

    public WriterToASCI(OutputStream outputStream) {
        this.m_os = outputStream;
    }

    public void write(char[] arrc, int n2, int n3) throws IOException {
        int n4 = n3 + n2;
        for (int i2 = n2; i2 < n4; ++i2) {
            this.m_os.write(arrc[i2]);
        }
    }

    public void write(int n2) throws IOException {
        this.m_os.write(n2);
    }

    public void write(String string) throws IOException {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.m_os.write(string.charAt(i2));
        }
    }

    public void flush() throws IOException {
        this.m_os.flush();
    }

    public void close() throws IOException {
        this.m_os.close();
    }

    public Writer getWriter() {
        return null;
    }
}

