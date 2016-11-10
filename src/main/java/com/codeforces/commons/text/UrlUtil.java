/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.text;

import com.codeforces.commons.text.StringUtil;
import java.net.URI;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlUtil {
    private static final String[] ALLOWED_SCHEMES = new String[]{"http", "https"};

    public static String appendParameterToUrl(String string, String string2, String string3) {
        if (!UrlUtil.isValidUri(string) || StringUtil.isBlank(string2)) {
            return string;
        }
        String string4 = StringUtil.isBlank(string3) ? string2 : string2 + '=' + string3;
        int n2 = string.indexOf(63);
        int n3 = string.indexOf(35);
        if (n2 == -1 && n3 == -1) {
            return string + '?' + string4;
        }
        if (n2 == -1 || n3 != -1 && n2 > n3) {
            return string.substring(0, n3) + '?' + string4 + string.substring(n3);
        }
        StringBuilder stringBuilder = new StringBuilder(string.substring(0, n2 + 1)).append(string4);
        if (string.length() > n2 + 1) {
            stringBuilder.append('&').append(string.substring(n2 + 1));
        }
        return stringBuilder.toString();
    }

    public static boolean isValidUrl(String string) {
        return UrlUtil.isValidUrl(string, ALLOWED_SCHEMES);
    }

    public static boolean isValidUrl(String string, String[] arrstring) {
        if (StringUtil.isBlank(string)) {
            return false;
        }
        UrlValidator urlValidator = new UrlValidator(arrstring, 8);
        return urlValidator.isValid(string);
    }

    public static boolean isValidUri(String string) {
        if (string == null) {
            return false;
        }
        try {
            URI.create(string);
            return true;
        }
        catch (RuntimeException runtimeException) {
            return false;
        }
    }
}

