/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.system;

import com.codeforces.commons.text.StringUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.SystemUtils;

public final class EnvironmentUtil {
    private static final Pattern WINDOWS_SYSTEM_VARIABLE_PATTERN = Pattern.compile("%[A-Za-z][A-Za-z0-9_]*%");
    private static final Pattern NX_SYSTEM_VARIABLE_PATTERN = Pattern.compile("\\$[A-Za-z_][A-Za-z0-9_]*");

    public static String expandSystemVariablesQuietly(String string) {
        try {
            return EnvironmentUtil.expandSystemVariables(string);
        }
        catch (RuntimeException runtimeException) {
            return string;
        }
    }

    public static String expandSystemVariables(String string) {
        if (StringUtil.isBlank(string)) {
            return string;
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            return EnvironmentUtil.expandWindowsStyleSystemVariables(string);
        }
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_FREE_BSD || SystemUtils.IS_OS_MAC_OSX) {
            return EnvironmentUtil.expandNxStyleSystemVariables(string);
        }
        throw new NotImplementedException("" + '\'' + SystemUtils.OS_NAME + "' OS is not supported.");
    }

    static String expandWindowsStyleSystemVariables(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = WINDOWS_SYSTEM_VARIABLE_PATTERN.matcher(string);
        int n2 = 0;
        while (matcher.find()) {
            String string2;
            String string3;
            int n3 = matcher.start();
            int n4 = matcher.end();
            if (n3 > n2) {
                stringBuilder.append(string.substring(n2, n3));
            }
            if ((string3 = StringUtil.trimToNull(System.getenv(string2 = string.substring(n3 + 1, n4 - 1)))) == null) {
                stringBuilder.append('%').append(string2).append('%');
            } else {
                stringBuilder.append(string3);
            }
            n2 = n4;
        }
        return stringBuilder.append(string.substring(n2)).toString();
    }

    static String expandNxStyleSystemVariables(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = NX_SYSTEM_VARIABLE_PATTERN.matcher(string);
        int n2 = 0;
        while (matcher.find()) {
            int n3 = matcher.start();
            int n4 = matcher.end();
            if (n3 > n2) {
                stringBuilder.append(string.substring(n2, n3));
            }
            String string2 = string.substring(n3 + 1, n4);
            String string3 = StringUtil.trimToNull(System.getenv(string2));
            if (n3 > 0 && (n4 < string.length() && string.charAt(n3 - 1) == '\'' && string.charAt(n4) == '\'' || string.charAt(n3 - 1) == '\\')) {
                stringBuilder.append('$').append(string2);
            } else if (string3 != null) {
                stringBuilder.append(string3);
            }
            n2 = n4;
        }
        return stringBuilder.append(string.substring(n2)).toString();
    }
}

