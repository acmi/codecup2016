/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Preconditions;

public final class Strings {
    public static String repeat(String string, int n2) {
        int n3;
        Preconditions.checkNotNull(string);
        if (n2 <= 1) {
            Preconditions.checkArgument(n2 >= 0, "invalid count: %s", n2);
            return n2 == 0 ? "" : string;
        }
        int n4 = string.length();
        long l2 = (long)n4 * (long)n2;
        int n5 = (int)l2;
        if ((long)n5 != l2) {
            throw new ArrayIndexOutOfBoundsException("Required array size too large: " + l2);
        }
        char[] arrc = new char[n5];
        string.getChars(0, n4, arrc, 0);
        for (n3 = n4; n3 < n5 - n3; n3 <<= 1) {
            System.arraycopy(arrc, 0, arrc, n3, n3);
        }
        System.arraycopy(arrc, 0, arrc, n3, n5 - n3);
        return new String(arrc);
    }
}

