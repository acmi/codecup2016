/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.util;

public final class HexBin {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 16;
    private static final byte[] hexNumberTable = new byte[128];
    private static final char[] lookUpHexAlphabet = new char[16];

    public static String encode(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        int n2 = arrby.length;
        int n3 = n2 * 2;
        char[] arrc = new char[n3];
        int n4 = 0;
        while (n4 < n2) {
            int n5 = arrby[n4];
            if (n5 < 0) {
                n5 += 256;
            }
            arrc[n4 * 2] = lookUpHexAlphabet[n5 >> 4];
            arrc[n4 * 2 + 1] = lookUpHexAlphabet[n5 & 15];
            ++n4;
        }
        return new String(arrc);
    }

    public static byte[] decode(String string) {
        if (string == null) {
            return null;
        }
        int n2 = string.length();
        if (n2 % 2 != 0) {
            return null;
        }
        char[] arrc = string.toCharArray();
        int n3 = n2 / 2;
        byte[] arrby = new byte[n3];
        int n4 = 0;
        while (n4 < n3) {
            int n5;
            int n6;
            char c2 = arrc[n4 * 2];
            int n7 = n6 = c2 < '' ? hexNumberTable[c2] : -1;
            if (n6 == -1) {
                return null;
            }
            c2 = arrc[n4 * 2 + 1];
            int n8 = n5 = c2 < '' ? hexNumberTable[c2] : -1;
            if (n5 == -1) {
                return null;
            }
            arrby[n4] = (byte)(n6 << 4 | n5);
            ++n4;
        }
        return arrby;
    }

    static {
        int n2 = 0;
        while (n2 < 128) {
            HexBin.hexNumberTable[n2] = -1;
            ++n2;
        }
        int n3 = 57;
        while (n3 >= 48) {
            HexBin.hexNumberTable[n3] = (byte)(n3 - 48);
            --n3;
        }
        int n4 = 70;
        while (n4 >= 65) {
            HexBin.hexNumberTable[n4] = (byte)(n4 - 65 + 10);
            --n4;
        }
        int n5 = 102;
        while (n5 >= 97) {
            HexBin.hexNumberTable[n5] = (byte)(n5 - 97 + 10);
            --n5;
        }
        int n6 = 0;
        while (n6 < 10) {
            HexBin.lookUpHexAlphabet[n6] = (char)(48 + n6);
            ++n6;
        }
        int n7 = 10;
        while (n7 <= 15) {
            HexBin.lookUpHexAlphabet[n7] = (char)(65 + n7 - 10);
            ++n7;
        }
    }
}

