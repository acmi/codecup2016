/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.util.Arrays;
import org.apache.xerces.util.XMLChar;

public class XML11Char {
    private static final byte[] XML11CHARS = new byte[65536];
    public static final int MASK_XML11_VALID = 1;
    public static final int MASK_XML11_SPACE = 2;
    public static final int MASK_XML11_NAME_START = 4;
    public static final int MASK_XML11_NAME = 8;
    public static final int MASK_XML11_CONTROL = 16;
    public static final int MASK_XML11_CONTENT = 32;
    public static final int MASK_XML11_NCNAME_START = 64;
    public static final int MASK_XML11_NCNAME = 128;
    public static final int MASK_XML11_CONTENT_INTERNAL = 48;

    public static boolean isXML11Space(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 2) != 0;
    }

    public static boolean isXML11Valid(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 1) != 0 || 65536 <= n2 && n2 <= 1114111;
    }

    public static boolean isXML11Invalid(int n2) {
        return !XML11Char.isXML11Valid(n2);
    }

    public static boolean isXML11ValidLiteral(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 1) != 0 && (XML11CHARS[n2] & 16) == 0 || 65536 <= n2 && n2 <= 1114111;
    }

    public static boolean isXML11Content(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 32) != 0 || 65536 <= n2 && n2 <= 1114111;
    }

    public static boolean isXML11InternalEntityContent(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 48) != 0 || 65536 <= n2 && n2 <= 1114111;
    }

    public static boolean isXML11NameStart(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 4) != 0 || 65536 <= n2 && n2 < 983040;
    }

    public static boolean isXML11Name(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 8) != 0 || n2 >= 65536 && n2 < 983040;
    }

    public static boolean isXML11NCNameStart(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 64) != 0 || 65536 <= n2 && n2 < 983040;
    }

    public static boolean isXML11NCName(int n2) {
        return n2 < 65536 && (XML11CHARS[n2] & 128) != 0 || 65536 <= n2 && n2 < 983040;
    }

    public static boolean isXML11NameHighSurrogate(int n2) {
        return 55296 <= n2 && n2 <= 56191;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isXML11ValidName(String string) {
        char c2;
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        int n3 = 1;
        char c3 = string.charAt(0);
        if (!XML11Char.isXML11NameStart(c3)) {
            if (n2 <= 1) return false;
            if (!XML11Char.isXML11NameHighSurrogate(c3)) return false;
            c2 = string.charAt(1);
            if (!XMLChar.isLowSurrogate(c2)) return false;
            if (!XML11Char.isXML11NameStart(XMLChar.supplemental(c3, c2))) {
                return false;
            }
            n3 = 2;
        }
        while (n3 < n2) {
            c3 = string.charAt(n3);
            if (!XML11Char.isXML11Name(c3)) {
                if (++n3 >= n2) return false;
                if (!XML11Char.isXML11NameHighSurrogate(c3)) return false;
                c2 = string.charAt(n3);
                if (!XMLChar.isLowSurrogate(c2)) return false;
                if (!XML11Char.isXML11Name(XMLChar.supplemental(c3, c2))) {
                    return false;
                }
            }
            ++n3;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isXML11ValidNCName(String string) {
        char c2;
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        int n3 = 1;
        char c3 = string.charAt(0);
        if (!XML11Char.isXML11NCNameStart(c3)) {
            if (n2 <= 1) return false;
            if (!XML11Char.isXML11NameHighSurrogate(c3)) return false;
            c2 = string.charAt(1);
            if (!XMLChar.isLowSurrogate(c2)) return false;
            if (!XML11Char.isXML11NCNameStart(XMLChar.supplemental(c3, c2))) {
                return false;
            }
            n3 = 2;
        }
        while (n3 < n2) {
            c3 = string.charAt(n3);
            if (!XML11Char.isXML11NCName(c3)) {
                if (++n3 >= n2) return false;
                if (!XML11Char.isXML11NameHighSurrogate(c3)) return false;
                c2 = string.charAt(n3);
                if (!XMLChar.isLowSurrogate(c2)) return false;
                if (!XML11Char.isXML11NCName(XMLChar.supplemental(c3, c2))) {
                    return false;
                }
            }
            ++n3;
        }
        return true;
    }

    public static boolean isXML11ValidNmtoken(String string) {
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (!XML11Char.isXML11Name(c2)) {
                if (++n3 < n2 && XML11Char.isXML11NameHighSurrogate(c2)) {
                    char c3 = string.charAt(n3);
                    if (!XMLChar.isLowSurrogate(c3) || !XML11Char.isXML11Name(XMLChar.supplemental(c2, c3))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            ++n3;
        }
        return true;
    }

    static {
        Arrays.fill(XML11CHARS, 1, 9, 17);
        XML11Char.XML11CHARS[9] = 35;
        XML11Char.XML11CHARS[10] = 3;
        Arrays.fill(XML11CHARS, 11, 13, 17);
        XML11Char.XML11CHARS[13] = 3;
        Arrays.fill(XML11CHARS, 14, 32, 17);
        XML11Char.XML11CHARS[32] = 35;
        Arrays.fill(XML11CHARS, 33, 38, 33);
        XML11Char.XML11CHARS[38] = 1;
        Arrays.fill(XML11CHARS, 39, 45, 33);
        Arrays.fill(XML11CHARS, 45, 47, -87);
        XML11Char.XML11CHARS[47] = 33;
        Arrays.fill(XML11CHARS, 48, 58, -87);
        XML11Char.XML11CHARS[58] = 45;
        XML11Char.XML11CHARS[59] = 33;
        XML11Char.XML11CHARS[60] = 1;
        Arrays.fill(XML11CHARS, 61, 65, 33);
        Arrays.fill(XML11CHARS, 65, 91, -19);
        Arrays.fill(XML11CHARS, 91, 93, 33);
        XML11Char.XML11CHARS[93] = 1;
        XML11Char.XML11CHARS[94] = 33;
        XML11Char.XML11CHARS[95] = -19;
        XML11Char.XML11CHARS[96] = 33;
        Arrays.fill(XML11CHARS, 97, 123, -19);
        Arrays.fill(XML11CHARS, 123, 127, 33);
        Arrays.fill(XML11CHARS, 127, 133, 17);
        XML11Char.XML11CHARS[133] = 35;
        Arrays.fill(XML11CHARS, 134, 160, 17);
        Arrays.fill(XML11CHARS, 160, 183, 33);
        XML11Char.XML11CHARS[183] = -87;
        Arrays.fill(XML11CHARS, 184, 192, 33);
        Arrays.fill(XML11CHARS, 192, 215, -19);
        XML11Char.XML11CHARS[215] = 33;
        Arrays.fill(XML11CHARS, 216, 247, -19);
        XML11Char.XML11CHARS[247] = 33;
        Arrays.fill(XML11CHARS, 248, 768, -19);
        Arrays.fill(XML11CHARS, 768, 880, -87);
        Arrays.fill(XML11CHARS, 880, 894, -19);
        XML11Char.XML11CHARS[894] = 33;
        Arrays.fill(XML11CHARS, 895, 8192, -19);
        Arrays.fill(XML11CHARS, 8192, 8204, 33);
        Arrays.fill(XML11CHARS, 8204, 8206, -19);
        Arrays.fill(XML11CHARS, 8206, 8232, 33);
        XML11Char.XML11CHARS[8232] = 35;
        Arrays.fill(XML11CHARS, 8233, 8255, 33);
        Arrays.fill(XML11CHARS, 8255, 8257, -87);
        Arrays.fill(XML11CHARS, 8257, 8304, 33);
        Arrays.fill(XML11CHARS, 8304, 8592, -19);
        Arrays.fill(XML11CHARS, 8592, 11264, 33);
        Arrays.fill(XML11CHARS, 11264, 12272, -19);
        Arrays.fill(XML11CHARS, 12272, 12289, 33);
        Arrays.fill(XML11CHARS, 12289, 55296, -19);
        Arrays.fill(XML11CHARS, 57344, 63744, 33);
        Arrays.fill(XML11CHARS, 63744, 64976, -19);
        Arrays.fill(XML11CHARS, 64976, 65008, 33);
        Arrays.fill(XML11CHARS, 65008, 65534, -19);
    }
}

