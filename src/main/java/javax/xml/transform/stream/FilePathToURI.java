/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.stream;

import java.io.File;
import java.io.UnsupportedEncodingException;

class FilePathToURI {
    private static boolean[] gNeedEscaping = new boolean[128];
    private static char[] gAfterEscaping1 = new char[128];
    private static char[] gAfterEscaping2 = new char[128];
    private static char[] gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String filepath2URI(String string) {
        int n2;
        if (string == null) {
            return null;
        }
        char c2 = File.separatorChar;
        string = string.replace(c2, '/');
        int n3 = string.length();
        StringBuffer stringBuffer = new StringBuffer(n3 * 3);
        stringBuffer.append("file://");
        if (n3 >= 2 && string.charAt(1) == ':' && (n2 = Character.toUpperCase(string.charAt(0))) >= 65 && n2 <= 90) {
            stringBuffer.append('/');
        }
        int n4 = 0;
        while (n4 < n3) {
            n2 = string.charAt(n4);
            if (n2 >= 128) break;
            if (gNeedEscaping[n2]) {
                stringBuffer.append('%');
                stringBuffer.append(gAfterEscaping1[n2]);
                stringBuffer.append(gAfterEscaping2[n2]);
            } else {
                stringBuffer.append((char)n2);
            }
            ++n4;
        }
        if (n4 < n3) {
            byte[] arrby = null;
            try {
                arrby = string.substring(n4).getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return string;
            }
            n3 = arrby.length;
            n4 = 0;
            while (n4 < n3) {
                char c3 = arrby[n4];
                if (c3 < '\u0000') {
                    n2 = c3 + 256;
                    stringBuffer.append('%');
                    stringBuffer.append(gHexChs[n2 >> 4]);
                    stringBuffer.append(gHexChs[n2 & 15]);
                } else if (gNeedEscaping[c3]) {
                    stringBuffer.append('%');
                    stringBuffer.append(gAfterEscaping1[c3]);
                    stringBuffer.append(gAfterEscaping2[c3]);
                } else {
                    stringBuffer.append(c3);
                }
                ++n4;
            }
        }
        return stringBuffer.toString();
    }

    static {
        int n2 = 0;
        while (n2 <= 31) {
            FilePathToURI.gNeedEscaping[n2] = true;
            FilePathToURI.gAfterEscaping1[n2] = gHexChs[n2 >> 4];
            FilePathToURI.gAfterEscaping2[n2] = gHexChs[n2 & 15];
            ++n2;
        }
        FilePathToURI.gNeedEscaping[127] = true;
        FilePathToURI.gAfterEscaping1[127] = 55;
        FilePathToURI.gAfterEscaping2[127] = 70;
        char[] arrc = new char[]{' ', '<', '>', '#', '%', '\"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
        int n3 = arrc.length;
        int n4 = 0;
        while (n4 < n3) {
            char c2 = arrc[n4];
            FilePathToURI.gNeedEscaping[c2] = true;
            FilePathToURI.gAfterEscaping1[c2] = gHexChs[c2 >> 4];
            FilePathToURI.gAfterEscaping2[c2] = gHexChs[c2 & 15];
            ++n4;
        }
    }
}

