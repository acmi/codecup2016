/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Preconditions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;

public final class Throwables {
    private static final Object jla = Throwables.getJLA();
    private static final Method getStackTraceElementMethod = jla == null ? null : Throwables.getGetMethod();
    private static final Method getStackTraceDepthMethod = jla == null ? null : Throwables.getSizeMethod();

    public static <X extends Throwable> void propagateIfInstanceOf(Throwable throwable, Class<X> class_) throws Throwable {
        if (throwable != null && class_.isInstance(throwable)) {
            throw (Throwable)class_.cast(throwable);
        }
    }

    public static void propagateIfPossible(Throwable throwable) {
        Throwables.propagateIfInstanceOf(throwable, Error.class);
        Throwables.propagateIfInstanceOf(throwable, RuntimeException.class);
    }

    public static <X extends Throwable> void propagateIfPossible(Throwable throwable, Class<X> class_) throws Throwable {
        Throwables.propagateIfInstanceOf(throwable, class_);
        Throwables.propagateIfPossible(throwable);
    }

    public static RuntimeException propagate(Throwable throwable) {
        Throwables.propagateIfPossible(Preconditions.checkNotNull(throwable));
        throw new RuntimeException(throwable);
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private static Object getJLA() {
        try {
            Class class_ = Class.forName("sun.misc.SharedSecrets", false, null);
            Method method = class_.getMethod("getJavaLangAccess", new Class[0]);
            return method.invoke(null, new Object[0]);
        }
        catch (ThreadDeath threadDeath) {
            throw threadDeath;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getGetMethod() {
        return Throwables.getJlaMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
    }

    private static Method getSizeMethod() {
        return Throwables.getJlaMethod("getStackTraceDepth", Throwable.class);
    }

    private static /* varargs */ Method getJlaMethod(String string, Class<?> ... arrclass) throws ThreadDeath {
        try {
            return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(string, arrclass);
        }
        catch (ThreadDeath threadDeath) {
            throw threadDeath;
        }
        catch (Throwable throwable) {
            return null;
        }
    }
}

