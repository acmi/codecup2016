/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.PrintStream;
import java.util.Properties;
import org.apache.xml.serialize.SecuritySupport;

final class ObjectFactory {
    private static final boolean DEBUG = ObjectFactory.isDebugEnabled();
    private static Properties fXercesProperties = null;
    private static long fLastModified = -1;
    static Class class$org$apache$xml$serialize$ObjectFactory;

    ObjectFactory() {
    }

    private static boolean isDebugEnabled() {
        try {
            String string = SecuritySupport.getSystemProperty("xerces.debug");
            return string != null && !"false".equals(string);
        }
        catch (SecurityException securityException) {
            return false;
        }
    }

    private static void debugPrintln(String string) {
        if (DEBUG) {
            System.err.println("XERCES: " + string);
        }
    }

    static Object newInstance(String string, ClassLoader classLoader, boolean bl) throws ConfigurationError {
        try {
            Class class_ = ObjectFactory.findProviderClass(string, classLoader, bl);
            Object t2 = class_.newInstance();
            if (DEBUG) {
                ObjectFactory.debugPrintln("created new instance of " + class_ + " using ClassLoader: " + classLoader);
            }
            return t2;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ConfigurationError("Provider " + string + " not found", classNotFoundException);
        }
        catch (Exception exception) {
            throw new ConfigurationError("Provider " + string + " could not be instantiated: " + exception, exception);
        }
    }

    static Class findProviderClass(String string, ClassLoader classLoader, boolean bl) throws ClassNotFoundException, ConfigurationError {
        Class class_;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            int n2 = string.lastIndexOf(".");
            String string2 = string;
            if (n2 != -1) {
                string2 = string.substring(0, n2);
            }
            securityManager.checkPackageAccess(string2);
        }
        if (classLoader == null) {
            class_ = Class.forName(string);
        } else {
            try {
                class_ = classLoader.loadClass(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                if (bl) {
                    Class class_2 = class$org$apache$xml$serialize$ObjectFactory == null ? (ObjectFactory.class$org$apache$xml$serialize$ObjectFactory = ObjectFactory.class$("org.apache.xml.serialize.ObjectFactory")) : class$org$apache$xml$serialize$ObjectFactory;
                    ClassLoader classLoader2 = class_2.getClassLoader();
                    if (classLoader2 == null) {
                        class_ = Class.forName(string);
                    }
                    if (classLoader != classLoader2) {
                        classLoader = classLoader2;
                        class_ = classLoader.loadClass(string);
                    }
                    throw classNotFoundException;
                }
                throw classNotFoundException;
            }
        }
        return class_;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static final class ConfigurationError
    extends Error {
        private Exception exception;

        ConfigurationError(String string, Exception exception) {
            super(string);
            this.exception = exception;
        }
    }

}

