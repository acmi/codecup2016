/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Properties;
import org.apache.xml.serializer.SecuritySupport;

final class ObjectFactory {
    private static Properties fXalanProperties = null;
    private static long fLastModified = -1;
    static Class class$org$apache$xml$serializer$ObjectFactory;

    ObjectFactory() {
    }

    static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader classLoader;
        ClassLoader classLoader2 = SecuritySupport.getContextClassLoader();
        ClassLoader classLoader3 = classLoader = SecuritySupport.getSystemClassLoader();
        do {
            if (classLoader2 == classLoader3) {
                Class class_ = class$org$apache$xml$serializer$ObjectFactory == null ? (ObjectFactory.class$org$apache$xml$serializer$ObjectFactory = ObjectFactory.class$("org.apache.xml.serializer.ObjectFactory")) : class$org$apache$xml$serializer$ObjectFactory;
                ClassLoader classLoader4 = class_.getClassLoader();
                classLoader3 = classLoader;
                do {
                    if (classLoader4 == classLoader3) {
                        return classLoader;
                    }
                    if (classLoader3 == null) break;
                    classLoader3 = SecuritySupport.getParentClassLoader(classLoader3);
                } while (true);
                return classLoader4;
            }
            if (classLoader3 == null) break;
            classLoader3 = SecuritySupport.getParentClassLoader(classLoader3);
        } while (true);
        return classLoader2;
    }

    static Class findProviderClass(String string, ClassLoader classLoader, boolean bl) throws ClassNotFoundException, ConfigurationError {
        Class class_;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            int n2 = string.lastIndexOf(46);
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
                    Class class_2 = class$org$apache$xml$serializer$ObjectFactory == null ? (ObjectFactory.class$org$apache$xml$serializer$ObjectFactory = ObjectFactory.class$("org.apache.xml.serializer.ObjectFactory")) : class$org$apache$xml$serializer$ObjectFactory;
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

    static class ConfigurationError
    extends Error {
    }

}

