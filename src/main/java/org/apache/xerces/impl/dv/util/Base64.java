/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.util;

public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int SIXBIT = 6;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[128];
    private static final char[] lookUpBase64Alphabet = new char[64];

    protected static boolean isWhiteSpace(char c2) {
        return c2 == ' ' || c2 == '\r' || c2 == '\n' || c2 == '\t';
    }

    protected static boolean isPad(char c2) {
        return c2 == '=';
    }

    protected static boolean isData(char c2) {
        return c2 < '' && base64Alphabet[c2] != -1;
    }

    protected static boolean isBase64(char c2) {
        return Base64.isWhiteSpace(c2) || Base64.isPad(c2) || Base64.isData(c2);
    }

    public static String encode(byte[] arrby) {
        byte by;
        byte by2;
        if (arrby == null) {
            return null;
        }
        int n2 = arrby.length * 8;
        if (n2 == 0) {
            return "";
        }
        int n3 = n2 % 24;
        int n4 = n2 / 24;
        int n5 = n3 != 0 ? n4 + 1 : n4;
        char[] arrc = null;
        arrc = new char[n5 * 4];
        byte by3 = 0;
        byte by4 = 0;
        byte by5 = 0;
        byte by6 = 0;
        byte by7 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        while (n8 < n4) {
            by5 = arrby[n7++];
            by6 = arrby[n7++];
            by7 = arrby[n7++];
            by4 = (byte)(by6 & 15);
            by3 = (byte)(by5 & 3);
            by2 = (by5 & -128) == 0 ? (byte)(by5 >> 2) : (byte)(by5 >> 2 ^ 192);
            by = (by6 & -128) == 0 ? (byte)(by6 >> 4) : (byte)(by6 >> 4 ^ 240);
            byte by8 = (by7 & -128) == 0 ? (byte)(by7 >> 6) : (byte)(by7 >> 6 ^ 252);
            arrc[n6++] = lookUpBase64Alphabet[by2];
            arrc[n6++] = lookUpBase64Alphabet[by | by3 << 4];
            arrc[n6++] = lookUpBase64Alphabet[by4 << 2 | by8];
            arrc[n6++] = lookUpBase64Alphabet[by7 & 63];
            ++n8;
        }
        if (n3 == 8) {
            by5 = arrby[n7];
            by3 = (byte)(by5 & 3);
            by2 = (by5 & -128) == 0 ? (byte)(by5 >> 2) : (byte)(by5 >> 2 ^ 192);
            arrc[n6++] = lookUpBase64Alphabet[by2];
            arrc[n6++] = lookUpBase64Alphabet[by3 << 4];
            arrc[n6++] = 61;
            arrc[n6++] = 61;
        } else if (n3 == 16) {
            by5 = arrby[n7];
            by6 = arrby[n7 + 1];
            by4 = (byte)(by6 & 15);
            by3 = (byte)(by5 & 3);
            by2 = (by5 & -128) == 0 ? (byte)(by5 >> 2) : (byte)(by5 >> 2 ^ 192);
            by = (by6 & -128) == 0 ? (byte)(by6 >> 4) : (byte)(by6 >> 4 ^ 240);
            arrc[n6++] = lookUpBase64Alphabet[by2];
            arrc[n6++] = lookUpBase64Alphabet[by | by3 << 4];
            arrc[n6++] = lookUpBase64Alphabet[by4 << 2];
            arrc[n6++] = 61;
        }
        return new String(arrc);
    }

    public static byte[] decode(String string) {
        if (string == null) {
            return null;
        }
        char[] arrc = string.toCharArray();
        int n2 = Base64.removeWhiteSpace(arrc);
        if (n2 % 4 != 0) {
            return null;
        }
        int n3 = n2 / 4;
        if (n3 == 0) {
            return new byte[0];
        }
        byte[] arrby = null;
        byte by = 0;
        byte by2 = 0;
        byte by3 = 0;
        byte by4 = 0;
        char c2 = '\u0000';
        char c3 = '\u0000';
        char c4 = '\u0000';
        char c5 = '\u0000';
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        arrby = new byte[n3 * 3];
        while (n4 < n3 - 1) {
            if (!(Base64.isData(c2 = arrc[n6++]) && Base64.isData(c3 = arrc[n6++]) && Base64.isData(c4 = arrc[n6++]) && Base64.isData(c5 = arrc[n6++]))) {
                return null;
            }
            by = base64Alphabet[c2];
            by2 = base64Alphabet[c3];
            by3 = base64Alphabet[c4];
            by4 = base64Alphabet[c5];
            arrby[n5++] = (byte)(by << 2 | by2 >> 4);
            arrby[n5++] = (byte)((by2 & 15) << 4 | by3 >> 2 & 15);
            arrby[n5++] = (byte)(by3 << 6 | by4);
            ++n4;
        }
        if (!Base64.isData(c2 = arrc[n6++]) || !Base64.isData(c3 = arrc[n6++])) {
            return null;
        }
        by = base64Alphabet[c2];
        by2 = base64Alphabet[c3];
        c4 = arrc[n6++];
        c5 = arrc[n6++];
        if (!Base64.isData(c4) || !Base64.isData(c5)) {
            if (Base64.isPad(c4) && Base64.isPad(c5)) {
                if ((by2 & 15) != 0) {
                    return null;
                }
                byte[] arrby2 = new byte[n4 * 3 + 1];
                System.arraycopy(arrby, 0, arrby2, 0, n4 * 3);
                arrby2[n5] = (byte)(by << 2 | by2 >> 4);
                return arrby2;
            }
            if (!Base64.isPad(c4) && Base64.isPad(c5)) {
                by3 = base64Alphabet[c4];
                if ((by3 & 3) != 0) {
                    return null;
                }
                byte[] arrby3 = new byte[n4 * 3 + 2];
                System.arraycopy(arrby, 0, arrby3, 0, n4 * 3);
                arrby3[n5++] = (byte)(by << 2 | by2 >> 4);
                arrby3[n5] = (byte)((by2 & 15) << 4 | by3 >> 2 & 15);
                return arrby3;
            }
            return null;
        }
        by3 = base64Alphabet[c4];
        by4 = base64Alphabet[c5];
        arrby[n5++] = (byte)(by << 2 | by2 >> 4);
        arrby[n5++] = (byte)((by2 & 15) << 4 | by3 >> 2 & 15);
        arrby[n5++] = (byte)(by3 << 6 | by4);
        return arrby;
    }

    protected static int removeWhiteSpace(char[] arrc) {
        if (arrc == null) {
            return 0;
        }
        int n2 = 0;
        int n3 = arrc.length;
        int n4 = 0;
        while (n4 < n3) {
            if (!Base64.isWhiteSpace(arrc[n4])) {
                arrc[n2++] = arrc[n4];
            }
            ++n4;
        }
        return n2;
    }

    static {
        int n2 = 0;
        while (n2 < 128) {
            Base64.base64Alphabet[n2] = -1;
            ++n2;
        }
        int n3 = 90;
        while (n3 >= 65) {
            Base64.base64Alphabet[n3] = (byte)(n3 - 65);
            --n3;
        }
        int n4 = 122;
        while (n4 >= 97) {
            Base64.base64Alphabet[n4] = (byte)(n4 - 97 + 26);
            --n4;
        }
        int n5 = 57;
        while (n5 >= 48) {
            Base64.base64Alphabet[n5] = (byte)(n5 - 48 + 52);
            --n5;
        }
        Base64.base64Alphabet[43] = 62;
        Base64.base64Alphabet[47] = 63;
        int n6 = 0;
        while (n6 <= 25) {
            Base64.lookUpBase64Alphabet[n6] = (char)(65 + n6);
            ++n6;
        }
        int n7 = 26;
        int n8 = 0;
        while (n7 <= 51) {
            Base64.lookUpBase64Alphabet[n7] = (char)(97 + n8);
            ++n7;
            ++n8;
        }
        int n9 = 52;
        int n10 = 0;
        while (n9 <= 61) {
            Base64.lookUpBase64Alphabet[n9] = (char)(48 + n10);
            ++n9;
            ++n10;
        }
        Base64.lookUpBase64Alphabet[62] = 43;
        Base64.lookUpBase64Alphabet[63] = 47;
    }
}

