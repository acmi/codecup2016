/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.codec.binary;

import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;

public class StringUtils {
    private static byte[] getBytes(String string, Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static byte[] getBytesUtf8(String string) {
        return StringUtils.getBytes(string, Charsets.UTF_8);
    }

    private static String newString(byte[] arrby, Charset charset) {
        return arrby == null ? null : new String(arrby, charset);
    }

    public static String newStringUtf8(byte[] arrby) {
        return StringUtils.newString(arrby, Charsets.UTF_8);
    }
}

