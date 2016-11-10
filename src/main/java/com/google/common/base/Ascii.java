/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

public final class Ascii {
    public static String toLowerCase(String string) {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!Ascii.isUpperCase(string.charAt(i2))) continue;
            char[] arrc = string.toCharArray();
            while (i2 < n2) {
                char c2 = arrc[i2];
                if (Ascii.isUpperCase(c2)) {
                    arrc[i2] = (char)(c2 ^ 32);
                }
                ++i2;
            }
            return String.valueOf(arrc);
        }
        return string;
    }

    public static boolean isUpperCase(char c2) {
        return c2 >= 'A' && c2 <= 'Z';
    }
}

