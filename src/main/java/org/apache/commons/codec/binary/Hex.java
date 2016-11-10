/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.codec.binary;

import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;

public class Hex {
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final Charset charset;

    public static char[] encodeHex(byte[] arrby) {
        return Hex.encodeHex(arrby, true);
    }

    public static char[] encodeHex(byte[] arrby, boolean bl) {
        return Hex.encodeHex(arrby, bl ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(byte[] arrby, char[] arrc) {
        int n2 = arrby.length;
        char[] arrc2 = new char[n2 << 1];
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            arrc2[n3++] = arrc[(240 & arrby[i2]) >>> 4];
            arrc2[n3++] = arrc[15 & arrby[i2]];
        }
        return arrc2;
    }

    public static String encodeHexString(byte[] arrby) {
        return new String(Hex.encodeHex(arrby));
    }

    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
}

