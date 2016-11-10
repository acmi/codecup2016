/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.helpers;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;

public class Loader {
    private static boolean java1;
    private static boolean ignoreTCL;
    static Class class$org$apache$log4j$helpers$Loader;
    static Class class$java$lang$Thread;

    public static URL getResource(String string) {
        ClassLoader classLoader = null;
        URL uRL = null;
        try {
            if (!java1 && !ignoreTCL && (classLoader = Loader.getTCL()) != null) {
                LogLog.debug("Trying to find [" + string + "] using context classloader " + classLoader + ".");
                uRL = classLoader.getResource(string);
                if (uRL != null) {
                    return uRL;
                }
            }
            Class class_ = class$org$apache$log4j$helpers$Loader == null ? (Loader.class$org$apache$log4j$helpers$Loader = Loader.class$("org.apache.log4j.helpers.Loader")) : class$org$apache$log4j$helpers$Loader;
            classLoader = class_.getClassLoader();
            if (classLoader != null) {
                LogLog.debug("Trying to find [" + string + "] using " + classLoader + " class loader.");
                uRL = classLoader.getResource(string);
                if (uRL != null) {
                    return uRL;
                }
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getTargetException() instanceof InterruptedException || invocationTargetException.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", invocationTargetException);
        }
        catch (Throwable throwable) {
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", throwable);
        }
        LogLog.debug("Trying to find [" + string + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(string);
    }

    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            Class class_ = class$java$lang$Thread == null ? (Loader.class$java$lang$Thread = Loader.class$("java.lang.Thread")) : class$java$lang$Thread;
            method = class_.getMethod("getContextClassLoader", null);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return null;
        }
        return (ClassLoader)method.invoke(Thread.currentThread(), null);
    }

    public static Class loadClass(String string) throws ClassNotFoundException {
        if (java1 || ignoreTCL) {
            return Class.forName(string);
        }
        try {
            return Loader.getTCL().loadClass(string);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getTargetException() instanceof InterruptedException || invocationTargetException.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return Class.forName(string);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }

    static {
        int n2;
        String string;
        java1 = true;
        ignoreTCL = false;
        String string2 = OptionConverter.getSystemProperty("java.version", null);
        if (string2 != null && (n2 = string2.indexOf(46)) != -1 && string2.charAt(n2 + 1) != '1') {
            java1 = false;
        }
        if ((string = OptionConverter.getSystemProperty("log4j.ignoreTCL", null)) != null) {
            ignoreTCL = OptionConverter.toBoolean(string, true);
        }
    }
}

