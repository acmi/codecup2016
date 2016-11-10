/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.helpers;

import java.io.PrintStream;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Util {
    private static ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED;

    private Util() {
    }

    public static String safeGetSystemProperty(String string) {
        if (string == null) {
            throw new IllegalArgumentException("null input");
        }
        String string2 = null;
        try {
            string2 = System.getProperty(string);
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        return string2;
    }

    public static boolean safeGetBooleanSystemProperty(String string) {
        String string2 = Util.safeGetSystemProperty(string);
        if (string2 == null) {
            return false;
        }
        return string2.equalsIgnoreCase("true");
    }

    private static ClassContextSecurityManager getSecurityManager() {
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER;
        }
        if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED) {
            return null;
        }
        SECURITY_MANAGER = Util.safeCreateSecurityManager();
        SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
        return SECURITY_MANAGER;
    }

    private static ClassContextSecurityManager safeCreateSecurityManager() {
        try {
            return new ClassContextSecurityManager();
        }
        catch (SecurityException securityException) {
            return null;
        }
    }

    public static Class<?> getCallingClass() {
        int n2;
        ClassContextSecurityManager classContextSecurityManager = Util.getSecurityManager();
        if (classContextSecurityManager == null) {
            return null;
        }
        Class<?>[] arrclass = classContextSecurityManager.getClassContext();
        String string = Util.class.getName();
        for (n2 = 0; n2 < arrclass.length && !string.equals(arrclass[n2].getName()); ++n2) {
        }
        if (n2 >= arrclass.length || n2 + 2 >= arrclass.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
        }
        return arrclass[n2 + 2];
    }

    public static final void report(String string, Throwable throwable) {
        System.err.println(string);
        System.err.println("Reported exception:");
        throwable.printStackTrace();
    }

    public static final void report(String string) {
        System.err.println("SLF4J: " + string);
    }

    static {
        SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class ClassContextSecurityManager
    extends SecurityManager {
        private ClassContextSecurityManager() {
        }

        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

}

