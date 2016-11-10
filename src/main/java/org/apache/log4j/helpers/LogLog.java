/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.helpers;

import java.io.PrintStream;
import org.apache.log4j.helpers.OptionConverter;

public class LogLog {
    protected static boolean debugEnabled = false;
    private static boolean quietMode = false;

    public static void setInternalDebugging(boolean bl) {
        debugEnabled = bl;
    }

    public static void debug(String string) {
        if (debugEnabled && !quietMode) {
            System.out.println("log4j: " + string);
        }
    }

    public static void debug(String string, Throwable throwable) {
        if (debugEnabled && !quietMode) {
            System.out.println("log4j: " + string);
            if (throwable != null) {
                throwable.printStackTrace(System.out);
            }
        }
    }

    public static void error(String string) {
        if (quietMode) {
            return;
        }
        System.err.println("log4j:ERROR " + string);
    }

    public static void error(String string, Throwable throwable) {
        if (quietMode) {
            return;
        }
        System.err.println("log4j:ERROR " + string);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    public static void warn(String string) {
        if (quietMode) {
            return;
        }
        System.err.println("log4j:WARN " + string);
    }

    public static void warn(String string, Throwable throwable) {
        if (quietMode) {
            return;
        }
        System.err.println("log4j:WARN " + string);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    static {
        String string = OptionConverter.getSystemProperty("log4j.debug", null);
        if (string == null) {
            string = OptionConverter.getSystemProperty("log4j.configDebug", null);
        }
        if (string != null) {
            debugEnabled = OptionConverter.toBoolean(string, true);
        }
    }
}

