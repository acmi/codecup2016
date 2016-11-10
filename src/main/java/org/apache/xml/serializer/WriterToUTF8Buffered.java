/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.xml.serializer.WriterChain;

final class WriterToUTF8Buffered
extends Writer
implements WriterChain {
    private final OutputStream m_os;
    private final byte[] m_outputBytes;
    private final char[] m_inputChars;
    private int count;

    public WriterToUTF8Buffered(OutputStream outputStream) {
        this.m_os = outputStream;
        this.m_outputBytes = new byte[16387];
        this.m_inputChars = new char[5463];
        this.count = 0;
    }

    public void write(int n2) throws IOException {
        if (this.count >= 16384) {
            this.flushBuffer();
        }
        if (n2 < 128) {
            this.m_outputBytes[this.count++] = (byte)n2;
        } else if (n2 < 2048) {
            this.m_outputBytes[this.count++] = (byte)(192 + (n2 >> 6));
            this.m_outputBytes[this.count++] = (byte)(128 + (n2 & 63));
        } else if (n2 < 65536) {
            this.m_outputBytes[this.count++] = (byte)(224 + (n2 >> 12));
            this.m_outputBytes[this.count++] = (byte)(128 + (n2 >> 6 & 63));
            this.m_outputBytes[this.count++] = (byte)(128 + (n2 & 63));
        } else {
            this.m_outputBytes[this.count++] = (byte)(240 + (n2 >> 18));
            this.m_outputBytes[this.count++] = (byte)(128 + (n2 >> 12 & 63));
            this.m_outputBytes[this.count++] = (byte)(128 + (n2 >> 6 & 63));
            this.m_outputBytes[this.count++] = (byte)(128 + (n2 & 63));
        }
    }

    public void write(char[] arrc, int n2, int n3) throws IOException {
        int n4;
        char c2;
        int n5 = 3 * n3;
        if (n5 >= 16384 - this.count) {
            this.flushBuffer();
            if (n5 > 16384) {
                int n6 = n3 / 5461;
                int n7 = n3 % 5461 > 0 ? n6 + 1 : n6;
                int n8 = n2;
                for (int i2 = 1; i2 <= n7; ++i2) {
                    int n9 = n8;
                    n8 = n2 + (int)((long)n3 * (long)i2 / (long)n7);
                    char c3 = arrc[n8 - 1];
                    char c4 = arrc[n8 - 1];
                    if (c3 >= '\ud800' && c3 <= '\udbff') {
                        n8 = n8 < n2 + n3 ? ++n8 : --n8;
                    }
                    int n10 = n8 - n9;
                    this.write(arrc, n9, n10);
                }
                return;
            }
        }
        int n11 = n3 + n2;
        byte[] arrby = this.m_outputBytes;
        int n12 = this.count;
        for (n4 = n2; n4 < n11 && (c2 = arrc[n4]) < ''; ++n4) {
            arrby[n12++] = (byte)c2;
        }
        while (n4 < n11) {
            c2 = arrc[n4];
            if (c2 < '') {
                arrby[n12++] = (byte)c2;
            } else if (c2 < '\u0800') {
                arrby[n12++] = (byte)(192 + (c2 >> 6));
                arrby[n12++] = (byte)(128 + (c2 & 63));
            } else if (c2 >= '\ud800' && c2 <= '\udbff') {
                char c5 = c2;
                char c6 = arrc[++n4];
                arrby[n12++] = (byte)(240 | c5 + 64 >> 8 & 240);
                arrby[n12++] = (byte)(128 | c5 + 64 >> 2 & 63);
                arrby[n12++] = (byte)(128 | (c6 >> 6 & 15) + (c5 << 4 & 48));
                arrby[n12++] = (byte)(128 | c6 & 63);
            } else {
                arrby[n12++] = (byte)(224 + (c2 >> 12));
                arrby[n12++] = (byte)(128 + (c2 >> 6 & 63));
                arrby[n12++] = (byte)(128 + (c2 & 63));
            }
            ++n4;
        }
        this.count = n12;
    }

    public void write(String string) throws IOException {
        char c2;
        int n2;
        int n3 = string.length();
        int n4 = 3 * n3;
        if (n4 >= 16384 - this.count) {
            this.flushBuffer();
            if (n4 > 16384) {
                boolean bl = false;
                int n5 = n3 / 5461;
                int n6 = n3 % 5461 > 0 ? n5 + 1 : n5;
                int n7 = 0;
                for (int i2 = 1; i2 <= n6; ++i2) {
                    int n8 = n7;
                    n7 = 0 + (int)((long)n3 * (long)i2 / (long)n6);
                    string.getChars(n8, n7, this.m_inputChars, 0);
                    int n9 = n7 - n8;
                    char c3 = this.m_inputChars[n9 - 1];
                    if (c3 >= '\ud800' && c3 <= '\udbff') {
                        --n7;
                        --n9;
                        if (i2 == n6) {
                            // empty if block
                        }
                    }
                    this.write(this.m_inputChars, 0, n9);
                }
                return;
            }
        }
        string.getChars(0, n3, this.m_inputChars, 0);
        char[] arrc = this.m_inputChars;
        int n10 = n3;
        byte[] arrby = this.m_outputBytes;
        int n11 = this.count;
        for (n2 = 0; n2 < n10 && (c2 = arrc[n2]) < ''; ++n2) {
            arrby[n11++] = (byte)c2;
        }
        while (n2 < n10) {
            c2 = arrc[n2];
            if (c2 < '') {
                arrby[n11++] = (byte)c2;
            } else if (c2 < '\u0800') {
                arrby[n11++] = (byte)(192 + (c2 >> 6));
                arrby[n11++] = (byte)(128 + (c2 & 63));
            } else if (c2 >= '\ud800' && c2 <= '\udbff') {
                char c4 = c2;
                char c5 = arrc[++n2];
                arrby[n11++] = (byte)(240 | c4 + 64 >> 8 & 240);
                arrby[n11++] = (byte)(128 | c4 + 64 >> 2 & 63);
                arrby[n11++] = (byte)(128 | (c5 >> 6 & 15) + (c4 << 4 & 48));
                arrby[n11++] = (byte)(128 | c5 & 63);
            } else {
                arrby[n11++] = (byte)(224 + (c2 >> 12));
                arrby[n11++] = (byte)(128 + (c2 >> 6 & 63));
                arrby[n11++] = (byte)(128 + (c2 & 63));
            }
            ++n2;
        }
        this.count = n11;
    }

    public void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.m_os.write(this.m_outputBytes, 0, this.count);
            this.count = 0;
        }
    }

    public void flush() throws IOException {
        this.flushBuffer();
        this.m_os.flush();
    }

    public void close() throws IOException {
        this.flushBuffer();
        this.m_os.close();
    }

    public Writer getWriter() {
        return null;
    }
}

