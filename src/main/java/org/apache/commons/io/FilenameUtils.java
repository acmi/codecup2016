/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.io;

import java.io.File;

public class FilenameUtils {
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char OTHER_SEPARATOR = FilenameUtils.isSystemWindows() ? 47 : 92;

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    public static int indexOfLastSeparator(String string) {
        if (string == null) {
            return -1;
        }
        int n2 = string.lastIndexOf(47);
        int n3 = string.lastIndexOf(92);
        return Math.max(n2, n3);
    }

    public static int indexOfExtension(String string) {
        if (string == null) {
            return -1;
        }
        int n2 = string.lastIndexOf(46);
        int n3 = FilenameUtils.indexOfLastSeparator(string);
        return n3 > n2 ? -1 : n2;
    }

    public static String getName(String string) {
        if (string == null) {
            return null;
        }
        FilenameUtils.failIfNullBytePresent(string);
        int n2 = FilenameUtils.indexOfLastSeparator(string);
        return string.substring(n2 + 1);
    }

    private static void failIfNullBytePresent(String string) {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (string.charAt(i2) != '\u0000') continue;
            throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
    }

    public static String getExtension(String string) {
        if (string == null) {
            return null;
        }
        int n2 = FilenameUtils.indexOfExtension(string);
        if (n2 == -1) {
            return "";
        }
        return string.substring(n2 + 1);
    }
}

