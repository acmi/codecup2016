/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.exception;

import java.util.HashSet;

public class ExceptionUtil {
    public static String toString(Throwable throwable) {
        HashSet<Throwable> hashSet = new HashSet<Throwable>();
        StringBuilder stringBuilder = new StringBuilder();
        while (throwable != null && !hashSet.contains(throwable)) {
            StackTraceElement[] arrstackTraceElement;
            if (!hashSet.isEmpty()) {
                stringBuilder.append('\n');
            }
            hashSet.add(throwable);
            stringBuilder.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage()).append('\n');
            for (StackTraceElement stackTraceElement : arrstackTraceElement = throwable.getStackTrace()) {
                stringBuilder.append("    ").append(stackTraceElement.toString()).append('\n');
            }
            throwable = throwable.getCause();
        }
        return stringBuilder.toString();
    }
}

