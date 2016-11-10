/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.StringUtils;

public class Base64
extends BaseNCodec {
    static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};
    private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private final byte[] encodeTable;
    private final byte[] decodeTable = DECODE_TABLE;
    private final byte[] lineSeparator;
    private final int decodeSize;
    private final int encodeSize;

    public Base64() {
        this(0);
    }

    public Base64(boolean bl) {
        this(76, CHUNK_SEPARATOR, bl);
    }

    public Base64(int n2) {
        this(n2, CHUNK_SEPARATOR);
    }

    public Base64(int n2, byte[] arrby) {
        this(n2, arrby, false);
    }

    public Base64(int n2, byte[] arrby, boolean bl) {
        super(3, 4, n2, arrby == null ? 0 : arrby.length);
        if (arrby != null) {
            if (this.containsAlphabetOrPad(arrby)) {
                String string = StringUtils.newStringUtf8(arrby);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + string + "]");
            }
            if (n2 > 0) {
                this.encodeSize = 4 + arrby.length;
                this.lineSeparator = new byte[arrby.length];
                System.arraycopy(arrby, 0, this.lineSeparator, 0, arrby.length);
            } else {
                this.encodeSize = 4;
                this.lineSeparator = null;
            }
        } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
        }
        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = bl ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    @Override
    void encode(byte[] arrby, int n2, int n3, BaseNCodec.Context context) {
        if (context.eof) {
            return;
        }
        if (n3 < 0) {
            context.eof = true;
            if (0 == context.modulus && this.lineLength == 0) {
                return;
            }
            byte[] arrby2 = this.ensureBufferSize(this.encodeSize, context);
            int n4 = context.pos;
            switch (context.modulus) {
                case 0: {
                    break;
                }
                case 1: {
                    arrby2[context.pos++] = this.encodeTable[context.ibitWorkArea >> 2 & 63];
                    arrby2[context.pos++] = this.encodeTable[context.ibitWorkArea << 4 & 63];
                    if (this.encodeTable != STANDARD_ENCODE_TABLE) break;
                    arrby2[context.pos++] = this.pad;
                    arrby2[context.pos++] = this.pad;
                    break;
                }
                case 2: {
                    arrby2[context.pos++] = this.encodeTable[context.ibitWorkArea >> 10 & 63];
                    arrby2[context.pos++] = this.encodeTable[context.ibitWorkArea >> 4 & 63];
                    arrby2[context.pos++] = this.encodeTable[context.ibitWorkArea << 2 & 63];
                    if (this.encodeTable != STANDARD_ENCODE_TABLE) break;
                    arrby2[context.pos++] = this.pad;
                    break;
                }
                default: {
                    throw new IllegalStateException("Impossible modulus " + context.modulus);
                }
            }
            context.currentLinePos += context.pos - n4;
            if (this.lineLength > 0 && context.currentLinePos > 0) {
                System.arraycopy(this.lineSeparator, 0, arrby2, context.pos, this.lineSeparator.length);
                context.pos += this.lineSeparator.length;
            }
        } else {
            for (int i2 = 0; i2 < n3; ++i2) {
                int n5;
                byte[] arrby3 = this.ensureBufferSize(this.encodeSize, context);
                context.modulus = (context.modulus + 1) % 3;
                if ((n5 = arrby[n2++]) < 0) {
                    n5 += 256;
                }
                context.ibitWorkArea = (context.ibitWorkArea << 8) + n5;
                if (0 != context.modulus) continue;
                arrby3[context.pos++] = this.encodeTable[context.ibitWorkArea >> 18 & 63];
                arrby3[context.pos++] = this.encodeTable[context.ibitWorkArea >> 12 & 63];
                arrby3[context.pos++] = this.encodeTable[context.ibitWorkArea >> 6 & 63];
                arrby3[context.pos++] = this.encodeTable[context.ibitWorkArea & 63];
                context.currentLinePos += 4;
                if (this.lineLength <= 0 || this.lineLength > context.currentLinePos) continue;
                System.arraycopy(this.lineSeparator, 0, arrby3, context.pos, this.lineSeparator.length);
                context.pos += this.lineSeparator.length;
                context.currentLinePos = 0;
            }
        }
    }

    public static String encodeBase64URLSafeString(byte[] arrby) {
        return StringUtils.newStringUtf8(Base64.encodeBase64(arrby, false, true));
    }

    public static byte[] encodeBase64(byte[] arrby, boolean bl, boolean bl2) {
        return Base64.encodeBase64(arrby, bl, bl2, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] arrby, boolean bl, boolean bl2, int n2) {
        if (arrby == null || arrby.length == 0) {
            return arrby;
        }
        Base64 base64 = bl ? new Base64(bl2) : new Base64(0, CHUNK_SEPARATOR, bl2);
        long l2 = base64.getEncodedLength(arrby);
        if (l2 > (long)n2) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + l2 + ") than the specified maximum size of " + n2);
        }
        return base64.encode(arrby);
    }

    @Override
    protected boolean isInAlphabet(byte by) {
        return by >= 0 && by < this.decodeTable.length && this.decodeTable[by] != -1;
    }
}

