/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer.utils;

import java.io.File;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.URI;

public final class SystemIDResolver {
    public static String getAbsoluteURIFromRelative(String string) {
        if (string == null || string.length() == 0) {
            return "";
        }
        String string2 = string;
        if (!SystemIDResolver.isAbsolutePath(string)) {
            try {
                string2 = SystemIDResolver.getAbsolutePathFromRelativePath(string);
            }
            catch (SecurityException securityException) {
                return "file:" + string;
            }
        }
        String string3 = null != string2 ? (string2.startsWith(File.separator) ? "file://" + string2 : "file:///" + string2) : "file:" + string;
        return SystemIDResolver.replaceChars(string3);
    }

    private static String getAbsolutePathFromRelativePath(String string) {
        return new File(string).getAbsolutePath();
    }

    public static boolean isAbsoluteURI(String string) {
        if (SystemIDResolver.isWindowsAbsolutePath(string)) {
            return false;
        }
        int n2 = string.indexOf(35);
        int n3 = string.indexOf(63);
        int n4 = string.indexOf(47);
        int n5 = string.indexOf(58);
        int n6 = string.length() - 1;
        if (n2 > 0) {
            n6 = n2;
        }
        if (n3 > 0 && n3 < n6) {
            n6 = n3;
        }
        if (n4 > 0 && n4 < n6) {
            n6 = n4;
        }
        return n5 > 0 && n5 < n6;
    }

    public static boolean isAbsolutePath(String string) {
        if (string == null) {
            return false;
        }
        File file = new File(string);
        return file.isAbsolute();
    }

    private static boolean isWindowsAbsolutePath(String string) {
        if (!SystemIDResolver.isAbsolutePath(string)) {
            return false;
        }
        if (string.length() > 2 && string.charAt(1) == ':' && Character.isLetter(string.charAt(0)) && (string.charAt(2) == '\\' || string.charAt(2) == '/')) {
            return true;
        }
        return false;
    }

    private static String replaceChars(String string) {
        StringBuffer stringBuffer = new StringBuffer(string);
        int n2 = stringBuffer.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = stringBuffer.charAt(i2);
            if (c2 == ' ') {
                stringBuffer.setCharAt(i2, '%');
                stringBuffer.insert(i2 + 1, "20");
                n2 += 2;
                i2 += 2;
                continue;
            }
            if (c2 != '\\') continue;
            stringBuffer.setCharAt(i2, '/');
        }
        return stringBuffer.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getAbsoluteURI(String string) {
        int n2;
        String string2 = string;
        if (!SystemIDResolver.isAbsoluteURI(string)) return SystemIDResolver.getAbsoluteURIFromRelative(string);
        if (!string.startsWith("file:")) return string;
        String string3 = string.substring(5);
        if (string3 == null || !string3.startsWith("/")) return SystemIDResolver.getAbsoluteURIFromRelative(string.substring(5));
        if (!string3.startsWith("///") && string3.startsWith("//") || (n2 = string.indexOf(58, 5)) <= 0) return SystemIDResolver.replaceChars(string2);
        String string4 = string.substring(n2 - 1);
        try {
            if (SystemIDResolver.isAbsolutePath(string4)) return SystemIDResolver.replaceChars(string2);
            string2 = string.substring(0, n2 - 1) + SystemIDResolver.getAbsolutePathFromRelativePath(string4);
            return SystemIDResolver.replaceChars(string2);
        }
        catch (SecurityException securityException) {
            return string;
        }
    }

    public static String getAbsoluteURI(String string, String string2) throws TransformerException {
        if (string2 == null) {
            return SystemIDResolver.getAbsoluteURI(string);
        }
        String string3 = SystemIDResolver.getAbsoluteURI(string2);
        URI uRI = null;
        try {
            URI uRI2 = new URI(string3);
            uRI = new URI(uRI2, string);
        }
        catch (URI.MalformedURIException malformedURIException) {
            throw new TransformerException(malformedURIException);
        }
        return SystemIDResolver.replaceChars(uRI.toString());
    }
}

