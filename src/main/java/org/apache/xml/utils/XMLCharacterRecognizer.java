/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class XMLCharacterRecognizer {
    public static boolean isWhiteSpace(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n';
    }

    public static boolean isWhiteSpace(char[] arrc, int n2, int n3) {
        int n4 = n2 + n3;
        for (int i2 = n2; i2 < n4; ++i2) {
            if (XMLCharacterRecognizer.isWhiteSpace(arrc[i2])) continue;
            return false;
        }
        return true;
    }

    public static boolean isWhiteSpace(StringBuffer stringBuffer) {
        int n2 = stringBuffer.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (XMLCharacterRecognizer.isWhiteSpace(stringBuffer.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isWhiteSpace(String string) {
        if (null != string) {
            int n2 = string.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                if (XMLCharacterRecognizer.isWhiteSpace(string.charAt(i2))) continue;
                return false;
            }
        }
        return true;
    }
}

