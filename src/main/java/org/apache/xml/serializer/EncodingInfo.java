/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import org.apache.xml.serializer.Encodings;

public final class EncodingInfo {
    private final char m_highCharInContiguousGroup;
    final String name;
    final String javaName;
    private InEncoding m_encoding;

    public boolean isInEncoding(char c2) {
        if (this.m_encoding == null) {
            this.m_encoding = new EncodingImpl(this, null);
        }
        return this.m_encoding.isInEncoding(c2);
    }

    public boolean isInEncoding(char c2, char c3) {
        if (this.m_encoding == null) {
            this.m_encoding = new EncodingImpl(this, null);
        }
        return this.m_encoding.isInEncoding(c2, c3);
    }

    public EncodingInfo(String string, String string2, char c2) {
        this.name = string;
        this.javaName = string2;
        this.m_highCharInContiguousGroup = c2;
    }

    private static boolean inEncoding(char c2, String string) {
        boolean bl;
        block2 : {
            try {
                char[] arrc = new char[]{c2};
                String string2 = new String(arrc);
                byte[] arrby = string2.getBytes(string);
                bl = EncodingInfo.inEncoding(c2, arrby);
            }
            catch (Exception exception) {
                bl = false;
                if (string != null) break block2;
                bl = true;
            }
        }
        return bl;
    }

    private static boolean inEncoding(char c2, char c3, String string) {
        boolean bl;
        try {
            char[] arrc = new char[]{c2, c3};
            String string2 = new String(arrc);
            byte[] arrby = string2.getBytes(string);
            bl = EncodingInfo.inEncoding(c2, arrby);
        }
        catch (Exception exception) {
            bl = false;
        }
        return bl;
    }

    private static boolean inEncoding(char c2, byte[] arrby) {
        boolean bl = arrby == null || arrby.length == 0 ? false : (arrby[0] == 0 ? false : arrby[0] != 63 || c2 == '?');
        return bl;
    }

    static boolean access$100(char c2, String string) {
        return EncodingInfo.inEncoding(c2, string);
    }

    static boolean access$200(char c2, char c3, String string) {
        return EncodingInfo.inEncoding(c2, c3, string);
    }

    static class 1 {
    }

    private class EncodingImpl
    implements InEncoding {
        private final String m_encoding;
        private final int m_first;
        private final int m_explFirst;
        private final int m_explLast;
        private final int m_last;
        private InEncoding m_before;
        private InEncoding m_after;
        private final boolean[] m_alreadyKnown;
        private final boolean[] m_isInEncoding;
        private final EncodingInfo this$0;

        public boolean isInEncoding(char c2) {
            boolean bl;
            int n2 = Encodings.toCodePoint(c2);
            if (n2 < this.m_explFirst) {
                if (this.m_before == null) {
                    this.m_before = new EncodingImpl(this.this$0, this.m_encoding, this.m_first, this.m_explFirst - 1, n2);
                }
                bl = this.m_before.isInEncoding(c2);
            } else if (this.m_explLast < n2) {
                if (this.m_after == null) {
                    this.m_after = new EncodingImpl(this.this$0, this.m_encoding, this.m_explLast + 1, this.m_last, n2);
                }
                bl = this.m_after.isInEncoding(c2);
            } else {
                int n3 = n2 - this.m_explFirst;
                if (this.m_alreadyKnown[n3]) {
                    bl = this.m_isInEncoding[n3];
                } else {
                    bl = EncodingInfo.access$100(c2, this.m_encoding);
                    this.m_alreadyKnown[n3] = true;
                    this.m_isInEncoding[n3] = bl;
                }
            }
            return bl;
        }

        public boolean isInEncoding(char c2, char c3) {
            boolean bl;
            int n2 = Encodings.toCodePoint(c2, c3);
            if (n2 < this.m_explFirst) {
                if (this.m_before == null) {
                    this.m_before = new EncodingImpl(this.this$0, this.m_encoding, this.m_first, this.m_explFirst - 1, n2);
                }
                bl = this.m_before.isInEncoding(c2, c3);
            } else if (this.m_explLast < n2) {
                if (this.m_after == null) {
                    this.m_after = new EncodingImpl(this.this$0, this.m_encoding, this.m_explLast + 1, this.m_last, n2);
                }
                bl = this.m_after.isInEncoding(c2, c3);
            } else {
                int n3 = n2 - this.m_explFirst;
                if (this.m_alreadyKnown[n3]) {
                    bl = this.m_isInEncoding[n3];
                } else {
                    bl = EncodingInfo.access$200(c2, c3, this.m_encoding);
                    this.m_alreadyKnown[n3] = true;
                    this.m_isInEncoding[n3] = bl;
                }
            }
            return bl;
        }

        private EncodingImpl(EncodingInfo encodingInfo) {
            this(encodingInfo, encodingInfo.javaName, 0, Integer.MAX_VALUE, 0);
        }

        private EncodingImpl(EncodingInfo encodingInfo, String string, int n2, int n3, int n4) {
            this.this$0 = encodingInfo;
            this.m_alreadyKnown = new boolean[128];
            this.m_isInEncoding = new boolean[128];
            this.m_first = n2;
            this.m_last = n3;
            this.m_explFirst = n4;
            this.m_explLast = n4 + 127;
            this.m_encoding = string;
            if (encodingInfo.javaName != null) {
                int n5;
                if (0 <= this.m_explFirst && this.m_explFirst <= 127 && ("UTF8".equals(encodingInfo.javaName) || "UTF-16".equals(encodingInfo.javaName) || "ASCII".equals(encodingInfo.javaName) || "US-ASCII".equals(encodingInfo.javaName) || "Unicode".equals(encodingInfo.javaName) || "UNICODE".equals(encodingInfo.javaName) || encodingInfo.javaName.startsWith("ISO8859"))) {
                    for (n5 = 1; n5 < 127; ++n5) {
                        int n6 = n5 - this.m_explFirst;
                        if (0 > n6 || n6 >= 128) continue;
                        this.m_alreadyKnown[n6] = true;
                        this.m_isInEncoding[n6] = true;
                    }
                }
                if (encodingInfo.javaName == null) {
                    for (n5 = 0; n5 < this.m_alreadyKnown.length; ++n5) {
                        this.m_alreadyKnown[n5] = true;
                        this.m_isInEncoding[n5] = true;
                    }
                }
            }
        }

        EncodingImpl(EncodingInfo encodingInfo, 1 var2_2) {
            this(encodingInfo);
        }
    }

    private static interface InEncoding {
        public boolean isInEncoding(char var1);

        public boolean isInEncoding(char var1, char var2);
    }

}

