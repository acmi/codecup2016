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

public final class UTF8Reader
extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 2048;
    private static final boolean DEBUG_READ = false;
    protected final InputStream fInputStream;
    protected final byte[] fBuffer;
    protected int fOffset;
    private int fSurrogate = -1;
    private final MessageFormatter fFormatter;
    private final Locale fLocale;

    public UTF8Reader(InputStream inputStream) {
        this(inputStream, 2048, (MessageFormatter)new XMLMessageFormatter(), Locale.getDefault());
    }

    public UTF8Reader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, 2048, messageFormatter, locale);
    }

    public UTF8Reader(InputStream inputStream, int n2, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, new byte[n2], messageFormatter, locale);
    }

    public UTF8Reader(InputStream inputStream, byte[] arrby, MessageFormatter messageFormatter, Locale locale) {
        this.fInputStream = inputStream;
        this.fBuffer = arrby;
        this.fFormatter = messageFormatter;
        this.fLocale = locale;
    }

    public int read() throws IOException {
        int n2 = this.fSurrogate;
        if (this.fSurrogate == -1) {
            int n3;
            int n4 = 0;
            int n5 = n3 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
            if (n3 == -1) {
                return -1;
            }
            if (n3 < 128) {
                n2 = (char)n3;
            } else if ((n3 & 224) == 192 && (n3 & 30) != 0) {
                int n6;
                int n7 = n6 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
                if (n6 == -1) {
                    this.expectedByte(2, 2);
                }
                if ((n6 & 192) != 128) {
                    this.invalidByte(2, 2, n6);
                }
                n2 = n3 << 6 & 1984 | n6 & 63;
            } else if ((n3 & 240) == 224) {
                int n8;
                int n9;
                int n10 = n8 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
                if (n8 == -1) {
                    this.expectedByte(2, 3);
                }
                if ((n8 & 192) != 128 || n3 == 237 && n8 >= 160 || (n3 & 15) == 0 && (n8 & 32) == 0) {
                    this.invalidByte(2, 3, n8);
                }
                int n11 = n9 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
                if (n9 == -1) {
                    this.expectedByte(3, 3);
                }
                if ((n9 & 192) != 128) {
                    this.invalidByte(3, 3, n9);
                }
                n2 = n3 << 12 & 61440 | n8 << 6 & 4032 | n9 & 63;
            } else if ((n3 & 248) == 240) {
                int n12;
                int n13;
                int n14;
                int n15;
                int n16 = n13 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
                if (n13 == -1) {
                    this.expectedByte(2, 4);
                }
                if ((n13 & 192) != 128 || (n13 & 48) == 0 && (n3 & 7) == 0) {
                    this.invalidByte(2, 3, n13);
                }
                int n17 = n15 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
                if (n15 == -1) {
                    this.expectedByte(3, 4);
                }
                if ((n15 & 192) != 128) {
                    this.invalidByte(3, 3, n15);
                }
                int n18 = n12 = n4 == this.fOffset ? this.fInputStream.read() : this.fBuffer[n4++] & 255;
                if (n12 == -1) {
                    this.expectedByte(4, 4);
                }
                if ((n12 & 192) != 128) {
                    this.invalidByte(4, 4, n12);
                }
                if ((n14 = n3 << 2 & 28 | n13 >> 4 & 3) > 16) {
                    this.invalidSurrogate(n14);
                }
                int n19 = n14 - 1;
                int n20 = 55296 | n19 << 6 & 960 | n13 << 2 & 60 | n15 >> 4 & 3;
                int n21 = 56320 | n15 << 6 & 960 | n12 & 63;
                n2 = n20;
                this.fSurrogate = n21;
            } else {
                this.invalidByte(1, 1, n3);
            }
        } else {
            this.fSurrogate = -1;
        }
        return n2;
    }

    public int read(char[] arrc, int n2, int n3) throws IOException {
        char c2;
        int n4 = n2;
        int n5 = 0;
        if (this.fOffset == 0) {
            if (n3 > this.fBuffer.length) {
                n3 = this.fBuffer.length;
            }
            if (this.fSurrogate != -1) {
                arrc[n4++] = (char)this.fSurrogate;
                this.fSurrogate = -1;
                --n3;
            }
            if ((n5 = this.fInputStream.read(this.fBuffer, 0, n3)) == -1) {
                return -1;
            }
            n5 += n4 - n2;
        } else {
            n5 = this.fOffset;
            this.fOffset = 0;
        }
        int n6 = n5;
        int n7 = 0;
        while (n7 < n6) {
            c2 = this.fBuffer[n7];
            if (c2 < '\u0000') break;
            arrc[n4++] = c2;
            ++n7;
        }
        while (n7 < n6) {
            c2 = this.fBuffer[n7];
            if (c2 >= '\u0000') {
                arrc[n4++] = c2;
            } else {
                int n8;
                int n9;
                int n10;
                int n11 = c2 & 255;
                if ((n11 & 224) == 192 && (n11 & 30) != 0) {
                    n8 = -1;
                    if (++n7 < n6) {
                        n8 = this.fBuffer[n7] & 255;
                    } else {
                        n8 = this.fInputStream.read();
                        if (n8 == -1) {
                            if (n4 > n2) {
                                this.fBuffer[0] = (byte)n11;
                                this.fOffset = 1;
                                return n4 - n2;
                            }
                            this.expectedByte(2, 2);
                        }
                        ++n5;
                    }
                    if ((n8 & 192) != 128) {
                        if (n4 > n2) {
                            this.fBuffer[0] = (byte)n11;
                            this.fBuffer[1] = (byte)n8;
                            this.fOffset = 2;
                            return n4 - n2;
                        }
                        this.invalidByte(2, 2, n8);
                    }
                    n9 = n11 << 6 & 1984 | n8 & 63;
                    arrc[n4++] = (char)n9;
                    --n5;
                } else if ((n11 & 240) == 224) {
                    n8 = -1;
                    if (++n7 < n6) {
                        n8 = this.fBuffer[n7] & 255;
                    } else {
                        n8 = this.fInputStream.read();
                        if (n8 == -1) {
                            if (n4 > n2) {
                                this.fBuffer[0] = (byte)n11;
                                this.fOffset = 1;
                                return n4 - n2;
                            }
                            this.expectedByte(2, 3);
                        }
                        ++n5;
                    }
                    if ((n8 & 192) != 128 || n11 == 237 && n8 >= 160 || (n11 & 15) == 0 && (n8 & 32) == 0) {
                        if (n4 > n2) {
                            this.fBuffer[0] = (byte)n11;
                            this.fBuffer[1] = (byte)n8;
                            this.fOffset = 2;
                            return n4 - n2;
                        }
                        this.invalidByte(2, 3, n8);
                    }
                    n9 = -1;
                    if (++n7 < n6) {
                        n9 = this.fBuffer[n7] & 255;
                    } else {
                        n9 = this.fInputStream.read();
                        if (n9 == -1) {
                            if (n4 > n2) {
                                this.fBuffer[0] = (byte)n11;
                                this.fBuffer[1] = (byte)n8;
                                this.fOffset = 2;
                                return n4 - n2;
                            }
                            this.expectedByte(3, 3);
                        }
                        ++n5;
                    }
                    if ((n9 & 192) != 128) {
                        if (n4 > n2) {
                            this.fBuffer[0] = (byte)n11;
                            this.fBuffer[1] = (byte)n8;
                            this.fBuffer[2] = (byte)n9;
                            this.fOffset = 3;
                            return n4 - n2;
                        }
                        this.invalidByte(3, 3, n9);
                    }
                    n10 = n11 << 12 & 61440 | n8 << 6 & 4032 | n9 & 63;
                    arrc[n4++] = (char)n10;
                    n5 -= 2;
                } else if ((n11 & 248) == 240) {
                    int n12;
                    n8 = -1;
                    if (++n7 < n6) {
                        n8 = this.fBuffer[n7] & 255;
                    } else {
                        n8 = this.fInputStream.read();
                        if (n8 == -1) {
                            if (n4 > n2) {
                                this.fBuffer[0] = (byte)n11;
                                this.fOffset = 1;
                                return n4 - n2;
                            }
                            this.expectedByte(2, 4);
                        }
                        ++n5;
                    }
                    if ((n8 & 192) != 128 || (n8 & 48) == 0 && (n11 & 7) == 0) {
                        if (n4 > n2) {
                            this.fBuffer[0] = (byte)n11;
                            this.fBuffer[1] = (byte)n8;
                            this.fOffset = 2;
                            return n4 - n2;
                        }
                        this.invalidByte(2, 4, n8);
                    }
                    n9 = -1;
                    if (++n7 < n6) {
                        n9 = this.fBuffer[n7] & 255;
                    } else {
                        n9 = this.fInputStream.read();
                        if (n9 == -1) {
                            if (n4 > n2) {
                                this.fBuffer[0] = (byte)n11;
                                this.fBuffer[1] = (byte)n8;
                                this.fOffset = 2;
                                return n4 - n2;
                            }
                            this.expectedByte(3, 4);
                        }
                        ++n5;
                    }
                    if ((n9 & 192) != 128) {
                        if (n4 > n2) {
                            this.fBuffer[0] = (byte)n11;
                            this.fBuffer[1] = (byte)n8;
                            this.fBuffer[2] = (byte)n9;
                            this.fOffset = 3;
                            return n4 - n2;
                        }
                        this.invalidByte(3, 4, n9);
                    }
                    n10 = -1;
                    if (++n7 < n6) {
                        n10 = this.fBuffer[n7] & 255;
                    } else {
                        n10 = this.fInputStream.read();
                        if (n10 == -1) {
                            if (n4 > n2) {
                                this.fBuffer[0] = (byte)n11;
                                this.fBuffer[1] = (byte)n8;
                                this.fBuffer[2] = (byte)n9;
                                this.fOffset = 3;
                                return n4 - n2;
                            }
                            this.expectedByte(4, 4);
                        }
                        ++n5;
                    }
                    if ((n10 & 192) != 128) {
                        if (n4 > n2) {
                            this.fBuffer[0] = (byte)n11;
                            this.fBuffer[1] = (byte)n8;
                            this.fBuffer[2] = (byte)n9;
                            this.fBuffer[3] = (byte)n10;
                            this.fOffset = 4;
                            return n4 - n2;
                        }
                        this.invalidByte(4, 4, n9);
                    }
                    if ((n12 = n11 << 2 & 28 | n8 >> 4 & 3) > 16) {
                        this.invalidSurrogate(n12);
                    }
                    int n13 = n12 - 1;
                    int n14 = n8 & 15;
                    int n15 = n9 & 63;
                    int n16 = n10 & 63;
                    int n17 = 55296 | n13 << 6 & 960 | n14 << 2 | n15 >> 4;
                    int n18 = 56320 | n15 << 6 & 960 | n16;
                    arrc[n4++] = (char)n17;
                    if ((n5 -= 2) <= n3) {
                        arrc[n4++] = (char)n18;
                    } else {
                        this.fSurrogate = n18;
                        --n5;
                    }
                } else {
                    if (n4 > n2) {
                        this.fBuffer[0] = (byte)n11;
                        this.fOffset = 1;
                        return n4 - n2;
                    }
                    this.invalidByte(1, 1, n11);
                }
            }
            ++n7;
        }
        return n5;
    }

    public long skip(long l2) throws IOException {
        int n2;
        int n3;
        long l3 = l2;
        char[] arrc = new char[this.fBuffer.length];
        while ((n2 = this.read(arrc, 0, n3 = (long)arrc.length < l3 ? arrc.length : (int)l3)) > 0 && (l3 -= (long)n2) > 0) {
        }
        long l4 = l2 - l3;
        return l4;
    }

    public boolean ready() throws IOException {
        return false;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int n2) throws IOException {
        throw new IOException(this.fFormatter.formatMessage(this.fLocale, "OperationNotSupported", new Object[]{"mark()", "UTF-8"}));
    }

    public void reset() throws IOException {
        this.fOffset = 0;
        this.fSurrogate = -1;
    }

    public void close() throws IOException {
        this.fInputStream.close();
    }

    private void expectedByte(int n2, int n3) throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "ExpectedByte", new Object[]{Integer.toString(n2), Integer.toString(n3)});
    }

    private void invalidByte(int n2, int n3, int n4) throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidByte", new Object[]{Integer.toString(n2), Integer.toString(n3)});
    }

    private void invalidSurrogate(int n2) throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidHighSurrogate", new Object[]{Integer.toHexString(n2)});
    }
}

