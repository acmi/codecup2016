/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3;

public class BooleanUtils {
    public static Boolean toBooleanObject(String string) {
        if (string == "true") {
            return Boolean.TRUE;
        }
        if (string == null) {
            return null;
        }
        switch (string.length()) {
            case 1: {
                char c2 = string.charAt(0);
                if (c2 == 'y' || c2 == 'Y' || c2 == 't' || c2 == 'T') {
                    return Boolean.TRUE;
                }
                if (c2 != 'n' && c2 != 'N' && c2 != 'f' && c2 != 'F') break;
                return Boolean.FALSE;
            }
            case 2: {
                char c3 = string.charAt(0);
                char c4 = string.charAt(1);
                if (!(c3 != 'o' && c3 != 'O' || c4 != 'n' && c4 != 'N')) {
                    return Boolean.TRUE;
                }
                if (c3 != 'n' && c3 != 'N' || c4 != 'o' && c4 != 'O') break;
                return Boolean.FALSE;
            }
            case 3: {
                char c5 = string.charAt(0);
                char c6 = string.charAt(1);
                char c7 = string.charAt(2);
                if (!(c5 != 'y' && c5 != 'Y' || c6 != 'e' && c6 != 'E' || c7 != 's' && c7 != 'S')) {
                    return Boolean.TRUE;
                }
                if (c5 != 'o' && c5 != 'O' || c6 != 'f' && c6 != 'F' || c7 != 'f' && c7 != 'F') break;
                return Boolean.FALSE;
            }
            case 4: {
                char c8 = string.charAt(0);
                char c9 = string.charAt(1);
                char c10 = string.charAt(2);
                char c11 = string.charAt(3);
                if (c8 != 't' && c8 != 'T' || c9 != 'r' && c9 != 'R' || c10 != 'u' && c10 != 'U' || c11 != 'e' && c11 != 'E') break;
                return Boolean.TRUE;
            }
            case 5: {
                char c12 = string.charAt(0);
                char c13 = string.charAt(1);
                char c14 = string.charAt(2);
                char c15 = string.charAt(3);
                char c16 = string.charAt(4);
                if (c12 != 'f' && c12 != 'F' || c13 != 'a' && c13 != 'A' || c14 != 'l' && c14 != 'L' || c15 != 's' && c15 != 'S' || c16 != 'e' && c16 != 'E') break;
                return Boolean.FALSE;
            }
        }
        return null;
    }

    public static boolean toBoolean(String string) {
        return BooleanUtils.toBooleanObject(string) == Boolean.TRUE;
    }

    public static int compare(boolean bl, boolean bl2) {
        if (bl == bl2) {
            return 0;
        }
        if (bl) {
            return 1;
        }
        return -1;
    }
}

