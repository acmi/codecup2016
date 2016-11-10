/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3;

import java.util.Iterator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

public class StringUtils {
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isBlank(CharSequence charSequence) {
        int n2;
        if (charSequence == null || (n2 = charSequence.length()) == 0) {
            return true;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            if (Character.isWhitespace(charSequence.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !StringUtils.isBlank(charSequence);
    }

    public static /* varargs */ boolean containsAny(CharSequence charSequence, char ... arrc) {
        if (StringUtils.isEmpty(charSequence) || ArrayUtils.isEmpty(arrc)) {
            return false;
        }
        int n2 = charSequence.length();
        int n3 = arrc.length;
        int n4 = n2 - 1;
        int n5 = n3 - 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = charSequence.charAt(i2);
            for (int i3 = 0; i3 < n3; ++i3) {
                if (arrc[i3] != c2) continue;
                if (Character.isHighSurrogate(c2)) {
                    if (i3 == n5) {
                        return true;
                    }
                    if (i2 >= n4 || arrc[i3 + 1] != charSequence.charAt(i2 + 1)) continue;
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    public static /* varargs */ boolean containsNone(CharSequence charSequence, char ... arrc) {
        if (charSequence == null || arrc == null) {
            return true;
        }
        int n2 = charSequence.length();
        int n3 = n2 - 1;
        int n4 = arrc.length;
        int n5 = n4 - 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = charSequence.charAt(i2);
            for (int i3 = 0; i3 < n4; ++i3) {
                if (arrc[i3] != c2) continue;
                if (Character.isHighSurrogate(c2)) {
                    if (i3 == n5) {
                        return false;
                    }
                    if (i2 >= n3 || arrc[i3 + 1] != charSequence.charAt(i2 + 1)) continue;
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    public static String join(Object[] arrobject, String string) {
        if (arrobject == null) {
            return null;
        }
        return StringUtils.join(arrobject, string, 0, arrobject.length);
    }

    public static String join(Object[] arrobject, String string, int n2, int n3) {
        int n4;
        if (arrobject == null) {
            return null;
        }
        if (string == null) {
            string = "";
        }
        if ((n4 = n3 - n2) <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(n4 * 16);
        for (int i2 = n2; i2 < n3; ++i2) {
            if (i2 > n2) {
                stringBuilder.append(string);
            }
            if (arrobject[i2] == null) continue;
            stringBuilder.append(arrobject[i2]);
        }
        return stringBuilder.toString();
    }

    public static String join(Iterator<?> iterator, String string) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object obj = iterator.next();
        if (!iterator.hasNext()) {
            String string2 = ObjectUtils.toString(obj);
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder(256);
        if (obj != null) {
            stringBuilder.append(obj);
        }
        while (iterator.hasNext()) {
            Object obj2;
            if (string != null) {
                stringBuilder.append(string);
            }
            if ((obj2 = iterator.next()) == null) continue;
            stringBuilder.append(obj2);
        }
        return stringBuilder.toString();
    }

    public static String join(Iterable<?> iterable, String string) {
        if (iterable == null) {
            return null;
        }
        return StringUtils.join(iterable.iterator(), string);
    }

    public static String replace(String string, String string2, String string3) {
        return StringUtils.replace(string, string2, string3, -1);
    }

    public static String replace(String string, String string2, String string3, int n2) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(string2) || string3 == null || n2 == 0) {
            return string;
        }
        int n3 = 0;
        int n4 = string.indexOf(string2, n3);
        if (n4 == -1) {
            return string;
        }
        int n5 = string2.length();
        int n6 = string3.length() - n5;
        int n7 = n6 = n6 < 0 ? 0 : n6;
        StringBuilder stringBuilder = new StringBuilder(string.length() + (n6 *= n2 < 0 ? 16 : (n2 > 64 ? 64 : n2)));
        while (n4 != -1) {
            stringBuilder.append(string.substring(n3, n4)).append(string3);
            n3 = n4 + n5;
            if (--n2 == 0) break;
            n4 = string.indexOf(string2, n3);
        }
        stringBuilder.append(string.substring(n3));
        return stringBuilder.toString();
    }

    public static String repeat(String string, int n2) {
        if (string == null) {
            return null;
        }
        if (n2 <= 0) {
            return "";
        }
        int n3 = string.length();
        if (n2 == 1 || n3 == 0) {
            return string;
        }
        if (n3 == 1 && n2 <= 8192) {
            return StringUtils.repeat(string.charAt(0), n2);
        }
        int n4 = n3 * n2;
        switch (n3) {
            case 1: {
                return StringUtils.repeat(string.charAt(0), n2);
            }
            case 2: {
                char c2 = string.charAt(0);
                char c3 = string.charAt(1);
                char[] arrc = new char[n4];
                for (int i2 = n2 * 2 - 2; i2 >= 0; --i2) {
                    arrc[i2] = c2;
                    arrc[i2 + 1] = c3;
                    --i2;
                }
                return new String(arrc);
            }
        }
        StringBuilder stringBuilder = new StringBuilder(n4);
        for (int i3 = 0; i3 < n2; ++i3) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static String repeat(char c2, int n2) {
        char[] arrc = new char[n2];
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            arrc[i2] = c2;
        }
        return new String(arrc);
    }

    public static String capitalize(String string) {
        int n2;
        if (string == null || (n2 = string.length()) == 0) {
            return string;
        }
        char c2 = string.charAt(0);
        if (Character.isTitleCase(c2)) {
            return string;
        }
        return new StringBuilder(n2).append(Character.toTitleCase(c2)).append(string.substring(1)).toString();
    }

    public static String uncapitalize(String string) {
        int n2;
        if (string == null || (n2 = string.length()) == 0) {
            return string;
        }
        char c2 = string.charAt(0);
        if (Character.isLowerCase(c2)) {
            return string;
        }
        return new StringBuilder(n2).append(Character.toLowerCase(c2)).append(string.substring(1)).toString();
    }
}

